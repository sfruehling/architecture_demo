package de.andrena.architecturedemo.web.exception;

public class NoIdAllowedException extends RuntimeException {

	private final static long serialVersionUID = 1L;

	public NoIdAllowedException() {
		super("No id allowed when creating a new enitity.");
	}

}
