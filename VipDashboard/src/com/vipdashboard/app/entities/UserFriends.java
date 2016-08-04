package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class UserFriends {
	@SerializedName("UserFriend")
	public UserFriend userFriend;
	public UserFriends() {
		userFriend = new UserFriend();
	}
}
