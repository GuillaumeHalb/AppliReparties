package com.ensimag.server.impl;

import java.io.Serializable;

import com.ensimag.services.bank.IAccount;
import com.ensimag.services.bank.IBankAction;
import com.ensimag.services.bank.IBankNode;

public class AuthorizeOverdraw implements IBankAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1674646404391100140L;
	private long accountNumber;
	private int overdraw;
	public AuthorizeOverdraw(long accountNumber, int overdraw) {
		this.accountNumber = accountNumber;
		this.overdraw = overdraw;
	}

	@Override
	public Serializable execute(IBankNode node) throws Exception {
		IAccount account = node.getAccount(accountNumber);
		return account.setAllowedOverdraw(overdraw);
	}

}
