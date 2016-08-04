package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Statistics2Gs {
	@SerializedName("Statistics2Gs")
	public List<Statistics2G> twoGList;

	public Statistics2Gs() {
		twoGList = new ArrayList<Statistics2G>();
	}

}
