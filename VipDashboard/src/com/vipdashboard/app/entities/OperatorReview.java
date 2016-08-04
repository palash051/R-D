package com.vipdashboard.app.entities;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class OperatorReview {

	@SerializedName("SLNO")
	public int SLNO;
	@SerializedName("CompanyID")
	public int CompanyID;
	@SerializedName("PhoneID")
	public int PhoneID;
	@SerializedName("NetworkQuality")
	public int NetworkQuality;
	@SerializedName("CustomerCare")
	public int CustomerCare;
	@SerializedName("PackagesPrice")
	public int PackagesPrice;
	@SerializedName("Remarks")
	public String Remarks;
	@SerializedName("ReviewAt")
	public Long ReviewAt;	

	public Company Company;
	public PhoneBasicInformation PhoneBasicInformation;

}
