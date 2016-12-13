package com.ensimag.server.impl;
import java.io.Serializable;

import com.ensimag.services.bank.IAccount;
import com.ensimag.services.bank.IBankNode;

public class AccountBankNode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4969950804506576944L;

	
	private IAccount account;
	private IBankNode node;
	
	public AccountBankNode(IAccount account, IBankNode node) {
		this.setAccount(account);
		this.setNode(node);
	}

	public IBankNode getNode() {
		return node;
	}

	public void setNode(IBankNode node) {
		this.node = node;
	}

	public IAccount getAccount() {
		return account;
	}

	public void setAccount(IAccount account) {
		this.account = account;
	}

}
