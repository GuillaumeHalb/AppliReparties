package com.ensimag.server.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;

import com.ensimag.services.bank.IAccount;
import com.ensimag.services.bank.IBank;
import com.ensimag.services.bank.IUser;

public class Bank implements IBank {

	private String bankName;
	private int bankID;
	private Map<Long, IAccount> accountList;

	public Bank(int bankID, String bankName) {
		super();
		this.bankID = bankID;
		this.accountList = new HashMap<Long, IAccount>();
		this.setBankName(bankName);
	}

	public Bank(int bankID, HashMap<Long, IAccount> accountList, String bankName) {
		super();
		this.bankID = bankID;
		this.accountList = accountList;
		this.setBankName(bankName);
	}

	@Override
	public List<IAccount> getAccounts() throws RemoteException {
		if (accountList == null) {
			throw new RemoteException();
		}

		return (List) this.accountList.values();
	}

	@Override
	public IAccount getAccount(long number) throws AccountNotFoundException, RemoteException {
		if (accountList == null) {
			throw new RemoteException();
		}

		try {
			return this.accountList.get(number);
		} catch (Exception e) {
			throw new AccountNotFoundException();
		}
	}

	@Override
	public IAccount openAccount(IUser user) throws RemoteException {
		if (accountList == null) {
			throw new RemoteException();
		}
		IAccount account = new Account(user, this.accountList.size() + 1, 0);
		this.accountList.put((long) (this.accountList.size() + 1), account);
		return account;
	}

	@Override
	public boolean closeAccount(long number) throws AccountNotFoundException, RemoteException {
		if (accountList == null) {
			throw new RemoteException();
		}
		IAccount accountToClose = getAccount(number);
		if (accountToClose != null) {
			this.accountList.remove(accountToClose.getAccountNumber(), accountToClose);
			return true;
		} else {
			throw new AccountNotFoundException("Pas de compte numero" + accountToClose.getAccountNumber() + "à " + this.bankName);
		}
	}

	@Override
	public long getBankId() {
		return this.bankID;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}
