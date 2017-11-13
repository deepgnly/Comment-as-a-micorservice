package com.commentservice.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.commentservice.dboperations.ResourceCrudOperations;
import com.commentservice.model.ResourceModel;

@Service
public class ResourceServiceImpl implements ResourceService {
	private ResourceCrudOperations resourceCrudObj;

	public ResourceServiceImpl(String collectionName, boolean useCF, String mongoUrl) {
		resourceCrudObj = new ResourceCrudOperations(collectionName, useCF, mongoUrl);
	}

	@Override
	public Iterable<ResourceModel> findAllResource() {
		return this.resourceCrudObj.findAll();
	}

	@Override
	public ResourceModel createResource(ResourceModel resourceObj) {
		return this.resourceCrudObj.save(resourceObj);
	}

	@Override
	public void deleteResource(String id) {
		this.resourceCrudObj.delete(id);
	}

	@Override
	public ResourceModel updateResource(ResourceModel resourceObj) {
		// TODO Auto-generated method stub
		return resourceCrudObj.updateResource(resourceObj);
	}

	@Override
	public ResourceModel getResourceById(String id) {
		// TODO Auto-generated method stub
		return this.resourceCrudObj.findOne(id);
	}

	@Override
	public ArrayList<String> getAllCommentsOfTheResource(String resourceId) {
		// TODO Auto-generated method stub
		return this.resourceCrudObj.getAllCommentIds(resourceId);
	}
	
	@Override
	public String deleteRootComment(String resourceId,String commentId) {
		// TODO Auto-generated method stub
		return this.resourceCrudObj.deleteRootComment(resourceId,commentId);
	}

}
