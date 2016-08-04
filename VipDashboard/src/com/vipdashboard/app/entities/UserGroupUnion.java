package com.vipdashboard.app.entities;

import java.io.Serializable;


import com.google.gson.annotations.SerializedName;

public class UserGroupUnion implements Serializable{	
	private static final long serialVersionUID = 1L;
	@SerializedName("ID")
	public int ID;
	@SerializedName("Name")
	public String Name;
	@SerializedName("PostedDate")
	public String PostedDate;
	@SerializedName("type")
	public String Type;
	@SerializedName("LastMessage")
	public String LastMessage;
	@SerializedName("userOnlinestatus")
	public long userOnlinestatus;

	@SerializedName("ProfileImagePath")
	public String ProfileImagePath;
	
	@SerializedName("isFavourite")
	public boolean isFavourite;

	@SerializedName("Latitude")
	public double Latitude;
	@SerializedName("Longitude")
	public double Longitude;
	@SerializedName("User")
	public User user;
	@SerializedName("Group")
	public Group group;
	@SerializedName("Email")
	public String Email;


	public UserGroupUnion() {
		user = new User();
		group = new Group();
	}


}
