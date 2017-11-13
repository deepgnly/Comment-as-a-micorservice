package com.commentservice.services;

import java.util.ArrayList;

import com.commentservice.model.ResourceModel;

public interface ResourceService {
	
	Iterable<ResourceModel> findAllResource();

	ResourceModel getResourceById(String id);

	ResourceModel createResource(ResourceModel resourceObj);

	ResourceModel updateResource(ResourceModel resourceObj);

	void deleteResource(String id);

	ArrayList<String> getAllCommentsOfTheResource(String resourceId);

	String deleteRootComment(String resourceId, String commentId);

}
