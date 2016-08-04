package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class UserFriend {
	@SerializedName("UserFriendSLNO")
	public int UserFriendSLNO;
	@SerializedName("UserNumber")
	public int UserNumber;
	@SerializedName("FriendIDs")
	public String FriendIDs;
	@SerializedName("IsFriends")
	public boolean IsFriends;
	@SerializedName("RelationshipStatus")
	public String RelationshipStatus;

	public User User;
}
