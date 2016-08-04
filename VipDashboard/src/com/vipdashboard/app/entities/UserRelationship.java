package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class UserRelationship {
	@SerializedName("UserRelationshipID")
	public int UserRelationshipID;
	@SerializedName("UserNumber")
	public int UserNumber;
	@SerializedName("FriendNumber")
	public int FriendNumber;
	@SerializedName("IsFavourite")
	public boolean IsFavourite;
	@SerializedName("RelationshipID")
	public int RelationshipID;
	@SerializedName("IsActive")
	public boolean IsActive;
	public Relationship Relationship;
	public User User;
	public User User1;
}
