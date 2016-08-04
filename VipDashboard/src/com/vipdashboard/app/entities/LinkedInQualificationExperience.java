package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class LinkedInQualificationExperience {
	@SerializedName("INQENo")
	public int INQENo;
	@SerializedName("in_Number")
	public int in_Number;
	@SerializedName("Qualification_Experience")
	public String Qualification_Experience;
	@SerializedName("Duration_From")
	public String Duration_From;
	@SerializedName("Duration_To")
	public String Duration_To;
	@SerializedName("isWorkSpace")
	public boolean isWorkSpace;
	@SerializedName("Position")
	public String Position;
	@SerializedName("QualificationExperienceType")
	public String QualificationExperienceType;
	@SerializedName("LinkedIn_Person")
	public LinkedInPerson LinkedIn_Person;
}
