package com.example.demo;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Simulate the problem of how using certain custom data sources can interfere with Zonky's auto-configuration.
 * Notice that <code>spring.datasource</code> is used as configuration properties, rather than the typical
 * <code>app.datasource</code>.
 */
@Configuration(proxyBeanMethods = false)
public class TestDataSourceConfiguration {

	@Autowired
	DataSource dataSource;

	/**
	 * Questionable alternative solution to the {@link DemoDataSourceConfiguration} problem. Setting
	 * <code>@Profile("!testcontainers")</code> on that class seems simpler, but then we add test code to production code.
	 */
	@Bean
	Flyway flyway() {
		Flyway flyway = Flyway.configure().dataSource(dataSource).load();
		flyway.migrate();
		return flyway;
	}

}
