package com.vipdashboard.app.entities;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Collaboration implements Serializable{
	@SerializedName("MsgFrom")
	public int MsgFrom;
	@SerializedName("MsgText")
	public String MsgText;
	@SerializedName("GroupID")
	public int GroupID;
	@SerializedName("MsgTo")
	public int MsgTo;
	@SerializedName("Latitude")
	public double Latitude;
	@SerializedName("Longitude")
	public double Longitude;
	@SerializedName("PostedDate")
	public String PostedDate;
	public String UserType;
	@SerializedName("User")
	public User user;
	@SerializedName("Group")
	public Group group;
	@SerializedName("FilePath")
	public String FilePath;
	public String MsgFromName;
	public String GroupName;
	
	static final long serialVersionUID=10;  
}
