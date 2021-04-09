package de.andrena.architecturedemo.web.validation;

import de.andrena.architecturedemo.web.exception.IdInconsistentException;
import org.springframework.stereotype.Component;

@Component
public class IdConsistencyValidator {

	public void checkIdConsistency(String pathId, String dtoId) {
		if (!pathId.equals(dtoId)) {
			throw new IdInconsistentException();
		}
	}
}
