package de.andrena.basepackage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public class ValidationProviderImpl implements ValidationProvider {

	@Autowired
	private BindingResultsValidator bindingResultsValidator;

	@Autowired
	private IdConsistencyValidator idConsistencyValidator;

	@Autowired
	private VersionAndIdValidator versionAndIdValidator;

	@Override
	public void checkForValidationErrors(BindingResult result) {
		bindingResultsValidator.checkForValidationErrors(result);
	}

	@Override
	public void checkIdConsistency(String pathId, String dtoId) {
		idConsistencyValidator.checkIdConsistency(pathId, dtoId);
	}

	@Override
	public void checkThatVersionIsProvided(VersionableAndIdentible versionable) {
		versionAndIdValidator.checkThatVersionIsProvided(versionable);
	}

	@Override
	public void checkThatNoVersionIsGiven(VersionableAndIdentible versionable) {
		versionAndIdValidator.verifyNoVersionGiven(versionable);
	}

	@Override
	public void checkThatNoIdOrVersionAreGiven(VersionableAndIdentible versionable) {
		versionAndIdValidator.checkThatNoIdOrVersionAreGiven(versionable);
	}

}
