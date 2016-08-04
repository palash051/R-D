package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class SiteInformation {
	@SerializedName("SiteID")
	public int SiteID;
	@SerializedName("CompanyID")
	public int CompanyID;
	@SerializedName("Latitude")
	public double Latitude;
	@SerializedName("Longitude")
	public double Longitude;
	@SerializedName("Address")
	public String Address;
	@SerializedName("Vendor")
	public String Vendor;
	@SerializedName("BSC")
	public String BSC;
}
