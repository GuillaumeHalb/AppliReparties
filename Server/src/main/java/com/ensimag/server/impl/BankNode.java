package com.ensimag.server.impl;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;

import com.ensimag.services.bank.IAccount;
import com.ensimag.services.bank.IBank;
import com.ensimag.services.bank.IBankMessage;
import com.ensimag.services.bank.IBankNode;
import com.ensimag.services.bank.IUser;
import com.ensimag.services.message.EnumMessageType;
import com.ensimag.services.message.IAck;
import com.ensimag.services.message.IMessage;
import com.ensimag.services.message.IResult;
import com.ensimag.services.node.INode;

public class BankNode extends UnicastRemoteObject implements IBankNode {

	private static final long serialVersionUID = -2414181384542180339L;

	private Bank bank;
	private long id;
	// Liste des voisins
	private List<IBankNode> neighboors;
	// waitS = Liste de BankNodeId dont on attend un ack pour le message Id
	// Si List<BankNodeId> est vide, on n'attend plus de ack pour ce message.
	private Map<Long, List<Long>> waitS;
	// Noeud up
	// BankNodeId à qui je dois renvoyer le ack pour le message
	private Map<IBankMessage, Long> up;
	// boolean déjà_vu
	// Si IBankMessage appartient à la liste, on a déjà reçu le message (boolean
	// = true)
	private LinkedList<IBankMessage> deja_vu;

	// Message idMessage dont on attend n resultats
	private Map<Long, LinkedList<IResult<? extends Serializable>>> waitingResults;

	public BankNode(Bank bank, long id) throws RemoteException {
		super();
		this.setBank(bank);
		this.id = id;
		this.neighboors = new LinkedList<IBankNode>();
		this.waitS = new HashMap<Long, List<Long>>();
		this.up = new HashMap<IBankMessage, Long>();
		this.deja_vu = new LinkedList<IBankMessage>();
		this.waitingResults = new HashMap<Long, LinkedList<IResult<? extends Serializable>>>();
	}

	@Override
	public List<IAccount> getAccounts() throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		return this.getBank().getAccounts();
	}

	@Override
	public IAccount getAccount(long number) throws AccountNotFoundException, RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		return this.getBank().getAccount(number);
	}

	@Override
	public IAccount openAccount(IUser user) throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		return this.getBank().openAccount(user);
	}

	@Override
	public boolean closeAccount(long number) throws AccountNotFoundException, RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		return this.getBank().closeAccount(number);
	}

	@Override
	public long getId() throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		return this.id;
	}

	@Override
	public void addNeighboor(INode<IBankMessage> neighboor) throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		this.getNeighboors().add((IBankNode) neighboor);
	}

	@Override
	public void removeNeighboor(INode<IBankMessage> neighboor) throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		this.getNeighboors().remove(neighboor);
	}

	/**
	 * Execute the message action on the node if the node is the target,
	 * propagate the message to its neighboors otherwise
	 * 
	 * @param message
	 *            the message to handle
	 * @throws RemoteException
	 */
	@Override
	public void onMessage(IBankMessage message) throws RemoteException {
		System.out.println("onMessage");
		if (this.id < 0) {
			throw new RemoteException();
		}
		// Si le message est pour nous
		if ((message.getMessageType().equals(EnumMessageType.SINGLE_DEST) && message.getDestinationBankId() == this.id)
				|| message.getMessageType().equals(EnumMessageType.BROADCAST)) {
			// Envoyer un ack
			Ack ack = new Ack(this.id, message.getMessageId());
			getSender(message).onAck(ack);
			// On execute l'action
			Serializable data = null;
			try {
				data = message.getAction().execute(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			IResult result = new Result(data, message.getMessageId());
			// On envoie le résultat
			this.deliverResult(result);

		} else if (message.getMessageType().equals(EnumMessageType.DELIVERY)
				&& message.getDestinationBankId() == this.id) {
			// Envoyer un ack
			Ack ack = new Ack(this.id, message.getMessageId());
			getSender(message).onAck(ack);
			// On récupère le résultat
			//TODO: initialiser waitingResults
			if (this.waitingResults.get(message.getMessageId()) == null) {
				LinkedList<IResult<? extends Serializable>> listWaitingResults = this.waitingResults.get(message.getMessageId());
				listWaitingResults = new LinkedList<>();
			}
			this.waitingResults.get(message.getMessageId()).add(message.getResult());
		} else {
			// Noeud non puit et message non déjà vu
			if (!this.isWellNode() && !this.alreadySeen(message)) {
				this.deja_vu.add(message);
				assert (message.getSenderId() != 0);
				this.up.put(message, message.getSenderId());
				for (IBankNode neighboor : this.neighboors) {
					if (!(message.getSenderId() == neighboor.getId())) {
						List<Long> listWaitedAck = this.waitS.get(message.getMessageId());
						if (listWaitedAck == null) {
							listWaitedAck = new LinkedList<Long>();
							System.out.println("listWaitedAck null");
						} if (neighboor == null) {
							System.out.println("neighboor null. Comment est-ce possible ?");
						} if (neighboor.getId() == 0) {
							System.out.println("neighboor.getId() null " + neighboor.toString());
						}
						System.out.println("listWaitedAck: " + listWaitedAck.toString());
						System.out.println("neighboor: " + neighboor.toString());
						listWaitedAck.add(neighboor.getId());
						// TODO: check if this.waitS a bien été rempli.
						// Sinon, cloner neighboor ou remplacer la liste à
						// chaque fois.
						IBankMessage messageCloned = message.clone();
						messageCloned.setSenderId(this.id);
						neighboor.onMessage(messageCloned);
					}
				}
				// Expected : list of all neighboors excepted one
				System.out.println("BankNode waitS update: " + this.waitS.get(message));
			} else if (this.isWellNode() || this.alreadySeen(message)) {
				Ack ack = new Ack(this.id, message.getMessageId());
				getSender(message).onAck(ack);
			}
		}
	}

	private IBankNode getSender(IBankMessage message) throws RemoteException {
		IBankNode sender = null;
		for (IBankNode neighboor : getNeighboors()) {
			if (neighboor.getId() == message.getSenderId()) {
				sender = neighboor;
			}
		}
		if (sender == null) {
			throw new RemoteException("Sender is not in the neighboors");
		}
		return sender;
	}

	private boolean alreadySeen(IBankMessage message) {
		if (this.deja_vu.contains(message)) {
			return true;
		}
		return false;
	}

	// Returns true if this node is a well
	private boolean isWellNode() {
		if (this.neighboors.size() == 1) {
			return true;
		}
		return false;
	}

	@Override
	public void onAck(IAck ack) throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		System.out.println("BankNode onAck listAckWaited avant suppression du nouveau ack : "
				+ this.waitS.get(ack.getAckMessageId()));
		List<Long> listAckWaited = this.waitS.get(ack.getAckMessageId());
		if (listAckWaited == null) { // Premier ack
			listAckWaited = new LinkedList<Long>();
		}
		boolean removed = false;
		for (int i = 0; i < listAckWaited.size(); i++) {
			if (listAckWaited.get(i) == ack.getAckSenderId()) {
				listAckWaited.remove(i);
				removed = true;
			}
		}
		if (!removed) {
			System.out.println(
					"PB: BankNode onAck: no element deleted. Didn't expected ack from " + ack.getAckSenderId());
		}
		// Expected : un élément en moins.
		System.out.println("BankNode onAck listAckWaited après suppression du nouveau ack : "
				+ this.waitS.get(ack.getAckMessageId()));
		if (listAckWaited.size() == 0) {
			// Si on n'est pas à l'envoyeur initial
			if (this.up.get(ack.getAckMessageId()) != null ) {
				boolean neighboorFound = false;
				for (IBankNode neighboor : this.neighboors) {
					if (neighboor.getId() == this.up.get(ack.getAckMessageId())) {
						neighboor.onAck(ack);
						neighboorFound = true;
					}
				}
				if (!neighboorFound) {
					System.out.println("PB: BankNode onAck: neighboor not found to send ack.");
				}
			} else { // On est revenu au premier envoyeur : fin de la vague
				System.out.println("Ack back to primary sender : OK");
			}
		}
	}

	/**
	 * Get the result for the given message
	 * 
	 * @param messageId
	 *            the message id for which a result is waited
	 * @return the result of the sent message
	 */
	// Si le message était un SINGLE_DEST on attend qu'un seul result (liste
	// avec un seul élément)
	// Si le message était un BROADCAST on attend une liste avec un résultat par
	// noeud
	// Sinon pas de résultat attend.
	@Override
	public List<IResult<? extends Serializable>> getResultForMessage(long messageId) throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		return this.waitingResults.get(messageId);
	}

	/**
	 * Deliver a result to a bank
	 * 
	 * @param result
	 *            the result to deliver
	 * @return <code>true</code> if the result is delivered, <code>false</code>
	 *         otherwise
	 */
	// Pour l'instant on va dire que si on n'a pas reçu d'exception tout s'est
	// bien passé
	@Override
	public Boolean deliverResult(IResult<Serializable> result) throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}

		long destinationBankId = -1;
		for (IBankMessage message : this.deja_vu) {
			if (message.getMessageId() == result.getMessageId()) {
				destinationBankId = message.getOriginalBankSenderId();
			}
		}
		if (destinationBankId == -1) {
			System.out.println("ERREUR BankNode DeliverResult : lors de l'initialisation de la destination");
		}
		Message messageRetour = new Message(null, result.getMessageId(), this.getId(), destinationBankId,
				EnumMessageType.DELIVERY, result);

		for (IBankNode neighboor : this.neighboors) {
			// On fait suivre la réponse
			neighboor.onMessage(messageRetour);
			// On attend des ack pour la réponse
			List<Long> liste = waitS.get(result.getMessageId());

			liste.add(neighboor.getId());
		}
		// Si on a recu tous les acks
		if (waitS.get(result.getMessageId()).size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public IBank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public List<IBankNode> getNeighboors() throws RemoteException {
		return neighboors;
	}

	public void setNeighboors(List<IBankNode> neighboors) {
		this.neighboors = neighboors;
	}

	@Override
	public String getBankName() throws RemoteException {
		return bank.getBankName();
	}
}
