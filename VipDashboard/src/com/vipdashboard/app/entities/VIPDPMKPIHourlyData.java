package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class VIPDPMKPIHourlyData {
    
	@SerializedName("SLNo")
	public int SLNo;
	@SerializedName("KPIID")
	public int KPIID;
	@SerializedName("ReportHour")
	public String ReportHour;
	@SerializedName("KPIValue")
	public double KPIValue;
	@SerializedName("Description")
	public String Description;
/*	@SerializedName("PMKPI")
	public VIPDPMKPIHourlyData PMKPI;*/
}
