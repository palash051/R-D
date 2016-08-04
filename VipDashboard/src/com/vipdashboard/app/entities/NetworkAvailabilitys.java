package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class NetworkAvailabilitys {
	@SerializedName("NetworkAvailabilities")
	public List<NetworkAvailability> networkAvailableList;

	public NetworkAvailabilitys() {
		networkAvailableList = new ArrayList<NetworkAvailability>();
	}
}
