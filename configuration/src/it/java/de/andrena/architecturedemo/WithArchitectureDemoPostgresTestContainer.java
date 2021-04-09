package de.andrena.architecturedemo;

import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(classes = WithArchitectureDemoPostgresTestContainer.TestConfig.class)
@Testcontainers
public @interface WithArchitectureDemoPostgresTestContainer {


    @TestConfiguration
    class TestConfig {
        @Bean(destroyMethod = "stop")
        public ArchitectureDemoPostgreSqlTestContainer postgresTestContainer() {
            ArchitectureDemoPostgreSqlTestContainer container = new ArchitectureDemoPostgreSqlTestContainer();
            container.start();
            return container;
        }

        @Primary
        @Bean
        public DataSource dataSource(ArchitectureDemoPostgreSqlTestContainer architectureDemoPostgreSqlTestContainer) {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl(architectureDemoPostgreSqlTestContainer.getJdbcUrl());
            dataSource.setUsername(architectureDemoPostgreSqlTestContainer.getUsername());
            dataSource.setPassword(architectureDemoPostgreSqlTestContainer.getPassword());
            return dataSource;
        }

        @Bean
        public PhysicalNamingStrategy physical() {
            return new PhysicalNamingStrategyStandardImpl();
        }
    }
}
