package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class GPFeed {
	@SerializedName("FeedSLNO")
	public int FeedSLNO;
	@SerializedName("GP_SLNO")
	public int GP_SLNO;
	@SerializedName("FeedText")
	public String FeedText;
	@SerializedName("FeedTime")
	public String FeedTime;
	@SerializedName("GP_Person")
	public GPPerson GP_Person;
}
