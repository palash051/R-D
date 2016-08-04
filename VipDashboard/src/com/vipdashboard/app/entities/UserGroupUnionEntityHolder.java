package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class UserGroupUnionEntityHolder {
	@SerializedName("UserGroupUnion")
	public UserGroupUnion userGroupUnion;
	
	public UserGroupUnionEntityHolder() {
		userGroupUnion=new UserGroupUnion();
	}
}
