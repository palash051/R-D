package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class OperatorMail {
	@SerializedName("OperatorMailID")
	int OperatorMailID;
	@SerializedName("PhoneId")
	int PhoneId;
	@SerializedName("ToMail")
	String ToMail;
	@SerializedName("CC")
	String CC;
	@SerializedName("BCC")
	String BCC;
	@SerializedName("MailSubject")
	String MailSubject;
	@SerializedName("MailContent")
	String MailContent;
	@SerializedName("Signature")
	String Signature;
	@SerializedName("SIMID")
	String SIMID;
	@SerializedName("UserNumber")
	int UserNumber;
	@SerializedName("OperatorLicenseID")
	int OperatorLicenseID;
	@SerializedName("Name")
	String Name;
	@SerializedName("MobileNumber")
	String MobileNumber;
	
}
