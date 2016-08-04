package com.vipdashboard.app.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class UserGroupUnions{
	@SerializedName("UserGroupUnions")
	public List<UserGroupUnion> userGroupUnionList;

	public UserGroupUnions() {
		userGroupUnionList = new ArrayList<UserGroupUnion>();
	}

}
