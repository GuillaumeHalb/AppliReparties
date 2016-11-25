package com.ensimag.server.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.AccountNotFoundException;

import com.ensimag.services.bank.IAccount;
import com.ensimag.services.bank.IBank;
import com.ensimag.services.bank.IUser;

public class Bank implements IBank {
	
	private int bankID;
	private List<IAccount> accountList;

	public Bank(int bankID) {
		super();
		this.bankID = bankID;
		this.accountList = new ArrayList<IAccount>();
	}


	public Bank(int bankID, List<IAccount> accountList) {
		super();
		this.bankID = bankID;
		this.accountList = accountList;
	}


	@Override
	public List<IAccount> getAccounts() throws RemoteException {
		if(accountList == null){
			throw new RemoteException();
		}
		
		return this.accountList;
	}

	@Override
	public IAccount getAccount(long number) throws AccountNotFoundException, RemoteException {
		if(accountList == null){
			throw new RemoteException();
		}
		
		try
		{
			//Pq doit on le caster en int ?
			return this.accountList.get((int) number);
		}
		catch(Exception e)
		{
			throw new AccountNotFoundException();
		}
	}

	@Override
	public IAccount openAccount(IUser user) throws RemoteException {
		if(accountList == null){
			throw new RemoteException();
		}
		
		IAccount account = new Account(user);
		this.accountList.add(account);
		return account;
	}

	@Override
	public boolean closeAccount(long number) throws AccountNotFoundException, RemoteException {
		if(accountList == null){
			throw new RemoteException();
		}
		
		IAccount accountToClose = getAccount(number);
		if (this.accountList.remove(accountToClose)) {
			return true;
		} else {
			if (!this.accountList.contains(accountToClose)) {
				throw new AccountNotFoundException("Account not in this bank");
			}
		}
		throw new AccountNotFoundException();
	}

	@Override
	public long getBankId() {
		return this.bankID;
	}

}
