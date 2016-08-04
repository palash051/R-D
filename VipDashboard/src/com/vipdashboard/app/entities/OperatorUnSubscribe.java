package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class OperatorUnSubscribe {
	@SerializedName("SLNO")
	public int SLNO;
	@SerializedName("CompanyID")
	public int CompanyID;
	@SerializedName("PhoneID")
	public int PhoneID;
	@SerializedName("NumberToTerminate")
	public int NumberToTerminate;
	@SerializedName("Name")
	public String Name;
	@SerializedName("Email")
	public String Email;
	@SerializedName("RequestAt")
	public String RequestAt;
	@SerializedName("CustomerFeedback")
	public String CustomerFeedback;
	@SerializedName("CustomerFeedbackAt")
	public String CustomerFeedbackAt;

	public Company Company;
	public PhoneBasicInformation PhoneBasicInformation;
}
