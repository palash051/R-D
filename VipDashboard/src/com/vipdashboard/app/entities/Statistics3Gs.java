package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Statistics3Gs {
	@SerializedName("Statistics3Gs")
	public List<Statistics3G> threeGList;

	public Statistics3Gs() {
		threeGList = new ArrayList<Statistics3G>();
	}
}
