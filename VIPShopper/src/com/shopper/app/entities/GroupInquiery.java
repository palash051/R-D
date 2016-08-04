package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class GroupInquiery {

	@SerializedName("Groups")
	public Groups groups;

	public GroupInquiery() {
		groups = new Groups();
	}
}
