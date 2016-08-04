package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class UserFriendList {
	@SerializedName("Users")
	public List<User> userList;

	public UserFriendList() {
		
		userList = new ArrayList<User>();
	}
}
