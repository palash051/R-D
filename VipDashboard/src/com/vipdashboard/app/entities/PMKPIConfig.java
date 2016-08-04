package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class PMKPIConfig {
	@SerializedName("SLNo")
	public int SLNo;
	@SerializedName("ParentKPI")
	public int ParentKPI;
	@SerializedName("ChildKPI")
	public int ChildKPI;
	@SerializedName("Description")
	public String Description;
	@SerializedName("AddedBy")
	public int AddedBy;
	@SerializedName("AddedDate")
	public String AddedDate;
	@SerializedName("PMKPI")
	public PMKPI PMKPI;
	@SerializedName("PMKPI1")
	public PMKPI PMKPI1;
	@SerializedName("User")
	public User User;
}
