package com.commentservice.transferObjects;

public class CreateAndReadCommentsTO {

	private String guid;
	private String userId;
	private String commentString;
	private CreateAndReadCommentsTO childComment = null;
	private String commentId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCommentString() {
		return commentString;
	}

	public void setCommentString(String commentString) {
		this.commentString = commentString;
	}

	public CreateAndReadCommentsTO getChildComment() {
		return childComment;
	}

	public void setChildComment(CreateAndReadCommentsTO childComment) {
		this.childComment = childComment;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
}
