package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class FacebookFeed {
	@SerializedName("FBFID")
	public int FBFID;
	@SerializedName("FBNo")
	public int FBNo;
	@SerializedName("FText")
	public String FText;
	@SerializedName("Ftime")
	public String Ftime;
	@SerializedName("FacebookPerson")
	public FacebokPerson FacebookPerson;
}
