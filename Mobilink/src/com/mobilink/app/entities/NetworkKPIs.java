package com.mobilink.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class NetworkKPIs {
	@SerializedName("NetworkKPIs")
	public List<NetworkKPI> NetworkKPIList;
	public NetworkKPIs() {
		NetworkKPIList=new ArrayList<NetworkKPI>();
	}
}
