package ch.quotamanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ch.quotamanager.model.Usage;
import ch.quotamanager.model.User;
import ch.quotamanager.repository.UsageRepository;
import ch.quotamanager.repository.UserRepository;

@Service
public class UsageInformationService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UsageRepository usageRepository;

	@JsonPropertyOrder({ "FilesLimit", "PhotosLimit", "MaxUploadSize", "FilesUsage", "PhotosUsage"})
	public static class UsageInformation {
		
		private Usage usage;
		private User user;

		UsageInformation(User user, Usage usage) {
			this.user = user;
			this.usage = usage;
		}
		
		@JsonProperty(value = "FilesLimit", index = 0)
		public long getFilesLimit() {
			return user.getSubscription().getFileLimit();
		}

		@JsonProperty(value = "PhotosLimit", index = 1)
		public long getPhotosLimit() {
			return user.getSubscription().getPhotoLimit();
		}

		@JsonProperty(value = "MaxUploadSize", index = 2)
		public long getMaxUploadSize() {
			return user.getSubscription().getUploadSizeLimit();
		}

		@JsonProperty(value = "FilesUsage", index = 3)
		public long getFilesUsage() {
			return usage.getFileUsage();
		}

		@JsonProperty(value = "PhotosUsage", index = 4)
		public long getPhotosUsage() {
			return usage.getPhotoUsage();
		}
	}
	
	/**
	 * @param userId
	 *        The ID of the User, whose current {@link UsageInformation} should be returned. 
	 * 
	 * @return the {@link UsageInformation} for the {@link User} with the passed ID.
	 */
	public UsageInformation getUsageInfomationForUserWith(String userId) {
		User user = userRepository.getUser(userId);
		Usage usage = usageRepository.getUserUsage(userId);
		
		return new UsageInformation(user, usage);
	}

}
