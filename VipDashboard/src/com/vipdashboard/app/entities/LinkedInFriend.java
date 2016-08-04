package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class LinkedInFriend {
	@SerializedName("In_SLNO")
	public int In_SLNO;
	@SerializedName("in_Number")
	public int in_Number;
	@SerializedName("FriendID")
	public String FriendID;
	@SerializedName("LinkedIn_Person")
	public LinkedInPerson LinkedIn_Person;
}
