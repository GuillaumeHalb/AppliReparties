package com.ensimag.services.bank;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IAccount extends Serializable, Remote {

	/**
	 * @return the account user
	 */
	IUser getAccountUser() throws RemoteException;

	/**
	 * @return the account number
	 */
	long getAccountNumber() throws RemoteException;

	/**
	 * Add the amount of cash to the account
	 * 
	 * @param amount
	 *            the amount to add
	 * @return the new cash amount
	 */
	int add(int amount) throws RemoteException;

	/**
	 * Remove the amount on the account
	 * 
	 * @param amount
	 *            the amount to remove
	 * @return the new cash amount
	 * @throws NotEnoughMoneyException
	 *             if there is not enough money to remove the amount of money,
	 *             overdraw included
	 */
	int remove(int amount) throws NotEnoughMoneyException, RemoteException;

	/**
	 * @return the total of available money on the account
	 */
	int getTotal() throws RemoteException;

	/**
	 * Set the overdraw for the account
	 * 
	 * @param overdraw
	 *            the overdraw to set
	 * @return the set overdraw
	 */
	int setAllowedOverdraw(int overdraw) throws RemoteException;
}
