package com.ensimag.server.impl;

import com.ensimag.services.bank.IBankAction;
import com.ensimag.services.bank.IBankNode;

public class CloseAccount implements IBankAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2268974052446345222L;

	private long accountNumber;
	
	public CloseAccount(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	@Override
	public Boolean execute(IBankNode node) throws Exception {
		return node.closeAccount(accountNumber);
	}
}
