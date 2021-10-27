package ch.quotamanager.repository;

import ch.quotamanager.model.Usage;
import ch.quotamanager.model.User;

public interface UsageRepository {

	/**
	 * @param userId
	 *        The userId of the user to be updated.
	 * @param photoUsage
	 *        The new photo usage value in bytes.
	 * @param fileUsage
	 *        The new file usage value in bytes.
	 * 
	 * Updates the photo and file usage values for a specific user. 
	 */
	void updateUserUsage(String userId, long photoUsage, long fileUsage);

	/**
	 * @param userId
	 *        The userId of the {@link User} to be retrieved.
	 * 
	 * @return the {@link User} with the passed userId.
	 */
	Usage getUserUsage(String userId);

	/**
	 * Increments the file usage value for a specific {@link User}, but only if the incremented
	 * value would not be above a specific threshold. 
	 * 
	 * @param userId
	 * @param increment
	 * @param fileLimit
	 * 
	 * @return true, if the file usage could be incremented with the passed value, otherwise false.
	 */
	boolean increaseFileUsage(String userId, long increment, long fileLimit);

	/**
	 * Increments the file usage value for a specific {@link User}.
	 * 
	 * @param userId
	 * @param increment
	 * 
	 * @return true, if the file usage could be incremented with the passed value, otherwise false.
	 */
	boolean increaseFileUsage(String userId, long increment);

	/**
	 * Increments the photo usage value for a specific {@link User}, but only if the incremented
	 * value would not be above a specific threshold. 
	 * 
	 * @param userId
	 * @param increment
	 * @param fileLimit
	 * 
	 * @return true, if the file usage could be incremented with the passed value, otherwise false.
	 */
	boolean increasePhotoUsage(String userId, long increment, long photoLimit);

	/**
	 * Increments the photo usage value for a specific {@link User}.
	 * 
	 * @param userId
	 * @param increment
	 * 
	 * @return true, if the file usage could be incremented with the passed value, otherwise false.
	 */
	boolean increasePhotoUsage(String userId, long increment);

}
