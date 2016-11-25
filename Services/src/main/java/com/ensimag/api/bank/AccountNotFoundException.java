package com.ensimag.services;

/**
 * Exception in case of a not found account
 * 
 * @author guillaume
 * 
 */
public class AccountNotFoundException extends AbstractBankException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7353194012111929052L;

	public AccountNotFoundException(IAccount account) {
		super(account);
	}

	public AccountNotFoundException(IAccount account, String message) {
		super(account, message);
	}

	public AccountNotFoundException(IAccount account, Throwable t) {
		super(account, t);
	}

	@Override
	public String getMoreInformation() {
		// TODO Auto-generated method stub
		// return null;
		throw new UnsupportedOperationException();
	}

}
