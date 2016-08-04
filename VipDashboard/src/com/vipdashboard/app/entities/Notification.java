package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class Notification {
	@SerializedName("NotID")
	public int NotID;
	@SerializedName("GroupID")
	public int GroupID;
	@SerializedName("LevelID")
	public int LevelID;
	@SerializedName("MsgFrom")
	public int MsgFrom;
	@SerializedName("MsgTo")
	public int MsgTo;
	@SerializedName("NotificationText")
	public String NotificationText;
	@SerializedName("ProcessTime")
	public String ProcessTime;
	@SerializedName("UpdatedDate")
	public String UpdatedDate;
	@SerializedName("Description")
	public String Description;
	@SerializedName("Comments")
	public String Comments;
	@SerializedName("User")
	public User msgFromUser;
}
