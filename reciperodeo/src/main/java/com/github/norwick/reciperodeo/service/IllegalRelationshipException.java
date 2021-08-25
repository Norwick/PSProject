package com.github.norwick.reciperodeo.service;

/**
 * Used to throw exception if recipe relationship is being changed weirdly
 * @author Norwick Lee
 *
 */
@SuppressWarnings("serial")
public class IllegalRelationshipException extends Exception {
	
	@SuppressWarnings("javadoc")
	public IllegalRelationshipException() {
		super();
	}
	

	@SuppressWarnings("javadoc")
	public IllegalRelationshipException(String msg) {
		super(msg);
	}
}
