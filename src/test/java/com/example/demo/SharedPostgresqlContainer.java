package com.example.demo;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;

public class SharedPostgresqlContainer {

    private static final String IMAGE_VERSION = "postgres:12.7";

    private static final PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>(IMAGE_VERSION)
            .withReuse(true);

    public static class DockerPostgreDataSourceInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(final @NotNull ConfigurableApplicationContext applicationContext) {
            if (!postgresqlContainer.isRunning()) {
                postgresqlContainer.start();
            }

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                    "spring.datasource.url=" + postgresqlContainer.getJdbcUrl(),
                    "spring.datasource.driver-class-name=" + postgresqlContainer.getDriverClassName(),
                    "spring.datasource.username=" + postgresqlContainer.getUsername(),
                    "spring.datasource.password=" + postgresqlContainer.getPassword(),
                    "flyway.datasource.jdbc-url=" + postgresqlContainer.getJdbcUrl(),
                    "flyway.datasource.driver-class-name=" + postgresqlContainer.getDriverClassName()
            );
        }
    }

}
