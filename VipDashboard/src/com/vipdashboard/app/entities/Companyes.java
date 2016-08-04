package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class Companyes {
	@SerializedName("Company")
	public Company company;
	
	public Companyes() {
		
		company=new Company();
	}
}
