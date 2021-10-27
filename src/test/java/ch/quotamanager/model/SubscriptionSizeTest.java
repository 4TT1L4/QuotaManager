package ch.quotamanager.model;

import static ch.quotamanager.model.ByteConversionUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.quotamanager.model.SubscriptionSizeLimit;

public class SubscriptionSizeTest {

	@Test
	void SubscriptionSizeLimit_enumValues() {
		assertEquals(SubscriptionSizeLimit._128MB, 128_000_000L);
		assertEquals(SubscriptionSizeLimit._256MB, 256_000_000L);
		assertEquals(SubscriptionSizeLimit._1GB, 1_000_000_000L);
		assertEquals(SubscriptionSizeLimit._3GB, 3_000_000_000L);
		assertEquals(SubscriptionSizeLimit._5GB, 5_000_000_000L);
	}

	@Test
	void SubscriptionSizeLimit_helperMethods() {
		assertEquals(MB(128), 128_000_000L);
		assertEquals(MB(256), 256_000_000L);
		assertEquals(GB(1), 1_000_000_000L);
		assertEquals(GB(3), 3_000_000_000L);
		assertEquals(GB(5), 5_000_000_000L);
	}
}
