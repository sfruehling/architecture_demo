package de.andrena.architecturedemo.web.validation;

import de.andrena.architecturedemo.web.common.VersionableAndIdentible;
import de.andrena.architecturedemo.web.exception.NoIdAllowedException;
import de.andrena.architecturedemo.web.exception.NoVersionAllowedException;
import de.andrena.architecturedemo.web.exception.NoVersionProvidedException;
import org.springframework.stereotype.Component;

@Component
public class VersionAndIdValidator {
	public void checkThatVersionIsProvided(VersionableAndIdentible versionable) {
		if (versionable.getVersion() == null) {
			throw new NoVersionProvidedException();
		}
	}

	public void verifyNoVersionGiven(VersionableAndIdentible versionable) {
		if (versionable.getVersion() != null) {
			throw new NoVersionAllowedException();
		}
	}

	public void checkThatNoIdOrVersionAreGiven(VersionableAndIdentible versionable) {
		if (versionable.getId() != null) {
			throw new NoIdAllowedException();
		}
		if (versionable.getVersion() != null) {
			throw new NoVersionAllowedException();
		}
	}

}
