package ch.quotamanager.configuration;

import ch.quotamanager.model.Subscription;
import ch.quotamanager.repository.UsageRepository;
import ch.quotamanager.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


@Component
public class DbInitializer {

	private static final String user1Id = "5e15cbcfef537d543edeb100";
	private static final String user2Id = "5e15cbcfef537d543edeb101";
	private static final String user3Id = "5e15cbcfef537d543edeb102";
	private static final String user4Id = "5e15cbcfef537d543edeb104";
	private static final String user5Id = "5e15cbcfef537d543edeb105";
	
	@Autowired
	private UsageRepository usageRepository;

	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	public void init() {
		createTestUser(user1Id, Subscription.LIGHT, 1024L, 512L);
		createTestUser(user2Id, Subscription.STANDARD, 1024L, 512L);
		createTestUser(user3Id, Subscription.PRO, 1024L, 512L);
		createTestUser(user4Id, Subscription.LIGHT, Subscription.LIGHT.getPhotoLimit() - 1, 0L);
		createTestUser(user5Id, Subscription.LIGHT, 0L,  Subscription.LIGHT.getFileLimit() - 1);
	}

	private void createTestUser(final String userId, final Subscription subscription, long photoUsage, long fileUsage) {
		userRepository.createUserWithIdAndSubscription(userId, subscription);
		usageRepository.updateUserUsage(userId, photoUsage, fileUsage);
		
		System.out.println("Created user: " + userRepository.getUser(userId) + ".");
		System.out.println("Created usage: " + usageRepository.getUserUsage(userId) + ".");
	}
}
