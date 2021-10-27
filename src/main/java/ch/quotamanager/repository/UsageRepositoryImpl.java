package ch.quotamanager.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.result.UpdateResult;

import ch.quotamanager.model.Usage;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

@Repository
class UsageRepositoryImpl implements UsageRepository {

	private static final String USAGE_COLLECTION_NAME = "Usage";

	private static final String PHOTO_USAGE_FIELD_NAME = "photoUsage";
	private static final String FILE_USAGE_FIELD_NAME = "fileUsage";
	private static final String USER_ID_FIELD_NAME = "userId";

	private final MongoDatabase db;

	public UsageRepositoryImpl(final MongoDatabase db) {
		this.db = db;
	}

	public Usage getUserUsage(final String userId) {
		final Document usageDocument = getCollection().find(eq(USER_ID_FIELD_NAME, new ObjectId(userId))).first();
		return parseDocument(usageDocument);
	}

	public void updateUserUsage(final String userId, final long photoUsage, final long fileUsage) {
		getCollection().updateOne(
					eq(USER_ID_FIELD_NAME, new ObjectId(userId)),
					combine(
						set(PHOTO_USAGE_FIELD_NAME, photoUsage),
						set(FILE_USAGE_FIELD_NAME, fileUsage)
					),
				new UpdateOptions().upsert(true));
	}

	public boolean increaseFileUsage(final String userId, final long increment) {
		return increaseUsage(userId, FILE_USAGE_FIELD_NAME, increment);
	}
	
	public boolean increaseFileUsage(final String userId, final long increment, final long threshold) {
		return increaseUsage(userId, FILE_USAGE_FIELD_NAME, increment, threshold);
	}

	public boolean increasePhotoUsage(final String userId, final long increment) {
		return increaseUsage(userId, PHOTO_USAGE_FIELD_NAME, increment);
	}
	
	public boolean increasePhotoUsage(final String userId, final long increment, final long threshold) {
		return increaseUsage(userId, PHOTO_USAGE_FIELD_NAME, increment, threshold);
	}

	private boolean increaseUsage(final String userId, final String usageType, final long increment) {
		UpdateResult result = getCollection().updateOne(
				eq(USER_ID_FIELD_NAME, new ObjectId(userId)),
				inc(usageType, increment));
		
		return result.getModifiedCount() > 0;
	}
	
	private boolean increaseUsage(final String userId, final String usageType, final long increment, final long threshold) {
		UpdateResult result = getCollection().updateOne(
				combine(
					eq(USER_ID_FIELD_NAME, new ObjectId(userId)),
					lt(usageType, threshold - increment )
				),
				inc(usageType, increment));
		
		return result.getModifiedCount() > 0;
	}
	
	private static Usage parseDocument(final Document document) {
		final Usage usage = new Usage(
			document.getObjectId(USER_ID_FIELD_NAME).toString(),
			document.getLong(PHOTO_USAGE_FIELD_NAME),
			document.getLong(FILE_USAGE_FIELD_NAME)
		);
		return usage;
	}

	private MongoCollection<Document> getCollection() {
		return db.getCollection(USAGE_COLLECTION_NAME);
	}
}
