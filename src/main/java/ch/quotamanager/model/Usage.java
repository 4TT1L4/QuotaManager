package ch.quotamanager.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represent the current storage usage of a {@link User}.
 */
public class Usage {
	
	private final String userId;
	private final long photoUsage;
	private final long fileUsage;

	/**
	 * Constructor.
	 */
	public Usage(String userId, long photoUsage, long fileUsage) {
		this.userId = userId;
		this.photoUsage = photoUsage;
		this.fileUsage = fileUsage;
	}
	
	/**
	 * @return the userId of the {@link User}, to whom this storage {@link Usage} belongs.
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @return the current photo storage usage for the {@link User} (in bytes).
	 */
	public long getPhotoUsage() {
		return photoUsage;
	}

	/**
	 * @return the current file storage usage for the {@link User} (in bytes).
	 */
	public long getFileUsage() {
		return fileUsage;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("userId", userId)
				.append("photoUsage", photoUsage)
				.append("fileUsage", fileUsage)
				.toString();
	}
}
