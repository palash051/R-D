package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class UserFriendListRoot {
	@SerializedName("Users")
	public List<User> userList;
	
	public UserFriendListRoot() {
		userList=new ArrayList<User>();
	}
}
