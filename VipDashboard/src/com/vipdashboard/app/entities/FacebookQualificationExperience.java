package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class FacebookQualificationExperience {
	@SerializedName("FBQENo")
	public int FBQENo;
	@SerializedName("FBNo")
	public int FBNo;
	@SerializedName("Qualification_Experience")
	public String QualificationExperience;
	@SerializedName("Duration_From")
	public String Duration_From;
	@SerializedName("Duration_To")
	public String Duration_To;
	@SerializedName("isWorkSpace")
	public boolean isWorkSpace;
	@SerializedName("Position")
	public String Position;
	@SerializedName("Qualification_Experience_Type")
	public String QualificationExperienceType;
	@SerializedName("FacebookPerson")
	public FacebokPerson FacebookPerson;
	public int isSync;
}
