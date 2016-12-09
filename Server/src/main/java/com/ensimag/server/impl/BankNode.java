package com.ensimag.server.impl;

import java.io.Serializable;
import java.net.ConnectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.login.AccountNotFoundException;

import com.ensimag.services.bank.IAccount;
import com.ensimag.services.bank.IBank;
import com.ensimag.services.bank.IBankMessage;
import com.ensimag.services.bank.IBankNode;
import com.ensimag.services.bank.IUser;
import com.ensimag.services.message.EnumMessageType;
import com.ensimag.services.message.IAck;
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
	// Numero du message messageId <->BankNodeId à qui je dois renvoyer le ack
	// pour le message
	// Pb: on s'attend à deux up avec le même messageId : la requête et la
	// réponse
	private Map<Long, Long> up;
	// boolean déjà_vu
	// Si IBankMessage appartient à la liste, on a déjà reçu le message (boolean
	// = true)
	private Set<IBankMessage> deja_vu;
	// Message idMessage dont on attend n resultats
	private Map<Long, LinkedList<IResult<? extends Serializable>>> waitingResults;

	public BankNode(Bank bank, long id) throws RemoteException {
		super();
		this.setBank(bank);
		this.id = id;
		this.neighboors = new LinkedList<IBankNode>();
		this.waitS = new HashMap<Long, List<Long>>();
		this.up = new HashMap<Long, Long>();
		this.deja_vu = new HashSet<IBankMessage>();
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
	 * Execute l'action et envoie le résultat
	 */
	private IResult<Serializable> executeAction(IBankMessage message) {
		// On execute l'action
		Serializable data = null;
		try {
			data = message.getAction().execute(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		IResult<Serializable> result = new Result(data, message.getMessageId());
		return result;
	}

	/**
	 * Envoie un ack
	 */
	private void sendAckForMessage(IBankMessage message) {
		try {
			Ack ack = new Ack(this.getId(), message.getMessageId());
			getSender(message).onAck(ack);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void followMessage(IBankMessage message) {
		List<Long> listWaitedAck = this.waitS.get(message.getMessageId());
		if (listWaitedAck == null) {
			listWaitedAck = new LinkedList<Long>();
		}

		for (IBankNode neighboor : this.neighboors) {
			try {
				if (message.getSenderId() != neighboor.getId()) {
					listWaitedAck.add(neighboor.getId());
				}
			} catch (Exception e) {

			}
		}
		this.waitS.put(message.getMessageId(), listWaitedAck);
		for (IBankNode neighboor : this.neighboors) {
			try {
				if (message.getSenderId() != neighboor.getId()) {
					IBankMessage messageCloned = message.clone();
					messageCloned.setSenderId(this.id);
					neighboor.onMessage(messageCloned);
				}
			} catch (Exception e) {

			}
		}
	}

	// Send result only to the message sender
	private void sendResult(IBankMessage message, IResult<Serializable> result) {
		IBankMessage resultMessage = new Message(null, message.getMessageId(), this.id,
				message.getOriginalBankSenderId(), EnumMessageType.DELIVERY, result);

		if (this.up.get(message.getMessageId()) != null) {
			this.up.remove(message.getMessageId());
		}

		List<Long> listWaitedAck = this.waitS.get(message.getMessageId());
		if (listWaitedAck == null) {
			listWaitedAck = new LinkedList<Long>();
		}
		listWaitedAck.add(message.getSenderId());
		this.waitS.put(message.getMessageId(), listWaitedAck);
		try {
			this.getSender(message).onMessage(resultMessage);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void stockResult(IBankMessage message) {
		LinkedList<IResult<? extends Serializable>> resultList = this.waitingResults.get(message.getMessageId());

		if (this.up.get(message.getMessageId()) != null) {
			this.up.remove(message.getMessageId());
		}

		if (resultList == null) {
			resultList = new LinkedList<>();
		}
		resultList.add(message.getResult());
		this.waitingResults.put(message.getMessageId(), resultList);
	}

	private boolean dejaVu(IBankMessage message) {
		for (IBankMessage messageDejaVu : this.deja_vu) {
			if (message.equals(messageDejaVu)) {
				return true;
			}
		}
		return false;
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
		if (this.id < 0) {
			throw new RemoteException();
		}

		// On a déjà vu le message
		if (dejaVu(message)) {
			sendAckForMessage(message);
			return;
		} else {
			this.deja_vu.add(message);
			// On est un noeud puit
			if (isWellNode()) {
				this.sendAckForMessage(message);
				if ((message.getMessageType().equals(EnumMessageType.SINGLE_DEST)
						&& message.getDestinationBankId() == this.id)
						|| message.getMessageType().equals(EnumMessageType.BROADCAST)) {
					IResult<Serializable> result = this.executeAction(message);
					sendResult(message, result);
					return;
				} else if (message.getMessageType().equals(EnumMessageType.DELIVERY)) {
					if (message.getDestinationBankId() == this.id) {
						sendAckForMessage(message);
						stockResult(message);
					}
				} else {
					if ((message.getMessageType().equals(EnumMessageType.SINGLE_DEST)
							&& !(message.getDestinationBankId() == this.id))) {
						System.out.println("\t single dest pas pour nous");
					} else {
						System.out.println("\t Noeud puit onMessage : On ne doit pas être là");
					}
				}
				return;
				// On n'est pas un noeud puit
			} else {
				this.up.put(message.getMessageId(), message.getSenderId());
				// Le message est pour tout le monde
				if (message.getMessageType().equals(EnumMessageType.BROADCAST)) {
					IResult<Serializable> result = executeAction(message);
					sendResult(message, result);
					followMessage(message);
					return;
				} else if (message.getMessageType().equals(EnumMessageType.SINGLE_DEST)
						&& message.getDestinationBankId() == this.id) {
					sendAckForMessage(message);
					IResult<Serializable> result = executeAction(message);
					sendResult(message, result);
					return;
				} else if (message.getMessageType().equals(EnumMessageType.DELIVERY)
						&& message.getDestinationBankId() == this.id) {
					sendAckForMessage(message);
					stockResult(message);
					return;
				} else {
					followMessage(message);
					return;
				}
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

	// Returns true if this node is a well
	private boolean isWellNode() {
		if (this.neighboors.size() == 1) {
			return true;
		}
		return false;
	}

	@Override
	public void onAck(IAck ack) throws RemoteException {
		System.out.println("onAck " + ack.toString());

		List<Long> listAckWaited = this.waitS.get(ack.getAckMessageId());
		for (int i = 0; i < listAckWaited.size(); i++) {
			if (listAckWaited.get(i) == ack.getAckSenderId()) {
				listAckWaited.remove(i);
			}
		}

		if (listAckWaited.size() == 0) {
			// Si on n'est pas à l'envoyeur initial
			if (this.up.get(ack.getAckMessageId()) != null) {
				for (IBankNode neighboor : this.neighboors) {
					if (neighboor.getId() == this.up.get(ack.getAckMessageId())) {
						IAck ackSuivant = new Ack(this.getId(), ack.getAckMessageId());
						neighboor.onAck(ackSuivant);
					}
				}
			} else {
				System.out.println("\t Ack back to primary sender : OK");
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
		// for (IBankMessage message : this.deja_vu) {
		// if (message.getMessageId() == result.getMessageId()) {
		// destinationBankId = message.getOriginalBankSenderId();
		// }
		// }

		if (destinationBankId == -1) {
			System.out.println("ERREUR BankNode DeliverResult : lors de l'initialisation de la destination");
			return false;
		}

		Message messageRetour = new Message(null, result.getMessageId(), this.getId(), destinationBankId,
				EnumMessageType.DELIVERY, result);

		System.out.println("On fait suivre le résultat aux " + this.neighboors.size() + " voisins");
		List<Long> liste = waitS.get(result.getMessageId());
		if (liste == null) {
			liste = new LinkedList<Long>();
		}
		for (IBankNode neighboor : this.neighboors) {
			System.out.println("Voisin : " + neighboor.getBankName());
			// On fait suivre la réponse
			neighboor.onMessage(messageRetour);
			// On attend des ack pour la réponse
			liste.add(neighboor.getId());
		}
		waitS.put(result.getMessageId(), liste);
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

	@Override
	public IBankNode clone() {
		try {
			return new BankNode(this.bank, this.id);
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
}
