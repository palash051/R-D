package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class VIPDCDRDataPercentageRoot {
	@SerializedName("CDRData")
	public VIPDCDRDataPercentage CDRData;
	public VIPDCDRDataPercentageRoot() {
		CDRData=new VIPDCDRDataPercentage();
	}
}
