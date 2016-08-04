package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class UserFamilyMembers {
	
	@SerializedName("UserFamilyMembers")
	public List<UserFamilyMember> userFamilyMemberList;

	public UserFamilyMembers() {
		userFamilyMemberList = new ArrayList<UserFamilyMember>();
	}

}
