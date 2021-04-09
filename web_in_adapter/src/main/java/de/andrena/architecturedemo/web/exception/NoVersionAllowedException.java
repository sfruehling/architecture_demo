package de.andrena.architecturedemo.web.exception;

public class NoVersionAllowedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoVersionAllowedException() {
		super("No version allowed when creating a new entity.");
	}

}
