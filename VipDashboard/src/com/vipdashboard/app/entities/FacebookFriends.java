package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class FacebookFriends {
	@SerializedName("PFSLNo")
	public int PFSLNo;
	@SerializedName("FBNo")
	public int FBNo;
	@SerializedName("FriendsID")
	public String FriendsID;
	public String FriendName;
	@SerializedName("FacebookPerson")
	public FacebokPerson FacebookPerson;
}
