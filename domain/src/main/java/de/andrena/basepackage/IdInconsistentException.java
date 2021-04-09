package de.andrena.basepackage;

public class IdInconsistentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IdInconsistentException() {
		super("Id in path and Body are not equal.");
	}

}
