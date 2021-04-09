package de.andrena.basepackage;

public class IdAlreadyInUseException extends DatabaseException {

	private static final long serialVersionUID = 1L;

	public IdAlreadyInUseException(String id) {
		super("Cannot create entity with id " + id + " because id is already in use!");
	}
}
