package com.ensimag.services.bank;

import java.rmi.RemoteException;
import java.util.List;

import com.ensimag.services.node.INode;

/**
 * Specify what is a bank node
 * 
 * @author guillaume
 * 
 */
public interface IBankNode extends IBankOperations, INode<IBankMessage> {
	@Override
	void addNeighboor(INode<IBankMessage> neighboor) throws RemoteException;
	
	String getBankName() throws RemoteException;
	
	List<IBankNode> getNeighboors() throws RemoteException;
}
