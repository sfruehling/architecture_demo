package de.andrena.basepackage;

import org.springframework.stereotype.Component;

@Component
public class IdConsistencyValidator {

	public void checkIdConsistency(String pathId, String dtoId) {
		if (!pathId.equals(dtoId)) {
			throw new IdInconsistentException();
		}
	}
}
