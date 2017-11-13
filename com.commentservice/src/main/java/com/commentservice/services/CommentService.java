package com.commentservice.services;

import java.util.ArrayList;
import java.util.Map;

import com.commentservice.model.CommentModel;

public interface CommentService {
	Iterable<CommentModel> findAllComment();

	CommentModel getCommentById(String id);

	CommentModel createComment(CommentModel comment);

	ArrayList<CommentModel> createComments(ArrayList<CommentModel> comments);

	ArrayList<CommentModel> updateComments(ArrayList<CommentModel> comment);

	CommentModel updateComment(CommentModel eachComment);

	ArrayList<CommentModel> deleteComment(String id);

	Map<String, CommentModel> findAllComments(ArrayList<String> commentIds);

}
