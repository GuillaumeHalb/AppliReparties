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
	private List<BankNode> neighboors;
	// waitS = Liste de BankNodeId dont on attend un ack pour le message Id
	// Si List<BankNodeId> est vide, on n'attend plus de ack pour ce message.
	private Map<Long, List<Long>> waitS;  
	// Noeud up 
	// BankNodeId à qui je dois renvoyer le ack pour le message
	private Map<IBankMessage, Long> up;
	// boolean déjà_vu
	// Si IBankMessage appartient à la liste, on a déjà reçu le message (boolean = true)
	private LinkedList<IBankMessage> deja_vu; 
	
	public BankNode(Bank bank, long id) throws RemoteException {
		super();		
		this.setBank(bank);
		this.id = id;
		this.neighboors = new LinkedList<BankNode>();
		this.waitS = new HashMap<Long, List<Long>>();
		this.up = new HashMap<IBankMessage, Long>();
		this.deja_vu = new LinkedList<IBankMessage>();
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
		this.getNeighboors().add((BankNode) neighboor);
	}
	
	@Override
	public void removeNeighboor(INode<IBankMessage> neighboor) throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		this.getNeighboors().remove(neighboor);
	}
	
	
	// TODO: execute actions
	@Override
	public void onMessage(IBankMessage message) throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		
		// Si le message est pour nous
		if (message.getMessageType() == EnumMessageType.SINGLE_DEST 
				&& message.getDestinationBankId() == this.id) {
			// On envoie un ack
			
			// On execute l'action
			//this.deliverResult(message.getAction().execute(this));
			
			// On envoie le résultat
			
		}
		
		// Noeud non puit et message non déjà vu
		if (!this.isWellNode() && !this.alreadySeen(message)) {
			this.deja_vu.add(message);
			assert(message.getSenderId() != 0);
			this.up.put(message, message.getSenderId());
			for (BankNode neighboor : this.neighboors) {
				if (!(message.getSenderId() == neighboor.getId())) {
					List<Long> listWaitedAck = this.waitS.get(message.getMessageId());
					listWaitedAck.add(neighboor.getId());
					// TODO: check if this.waitS a bien été rempli. 
					// Sinon, cloner neighboor ou remplacer la liste à chaque fois. 
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
		
		
		/*
		// Check if the message has already been received
		if (this.reicevedMessage.contains(message)) {
			System.out.println("BankNode " + this.getId() + " already received: " + message.getMessageId());
		} else {
			//On ajoute le message à la liste des messages reçus
			this.reicevedMessage.add(message);
			// Si le message est pour tout le monde
			if (message.getMessageType() == EnumMessageType.BROADCAST) {
				// On envoie un ack
				Ack ack = new Ack(this.getId(), message.getMessageId());
				BankNode sender = this.getSender(message);				
				sender.onAck(ack);
				
				// On execute l'action
				try {
					Object result = message.getAction().execute(this);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// On envoie le résultat à l'envoyeur initial
				long messageId = -1; //TODO: find right number
				IBankMessage returnResultMessage = new Message(null, messageId, this.getId(), message.getOriginalBankSenderId(), EnumMessageType.DELIVERY);
				this.getSender(message).onMessage(returnResultMessage);
				// On attend un ack
				this.ackAttente.put(returnResultMessage, 1);
				// On fait tourner aux voisins qui ne l'ont pas encore eu
				for (BankNode neighboor : this.getNeighboors()) {
					if (neighboor.getId() != message.getSenderId()) {
						IBankMessage copie = message.clone();
						copie.setSenderId(this.getId());
						neighboor.onMessage(copie);
					}
				}
			} else if (message.getMessageType() == EnumMessageType.SINGLE_DEST) {
				// Si c'est pas pour nous
				if (!(message.getDestinationBankId() == this.getId())) {
					// on fait tourner aux voisins
					for (BankNode neighboor : this.getNeighboors()) {
						if (neighboor.getId() != message.getSenderId()) {
							IBankMessage copie = message.clone();
							copie.setSenderId(this.getId());
							neighboor.onMessage(copie);
						}
					}
				} else { // C'est pour nous
					// on envoie un ack
					Ack ack = new Ack(this.getId(), message.getMessageId());
					this.getSender(message).onAck(ack);
					// On exécute l'action
					try {
						Object result = message.getAction().execute(this);
					} catch (Exception e) {
						e.printStackTrace();
					}
					// On envoie le résultat
					long messageId = -1; //TODO: find right number
					IBankMessage returnResultMessage = new Message(null, messageId, this.getId(), message.getOriginalBankSenderId(), EnumMessageType.DELIVERY);
					this.getSender(message).onMessage(returnResultMessage);
					// On attend un ack
					this.ackAttente.put(returnResultMessage, 1);
				}
			} else if (message.getMessageType() == EnumMessageType.DELIVERY) {
				// Si ce n'est pas pour nous
				if (!(message.getDestinationBankId() == this.getId())) {
					// On fait tourner aux voisons
					for (BankNode neighboor : this.getNeighboors()) {
						if (neighboor.getId() != message.getSenderId()) {
							IBankMessage copie = message.clone();
							copie.setSenderId(this.getId());
							neighboor.onMessage(copie);
						}
					}
				} else { // Si c'est pour nous
					// On envoie un ack
					Ack ack = new Ack(this.getId(), message.getMessageId());
					this.getSender(message).onAck(ack);
					// TODO: que faire d'autres ?
				}
			}
		}*/
	}
	
	private BankNode getSender(IBankMessage message) throws RemoteException {
		BankNode sender = null;
		for (BankNode neighboor : getNeighboors()) {
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
	
	//TODO
	@Override
	public void onAck(IAck ack) throws RemoteException {		
		if (this.id < 0) {
			throw new RemoteException();
		}
		System.out.println("BankNode onAck listAckWaited avant suppression du nouveau ack : " + this.waitS.get(ack.getAckMessageId()));
		List<Long> listAckWaited = this.waitS.get(ack.getAckMessageId());
		boolean removed = false;
		for (int i = 0; i < listAckWaited.size(); i++) {
			if (listAckWaited.get(i) == ack.getAckSenderId()) {
				listAckWaited.remove(i);
				removed = true;
			}
		}
		if (!removed) {
			System.out.println("PB: BankNode onAck: no element deleted. Didn't expected ack from " + ack.getAckSenderId());
		}
		// Expected : un élément en moins.
		System.out.println("BankNode onAck listAckWaited après suppression du nouveau ack : " + this.waitS.get(ack.getAckMessageId()));
		if (listAckWaited.size() == 0) {
			if (this.up.get(ack.getAckMessageId()) != 0) {
				boolean neighboorFound = false;
				for (BankNode neighboor : this.neighboors) {
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
	
	//TODO
	@Override
	public List<IResult<? extends Serializable>> getResultForMessage(long messageId) throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		return null;
	}
	
	//TODO
	@Override
	public Boolean deliverResult(IResult<Serializable> result) throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		return null;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public List<BankNode> getNeighboors() {
		return neighboors;
	}

	public void setNeighboors(List<BankNode> neighboors) {
		this.neighboors = neighboors;
	}
}
