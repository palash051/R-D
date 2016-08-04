package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class UserLastLocations {
	@SerializedName("UserLastLocation")
	public List<UserLastLocation> userLastLocations;

	public UserLastLocations() {

		userLastLocations = new ArrayList<UserLastLocation>();
	}
}
