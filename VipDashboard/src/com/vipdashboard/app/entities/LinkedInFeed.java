package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class LinkedInFeed {
	@SerializedName("FeedSLNO")
	public int FeedSLNO;
	@SerializedName("in_Number")
	public int in_Number;
	@SerializedName("FeedText")
	public String FeedText;
	@SerializedName("FeedTime")
	public String FeedTime;
	@SerializedName("LinkedIn_Person")
	public LinkedInPerson LinkedIn_Person;
}
