package com.vipdashboard.app.entities;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class FacebokPerson {
	@SerializedName("FBNo")
	public int FBNo;
    @SerializedName("FB_UserID")
    public String FB_UserID;
    @SerializedName("FB_UserName")
    public String FB_UserName;
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
    @SerializedName("PrimaryEmail")
    public String PrimaryEmail;
    @SerializedName("Professional_Skills")
    public String Professional_Skills;
    @SerializedName("About")
    public String About;
    @SerializedName("Pages")
    public String Pages;
    @SerializedName("Groups")
    public String Groups;
    @SerializedName("Apps")
    public String Apps;
    @SerializedName("FacebookContact")
    public ArrayList<FacebookContact> FacebookContact;
    @SerializedName("FacebookFeed")
    public ArrayList<FacebookFeed> FacebookFeed;
    @SerializedName("FacebookFriends")
    public ArrayList<FacebookFriends> FacebookFriends;
    @SerializedName("Facebook_Qualification_Experience")
    public ArrayList<FacebookQualificationExperience> FacebookQualificationExperience;
    public String userNumber;
    public int isSync;
    
    public FacebokPerson(){
    	FacebookContact = new ArrayList<FacebookContact>();
    	FacebookFeed = new ArrayList<FacebookFeed>();
    	FacebookFriends = new ArrayList<FacebookFriends>();
    	FacebookQualificationExperience = new ArrayList<FacebookQualificationExperience>();
    }
}
