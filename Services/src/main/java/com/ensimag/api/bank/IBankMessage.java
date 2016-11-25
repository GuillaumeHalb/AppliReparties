package com.ensimag.services;

import java.io.Serializable;

import com.ensimag.services.IMessage;

/**
 * Specific message for banks
 * 
 * @author guillaume
 * 
 */
public interface IBankMessage extends IMessage<Serializable, com.ensimag.services.IBankNode> {
	@Override
	IBankAction getAction();

	@Override
	IBankMessage clone();
}
