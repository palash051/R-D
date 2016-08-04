package com.vipdashboard.app.entities;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class GPPerson {
	public GPPerson() {
		GP_Contact = new ArrayList<GPContact>();
		GP_Feed = new ArrayList<GPFeed>();
		GP_Friends = new ArrayList<GPFriends>();
		GP_Qualification_Experiance = new ArrayList<GPQualificationExperiance>();
		Users = new ArrayList<User>();
	}

	@SerializedName("GP_SLNO")
	public int GP_SLNO;
	@SerializedName("GP_UserID")
	public String GP_UserID;
	@SerializedName("Name")
	public String Name;
	@SerializedName("PP_Path")
	public String PP_Path;
	@SerializedName("Gender")
	public String Gender;
	@SerializedName("Relationship_Status")
	public String Relationship_Status;
	@SerializedName("DateOfBirth")
	public String DateOfBirth;
	@SerializedName("Religion")
	public String Religion;
	@SerializedName("Professional_Skills")
	public String Professional_Skills;
	@SerializedName("OtherNames")
	public String OtherNames;
	@SerializedName("GP_Contact")
	public ArrayList<GPContact> GP_Contact;
	@SerializedName("GP_Feed")
	public ArrayList<GPFeed> GP_Feed;
	@SerializedName("GP_Friends")
	public ArrayList<GPFriends> GP_Friends;
	@SerializedName("GP_Qualification_Experiance")
	public ArrayList<GPQualificationExperiance> GP_Qualification_Experiance;
	@SerializedName("Users")
	public ArrayList<User> Users;
}
