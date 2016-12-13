package com.ensimag.server.impl;

import java.io.Serializable;

import com.ensimag.services.bank.IBankAction;
import com.ensimag.services.bank.IBankMessage;
import com.ensimag.services.message.EnumMessageType;
import com.ensimag.services.message.IResult;

public class Message implements IBankMessage {

	private static final long serialVersionUID = 2481989767948951546L;

	private IBankAction action;
	private IResult<Serializable> result;
	private long messageId;
	private long originalBankSenderId;
	private long senderId;
	private long destinationBankId;
	private EnumMessageType messageType;

	public Message(IBankAction action, long messageId, long originalBankSenderId, long destinationBankId,
			EnumMessageType messageType, IResult<Serializable> result) {
		this.result = result;
		this.action = action;
		this.messageId = messageId;
		this.originalBankSenderId = originalBankSenderId;
		this.senderId = originalBankSenderId;
		this.destinationBankId = destinationBankId;
		this.messageType = messageType;
	}

	@Override
	public long getMessageId() {
		return this.messageId;
	}

	@Override
	public IBankAction getAction() {
		return this.action;
	}

	@Override
	public long getOriginalBankSenderId() {
		return this.originalBankSenderId;
	}

	@Override
	public long getSenderId() {
		return this.senderId;
	}

	@Override
	public void setSenderId(long senderId) {
		this.senderId = senderId;
	}

	@Override
	public long getDestinationBankId() {
		return this.destinationBankId;
	}

	@Override
	public IBankMessage clone() {
		return new Message(action, messageId, originalBankSenderId, destinationBankId, messageType, result);
	}

	@Override
	public EnumMessageType getMessageType() {
		return this.messageType;
	}

	@Override
	public IResult<Serializable> getResult() {
		return this.result;
	}

	@Override
	public String toString() {
		return "message id: " + this.getMessageId() + ", message type: " + this.getMessageType() + ", sender: "
				+ this.getSenderId() + ", original sender: " + this.getOriginalBankSenderId();
	}

	@Override
	public boolean equals(Object message) {
		Message message2 = (Message) message;
		if (this.getMessageId() == message2.getMessageId() && this.getMessageType().equals(message2.getMessageType()) && this.getOriginalBankSenderId()==message2.getOriginalBankSenderId()) {
			return true;
		}
		return false;
	}
}
