package com.ensimag.services;

/**
 * Exception in case of a lack of money
 * 
 * @author guillaume
 * 
 */
public class NotEnoughMoneyException extends AbstractBankException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7125679145121461203L;

	public NotEnoughMoneyException(IAccount account) {
		super(account);
	}

	public NotEnoughMoneyException(IAccount account, String message) {
		super(account, message);
	}

	public NotEnoughMoneyException(IAccount account, Throwable t) {
		super(account, t);
	}

	@Override
	public String getMoreInformation() {
		// TODO Auto-generated method stub
		// return null;
		throw new UnsupportedOperationException();
	}
}
