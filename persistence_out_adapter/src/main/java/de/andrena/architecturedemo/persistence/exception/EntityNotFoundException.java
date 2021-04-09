package de.andrena.architecturedemo.persistence.exception;

import de.andrena.architecturedemo.domain.exception.DatabaseException;

public class EntityNotFoundException extends DatabaseException {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String path) {
		super("No entity found at " + path);
	}

}
