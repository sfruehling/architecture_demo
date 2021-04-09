package de.andrena.architecturedemo.persistence;

import groovy.util.logging.Log4j2;
import org.testcontainers.containers.PostgreSQLContainer;

@Log4j2
public class ArchitectureDemoPostgreSqlTestContainer extends PostgreSQLContainer<ArchitectureDemoPostgreSqlTestContainer> {

	private static final String IMAGE_VERSION = "postgres:11.1";

	public static final String DATABASE_NAME = "test-db";
	public static final String USERNAME = "test";
	public static final String PASSWORD = "test";

	public ArchitectureDemoPostgreSqlTestContainer() {
		super(IMAGE_VERSION);
		withDatabaseName(DATABASE_NAME);
		withPassword(PASSWORD);
		withUsername(USERNAME);
	}

	@Override
	public void start() {
		logger().error("########################### STARTING CONTAINER");
		super.start();
	}

	@Override
	public void stop() {
		logger().error("########################### STOPPING CONTAINER");
		super.stop();
	}

}
