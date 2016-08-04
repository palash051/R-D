package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class FacebookClasses {
	@SerializedName("id")
	public String id;
	@SerializedName("with")
	public List<FacebookWith> withs;
	@SerializedName("name")
	public String name;
	
	public FacebookClasses(){
		withs = new ArrayList<FacebookWith>();
	}
}
