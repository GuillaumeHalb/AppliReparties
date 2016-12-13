package com.ensimag.server.impl;

import com.ensimag.services.message.EnumMessageType;
import com.ensimag.services.message.IAck;

public class Ack implements IAck {

	private static final long serialVersionUID = -6056893412666221878L;
	
	private long ackSenderId;
	private long ackMessageId;
	private EnumMessageType messageType;
	private long originalSenderId;
	
	public Ack(long ackSenderId, long ackMessageId, EnumMessageType type, long senderId) {
		this.ackSenderId = ackSenderId;
		this.ackMessageId = ackMessageId;
		this.messageType = type;
		this.originalSenderId = senderId;
	}
	
	@Override
	public long getAckSenderId() {
		return this.ackSenderId;
	}

	@Override
	public long getAckMessageId() {
		return this.ackMessageId;
	}
	
	public EnumMessageType getType() {
		return this.messageType;
	}
	
	public long getOriginalSenderId() {
		return this.originalSenderId;
	}
	
	@Override
	public String toString() {
		return "sender: " + ackSenderId + " ackId: " + ackMessageId + ", type " + messageType + ", original sender: " + this.originalSenderId;
	}
}
