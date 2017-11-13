package com.userservice.dboperations;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.userservice.model.UserModel;
import com.util.mongoOperations.MongoOperations;

public class UserCrudOperations {
	String collectionName;
	public final String DB_NAME = "userDB";
	public final String SEARCH_PARAM = "userId";
	boolean useCF;

	public UserCrudOperations(String colName, boolean usecf, String mongoUrl) {
		collectionName = colName;
		useCF = usecf;
		if (useCF == false) {
			MongoOperations.getInstance(false).bindToDb(mongoUrl, DB_NAME);
		}
	}

	public Iterable<UserModel> findAll() {
		MongoCollection<UserModel> collection = MongoOperations.getInstance(useCF).getDb().getCollection(collectionName,
				UserModel.class);
		return collection.find();
	}

	public UserModel findOne(String userId) {
		MongoCollection<UserModel> collection = MongoOperations.getInstance(useCF).getDb().getCollection(collectionName,
				UserModel.class);
		UserModel foundUser = collection.find(eq(SEARCH_PARAM, userId)).first();
		return foundUser;
	}

	public UserModel save(UserModel userObj) {
		MongoOperations.getInstance(useCF).createCollection(collectionName);
		MongoCollection<UserModel> collection = MongoOperations.getInstance(useCF).getDb().getCollection(collectionName,
				UserModel.class);
		collection.insertOne(userObj);
		return userObj;

	}

	public void delete(String userId) {
		MongoCollection<UserModel> collection = MongoOperations.getInstance(useCF).getDb().getCollection(collectionName,
				UserModel.class);
		collection.deleteOne(eq(SEARCH_PARAM, userId));

	}

	public UserModel updateUser(UserModel userObj) {
		MongoOperations.getInstance(useCF).createCollection(collectionName);
		MongoCollection<UserModel> collection = MongoOperations.getInstance(useCF).getDb().getCollection(collectionName,
				UserModel.class);
		long numberOfexistingObj = collection.count(eq(SEARCH_PARAM, userObj.getUserId()));
		if (numberOfexistingObj > 0) {
			collection.findOneAndReplace(eq(SEARCH_PARAM, userObj.getUserId()), userObj);
		} else {
			save(userObj);
		}
		return userObj;
	}

}
