package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PMKPIDatas {
	@SerializedName("PMKPIDatas")
	public List<PMKPIData> PMKPIDatas;

	public PMKPIDatas() {
		PMKPIDatas = new ArrayList<PMKPIData>();

	}
}
