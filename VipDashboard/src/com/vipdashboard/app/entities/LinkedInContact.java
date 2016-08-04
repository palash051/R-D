package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class LinkedInContact {
	@SerializedName("inContactNO")
	public int inContactNO;
	@SerializedName("in_Number")
	public int in_Number;
	@SerializedName("MobileNumber")
	public String MobileNumber;
	@SerializedName("Alternate_Emails")
	public String Alternate_Emails;
	@SerializedName("ZIPCode")
	public int ZIPCode;
	@SerializedName("Country")
	public String Country;
	@SerializedName("Website")
	public String Website;
	@SerializedName("LinkedIn_Person")
	public LinkedInPerson LinkedIn_Person;
}
