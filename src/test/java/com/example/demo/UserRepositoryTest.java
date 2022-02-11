package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.flywaydb.test.annotation.FlywayTest;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureEmbeddedDatabase
@TestMethodOrder(OrderAnnotation.class)
class UserRepositoryTest {

	@Autowired
	protected TestRestTemplate testRestTemplate;

	@Test
	@Order(1)
	void flywayTestMigrationShouldExecute() {
		var response = testRestTemplate.getForEntity("/users/1", String.class);

		assertAll(
				() -> assertThat(response.getStatusCode()).isEqualTo(OK),
				() -> assertThat(response.getBody()).satisfies(body ->
						assertThat(body).contains("user1")
				)
		);
	}

	@Test
	@Order(2)
	void user3CanBeCreated() {
		var user = new User(3, "testUser1");
		var response1 = testRestTemplate.postForEntity("/users", user, User.class);

		assertThat(response1.getStatusCode()).isEqualTo(CREATED);

		var response2 = testRestTemplate.getForEntity("/users/3", String.class);

		assertAll(
				() -> assertThat(response2.getStatusCode()).isEqualTo(OK),
				() -> assertThat(response2.getBody()).satisfies(body ->
						assertThat(body).contains("testUser1")
				)
		);
	}

	@Test
	@Order(3)
	@FlywayTest
	void user3ShouldNotExist() {
		var response = testRestTemplate.getForEntity("/users/3", String.class);

		assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
	}

}
