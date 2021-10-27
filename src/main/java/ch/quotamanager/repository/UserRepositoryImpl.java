package ch.quotamanager.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

import ch.quotamanager.model.Subscription;
import ch.quotamanager.model.User;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@Repository
public class UserRepositoryImpl implements UserRepository {

	private static final String USER_COLLECTION_NAME = "Users";

	private static final String SUBSCRIPTION_FIELD_NAME = "subscription";
	private static final String ID_FIELD_NAME = "_id";

	private final MongoDatabase db;

	public UserRepositoryImpl(final MongoDatabase db) {
		this.db = db;
	}

	public User getUser(final String userId) {
		final Document userDocument = getCollection().find(eq(ID_FIELD_NAME, new ObjectId(userId))).first();
		return parseDocument(userDocument);
	}

	public void createUserWithIdAndSubscription(final String userId, final Subscription subscription) {
		final Document userDocument = new Document()
				.append(ID_FIELD_NAME, new ObjectId(userId))
				.append(SUBSCRIPTION_FIELD_NAME, subscription.name());
		getCollection().insertOne(userDocument);
	}

	private static User parseDocument(final Document document) {
		final User user = new User(
				document.getObjectId(ID_FIELD_NAME).toString(), 
				Subscription.valueOf(document.getString(SUBSCRIPTION_FIELD_NAME))
				);

		return user;
	}

	private MongoCollection<Document> getCollection() {
		return db.getCollection(USER_COLLECTION_NAME);
	}

	public long updateSubscription(String userId, Subscription newSubscription) {
		UpdateResult result = getCollection().updateOne(
				eq(ID_FIELD_NAME, new ObjectId(userId)),
				combine(
					set(SUBSCRIPTION_FIELD_NAME, newSubscription.name())
				)
			);
		
		return result.getMatchedCount();
	}
}
