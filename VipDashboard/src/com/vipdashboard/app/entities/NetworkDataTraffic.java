package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class NetworkDataTraffic {
	@SerializedName("SLNo")
	public int SLNo;
	@SerializedName("ReportDate")
	public String ReportDate;
	@SerializedName("TotalDataTraffic")
	public double TotalDataTraffic;
	@SerializedName("NetworkType")
	public String NetworkType;
}
