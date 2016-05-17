package me.mrfcker.exceptions;

public class AlreadyExistsException extends Exception {

	private static final long serialVersionUID = 655553249883668024L;

	public AlreadyExistsException(Object obj) {
		super(obj + " already exists!");
	}
	
	public AlreadyExistsException(String message) {
		super(message);
	}
	
}
