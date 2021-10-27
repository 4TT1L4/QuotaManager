package ch.quotamanager.model;

import static ch.quotamanager.model.ByteConversionUtil.*;

/**
 * Constants for the {@link Subscription} plan size limitations.
 */
public class SubscriptionSizeLimit {
	public final static long _128MB = MB(128); 
	public final static long _256MB = MB(256);
	public final static long _1GB = GB(1);
	public final static long _2GB = GB(2);
	public final static long _3GB = GB(3L);
	public final static long _5GB = GB(5L);
	
	/**
	 * Special value to represent the "unlimited" limitation.
	 */
	public final static long UNLIMITED = -1;
}
