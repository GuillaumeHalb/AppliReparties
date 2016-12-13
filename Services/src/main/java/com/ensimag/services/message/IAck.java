package com.ensimag.services.message;

import java.io.Serializable;

public interface IAck extends Serializable {

	/**
	 * @return the id of the ack sender
	 */
	long getAckSenderId();

	/**
	 * @return the message id corresponding to this ack
	 */
	long getAckMessageId();
	
	EnumMessageType getType();
}
