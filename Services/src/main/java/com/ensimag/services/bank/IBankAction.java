package com.ensimag.services.bank;

import java.io.Serializable;

import com.ensimag.services.message.IAction;

/**
 * Specific interface for actions on a bank
 * 
 * @author guillaume
 * 
 */
public interface IBankAction extends IAction<Serializable, IBankNode> {

}
