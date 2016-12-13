package com.ensimag.server.impl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.ensimag.services.bank.IAccount;
import com.ensimag.services.bank.IUser;
import com.ensimag.services.bank.NotEnoughMoneyException;

public class Account extends UnicastRemoteObject implements IAccount {

	private static final long serialVersionUID = 1412244524309258892L;
	
	private IUser user;
	private int number;
	private int balance;
	private int overdraw;
	
	public Account(IUser user, int number, int balance) throws RemoteException{
		super();
		this.user = user;
		this.number = number;
		this.balance = balance;
		this.overdraw = 0;
	}
	
	@Override
	public IUser getAccountUser() {
		return this.user;
	}

	@Override
	public long getAccountNumber() {
		return this.number;
	}

	@Override
	public int add(int amount) {
		assert(amount >= 0);
		this.balance += amount;
		return this.getTotal();
	}

	@Override
	public int remove(int amount) throws NotEnoughMoneyException {
		assert(amount >= 0);
		if (this.balance + this.overdraw >= amount)	{
			this.balance -= amount;
			return this.getTotal();
		} else {
			throw new NotEnoughMoneyException(this, "Pas assez d'argent (maximum : " + this.getTotal() + ", demandé : " + amount + ")");
		}
	}

	@Override
	public int getTotal() {
		return this.balance + this.overdraw;
	}

	@Override
	public int setAllowedOverdraw(int overdraw) {
		assert(overdraw >= 0);
		
		this.overdraw = overdraw;
		return this.overdraw;
	}
}
