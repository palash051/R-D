package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class UserFamilyMember {

	@SerializedName("UserNumber")
	public int UserNumber;
	@SerializedName("RelationShip")
	public String RelationShip;
	@SerializedName("RelationShip1")
	public int RelationShip1;
	@SerializedName("Name")
	public String Name;
	@SerializedName("ProfilePic")
	public String ProfilePic;
	@SerializedName("OnlineStatus")
	public long OnlineStatus;
}
