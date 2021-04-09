package de.andrena.basepackage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "version")
public class VersionEndpoint {

	@Value("${test.version.number}")
	private String versionNumber;

	@ReadOperation
	public String version() {
		return versionNumber;
	}
}
