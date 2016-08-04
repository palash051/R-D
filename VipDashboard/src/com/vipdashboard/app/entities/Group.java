package com.vipdashboard.app.entities;

import org.jivesoftware.smackx.muc.MultiUserChat;

import com.google.gson.annotations.SerializedName;

public class Group {
	@SerializedName("GroupID")
	public int GroupID;
	@SerializedName("Name")
	public String Name;
	@SerializedName("CompanyID")
	public int CompanyID;
	@SerializedName("CreatedDate")
	public String CreatedDate;
	@SerializedName("User")
	public User user;
	
	public MultiUserChat MultiUserChatId;
	
	public Group(){
		user = new User();
	}
}
