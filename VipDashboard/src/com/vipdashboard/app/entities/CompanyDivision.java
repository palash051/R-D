package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class CompanyDivision {
	@SerializedName("ID")
	 public int ID;
	@SerializedName("CompanyID")
	public int CompanyID;
	@SerializedName("DivisionID")
	public int DivisionID;
	@SerializedName("AliasText")
	public String AliasText;
	@SerializedName("Remarks")
	public String Remarks;
	@SerializedName("AddedByUser")
	public int AddedByUser;
	@SerializedName("CreateDate")
	public String CreateDate;
	@SerializedName("Company")
	public Company Company;
	@SerializedName("User")
	public User User;
	@SerializedName("Division")
	public Division Division;
	
	public CompanyDivision(){
		Company = new Company();
		Division = new Division();
		User = new User();
	}
}
