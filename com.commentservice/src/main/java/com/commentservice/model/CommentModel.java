package com.commentservice.model;

import java.time.LocalDate;
import java.util.UUID;

public class CommentModel {

	private String commentId;
	private String childCommentId;
	private String userId;
	private String commentString;

	LocalDate createdDate;
	
	public CommentModel(){
		
	}

	public CommentModel(boolean create) {
		this.commentId = UUID.randomUUID().toString().replace("-", "");
	}

	public String getCommentString() {
		return commentString;
	}

	public void setCommentString(String commentString) {
		this.commentString = commentString;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getChildCommentId() {
		return childCommentId;
	}

	public void setChildCommentId(String childCommentId) {
		this.childCommentId = childCommentId;
	}

	public String getCommentId() {
		return commentId;
	}
	
	public void setCommentId(String id){
		this.commentId=id;
	}
}