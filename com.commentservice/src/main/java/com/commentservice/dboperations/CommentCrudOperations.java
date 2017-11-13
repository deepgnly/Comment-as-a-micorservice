package com.commentservice.dboperations;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bson.conversions.Bson;

import com.commentservice.model.CommentModel;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.util.mongoOperations.MongoOperations;

public class CommentCrudOperations {
	String collectionName;
	public final String DB_NAME = "commentsDB";
	public final String SEARCH_PARAM = "commentId";
	public final String UPDATE_PARAM = "commentString";

	boolean useCF;
	private MongoCollection<CommentModel> collection;

	public CommentCrudOperations(String colName, boolean usecf, String mongoUrl) {
		collectionName = colName;
		useCF = usecf;
		if (useCF == false) {
			MongoOperations.getInstance(false).bindToDb(mongoUrl, DB_NAME);
		}
		MongoOperations.getInstance(useCF).createCollection(collectionName);
		this.collection = MongoOperations.getInstance(useCF).getDb().getCollection(collectionName, CommentModel.class);
	}

	public Map<String, CommentModel> findAll(ArrayList<String> topLevelCommentIds) {

		Map<String, CommentModel> listOfCommentsFound = new HashMap<String, CommentModel>();

		for (int i = 0; i < topLevelCommentIds.size(); i++) {
			String eachId = topLevelCommentIds.get(i);
			CommentModel foundComment = collection.find(eq(SEARCH_PARAM, eachId)).first();
			if (foundComment != null) {
				String searchId = foundComment.getChildCommentId();
				listOfCommentsFound.put(foundComment.getCommentId(), foundComment);
				while (searchId != null) {
					// check who has this id in its parent
					foundComment = collection.find(eq(SEARCH_PARAM, searchId)).first();
					if (foundComment != null) {
						searchId = foundComment.getChildCommentId();
						listOfCommentsFound.put(foundComment.getCommentId(), foundComment);
					} else {
						searchId = null;
					}

				}
			}

		}

		return listOfCommentsFound;
	}

	public CommentModel findOne(String commentId) {
		CommentModel foundUser = collection.find(eq(SEARCH_PARAM, commentId)).first();
		return foundUser;
	}

	public CommentModel save(CommentModel commentObj) {

		collection.insertOne(commentObj);
		return commentObj;

	}

	public ArrayList<CommentModel> saveMany(ArrayList<CommentModel> commentCollection) {
		collection.insertMany(commentCollection);
		return commentCollection;

	}

	public ArrayList<CommentModel> delete(String commentId) {
		ArrayList<CommentModel> listOfDeletedComments = new ArrayList<CommentModel>();

		while (commentId != null) {
			CommentModel deletedComment = collection.findOneAndDelete(eq(SEARCH_PARAM, commentId));
			listOfDeletedComments.add(deletedComment);
			if (deletedComment != null) {
				commentId = deletedComment.getChildCommentId();
			} else {
				commentId = null;
			}

		}
		return listOfDeletedComments;
	}

	public ArrayList<CommentModel> updateComments(ArrayList<CommentModel> commentObjList) {
		ArrayList<CommentModel> updatedComments = new ArrayList<CommentModel>();
		for (int i = 0; i < commentObjList.size(); i++) {
			CommentModel eachComment = commentObjList.get(i);
			collection.findOneAndReplace(eq(SEARCH_PARAM, eachComment.getUserId()), eachComment);
			updatedComments.add(eachComment);
		}
		return commentObjList;
	}

	public CommentModel updateComment(CommentModel eachComment) {
		Bson filter = Filters.eq(SEARCH_PARAM, eachComment.getCommentId());
		Bson updates = Updates.set(UPDATE_PARAM, eachComment.getCommentString());

		CommentModel updatedValue = collection.findOneAndUpdate(filter, updates);
		return updatedValue;
	}

}
