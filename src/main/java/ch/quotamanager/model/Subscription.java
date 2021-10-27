package ch.quotamanager.model;

import static ch.quotamanager.model.SubscriptionSizeLimit.*;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Subscription {

	LIGHT(_1GB, _1GB, _128MB),
	STANDARD(_3GB, _2GB, _256MB),
	PRO(UNLIMITED, _5GB, _1GB),
	
	;

	private final long photoLimit;
	private final long fileLimit;
	private final long uploadSizeLimit;

	private Subscription(final long photoLimit, final long fileLimit, final long uploadSizeLimit) {
		this.photoLimit = photoLimit;
		this.fileLimit = fileLimit;
		this.uploadSizeLimit = uploadSizeLimit;
	}

	/**
	 * @return the photo storage limit of the {@link Subscription} plan in bytes.
	 * 
	 * Please note the special value {@link SubscriptionSizeLimit#UNLIMITED}.
	 */
	public long getPhotoLimit() {
		return photoLimit;
	}

	/**
	 * @return the file storage limit of the {@link Subscription} plan in bytes.
	 * 
	 * Please note the special value {@link SubscriptionSizeLimit#UNLIMITED}.
	 */
	public long getFileLimit() {
		return fileLimit;
	}

	/**
	 * @return the size limit of the uploaded file for the {@link Subscription} plan in bytes.
	 * 
	 * Please note the special value {@link SubscriptionSizeLimit#UNLIMITED}.
	 */
	public long getUploadSizeLimit() {
		return uploadSizeLimit;
	}
}
