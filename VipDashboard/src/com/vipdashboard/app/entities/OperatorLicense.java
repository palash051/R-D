package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class OperatorLicense {
	
	@SerializedName("OperatorLicenseID")
	public int OperatorLicenseID;
	@SerializedName("OperatorName")
	public String OperatorName;
	@SerializedName("MCC")
	public String MCC;
	@SerializedName("MNC")
	public String MNC;
	@SerializedName("OperatorCountryISO")
	public String OperatorCountryISO;
	@SerializedName("OperatorCountry")
	public String OperatorCountry;
	@SerializedName("ExpiryDate")
	public String ExpiryDate;
	@SerializedName("IsActivateLicensed")
	public boolean IsActivateLicensed;
	@SerializedName("LastActivatedDate")
	public String LastActivatedDate;
	@SerializedName("CreateDate")
	public String CreateDate;
	@SerializedName("FirstUserNumber")
	public int FirstUserNumber;
	@SerializedName("HelpDeskMobileNo")
	public String HelpDeskMobileNo;
	@SerializedName("HelpDeskEmailAddress")
	public String HelpDeskEmailAddress;
	@SerializedName("AlternateHelpDeskMobileNo")
	public String AlternateHelpDeskMobileNo;
	@SerializedName("AlternateHelpDeskEmailAddress1")
	public String AlternateHelpDeskEmailAddress1;
	@SerializedName("Extra1")
	public String Extra1;
	@SerializedName("Extra2")
	public String Extra2;
	@SerializedName("Extra3")
	public String Extra3;
	@SerializedName("Extra4")
	public String Extra4;
	@SerializedName("Extra5")
	public String Extra5;
	
}
