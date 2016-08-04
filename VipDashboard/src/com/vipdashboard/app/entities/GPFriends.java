package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class GPFriends {
	@SerializedName("FFSLNO")
	public int FFSLNO;
	@SerializedName("GP_SLNO")
	public int GP_SLNO;
	@SerializedName("FriendID")
	public String FriendID;
	@SerializedName("GP_Person")
	public GPPerson GP_Person;
}
