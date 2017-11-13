package com.commentservice.model;

import java.util.ArrayList;

public class ResourceModel {

	private String resourceId;
	ArrayList<String> listOfCommentIds;

	public ArrayList<String> getListOfComments() {
		return listOfCommentIds;
	}

	public void setListOfComments(ArrayList<String> listOfComments) {
		this.listOfCommentIds = listOfComments;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

}
