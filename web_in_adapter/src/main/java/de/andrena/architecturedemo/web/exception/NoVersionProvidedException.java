package de.andrena.architecturedemo.web.exception;

public class NoVersionProvidedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoVersionProvidedException() {
		super("Please provide a version when updating an entity.");
	}

}
