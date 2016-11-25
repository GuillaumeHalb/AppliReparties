package com.ensimag.services.bank;


/**
 * Interface that defines a Bank
 * 
 * @author guillaume
 * 
 */
public interface IBank extends IBankOperations {

	/**
	 * @return bank id
	 */
	long getBankId();

}
