package com.mobilink.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CityRoot {
	@SerializedName("CitySetup")
	public List< City> citySetup;
	public CityRoot() {
		citySetup=new ArrayList<City>();
	}
}
