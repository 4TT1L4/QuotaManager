package ch.quotamanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.quotamanager.model.Subscription;
import ch.quotamanager.repository.UsageRepository;
import ch.quotamanager.repository.UserRepository;
import ch.quotamanager.service.SubscriptionManager;
import ch.quotamanager.service.UsageInformationService;
import ch.quotamanager.service.SubscriptionManager.Result;

/**
 * Tests the {@link UsageInformationService} class.
 */
public class SubscriptionManagerTest {

	private static final String EXISTING_USER_ID = "5e15cbcfef537d543edeb100";
	private static final String NOT_EXISTING_USER_ID = "5e15cbcfef537d543edeb101";

	@Mock
	UsageRepository usageRepository;

	@Mock
	UserRepository userRepository;

	// Object under test:
	@InjectMocks
	private SubscriptionManager subscriptionManager;

	@BeforeEach
	void beforeEach() {
		MockitoAnnotations.initMocks(this);

		when(userRepository.updateSubscription(eq(EXISTING_USER_ID), any(Subscription.class))).thenReturn(1L);
		when(userRepository.updateSubscription(eq(NOT_EXISTING_USER_ID), any(Subscription.class))).thenReturn(0L);
	}
	
	@Test
	void test_setSubscription_invalidUserId() {
		Result result = subscriptionManager.setSubscription("INVALID USER ID", "PRO");
		
		assertEquals(Result.INVALID_USER_ID, result);
	}

	@Test
	void test_setSubscription_invalidSubscriptionName() {
		Result result = subscriptionManager.setSubscription(EXISTING_USER_ID, "INVALID SUBSCRIPTION NAME");
		
		assertEquals(Result.INVALID_SUBSCRIPTION_TYPE, result);
	}

	@ParameterizedTest(name = "test_setSubscription_validInput - {arguments} Subscription (#{index})")
	@EnumSource(Subscription.class)
	void test_setSubscription_validInput(Subscription subscription) {
		Result result = subscriptionManager.setSubscription(EXISTING_USER_ID, subscription.name());
		
		assertEquals(Result.OK, result);
	}
	
	@ParameterizedTest(name = "test_setSubscriptionvalidInputWithMissingUser - {arguments} Subscription (#{index})")
	@EnumSource(Subscription.class)
	void test_setSubscription_validInputWithMissingUser(Subscription subscription) {
		Result result = subscriptionManager.setSubscription(NOT_EXISTING_USER_ID, subscription.name());
		
		assertEquals(Result.USER_NOT_FOUND, result);
	}
}
