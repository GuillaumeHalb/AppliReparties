package com.ensimag.server.impl;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;

import com.ensimag.services.bank.IAccount;
import com.ensimag.services.bank.IBankMessage;
import com.ensimag.services.bank.IBankNode;
import com.ensimag.services.bank.IUser;
import com.ensimag.services.message.IAck;
import com.ensimag.services.message.IResult;
import com.ensimag.services.node.INode;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class BankNode extends UnicastRemoteObject implements IBankNode {		

	private static final long serialVersionUID = -2414181384542180339L;
	
	private Bank bank;
	private int id;
//	private List<INode<IBankMessage>> neighboors;
	private Map<Long, BankNode> neighboors;
	
	public BankNode(Bank bank, int id) throws RemoteException {
		super();		
		this.bank = bank;
		this.id = id;
		this.neighboors = new HashMap<Long, BankNode>();
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
		this.neighboors.put(neighboor.getId(), (BankNode) neighboor);
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
		// Send ack to the sender
/*		INode<IBankMessage> sender = null;
		for (INode<IBankMessage> neighboor : neighboors) {
			if (neighboor.getId() == message.getSenderId()) {
				sender = neighboor;
			}
		}
		assert(sender != null);
		Ack ack = new Ack(this.getId(), message.getMessageId());
		sender.onAck(ack);
		
		if (message.getDestinationBankId() == this.bank.getBankId()) {
			try {
				message.getAction().execute(this);
			} catch (Exception exception) {
				throw new RemoteException("Error while executing message action");
			}
		} else {
			message.setSenderId(this.id);
			for (INode<IBankMessage> neighboor : this.neighboors) {
				neighboor.onMessage(message);
			}
		} */
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
