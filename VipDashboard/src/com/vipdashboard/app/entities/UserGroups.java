package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class UserGroups {
	@SerializedName("UserGroups")
	public List<UserGroup> userGroupList;

	public UserGroups() {
		userGroupList = new ArrayList<UserGroup>();
	}
}
