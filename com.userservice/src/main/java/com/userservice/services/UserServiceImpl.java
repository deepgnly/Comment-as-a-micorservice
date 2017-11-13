package com.userservice.services;

import org.springframework.stereotype.Service;
import com.userservice.dboperations.UserCrudOperations;
import com.userservice.model.UserModel;

@Service
public class UserServiceImpl implements UserService {
	private UserCrudOperations userCrudObj;

	public UserServiceImpl(String collectionName, boolean useCF, String mongoUrl) {
		userCrudObj = new UserCrudOperations(collectionName, useCF, mongoUrl);
	}

	@Override
	public Iterable<UserModel> findAllUsers() {
		return this.userCrudObj.findAll();
	}

	@Override
	public UserModel getUserById(String id) {
		return this.userCrudObj.findOne(id);
	}

	@Override
	public UserModel createUser(UserModel userObj) {
		return this.userCrudObj.save(userObj);
	}

	@Override
	public void deleteUser(String id) {
		this.userCrudObj.delete(id);
	}

	@Override
	public UserModel updateUser(UserModel userObj) {
		// TODO Auto-generated method stub
		return userCrudObj.updateUser(userObj);
	}

}
