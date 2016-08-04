package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class UserRoot {
	@SerializedName("User")
	public User user;
	public UserRoot() {
		user=new User();
	}
}
