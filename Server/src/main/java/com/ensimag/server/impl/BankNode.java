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
	// waitS = Liste de BankNodeId dont on attend un ack pour le message (Id,
	// type)
	// Si List<BankNodeId> est vide, on n'attend plus de ack pour ce message.
	private Map<CoupleMessageIDType, List<Long>> waitS;
	// Noeud up
	// Numero du message messageId <->BankNodeId à qui je dois renvoyer le ack
	// pour le message
	// Pb: on s'attend à deux up avec le même messageId : la requête et la
	// réponse
	private Map<CoupleMessageIDType, Long> up;
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
		this.waitS = new HashMap<CoupleMessageIDType, List<Long>>();
		this.up = new HashMap<CoupleMessageIDType, Long>();
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
	 * 
	 * @throws Exception
	 */
	private IResult<Serializable> executeAction(IBankMessage message) throws Exception {
		// On execute l'action
		Serializable data = null;
		try {
			data = message.getAction().execute(this);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		IResult<Serializable> result = new Result(data, message.getMessageId());
		return result;
	}

	/**
	 * Envoie un ack
	 */
	private void sendAckForMessage(IBankMessage message) {
		System.out.println("\t send ack for message " + message.toString());
		try {
			Ack ack = new Ack(this.getId(), message.getMessageId(), message.getMessageType());
			getSender(message).onAck(ack);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void followMessage(IBankMessage message) throws Exception {
		CoupleMessageIDType couple = new CoupleMessageIDType(message.getMessageId(), message.getMessageType());
		List<Long> listWaitedAck = this.waitS.get(couple);
		if (listWaitedAck == null) {
			listWaitedAck = new LinkedList<Long>();
		}
		int runningNeighboors = 0;
		for (IBankNode neighboor : this.neighboors) {
			try {
				if (message.getSenderId() != neighboor.getId()) {
					listWaitedAck.add(neighboor.getId());
					runningNeighboors++;
				}
			} catch (Exception e) {
				System.out.println("Un noeud voisin ne repond pas, redirection du message vers le noeud suivant");
			}
		}
		if (runningNeighboors == 0) {
			throw new Exception("Noeud isolé ! \n Requête impossible");
		}
		this.waitS.put(couple, listWaitedAck);
		for (IBankNode neighboor : this.neighboors) {
			if (message.getMessageType().equals(EnumMessageType.BROADCAST)) {
				System.out.println("resultats : " + this.waitingResults.toString());
			}
			try {
				if (message.getSenderId() != neighboor.getId()) {
					IBankMessage messageCloned = message.clone();
					messageCloned.setSenderId(this.id);
					System.out.println("\t fait suivre le message de type " + message.getMessageType() + " n°"
							+ message.getMessageId() + " à " + neighboor.getId());
					neighboor.onMessage(messageCloned);
				}
			} catch (Exception e) {

			}
		}
	}

	// Send result only to the message sender
	private void sendResult(IBankMessage message, IResult<Serializable> result) throws Exception {
		IBankMessage resultMessage = new Message(null, message.getMessageId(), this.id,
				message.getOriginalBankSenderId(), EnumMessageType.DELIVERY, result);

		CoupleMessageIDType couple = new CoupleMessageIDType(message.getMessageId(), message.getMessageType());
		if (this.up.get(couple) != null) {
			this.up.remove(couple);
		}

		List<Long> listWaitedAck = this.waitS.get(couple);
		if (listWaitedAck == null) {
			listWaitedAck = new LinkedList<Long>();
		}
		listWaitedAck.add(message.getSenderId());
		this.waitS.put(new CoupleMessageIDType(resultMessage.getMessageId(), EnumMessageType.DELIVERY), listWaitedAck);
		try {
			System.out.println("\t Envoie la reponse à " + this.getSender(message).getId());
			this.getSender(message).onMessage(resultMessage);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void stockResult(IBankMessage message) {
		System.out.println("\t Recupere le resultat du message");
		LinkedList<IResult<? extends Serializable>> resultList = this.waitingResults.get(message.getMessageId());
		CoupleMessageIDType couple = new CoupleMessageIDType(message.getMessageId(), message.getMessageType());

		if (this.up.get(couple) != null) {
			this.up.remove(couple);
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
	 * @throws Exception
	 */
	@Override
	public void onMessage(IBankMessage message) throws Exception {
		System.out.println("onMessage " + message.toString());
		if (this.id < 0) {
			throw new RemoteException();
		}

		// On a déjà vu le message
		if (dejaVu(message)) {
			System.out.println("\t deja vu");
			sendAckForMessage(message);
			return;
		} else {
			System.out.println("\t Premier passage");
			this.deja_vu.add(message);
			// On est un noeud puit
			if (isWellNode()) {
				System.out.println("\t noeud puit");
				this.sendAckForMessage(message);
				if ((message.getMessageType().equals(EnumMessageType.SINGLE_DEST)
						&& message.getDestinationBankId() == this.id)
						|| message.getMessageType().equals(EnumMessageType.BROADCAST)) {
					System.out.println("\t execute l'action du message");
					IResult<Serializable> result = this.executeAction(message);
					sendResult(message, result);
					return;
				} else if (message.getMessageType().equals(EnumMessageType.DELIVERY)) {
					if (message.getDestinationBankId() == this.id) {
						sendAckForMessage(message);
						stockResult(message);
					}
				} 
				return;
				// On n'est pas un noeud puit
			} else {
				System.out.println("\t noeud non puit");
				if (message.getOriginalBankSenderId() != this.id) {
					CoupleMessageIDType couple = new CoupleMessageIDType(message.getMessageId(),
							message.getMessageType());
					this.up.put(couple, message.getSenderId());
				}
				// Le message est pour tout le monde
				if (message.getMessageType().equals(EnumMessageType.BROADCAST)) {
					System.out.println("\t Broadcast");
					IResult<Serializable> result = executeAction(message);
					if (message.getOriginalBankSenderId() != this.getId()) {
						sendResult(message, result);
					} else {
						IBankMessage messageResult = new Message(null, message.getMessageId(), this.id,
								message.getOriginalBankSenderId(), EnumMessageType.DELIVERY, result);
						this.onMessage(messageResult);
					}
					followMessage(message);
					return;
				} else if (message.getMessageType().equals(EnumMessageType.SINGLE_DEST)
						&& message.getDestinationBankId() == this.id) {
					System.out.println("\t execute l'action du message");
					if (message.getOriginalBankSenderId() == message.getDestinationBankId()) {
						System.out.println("\t le message est envoyé à nous meme");
						IResult<Serializable> result = executeAction(message);
						IBankMessage messageResult = new Message(null, message.getMessageId(), this.id,
								message.getOriginalBankSenderId(), EnumMessageType.DELIVERY, result);
						this.onMessage(messageResult);
					} else {
						System.out.println();
						sendAckForMessage(message);
						IResult<Serializable> result = executeAction(message);
						sendResult(message, result);
					}
					return;
				} else if (message.getMessageType().equals(EnumMessageType.DELIVERY)
						&& message.getDestinationBankId() == this.id) {
					if (message.getOriginalBankSenderId() != message.getDestinationBankId()) {
						sendAckForMessage(message);
					} else {
						System.out.println("\t on recoit notre propre resultat");
					}
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
			try {
				if (neighboor.getId() == message.getSenderId()) {
					sender = neighboor;
				}
			} catch (Exception e) {

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
		System.out.println("\t reçoit le ack : " + ack.toString());

		CoupleMessageIDType couple = new CoupleMessageIDType(ack.getAckMessageId(), ack.getType());
		List<Long> listAckWaited = this.waitS.get(couple);
		for (int i = 0; i < listAckWaited.size(); i++) {
			if (listAckWaited.get(i) == ack.getAckSenderId()) {
				listAckWaited.remove(i);
			}
		}
		this.waitS.put(couple, listAckWaited);

		if (listAckWaited.size() == 0) {
			// Si on n'est pas à l'envoyeur initial
			System.out.println("\t on a recu tous les acks en attente");
			if (this.up.get(couple) != null) {
				for (IBankNode neighboor : this.neighboors) {
					if (neighboor.getId() == this.up.get(couple)) { // TODO: pas
																	// sur ici
						IAck ackSuivant = new Ack(this.getId(), ack.getAckMessageId(), ack.getType());
						System.out.println("\t fait suivre le ack à " + neighboor.getId());
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
		System.out.println("get res: " + this.waitingResults.get(messageId).toString());
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
		System.out.println("deliverResult");
		return true;
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
