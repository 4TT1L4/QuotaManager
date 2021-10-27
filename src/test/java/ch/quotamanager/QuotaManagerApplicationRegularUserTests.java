package ch.quotamanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;

import ch.quotamanager.model.SubscriptionSizeLimit;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class QuotaManagerApplicationRegularUserTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@BeforeEach
	void setUp() {
		final BasicAuthenticationInterceptor bai = new BasicAuthenticationInterceptor("5e15cbcfef537d543edeb100", "password");

		testRestTemplate.getRestTemplate().getInterceptors().clear();
		testRestTemplate.getRestTemplate().getInterceptors().add(bai);
	}

	@Test
	void usage_get_forUserWithLightSubscription() {
		final var response = testRestTemplate.getForEntity("/usage", HashMap.class);

		long filesLimit = (Integer)response.getBody().get("FilesLimit");
		long photosLimit = (Integer)response.getBody().get("PhotosLimit");
		long maxUploadSize = (Integer)response.getBody().get("MaxUploadSize");
		long filesUsage = (Integer)response.getBody().get("FilesUsage");
		long photosUsage = (Integer) response.getBody().get("PhotosUsage");

		assertEquals(SubscriptionSizeLimit._1GB, filesLimit);
		assertEquals(SubscriptionSizeLimit._1GB, photosLimit);
		assertEquals(SubscriptionSizeLimit._128MB, maxUploadSize);
		
		assertTrue(filesUsage >= 0);
		assertTrue(photosUsage >= 0);
	}
}
