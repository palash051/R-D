package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class CompanyType {
	@SerializedName("CompanyTypeID")
	public int CompanyTypeID;
	@SerializedName("CompanyTypeName")
	public String CompanyTypeName;
	@SerializedName("IsActive")
	public boolean IsActive;
}
