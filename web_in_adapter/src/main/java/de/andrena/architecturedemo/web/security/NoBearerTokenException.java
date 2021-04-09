package de.andrena.architecturedemo.web.security;

public class NoBearerTokenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoBearerTokenException() {
		super("No bearer token present.");
	}
}
