package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class PMKPIData {
	@SerializedName("SLNo")
	public int SLNo;
	@SerializedName("KPIID")
	public int KPIID;
	@SerializedName("KPIValue")
	public double KPIValue;
	@SerializedName("Description")
	public String Description;
	@SerializedName("LastRefresh")
	public String LastRefresh;
	@SerializedName("AddedBy")
	public int AddedBy;
	@SerializedName("AddedAt")
	public String AddedAt;
	@SerializedName("PMKPI")
	public PMKPI PMKPI;
	@SerializedName("User")
	public User User;
}
