package com.ensimag.server.impl;

import com.ensimag.services.bank.IAccount;
import com.ensimag.services.bank.IBankAction;
import com.ensimag.services.bank.IBankNode;
import com.ensimag.services.bank.IUser;

public class AddAccount implements IBankAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -644404690935148149L;
	private IUser user;
	
	public AddAccount(IUser user) {
		super();
		this.user = user;
	}

	@Override
	public AccountBankNode execute(IBankNode node) throws Exception {

		return new AccountBankNode(node.openAccount(user),node);
	}

}
