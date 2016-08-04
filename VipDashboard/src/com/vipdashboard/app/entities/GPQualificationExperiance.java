package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class GPQualificationExperiance {
	@SerializedName("GPQENo")
	public int GPQENo;
	@SerializedName("GP_SLNO")
	public int GP_SLNO;
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
	@SerializedName("GP_Person")
	public GPPerson GP_Person;
}
