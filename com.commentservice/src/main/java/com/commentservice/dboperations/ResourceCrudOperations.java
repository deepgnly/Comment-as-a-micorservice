package com.commentservice.dboperations;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;

import com.commentservice.model.ResourceModel;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.util.mongoOperations.MongoOperations;

public class ResourceCrudOperations {
	String collectionName;
	public final String DB_NAME = "commentsDB";
	public final String SEARCH_PARAM = "resourceId";
	boolean useCF;
	MongoCollection<ResourceModel> collection;

	public ResourceCrudOperations(String colName, boolean usecf, String mongoUrl) {
		collectionName = colName;
		useCF = usecf;
		if (useCF == false) {
			MongoOperations.getInstance(false).bindToDb(mongoUrl, DB_NAME);
		}

		MongoOperations.getInstance(useCF).createCollection(collectionName);
		collection = MongoOperations.getInstance(useCF).getDb().getCollection(collectionName, ResourceModel.class);
	}

	public Iterable<ResourceModel> findAll() {
		return collection.find();
	}

	public ResourceModel findOne(String commentId) {
		ResourceModel foundUser = collection.find(eq(SEARCH_PARAM, commentId)).first();
		return foundUser;
	}

	public ResourceModel save(ResourceModel resourceObj) {
		collection.insertOne(resourceObj);
		return resourceObj;

	}

	public ArrayList<ResourceModel> saveMany(ArrayList<ResourceModel> commentCollection) {
		collection.insertMany(commentCollection);
		return commentCollection;

	}

	public void delete(String commentId) {
		collection.deleteOne(eq(SEARCH_PARAM, commentId));

	}

	public ResourceModel updateResource(ResourceModel resourceObj) {

		long numberOfexistingObj = collection.count(eq(SEARCH_PARAM, resourceObj.getResourceId()));
		if (numberOfexistingObj > 0) {
			collection.findOneAndUpdate(eq(SEARCH_PARAM, resourceObj.getResourceId()),
					Updates.pushEach("listOfComments", resourceObj.getListOfComments()));
		} else {
			save(resourceObj);
		}
		return resourceObj;
	}

	public ArrayList<String> getAllCommentIds(String resourceId) {
		ResourceModel listOfComments = collection.find(eq(SEARCH_PARAM, resourceId)).first();
		if (listOfComments == null) {
			return new ArrayList<String>();
		}
		return listOfComments.getListOfComments();
	}

	public String deleteRootComment(String resourceId, String commentId) {
		BasicDBObject match = new BasicDBObject(SEARCH_PARAM, resourceId);
		BasicDBObject update = new BasicDBObject("listOfComments", commentId);
		collection.updateOne(match, new BasicDBObject("$pull", update));
		return commentId;

	}

}
