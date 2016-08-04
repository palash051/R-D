package com.mobilink.app.entities;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class CompanySetup {
	@SerializedName("CompanyID")
	public int CompanyID;
	@SerializedName("CompanyName")
	public String CompanyName;
	@SerializedName("CompanyDescription")
	public String CompanyDescription;
	@SerializedName("Spoc")
	public String Spoc;
	@SerializedName("Address")
	public String Address;
	@SerializedName("Mobile")
	public String Mobile;
	@SerializedName("Email")
	public String Email;
	@SerializedName("entered_by")
	public String entered_by;
	@SerializedName("updated_by")
	public String updated_by;
	@SerializedName("add_time")
	public String add_time;
	@SerializedName("update_time")
	public String update_time;
	@SerializedName("CompanyLogo")
	public String CompanyLogo;

	public ArrayList<FinancialData> FinancialDatas;
	public ArrayList<NetworkKPI> NetworkKPIs;
}
