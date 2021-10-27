package ch.quotamanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.quotamanager.model.Subscription;
import ch.quotamanager.model.User;
import ch.quotamanager.repository.UserRepository;

@Service
public class SubscriptionManager {

	/**
	 * {@link Result} of setting the {@link Subscription} of a specific {@link User}.
	 */
	public enum Result {
		OK,
		USER_NOT_FOUND,
		INVALID_USER_ID,
		INVALID_SUBSCRIPTION_TYPE
	}

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Updates the {@link Subscription} plan of a specific {@link User}.
	 * 
	 * @param userId
	 *        The ID of the User to be updated.
	 * @param newSubscription
	 *        The {@link Subscription} to be set for the specific user.
	 * 
	 * @return The {@link Result} of the operation.
	 */
	public Result setSubscription(String userId, String subscriptonString) {
		if (!User.isValidId(userId)) {
			return Result.INVALID_USER_ID;
		}
		
		Subscription newSubscription;
		if ("PRO".equals(subscriptonString) ||
			"STANDARD".equals(subscriptonString) ||
			"LIGHT".equals(subscriptonString) 
				) {
			newSubscription = Subscription.valueOf(Subscription.class, subscriptonString);
		} else {
			return Result.INVALID_SUBSCRIPTION_TYPE;
		}
		
		long updated = userRepository.updateSubscription(userId, newSubscription);
		
		if (updated > 0) {
			return Result.OK;
		}
		else {
			return Result.USER_NOT_FOUND;
		}
	}

}
