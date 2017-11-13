package com.commentservice.transferObjects;

import java.util.ArrayList;

public class UpdateResourceTO {

	private ArrayList<UpdateCommentsTO> listOfCommentsToBeUpdated;

	public ArrayList<UpdateCommentsTO> getListOfCommentsToBeUpdated() {
		return listOfCommentsToBeUpdated;
	}

	public void setListOfCommentsToBeUpdated(ArrayList<UpdateCommentsTO> listOfCommentsToBeUpdated) {
		this.listOfCommentsToBeUpdated = listOfCommentsToBeUpdated;
	}
}
