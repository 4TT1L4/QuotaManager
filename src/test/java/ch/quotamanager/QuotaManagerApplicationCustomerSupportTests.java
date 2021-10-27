package ch.quotamanager;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuotaManagerApplicationCustomerSupportTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@BeforeEach
	void setUp() {
		final BasicAuthenticationInterceptor bai = new BasicAuthenticationInterceptor("5e15cbcfef537d543edeb103", "password");
		
		testRestTemplate.getRestTemplate().getInterceptors().clear();
		testRestTemplate.getRestTemplate().getInterceptors().add(bai);
	}

	@Test
	void usage_get_forbiddenForCustomerSupport() {
		final var response = testRestTemplate.getForEntity("/usage", HashMap.class);

		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(), "usage should be forbidden for customer support users");
	}
}
