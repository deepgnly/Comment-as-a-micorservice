package com.userservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.userservice.model.UserModel;
import com.userservice.services.UserService;
import com.userservice.services.UserServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class UserController {

	private UserService userService;
	public static final String PLAIN_RESPONSE_TYPE = "text/plain";
	public static final String JSON_RESPONSE_TYPE = "application/json";
	public static final String COLLECTION_NAME = "userCollection";
	boolean USE_CF = false;
	String MONGO_LOCAL_HOST_URL = "mongodb://localhost:27017";

	@Autowired
	public void setUserService() {
		this.userService = new UserServiceImpl(COLLECTION_NAME, USE_CF, MONGO_LOCAL_HOST_URL);
	}

	@ApiOperation(value = "Ping User Service")
	@RequestMapping(value = "/user/ping", method = RequestMethod.GET, produces = PLAIN_RESPONSE_TYPE)
	public ResponseEntity<?> pingUser() {
		return new ResponseEntity<>("Ping User Successful", HttpStatus.OK);
	}

	@ApiOperation(value = "Creates a User")
	@RequestMapping(value = "/user/create", method = RequestMethod.POST, produces = PLAIN_RESPONSE_TYPE)
	public ResponseEntity<?> addUser(@RequestBody UserModel userModelObj) {
		UserModel createdUser = userService.createUser(userModelObj);
		return new ResponseEntity<>("New User added successfully", HttpStatus.OK);
	}

	@ApiOperation(value = "Updates a user")
	@RequestMapping(value = "/user/update/{userId}", method = RequestMethod.PUT, produces = PLAIN_RESPONSE_TYPE)
	public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserModel updatedUser) {
		userService.updateUser(updatedUser);
		return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
	}

	@ApiOperation(value = "List all available users", response = Iterable.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfull in finding the list"),
			@ApiResponse(code = 404, message = "The resource you were trying to reach is not found") })
	@RequestMapping(value = "/user/list", method = RequestMethod.GET, produces = "application/json")
	public Iterable<UserModel> list() {
		Iterable<UserModel> userList = userService.findAllUsers();
		return userList;
	}

	@ApiOperation(value = "Find user with an Id", response = UserModel.class)
	@RequestMapping(value = "/user/find/{userId}", method = RequestMethod.GET, produces = JSON_RESPONSE_TYPE)
	public UserModel findPUser(@PathVariable String userId) {
		UserModel usrModelObj = userService.getUserById(userId);
		return usrModelObj;
	}

	@ApiOperation(value = "Delete a user")
	@RequestMapping(value = "/user/delete/{userId}", method = RequestMethod.DELETE, produces = PLAIN_RESPONSE_TYPE)
	public ResponseEntity<?> delete(@PathVariable String userId) {
		userService.deleteUser(userId);
		return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);

	}

}
