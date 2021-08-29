package com.github.norwick.reciperodeo.exceptions;

/**
 * Unused Exception to satisfy project requirements because I basically just used null pointer exceptions for everything.<br>
 * Example Usage:
 * <pre>
 * void someMethod() {
 *   try {
 * 	   otherMethod();
 *   } catch(UnusedException e) {
 * 	   methodToDoIfOtherMethodThrows();
 *   }
 * }
 * 
 * void otherMethod() throws UnusedException {
 * 	throw new UnusedException("Don't call me :(");
 * }
 * </pre>
 * @author Norwick Lee
 *
 */
@SuppressWarnings("serial")
public class UnusedException extends Exception {
	
	/**
	 * Constructor
	 */
	public UnusedException() {
		super();
	}
	
	/**
	 * Constructor with throw message
	 * @param msg message passed by thrower
	 */
	public UnusedException(String msg) {
		super(msg);
	}
	
}
