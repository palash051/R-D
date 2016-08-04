package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Users {
	@SerializedName("Users")
	public List<User> userList;

	public Users() {
		
		userList = new ArrayList<User>();
	}
}
