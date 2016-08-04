package com.leadership.app.entities;

import com.google.gson.annotations.SerializedName;

public class FinancialData {
	@SerializedName("ID")
	public int ID;
	@SerializedName("CompanyID")
    public int CompanyID;
	@SerializedName("Year")
    public String Year;
	@SerializedName("Quarter")
    public String Quarter;
	@SerializedName("Type")
    public String Type;
	@SerializedName("Value")
    public double Value;

    public CompanySetup companySetup;
}
