package com.ensimag.server.impl;

import com.ensimag.services.message.IResult;

public class Result implements IResult<T> {

	private T data;
	private long messageId;
	
	public Result(T data, long messageId) {
		this.data = data;
		this.messageId = messageId;
	}
	
	@Override
	public T getData() {
		return this.data;
	}

	@Override
	public long getMessageId() {
		return this.messageId;
	}

}
