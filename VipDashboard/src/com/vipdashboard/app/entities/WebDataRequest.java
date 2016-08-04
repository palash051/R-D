package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class WebDataRequest {
	
	@SerializedName("PhoneID")
	public int PhoneID;
	@SerializedName("PhoneNo")
	public String PhoneNo;
	@SerializedName("IsUpdate")
	public int IsUpdate;
	@SerializedName("RequestAt")
	public String RequestAt;
	@SerializedName("RefreshAt")
	public String RefreshAt;
	@SerializedName("RequestBy")
	public int RequestBy;

}
