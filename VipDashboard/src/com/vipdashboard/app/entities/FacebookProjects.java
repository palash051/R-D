package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class FacebookProjects {
	@SerializedName("id")
	public String id;
	@SerializedName("with")
	public List<FacebookWith> withs;
	@SerializedName("name")
	public String name;
	
	public FacebookProjects(){
		withs = new ArrayList<FacebookWith>();
	}
}
