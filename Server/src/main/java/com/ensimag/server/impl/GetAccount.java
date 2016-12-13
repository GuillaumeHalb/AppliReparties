package com.ensimag.server.impl;

import java.io.Serializable;

import com.ensimag.services.bank.IBankAction;
import com.ensimag.services.bank.IBankNode;

public class GetAccount implements IBankAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8717717905900651015L;
	
	private long accountNumber;
	
	public GetAccount(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public Serializable execute(IBankNode node) throws Exception {
		return new AccountBankNode(node.getAccount(accountNumber), node);
	}

}
