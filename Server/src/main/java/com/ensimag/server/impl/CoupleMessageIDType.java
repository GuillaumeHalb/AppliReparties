package com.ensimag.server.impl;

import com.ensimag.services.message.EnumMessageType;

public class CoupleMessageIDType {

	private long messageId;
	private EnumMessageType type;
	private long originalSenderId;

	public CoupleMessageIDType(long id, EnumMessageType type, long senderId) {
		this.messageId = id;
		this.type = type;
		this.originalSenderId = senderId;
	}

	public long getMessageId() {
		return messageId;
	}

	public EnumMessageType getType() {
		return type;
	}

	public long getOriginalSenderId() {
		return this.originalSenderId;
	}
	
	@Override
	public String toString() {
		return "(id: " + this.messageId + ", type: " + this.type + ", originalSenderId " + this.originalSenderId + ")";
	}

	@Override
	public boolean equals(Object o) {
		CoupleMessageIDType couple = (CoupleMessageIDType) o;
		if (this.messageId == couple.getMessageId() && this.type == couple.getType()) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result += prime*result + (int) (messageId);
		result += prime*result + type.hashCode();
		result += prime*result + (int) originalSenderId;
		return result;
	}
}
