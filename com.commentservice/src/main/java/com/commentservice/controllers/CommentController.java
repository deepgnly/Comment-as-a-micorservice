package com.commentservice.controllers;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.commentservice.model.CommentModel;
import com.commentservice.model.ResourceModel;
import com.commentservice.services.CommentService;
import com.commentservice.services.CommentServiceImpl;
import com.commentservice.services.ResourceService;
import com.commentservice.services.ResourceServiceImpl;
import com.commentservice.transferObjects.CreateAndReadCommentsTO;
import com.commentservice.transferObjects.CreateAndReadResourceTO;
import com.commentservice.transferObjects.UpdateCommentsTO;
import com.commentservice.transferObjects.UpdateResourceTO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class CommentController {

	private CommentService commentService;
	private ResourceService resourceService;
	public static final String PLAIN_RESPONSE_TYPE = "text/plain";
	public static final String JSON_RESPONSE_TYPE = "application/json";
	public static final String COLLECTION_NAME_COMMENTS = "commentsCollection";
	public static final String COLLECTION_NAME_RESOURCE = "resourceCollection";

	boolean USE_CF = false;
	String MONGO_LOCAL_HOST_URL = "mongodb://localhost:27017";

	@Autowired
	public void setCommentService() {
		this.commentService = new CommentServiceImpl(COLLECTION_NAME_COMMENTS, USE_CF, MONGO_LOCAL_HOST_URL);
		this.resourceService = new ResourceServiceImpl(COLLECTION_NAME_RESOURCE, USE_CF, MONGO_LOCAL_HOST_URL);
	}

	@ApiOperation(value = "Ping Comment Service")
	@RequestMapping(value = "/comment/ping", method = RequestMethod.GET, produces = PLAIN_RESPONSE_TYPE)
	public ResponseEntity<?> pingCommentService() {
		return new ResponseEntity<>("Ping Comment Successful", HttpStatus.OK);
	}

	// Can be used for bulk create
	@ApiOperation(value = "Create comments")
	@RequestMapping(value = "/comment/create", method = RequestMethod.POST, produces = JSON_RESPONSE_TYPE)
	public ResponseEntity<?> addComments(@RequestBody CreateAndReadResourceTO resourceTO) {

		ArrayList<CreateAndReadCommentsTO> allIncomingComments = resourceTO.getListOfComments();
		ResourceModel resourceModelObj = new ResourceModel();
		resourceModelObj.setResourceId(resourceTO.getResourceId());
		ArrayList<String> totalRootNodes = new ArrayList<String>();

		for (int i = 0; i < allIncomingComments.size(); i++) {
			CreateAndReadCommentsTO eachComment = allIncomingComments.get(i);
			ArrayList<CreateAndReadCommentsTO> commentsStack = new ArrayList<CreateAndReadCommentsTO>();
			commentsStack.add(eachComment);
			String commentString = eachComment.getCommentString();

			while (commentString != null) {
				eachComment = eachComment.getChildComment();
				if (eachComment != null) {
					commentString = eachComment.getCommentString();
					if (commentString != null) {
						commentsStack.add(eachComment);
					}
				}
			}
			String childCommentId = null;
			for (int j = commentsStack.size() - 1; j >= 0; j--) {
				CreateAndReadCommentsTO eachCommentWithinStack = commentsStack.get(j);
				childCommentId = parseAndSaveComment(eachCommentWithinStack, childCommentId).getCommentId();
				eachCommentWithinStack.setCommentId(childCommentId);
				if (j == 0) {
					totalRootNodes.add(childCommentId);

				}

			}
		}
		resourceModelObj.setListOfComments(totalRootNodes);
		if (totalRootNodes.size() > 0) {
			resourceService.updateResource(resourceModelObj);
		}
		return new ResponseEntity<>(resourceTO, HttpStatus.OK);
	}

	@ApiOperation(value = "Update Comments")
	@RequestMapping(value = "/comment/update", method = RequestMethod.PUT, produces = JSON_RESPONSE_TYPE)
	public ResponseEntity<?> updateComments(@RequestBody UpdateResourceTO updatedResourceToObj) {

		ArrayList<UpdateCommentsTO> listOfUpdatedComments = updatedResourceToObj.getListOfCommentsToBeUpdated();
		ArrayList<UpdateCommentsTO> listOfUpdatedCommentsFromDb = new ArrayList<UpdateCommentsTO>();

		for (int i = 0; i < listOfUpdatedComments.size(); i++) {
			CommentModel updatedValues = commentService
					.updateComment(parseToCommentModel(listOfUpdatedComments.get(i)));
			if (updatedValues != null) {
				listOfUpdatedCommentsFromDb.add(parseCommentModelToUpdate(updatedValues));
			}
		}
		updatedResourceToObj.setListOfCommentsToBeUpdated(listOfUpdatedCommentsFromDb);
		return new ResponseEntity<>(updatedResourceToObj, HttpStatus.OK);
	}

	@ApiOperation(value = "List all comments available for Resource", response = Iterable.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfull in finding the comments from the "),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@RequestMapping(value = "/comment/listAll/resource/{resourceId}", method = RequestMethod.GET, produces = "application/json")
	public CreateAndReadResourceTO listComments(@PathVariable String resourceId) {
		// get thelist of all the comment-ids associated with the resource
		ArrayList<String> listOfTopIds = this.resourceService.getAllCommentsOfTheResource(resourceId);
		Map<String, CommentModel> commentModelObj = commentService.findAllComments(listOfTopIds);
		CreateAndReadResourceTO resourceToObj = new CreateAndReadResourceTO();
		resourceToObj.setResourceId(resourceId);
		ArrayList<CreateAndReadCommentsTO> totalCommentsList = new ArrayList<CreateAndReadCommentsTO>();

		for (String key : commentModelObj.keySet()) {
			if (listOfTopIds.contains(key)) {
				CommentModel eachComment = commentModelObj.get(key);
				String childCommentId = eachComment.getChildCommentId();
				ArrayList<CommentModel> listOfCommentsStack = new ArrayList<CommentModel>();
				listOfCommentsStack.add(eachComment);
				CommentModel childComment;
				while (childCommentId != null) {
					boolean doesContainChildInMap = commentModelObj.containsKey(childCommentId);
					if (doesContainChildInMap == true) {
						childComment = commentModelObj.get(childCommentId);
						listOfCommentsStack.add(childComment);
						childCommentId = childComment.getChildCommentId();
					}
				}
				CreateAndReadCommentsTO previouscommentsTO = null;

				for (int i = listOfCommentsStack.size() - 1; i >= 0; i--) {
					CreateAndReadCommentsTO currentcommentsTO = parseCommentModel(listOfCommentsStack.get(i));
					if (previouscommentsTO != null) {
						currentcommentsTO.setChildComment(previouscommentsTO);
					}
					previouscommentsTO = currentcommentsTO;
				}

				totalCommentsList.add(previouscommentsTO);
			}
		}
		resourceToObj.setListOfComments(totalCommentsList);

		return resourceToObj;
	}

	@ApiOperation(value = "Delete a comment")
	@RequestMapping(value = "/comment/resource/{resourceId}/delete/{commentId}", method = RequestMethod.DELETE, produces = PLAIN_RESPONSE_TYPE)
	public ResponseEntity<?> deleteComment(@PathVariable String resourceId, @PathVariable String commentId) {
		ArrayList<String> listOfTopIds = this.resourceService.getAllCommentsOfTheResource(resourceId);
		commentService.deleteComment(commentId);
		if (listOfTopIds.contains(commentId)) {
			// remove the comment from the resource as well
			resourceService.deleteRootComment(resourceId, commentId);
		}
		return new ResponseEntity<>("Comment deleted recursively-successfully", HttpStatus.OK);

	}

	private CommentModel parseAndSaveComment(CreateAndReadCommentsTO eachIncomingComment, String parentId) {
		return this.commentService.createComment(this.parseToCommentModel(eachIncomingComment, parentId));
	}

	private CommentModel parseToCommentModel(CreateAndReadCommentsTO eachIncomingComment, String childCommentId) {
		CommentModel commentModelObj = new CommentModel(true);
		commentModelObj.setCommentString(eachIncomingComment.getCommentString());
		commentModelObj.setUserId(eachIncomingComment.getUserId());
		commentModelObj.setChildCommentId(childCommentId);
		return commentModelObj;
	}

	private CommentModel parseToCommentModel(UpdateCommentsTO eachIncomingComment) {
		CommentModel commentModelObj = new CommentModel(true);
		commentModelObj.setCommentId(eachIncomingComment.getCommentId());
		commentModelObj.setCommentString(eachIncomingComment.getCommentString());
		return commentModelObj;
	}

	private CreateAndReadCommentsTO parseCommentModel(CommentModel eachModelReceivedFromDb) {
		CreateAndReadCommentsTO commentsToObj = new CreateAndReadCommentsTO();
		commentsToObj.setCommentId(eachModelReceivedFromDb.getCommentId());
		commentsToObj.setCommentString(eachModelReceivedFromDb.getCommentString());
		commentsToObj.setUserId(eachModelReceivedFromDb.getUserId());

		return commentsToObj;
	}

	private UpdateCommentsTO parseCommentModelToUpdate(CommentModel eachModelReceivedFromDb) {
		UpdateCommentsTO commentsToObj = new UpdateCommentsTO();
		commentsToObj.setCommentId(eachModelReceivedFromDb.getCommentId());
		commentsToObj.setCommentString(eachModelReceivedFromDb.getCommentString());

		return commentsToObj;
	}

}
