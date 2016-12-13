package com.ensimag.services.bank;

import java.io.Serializable;

import com.ensimag.services.message.IAction;
import com.ensimag.services.message.IMessage;

/**
 * Specific message for banks
 * 
 * @author guillaume
 * 
 */
public interface IBankMessage extends IMessage<Serializable, com.ensimag.services.bank.IBankNode> {
	@Override
	IAction<Serializable, IBankNode> getAction();

	@Override
	IBankMessage clone();
	
	@Override
	boolean equals(Object message);
}
