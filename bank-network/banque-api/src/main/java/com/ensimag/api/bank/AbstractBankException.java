package com.ensimag.api.bank;

public abstract class AbstractBankException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1819049648485345419L;

	private final IAccount account;

	public AbstractBankException(IAccount account) {
		super();
		this.account = account;
	}

	public AbstractBankException(IAccount account, String message) {
		super(message);
		this.account = account;
	}

	public AbstractBankException(IAccount account, Throwable t) {
		super(t);
		this.account = account;
	}

	public IAccount getAccount() {
		return this.account;
	}

	public abstract String getMoreInformation();

}
