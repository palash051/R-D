package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class FamilyInquiery {
	@SerializedName("Departments")
	 public Departments departments;	 
	 public FamilyInquiery(){
		 departments=new Departments();
	 }	 
}


