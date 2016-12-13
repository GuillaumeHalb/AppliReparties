package com.ensimag.server.impl;

import java.io.Serializable;

import com.ensimag.services.bank.IAccount;
import com.ensimag.services.bank.IBankAction;
import com.ensimag.services.bank.IBankNode;

public class RemoveAmount implements IBankAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2263088098833637477L;
	private long accountNumber;
	private int amount;
	
	public RemoveAmount(long accountNumber, int amount) {
		this.accountNumber = accountNumber;
		this.amount = amount;
	}

	@Override
	public Serializable execute(IBankNode node) throws Exception {
		IAccount account = node.getAccount(accountNumber);
		return account.remove(amount);
	}

}
