package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class FacebookEducation {
	@SerializedName("type")
	public String type;
	@SerializedName("classes")
	public List<FacebookClasses> classes;
	@SerializedName("year")
	public FacebookYear year;
	@SerializedName("school")
	public FacebookSchool school;
	
	public FacebookEducation(){
		classes = new ArrayList<FacebookClasses>();
		year = new FacebookYear();
		school = new FacebookSchool();
	}
	
}
