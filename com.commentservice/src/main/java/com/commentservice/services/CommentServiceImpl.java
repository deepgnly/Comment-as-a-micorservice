package com.commentservice.services;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.commentservice.dboperations.CommentCrudOperations;
import com.commentservice.model.CommentModel;

@Service
public class CommentServiceImpl implements CommentService {
	private CommentCrudOperations commentCrudObj;

	public CommentServiceImpl(String collectionName, boolean useCF, String mongoUrl) {
		commentCrudObj = new CommentCrudOperations(collectionName, useCF, mongoUrl);
	}

	@Override
	public Map<String,CommentModel> findAllComments(ArrayList<String> commentId) {
		return this.commentCrudObj.findAll(commentId);
	}

	@Override
	public CommentModel createComment(CommentModel commentObj) {
		return this.commentCrudObj.save(commentObj);
	}

	@Override
	public ArrayList<CommentModel> deleteComment(String id) {
		return this.commentCrudObj.delete(id);
	}

	@Override
	public ArrayList<CommentModel> updateComments(ArrayList<CommentModel> commentObjList) {
		// TODO Auto-generated method stub
		return commentCrudObj.updateComments(commentObjList);
	}

	@Override
	public CommentModel getCommentById(String id) {
		// TODO Auto-generated method stub
		return this.commentCrudObj.findOne(id);
	}

	@Override
	public ArrayList<CommentModel> createComments(ArrayList<CommentModel> comments) {
		// TODO Auto-generated method stub
		return this.commentCrudObj.saveMany(comments);
	}

	@Override
	public Iterable<CommentModel> findAllComment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommentModel updateComment(CommentModel eachComment) {
		// TODO Auto-generated method stub
		return this.commentCrudObj.updateComment(eachComment);
	}


}
