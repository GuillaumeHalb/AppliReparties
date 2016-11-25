package com.ensimag.services;

import java.io.Serializable;

/**
 * Define an action
 * 
 * @author guillaume
 * 
 * @param <ResultType>
 *            result type of the action
 * @param <Node>
 *            the node type on which this action can be executed
 */
public interface IAction<ResultType extends Serializable, Node> extends Serializable {

	/**
	 * Execute the action
	 * 
	 * @param node
	 *            the node on which to execute the action
	 * @return the result of the action
	 * @throws Exception
	 */
	ResultType execute(Node node) throws Exception;

}
