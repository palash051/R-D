package com.vipdashboard.app.entities;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class Company {
	@SerializedName("CompanyID")
	public int CompanyID;
	@SerializedName("Name")
	public String Name;
	@SerializedName("Email")
	public String Email;
	@SerializedName("Phone")
	public String Phone;
	@SerializedName("Address")
	public String Address;
	@SerializedName("IsActive")
	public boolean IsActive;
	@SerializedName("CompanyTypeID")
	public int CompanyTypeID;
	@SerializedName("CompanyLogo")
	public String CompanyLogo;
	@SerializedName("WebAdd")
	public String WebAdd;
	@SerializedName("SupportAddress")
	public String SupportAddress;
	@SerializedName("Offers")
	public String Offers;
	@SerializedName("Country")
	public String Country;
	@SerializedName("isLicensed")
	public boolean isLicensed;
	@SerializedName("LastUpdated")
	public String LastUpdated;
	@SerializedName("UpdatedBy")
	public String UpdatedBy;
	@SerializedName("ExpiryDate")
	public String ExpiryDate;
	@SerializedName("MCC")
	public String MCC;
	@SerializedName("MNC")
	public String MNC;
	@SerializedName("CompanyType")
	public CompanyType CompanyType;
	@SerializedName("CompanyDivisions")
	public ArrayList<CompanyDivision> CompanyDivisions;

	public Company() {
		CompanyID = 0;
		Name = "";
		Email = "";
		Phone = "";
		Address = "";
		CompanyType = new CompanyType();
		CompanyDivisions = new ArrayList<CompanyDivision>();
	}
}
