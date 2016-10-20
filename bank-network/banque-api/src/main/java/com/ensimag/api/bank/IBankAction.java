package com.ensimag.api.bank;

import java.io.Serializable;

import com.ensimag.api.message.IAction;

/**
 * Specific interface for actions on a bank
 * 
 * @author guillaume
 * 
 */
public interface IBankAction extends IAction<Serializable, IBankNode> {

}
