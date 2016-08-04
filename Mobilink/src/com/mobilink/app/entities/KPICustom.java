package com.mobilink.app.entities;

import com.google.gson.annotations.SerializedName;

public class KPICustom {
	@SerializedName("ReportDate")
	public String ReportDate;
	@SerializedName("Value")
    public double Value ;
	@SerializedName("KPIName")
    public String KPIName ;
	@SerializedName("SLA")
    public int SLA ;
	@SerializedName("Name")
    public String Name ;
}
