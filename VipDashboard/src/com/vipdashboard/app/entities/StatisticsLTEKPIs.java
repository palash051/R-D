package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class StatisticsLTEKPIs {
	@SerializedName("LTEKPIStats")
	public List<StatisticsLTEKPI> LTEKPIList;

	public StatisticsLTEKPIs() {
		LTEKPIList = new ArrayList<StatisticsLTEKPI>();
	}
}
