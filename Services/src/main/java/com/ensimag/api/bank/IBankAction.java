package com.ensimag.services;

import java.io.Serializable;

import com.ensimag.services.IAction;

/**
 * Specific interface for actions on a bank
 * 
 * @author guillaume
 * 
 */
public interface IBankAction extends IAction<Serializable, IBankNode> {

}
