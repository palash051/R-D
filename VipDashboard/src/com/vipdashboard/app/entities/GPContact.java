package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class GPContact {
	@SerializedName("CSLNO")
	public int CSLNO;
	@SerializedName("GP_SLNO")
	public int GP_SLNO;
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
	@SerializedName("GP_Person")
	public GPPerson GP_Person;
}
