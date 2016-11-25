package com.ensimag.services;

import java.io.Serializable;

public interface IAccount extends Serializable {

	/**
	 * @return the account user
	 */
	IUser getAccountUser();

	/**
	 * @return the account number
	 */
	long getAccountNumber();

	/**
	 * Add the amount of cash to the account
	 * 
	 * @param amount
	 *            the amount to add
	 * @return the new cash amount
	 */
	int add(int amount);

	/**
	 * Remove the amount on the account
	 * 
	 * @param amount
	 *            the amount to remove
	 * @return the new cash amount
	 * @throws NotEnoughMoneyException
	 *             if there is not enough money to remove the amount of money,
	 *             overdraw included
	 */
	int remove(int amount) throws NotEnoughMoneyException;

	/**
	 * @return the total of available money on the account
	 */
	int getTotal();

	/**
	 * Set the overdraw for the account
	 * 
	 * @param overdraw
	 *            the overdraw to set
	 * @return the set overdraw
	 */
	int setAllowedOverdraw(int overdraw);
}
