package com.leadership.app.entities;

import com.google.gson.annotations.SerializedName;

public class City {
	@SerializedName("CityID")
	public int CityID;
	@SerializedName("CityName")
	public String CityName;
	@SerializedName("CityDescription")
	public String CityDescription;

}
