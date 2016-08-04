package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class UserGroupUnionRoot {
	@SerializedName("UserGroupUnion")
	public UserGroupUnion userGroupUnion;
	public UserGroupUnionRoot() {
		userGroupUnion=new UserGroupUnion();
	}
}
