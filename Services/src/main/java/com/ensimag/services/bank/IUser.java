package com.ensimag.services.bank;

import java.io.Serializable;

public interface IUser extends Serializable {

	/**
	 * @return the name of the user
	 */
	String getName();

	/**
	 * @return the first name
	 */
	String getFirstName();

	/**
	 * @return the age
	 */
	String getAge();

}
