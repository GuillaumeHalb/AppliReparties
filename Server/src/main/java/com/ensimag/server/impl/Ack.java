package com.ensimag.server.impl;

import com.ensimag.services.message.IAck;

public class Ack implements IAck {

	private static final long serialVersionUID = -6056893412666221878L;
	
	private long ackSenderId;
	private long ackMessageId;
	
	public Ack(long ackSenderId, long ackMessageId) {
		this.ackSenderId = ackSenderId;
		this.ackMessageId = ackMessageId;
	}
	
	@Override
	public long getAckSenderId() {
		return this.ackSenderId;
	}

	@Override
	public long getAckMessageId() {
		return this.ackMessageId;
	}
	
	@Override
	public String toString() {
		return "sender: " + ackSenderId + " ackId: " + ackMessageId;
	}
}
