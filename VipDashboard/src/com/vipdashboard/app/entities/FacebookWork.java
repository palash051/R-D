package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class FacebookWork {
	@SerializedName("position")
	public FacebookPosition position;
	@SerializedName("employer")
	public FacebookEmployes employes;
	@SerializedName("projects")
	public List<FacebookProjects> projects;
	@SerializedName("start_date")
	public String start_date;
	@SerializedName("end_date")
	public String end_date;
	@SerializedName("location")
	public FacebookLocation location;
	
	public FacebookWork(){
		position = new FacebookPosition();
		employes = new FacebookEmployes();
		location = new FacebookLocation();
		projects = new ArrayList<FacebookProjects>();
	}
}
