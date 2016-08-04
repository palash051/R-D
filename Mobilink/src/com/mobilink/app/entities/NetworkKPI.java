package com.mobilink.app.entities;

import com.google.gson.annotations.SerializedName;

public class NetworkKPI {
	@SerializedName("ID")
	public int ID;
	@SerializedName("CompanyID")
	public int CompanyID;
	@SerializedName("ReportType")
	public String ReportType;
	@SerializedName("ReportSubType")
	public String ReportSubType;
	@SerializedName("QuarterName")
	public String QuarterName;
	@SerializedName("Value")
	public double Value;
	//@SerializedName("AddedBy")
	public String AddedBy;
	//@SerializedName("AddedTime")
	public String AddedTime;
	@SerializedName("Year")
	public String Year;

	public CompanySetup companySetup;
}
