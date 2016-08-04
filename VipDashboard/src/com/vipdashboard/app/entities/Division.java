package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class Division {
	@SerializedName("DivisionID")
	public int DivisionID;
	@SerializedName("DivisionName")
	public String DivisionName;
	@SerializedName("DivisionType")
	public String DivisionType;
	@SerializedName("IsActive")
	public boolean IsActive;
}
