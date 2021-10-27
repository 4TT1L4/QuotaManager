package ch.quotamanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import ch.quotamanager.model.Subscription;
import ch.quotamanager.model.Usage;
import ch.quotamanager.model.User;
import ch.quotamanager.repository.UsageRepository;
import ch.quotamanager.repository.UserRepository;
import ch.quotamanager.service.UsageInformationService;
import ch.quotamanager.service.UsageInformationService.UsageInformation;

/**
 * Tests the {@link UsageInformationService} class.
 */
public class UsageInformationServiceTest {
	private MockUserRepository mockUserRepository;
	private MockUsageRepository mockUsageRepository;
	
	// Object under test:
	private UsageInformationService usageInformationService;
	
	@BeforeEach
	public void beforeEach() {
		// Create mocks:
		mockUserRepository = new MockUserRepository();
		mockUsageRepository = new MockUsageRepository();
		
		// Create object under test:
		usageInformationService = new UsageInformationService();
		
		// Inject mocks:
		usageInformationService.usageRepository = mockUsageRepository;
		usageInformationService.userRepository = mockUserRepository;
	}
	
	@ParameterizedTest(name = "test_getUsageInfomationForUserWithUserId user with {arguments} Subscription (#{index})")
	@EnumSource(Subscription.class)
	void test_getUsageInfomationForUserWithUserId(Subscription subscription) {
		// Given:
		final long fakeFileUsage = 123456;
		final long fakePhotoUsage = 234567;
		
		mockUserRepository.setSubscription(subscription);
		mockUsageRepository.setFakeFileUsage(fakeFileUsage);
		mockUsageRepository.setFakePhotoUsage(fakePhotoUsage);
		
		// When:
		UsageInformation result = usageInformationService.getUsageInfomationForUserWith("DummyUserId");
		
		// Then:
		assertEquals(subscription.getFileLimit(), result.getFilesLimit());
		assertEquals(subscription.getPhotoLimit(), result.getPhotosLimit());
		assertEquals(subscription.getUploadSizeLimit(), result.getMaxUploadSize());

		assertEquals(fakeFileUsage, result.getFilesUsage());
		assertEquals(fakePhotoUsage, result.getPhotosUsage());
	}

	private class MockUsageRepository implements UsageRepository {
		
		private long fakePhotoUsage;
		private long fakeFileUsage;

		public void setFakePhotoUsage(long fakePhotoUsage) {
			this.fakePhotoUsage = fakePhotoUsage;
		}

		public void setFakeFileUsage(long fakeFileUsage) {
			this.fakeFileUsage = fakeFileUsage;
		}
		
		@Override
		public void updateUserUsage(String userId, long photoUsage, long fileUsage) {
			return;
		}

		@Override
		public boolean increasePhotoUsage(String userId, long increment) {
			return true;
		}

		@Override
		public boolean increasePhotoUsage(String userId, long increment, long photoLimit) {
			return true;
		}

		@Override
		public boolean increaseFileUsage(String userId, long increment) {
			return true;
		}

		@Override
		public boolean increaseFileUsage(String userId, long increment, long fileLimit) {
			return true;
		}

		@Override
		public Usage getUserUsage(String userId) {
			return new Usage(userId, fakePhotoUsage, fakeFileUsage);
		}
	}
	
	private class MockUserRepository implements UserRepository {

		private Subscription subscription;
		
		void setSubscription(Subscription subscription) {
			this.subscription = subscription;
		}
		
		@Override
		public void createUserWithIdAndSubscription(String userId, Subscription subscription) {
			// nothing to do.
		}

		@Override
		public User getUser(String userId) {
			return new User(userId, subscription);
		}

		@Override
		public long updateSubscription(String userId, Subscription newSubscription) {
			return 1;
		}

	}
}
