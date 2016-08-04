package com.vipdashboard.app.entities;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class PMKPI {
	public PMKPI()
    {
        PMKPIConfigs = new ArrayList<PMKPIConfig>();
        PMKPIConfigs1 = new ArrayList<PMKPIConfig>();
        PMKPIDatas = new ArrayList<PMKPIData>();
    }
    @SerializedName("KPIID")

    public int KPIID;
    @SerializedName("CompanyID")
    public int CompanyID;
    @SerializedName("KPIType")
    public int KPIType;
    @SerializedName("KPI")
    public String KPI;
    @SerializedName("DisplayName")
    public String DisplayName;
    @SerializedName("SLAMinValue")
    public double SLAMinValue;
    @SerializedName("KPIFormula")
    public String KPIFormula;
    @SerializedName("Description")
    public String Description;
    @SerializedName("isActive")
    public boolean isActive;
    @SerializedName("AddedBy")
    public int AddedBy;
    @SerializedName("AddedDate")
    public String AddedDate;
    @SerializedName("Company")
    public Company Company;
    @SerializedName("PMSKPITYPE")
    public PMSKPITYPE PMSKPITYPE;
    @SerializedName("User")
    public User User;
    @SerializedName("PMKPIConfigs")
    public ArrayList<PMKPIConfig> PMKPIConfigs;
    @SerializedName("PMKPIConfigs1")
    public ArrayList<PMKPIConfig> PMKPIConfigs1;
    @SerializedName("PMKPIDatas")    
    public ArrayList<PMKPIData> PMKPIDatas;
}
