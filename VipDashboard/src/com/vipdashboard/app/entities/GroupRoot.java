package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class GroupRoot {
	@SerializedName("Group")
	public Group group;
	
	public GroupRoot() {
		group=new Group();
	}

}
