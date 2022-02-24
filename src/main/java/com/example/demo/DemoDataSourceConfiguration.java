package com.example.demo;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Simulate the problem of how using certain custom data sources can interfere with Zonky's auto-configuration.
 * Notice that <code>spring.datasource</code> is used as configuration properties, rather than the typical
 * <code>app.datasource</code>, which would have just worked. See <code>TestDataSourceConfiguration.java</code>
 * for an attempt to solve the problem.
 */
//@Profile("!testcontainers")
@Configuration(proxyBeanMethods = false)
public class DemoDataSourceConfiguration {

	@Bean
	@Primary
	@ConfigurationProperties("spring.datasource")
	public DataSourceProperties dataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@Primary
	@ConfigurationProperties("spring.datasource.hikari")
	public HikariDataSource dataSource(final DataSourceProperties properties) {
		return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	/**
	 * To be used for performing migrations with a different schema user.
	 */
	@Bean
	@FlywayDataSource
	@ConfigurationProperties("flyway.datasource")
	public HikariDataSource dataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

}
