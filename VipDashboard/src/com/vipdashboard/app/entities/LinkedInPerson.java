package com.vipdashboard.app.entities;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class LinkedInPerson {
	 public LinkedInPerson()
     {
         LinkedInContact = new ArrayList<LinkedInContact>();
         LinkedInFeed = new ArrayList<LinkedInFeed>();
         LinkedInFriend = new ArrayList<LinkedInFriend>();
         LinkedInQualificationExperience = new ArrayList<LinkedInQualificationExperience>();
         Users = new ArrayList<User>();
     }
     @SerializedName("in_Number")
     public int in_Number;
     @SerializedName("in_UserID")
     public String in_UserID;
     @SerializedName("in_UserName")
     public String in_UserName;
     @SerializedName("PP_Path")
     public String PP_Path;
     @SerializedName("Gender")
     public String Gender;
     @SerializedName("MaritalStatus")
     public String MaritalStatus;
     @SerializedName("DateOfBirth")
     public String DateOfBirth;
     @SerializedName("Professional_Skills")
     public String Professional_Skills;
     @SerializedName("LinkedInContact")
     public ArrayList<LinkedInContact> LinkedInContact;
     @SerializedName("LinkedInFeed")
     public ArrayList<LinkedInFeed> LinkedInFeed;
     @SerializedName("LinkedInFriend")
     public ArrayList<LinkedInFriend> LinkedInFriend;
     @SerializedName("LinkedInQualificationExperience")
     public ArrayList<LinkedInQualificationExperience> LinkedInQualificationExperience;
     @SerializedName("Users")
     public ArrayList<User> Users;
}
