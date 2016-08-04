package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class VIPDPMKPIHourlyDatas {
	@SerializedName("PMKPIHourlyDatas")
	public List<VIPDPMKPIHourlyData> VIPDPMKPIHourlyDatas;

	public VIPDPMKPIHourlyDatas() {
		VIPDPMKPIHourlyDatas = new ArrayList<VIPDPMKPIHourlyData>();

	}
}
