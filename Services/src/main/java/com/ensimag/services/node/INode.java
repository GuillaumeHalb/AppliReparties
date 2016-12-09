package com.ensimag.services.node;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.ensimag.services.message.IAck;
import com.ensimag.services.message.IResult;

/**
 * Node of the network
 * 
 * @author guillaume
 * 
 */
public interface INode<MessageType> extends Serializable, Remote {

	/**
	 * @return the id of the node
	 * @throws RemoteException
	 */
	long getId() throws RemoteException;

	/**
	 * Execute the message action on the node if the node is the target,
	 * propagate the message to its neighboors otherwise
	 * 
	 * @param message
	 *            the message to handle
	 * @throws RemoteException
	 * @throws Exception 
	 */
	void onMessage(MessageType message) throws RemoteException, Exception;

	/**
	 * Handles the ack of a given message
	 * 
	 * @param ack
	 *            the ack
	 * @throws RemoteException
	 */
	void onAck(IAck ack) throws RemoteException;

	/**
	 * Add a node as a known neighboor
	 * 
	 * @param neighboor
	 *            the neighboor to add
	 * @throws RemoteException
	 */
	void addNeighboor(INode<MessageType> neighboor) throws RemoteException;

	/**
	 * Remove a neighboor from the node
	 * 
	 * @param neighboor
	 *            the neighboor to remove
	 * @throws RemoteException
	 */
	void removeNeighboor(INode<MessageType> neighboor) throws RemoteException;

	/**
	 * Get the result for the given message
	 * 
	 * @param messageId
	 *            the message id for which a result is waited
	 * @return the result of the sent message
	 */
	List<IResult<? extends Serializable>> getResultForMessage(long messageId) throws RemoteException;

	/**
	 * Deliver a result to a bank
	 * 
	 * @param result
	 *            the result to deliver
	 * @return <code>true</code> if the result is delivered, <code>false</code>
	 *         otherwise
	 */
	Boolean deliverResult(IResult<Serializable> result) throws RemoteException;

}
