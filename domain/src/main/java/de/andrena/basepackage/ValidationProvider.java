package de.andrena.basepackage;

import org.springframework.validation.BindingResult;

public interface ValidationProvider {

	void checkForValidationErrors(BindingResult result);

	void checkIdConsistency(String pathId, String dtoId);

	void checkThatVersionIsProvided(VersionableAndIdentible versionable);

	void checkThatNoVersionIsGiven(VersionableAndIdentible versionable);

	void checkThatNoIdOrVersionAreGiven(VersionableAndIdentible versionable);

}
