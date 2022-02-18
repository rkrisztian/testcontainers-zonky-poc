package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

/**
 * Sometimes we want to write tests where we do not really care about the database, but we need {@link SpringBootTest} (for
 * whatever the reason may be). In this case we don't want Zonky to initialize itself; just using an H2 in-memory DB is fine.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test", "h2db"})
class UserRepositoryNonDockerTest {

	@Autowired
	protected TestRestTemplate testRestTemplate;

	@Test
	void user1CanBeCreated() {
		var user = new User(1, "testUser1");
		var response1 = testRestTemplate.postForEntity("/users", user, User.class);

		assertThat(response1.getStatusCode()).isEqualTo(CREATED);

		var response2 = testRestTemplate.getForEntity("/users/1", String.class);

		assertAll(
				() -> assertThat(response2.getStatusCode()).isEqualTo(OK),
				() -> assertThat(response2.getBody()).satisfies(body ->
						assertThat(body).contains("testUser1")
				)
		);
	}

}
