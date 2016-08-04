package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class UserLastLocation {
	@SerializedName("PhoneId")
	public int PhoneId;
	@SerializedName("SignalLevel")
	public int SignalLevel;
	@SerializedName("Latitude")
	public double Latitude;
	@SerializedName("Longitude")
	public double Longitude;
	@SerializedName("Time")
	public String Time;
	@SerializedName("UserNumber")
	public int UserNumber;
	@SerializedName("UserName")
	public String UserName;
}
