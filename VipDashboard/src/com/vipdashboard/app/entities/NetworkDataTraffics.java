package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class NetworkDataTraffics {
	@SerializedName("NetworkDataTraffics")
	public List<NetworkDataTraffic> networkDataTrafficList;

	public NetworkDataTraffics() {
		networkDataTrafficList = new ArrayList<NetworkDataTraffic>();
	}
}
