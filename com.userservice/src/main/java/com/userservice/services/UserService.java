package com.userservice.services;

import com.userservice.model.UserModel;

public interface UserService {
	Iterable<UserModel> findAllUsers();

	UserModel getUserById(String id);

	UserModel createUser(UserModel product);

	UserModel updateUser(UserModel product);

	void deleteUser(String id);

}
