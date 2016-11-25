package com.ensimag.services;

import java.rmi.RemoteException;

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
}
