package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class FacebookInfo {
	
	@SerializedName("birthday")
	public String birthday;	
	@SerializedName("political")
	public String political;	
	@SerializedName("locale")
	public String locale;	
	@SerializedName("link")
	public String link;	
	@SerializedName("updated_time")
	public String updated_time;	
	@SerializedName("relationship_status")
	public String relationship_status;	
	@SerializedName("religion")
	public String religion;	
	@SerializedName("id")
	public String id;	
	@SerializedName("first_name")
	public String first_name;	
	@SerializedName("username")
	public String username;	
	@SerializedName("timezone")
	public String timezone;	
	@SerializedName("quotes")
	public String quotes;	
	@SerializedName("bio")
	public String bio;	
	@SerializedName("email")
	public String email;	
	@SerializedName("verified")
	public String verified;	
	@SerializedName("name")
	public String name;	
	@SerializedName("last_name")
	public String last_name;	
	@SerializedName("gender")
	public String gender;
	@SerializedName("hometown")
	public FacebookHometown hometown;
	@SerializedName("location")
	public FacebookLocation location;
	@SerializedName("languages")
	public List<FacebookLanguage> facebookLanguages; 
	@SerializedName("favorite_teams")
	public List<FacebookFevourite> facebookFevourites; 
	@SerializedName("education")
	public List<FacebookEducation> educations;
	@SerializedName("work")
	public List<FacebookWork> works;
	
	public FacebookInfo(){
		hometown = new FacebookHometown();
		location = new FacebookLocation();
		facebookLanguages = new ArrayList<FacebookLanguage>();
		facebookFevourites = new ArrayList<FacebookFevourite>();
		educations = new ArrayList<FacebookEducation>();
		works = new ArrayList<FacebookWork>();
	}

}
