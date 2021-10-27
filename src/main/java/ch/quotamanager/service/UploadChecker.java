package ch.quotamanager.service;

import static ch.quotamanager.model.ByteConversionUtil.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.quotamanager.model.Subscription;
import ch.quotamanager.model.SubscriptionSizeLimit;
import ch.quotamanager.model.User;
import ch.quotamanager.repository.UsageRepository;
import ch.quotamanager.repository.UserRepository;

/**
 * Checks if a a given file can be still uploaded by a specific user.
 */
@Service
public class UploadChecker {

	// TODO: Discuss, if this size is big enough.
	final static long MAXIMAL_PHOTO_SIZE = MB(25);
	
	Logger logger = LoggerFactory.getLogger(UploadChecker.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UsageRepository usageRepository;
	
	/**
	 * {@link Result} of the upload checking.
	 */
	public enum Result {
		/**
		 * The {@link User} still had enough free storage for the file and the storage had been updated. 
		 */
		OK,
		
		/**
		 * The {@link User} did not have enough free storage to store the file. 
		 * 
		 * The storage usage was not updated.
		 */
		QUOTA_EXCEEDED,
		
		/**
		 * The uploaded file is too big for the current {@link Subscription} plan of the {@link User}.
		 * 
		 * This {@link User} with its current {@link Subscription} may not store files this big.
		 */
		MAX_UPLOAD_SIZE_VIOLATED,
	}
	
	public class CheckingForUser {

		private User user;

		public CheckingForUser(User user) {
			this.user = user;
		}

		private Result uploadFileWithSizeOf(long sizeOfUploadedFile) {
			
			if (isFileSizeOverUploadLimit(sizeOfUploadedFile)) {
				return Result.MAX_UPLOAD_SIZE_VIOLATED;
			}
			
			final boolean result;
			if (user.getSubscription().getFileLimit() == SubscriptionSizeLimit.UNLIMITED) {
				logger.debug("User has unlimited file storage.");
				result = usageRepository.increaseFileUsage(user.getId(), sizeOfUploadedFile);
			} else {
				result = usageRepository.increaseFileUsage(user.getId(), sizeOfUploadedFile, user.getSubscription().getFileLimit());
			}
			
			if (result) {
				return Result.OK;
			}
			else {
				return Result.QUOTA_EXCEEDED;
			}
		}

		private Result uploadPhotoWithSizeOf(long sizeOfUploadedPhoto) {
			
			if (isFileSizeOverUploadLimit(sizeOfUploadedPhoto)) {
				return Result.MAX_UPLOAD_SIZE_VIOLATED;
			}
			
			final boolean result;
			if (user.getSubscription().getPhotoLimit() == SubscriptionSizeLimit.UNLIMITED) {
				logger.debug("User has unlimited photo storage.");
				result = usageRepository.increasePhotoUsage(user.getId(), sizeOfUploadedPhoto);
			} else {
				result = usageRepository.increasePhotoUsage(user.getId(), sizeOfUploadedPhoto, user.getSubscription().getPhotoLimit());
			}
			
			if (result) {
				return Result.OK;
			}
			else {
				return Result.QUOTA_EXCEEDED;
			}
		}

		private boolean isFileSizeOverUploadLimit(long sizeOfUploadedFile) {
			boolean uploadedFileSizeIsLimited = !(user.getSubscription().getUploadSizeLimit() == SubscriptionSizeLimit.UNLIMITED);
			return uploadedFileSizeIsLimited && (sizeOfUploadedFile > user.getSubscription().getUploadSizeLimit());
		}
	}

	public CheckingForUser may(String userId) {
		User user = userRepository.getUser(userId);
		return new CheckingForUser(user);
	}

	public Result checkUpload(String userId, InputStream inputStream, long fileSize) {
		logger.debug("checkUpload - userId=" + fileSize + ", fileSize=" + fileSize );
		
		boolean smallEnoughToBeAPhoto = fileSize < MAXIMAL_PHOTO_SIZE;
		
		if (smallEnoughToBeAPhoto && photo(inputStream)) {
			logger.debug("checkUpload - Type: Photo" );
			return may(userId).uploadPhotoWithSizeOf(fileSize);
		}
		else {
			logger.debug("checkUpload - Type: Other" );
			return may(userId).uploadFileWithSizeOf(fileSize);
		}
	}

	/**
	 * @return true, if the passed {@link InputStream} contains an image.
	 */
	private boolean photo(InputStream input) {
			try {
				// Only BMP, GIF, JPG and PNG are recognized.
				// TODO: Decide, if we can afford this way of photo identification.
				//       Alternatively could rely on extensions or starting bytes.
				//       See also: https://stackoverflow.com/questions/4550296/how-to-identify-contents-of-a-byte-is-a-jpeg
				BufferedImage image = ImageIO.read(input);
				final boolean isImage = (image != null);
				return isImage;
			} catch (IOException e) {
				return false;
			}
	}
}
