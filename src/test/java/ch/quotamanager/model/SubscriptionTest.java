package ch.quotamanager.model;

import static ch.quotamanager.model.ByteConversionUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.quotamanager.model.Subscription;
import ch.quotamanager.model.SubscriptionSizeLimit;

public class SubscriptionTest {

	@Test
	void subscription_light() {
		assertEquals(GB(1), Subscription.LIGHT.getFileLimit());
		assertEquals(GB(1), Subscription.LIGHT.getPhotoLimit());
		assertEquals(MB(128), Subscription.LIGHT.getUploadSizeLimit());
	}

	@Test
	void subscription_standard() {
		assertEquals(GB(3), Subscription.STANDARD.getPhotoLimit());
		assertEquals(GB(2), Subscription.STANDARD.getFileLimit());
		assertEquals(MB(256), Subscription.STANDARD.getUploadSizeLimit());
	}

	@Test
	void subscription_pro() {
		assertEquals(SubscriptionSizeLimit.UNLIMITED, Subscription.PRO.getPhotoLimit());
		assertEquals(GB(5), Subscription.PRO.getFileLimit());
		assertEquals(GB(1), Subscription.PRO.getUploadSizeLimit());
	}
}
