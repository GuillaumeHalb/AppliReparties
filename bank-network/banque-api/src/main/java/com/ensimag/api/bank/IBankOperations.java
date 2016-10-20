package com.ensimag.api.bank;

import java.rmi.RemoteException;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

/**
 * All operations that can be done on a bank
 * 
 * @author guillaume
 * 
 */
public interface IBankOperations {
	/**
	 * @return the list of all accounts in the bank
	 */
	List<IAccount> getAccounts() throws RemoteException;

	/**
	 * @param number
	 *            the number of the account to get
	 * @return the account corresponding to the number
	 * @throws AccountNotFoundException
	 *             if the account is not found
	 */
	IAccount getAccount(long number) throws AccountNotFoundException, RemoteException;

	/**
	 * Open an account for the given user
	 * 
	 * @param user
	 *            the user
	 * @return the created account
	 */
	IAccount openAccount(IUser user) throws RemoteException;

	/**
	 * Close the account corresponding to the number
	 * 
	 * @param number
	 *            the account number
	 * @return <code>true</code> if the account has been deleted,
	 *         <code>false</code> otherwise
	 * @throws AccountNotFoundException
	 *             if the account is not found
	 */
	boolean closeAccount(long number) throws AccountNotFoundException, RemoteException;

}
