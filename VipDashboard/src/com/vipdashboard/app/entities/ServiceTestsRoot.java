package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ServiceTestsRoot {
	@SerializedName("ServiceTests")
	public List<ServiceTest> ServiceTestList;

	public ServiceTestsRoot() {
		ServiceTestList = new ArrayList<ServiceTest>();
	}
}
