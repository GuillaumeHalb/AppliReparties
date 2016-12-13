package com.ensimag.server.impl;

import com.ensimag.services.message.EnumMessageType;
import com.ensimag.services.message.IAck;

public class Ack implements IAck {

	private static final long serialVersionUID = -6056893412666221878L;
	
	private long ackSenderId;
	private long ackMessageId;
	private EnumMessageType messageType;
	
	public Ack(long ackSenderId, long ackMessageId, EnumMessageType type) {
		this.ackSenderId = ackSenderId;
		this.ackMessageId = ackMessageId;
		this.messageType = type;
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
	public EnumMessageType getType() {
		return this.messageType;
	}
	
	@Override
	public String toString() {
		return "sender: " + ackSenderId + " ackId: " + ackMessageId + ", type " + messageType;
	}
}
