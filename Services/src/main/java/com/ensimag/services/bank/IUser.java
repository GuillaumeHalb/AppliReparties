package com.ensimag.services.bank;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IUser extends Serializable, Remote {

	/**
	 * @return the name of the user
	 */
	String getName() throws RemoteException;

	/**
	 * @return the first name
	 */
	String getFirstName() throws RemoteException;

	/**
	 * @return the age
	 */
	String getAge() throws RemoteException;
}
