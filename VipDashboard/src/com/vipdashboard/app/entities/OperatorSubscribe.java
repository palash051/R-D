package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class OperatorSubscribe {
	@SerializedName("SLNO")
	public int SLNO;
	@SerializedName("CompanyID")
	public int CompanyID;
	@SerializedName("PhoneID")
	public int PhoneID;
	@SerializedName("ExistingPhone")
	public String ExistingPhone;
	@SerializedName("CountryIsoCode")
	public String CountryIsoCode;
	@SerializedName("Name")
	public String Name;
	@SerializedName("Email")
	public String Email;
	@SerializedName("SubscriberIdentity")
	public String SubscriberIdentity;
	@SerializedName("Photo")
	public String Photo;
	@SerializedName("SubscribeAt")
	public String SubscribeAt;
	@SerializedName("CustomerFeedback")
	public String CustomerFeedback;
	@SerializedName("customerFeedbackAt")
	public String customerFeedbackAt;

	public Company Company;
	public PhoneBasicInformation PhoneBasicInformation;
}
