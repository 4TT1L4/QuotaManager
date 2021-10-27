package ch.quotamanager.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.bson.types.ObjectId;

/**
 * Represents a user.
 */
public class User {

	private final String id;
	private final Subscription subscription;
	
	/**
	 * Constructor.
	 */
	public User(String id, Subscription subscription) {
		this.id = id;
		this.subscription = subscription;
	}

	/**
	 * @return the ID of the {@link User}.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return The current {@link Subscription} plan of the {@link User}.
	 */
	public Subscription getSubscription() {
		return subscription;
	}
	
	public static boolean isValidId(String id){
		return id != null && ObjectId.isValid(id);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
				.append("id", id)
				.append("subscription", subscription)
				.toString();
	}
}
