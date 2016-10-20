package com.ensimag.api.message;

public enum EnumMessageType {
	/**
	 * Message content need to be handled on every nodes. Message content is an
	 * action
	 */
	BROADCAST,
	/**
	 * Message content need to be handled on the specified node. Message content
	 * is an action
	 */
	SINGLE_DEST,
	/**
	 * Message content is a data result from a previous action
	 */
	DELIVERY

}
