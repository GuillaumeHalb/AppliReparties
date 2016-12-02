package com.ensimag.server.impl;

import java.io.Serializable;

import com.ensimag.services.message.IResult;

public class Result<T> implements IResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4893360019532742705L;
	private Serializable data;
	private long messageId;
	
	public Result(Serializable data, long messageId) {
		this.data = data;
		this.messageId = messageId;
	}
	
	@Override
	public Serializable getData() {
		return this.data;
	}

	@Override
	public long getMessageId() {
		return this.messageId;
	}

}
