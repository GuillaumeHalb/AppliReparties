package com.ensimag.server.impl;

import java.io.Serializable;

import com.ensimag.services.bank.IBankAction;
import com.ensimag.services.bank.IBankNode;

public class AddAmount implements IBankAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1209623170569772896L;

	private long accountNumber;
	private int amount;
	public AddAmount(long accountNumber, int amount) {
		this.accountNumber = accountNumber;
		this.amount = amount;
	}
	
	@Override
	public Serializable execute(IBankNode node) throws Exception {
		return node.getAccount(this.accountNumber).add(amount);
	}

}
