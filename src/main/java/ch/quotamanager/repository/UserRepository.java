package ch.quotamanager.repository;

import ch.quotamanager.model.Subscription;
import ch.quotamanager.model.User;

public interface UserRepository {

	/**
	 * Creates a new user with the passer ID and {@link Subscription} plan.
	 * 
	 * @param userId
	 * @param subscription
	 */
	void createUserWithIdAndSubscription(String userId, Subscription subscription);

	/**
	 * Retrieves the {@link User} with the passed userId. 
	 * 
	 * @param userId
	 * 
	 * @return the {@link User} with the passed userId.
	 */
	User getUser(String userId);

	/**
	 * Updates the {@link Subscription} plan for the {@link User} with the passed userId.
	 * 
	 * @param userId
	 * @param newSubscription
	 * 
	 * @return the number of updated users.
	 */
	long updateSubscription(String userId, Subscription newSubscription);

}
