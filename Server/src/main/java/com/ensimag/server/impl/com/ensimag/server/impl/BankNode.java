package com.ensimag.server.impl;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;
import javax.swing.text.DefaultEditorKit.InsertBreakAction;

import com.ensimag.services.message.EnumMessageType;
import com.ensimag.services.bank.IAccount;
import com.ensimag.services.bank.IBankMessage;
import com.ensimag.services.bank.IBankNode;
import com.ensimag.services.bank.IUser;
import com.ensimag.services.message.IAck;
import com.ensimag.services.message.IMessage;
import com.ensimag.services.message.IResult;
import com.ensimag.services.node.INode;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class BankNode extends UnicastRemoteObject implements IBankNode {		

	private static final long serialVersionUID = -2414181384542180339L;
	
	private Bank bank;
	private long id;
	private List<BankNode> neighboors;
	private Map<IBankMessage, Integer> ackAttente; // IBankMessage, nombre de ack en attente 
	private LinkedList<IBankMessage> reicevedMessage; // Message this node has already received 
	
	public BankNode(Bank bank, long id) throws RemoteException {
		super();		
		this.bank = bank;
		this.id = id;
		this.neighboors = new LinkedList<BankNode>();
		this.ackAttente = new HashMap<IBankMessage, Integer>();
		this.reicevedMessage = new LinkedList<IBankMessage>();
	}
	
	@Override
	public List<IAccount> getAccounts() throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		return this.bank.getAccounts();
	}

	@Override
	public IAccount getAccount(long number) throws AccountNotFoundException, RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		return this.bank.getAccount(number);
	}

	@Override
	public IAccount openAccount(IUser user) throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		return this.bank.openAccount(user);
	}

	@Override
	public boolean closeAccount(long number) throws AccountNotFoundException, RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		return this.bank.closeAccount(number);
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
		this.neighboors.add((BankNode) neighboor);
	}
	
	@Override
	public void removeNeighboor(INode<IBankMessage> neighboor) throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		this.neighboors.remove(neighboor);
	}
	
	@Override
	public void onMessage(IBankMessage message) throws RemoteException {
		if (this.id < 0) {
			throw new RemoteException();
		}
		
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
				for (BankNode neighboor : this.neighboors) {
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
					for (BankNode neighboor : this.neighboors) {
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
					for (BankNode neighboor : this.neighboors) {
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
		}
	}
	
	private BankNode getSender(IBankMessage message) throws RemoteException {
		BankNode sender = null;
		for (BankNode neighboor : neighboors) {
			if (neighboor.getId() == message.getSenderId()) {
				sender = neighboor;
			}
		}
		if (sender == null) {
			throw new RemoteException("Sender is not in the neighboors");
		}
		return sender;
	}
	
	//TODO
	@Override
	public void onAck(IAck ack) throws RemoteException {		
		if (this.id < 0) {
			throw new RemoteException();
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
}
