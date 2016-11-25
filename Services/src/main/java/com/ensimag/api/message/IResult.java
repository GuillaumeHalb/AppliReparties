package com.ensimag.services;

import java.io.Serializable;

/**
 * Result of an action
 * 
 * @author guillaume
 * 
 * @param <T>
 *            the result type of the action
 */
public interface IResult<T extends Serializable> extends Serializable {

	/**
	 * @return the result object
	 */
	T getData();

	/**
	 * @return the message id of the message that generated this result
	 */
	long getMessageId();

}
