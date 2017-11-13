package com.commentservice.transferObjects;

import java.util.ArrayList;

public class CreateAndReadResourceTO {

	private String resourceId;
	ArrayList<CreateAndReadCommentsTO> listOfComments;

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public ArrayList<CreateAndReadCommentsTO> getListOfComments() {
		return listOfComments;
	}

	public void setListOfComments(ArrayList<CreateAndReadCommentsTO> listOfComments) {
		this.listOfComments = listOfComments;
	}


}
