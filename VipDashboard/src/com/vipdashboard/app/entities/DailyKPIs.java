package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DailyKPIs {
	@SerializedName("DailyKPIs")
	public List<DailyKPI> dailyKPIList;

	public DailyKPIs() {
		dailyKPIList = new ArrayList<DailyKPI>();

	}
}
