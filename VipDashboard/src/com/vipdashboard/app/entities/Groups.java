package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Groups {

	@SerializedName("Groups")
	public List<Group> groupList;

	public Groups() {
		groupList = new ArrayList<Group>();

	}

}
