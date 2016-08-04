package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class TempUserTable {
	@SerializedName("TempNunber")
    public int TempNunber;
    @SerializedName("UserID")
    public String UserID;
    @SerializedName("Password")
    public String Password;
    @SerializedName("FirstName")
    public String FirstName;
    @SerializedName("LastName")
    public String LastName;
    @SerializedName("FullName")
    public String FullName;
    @SerializedName("Mobile")
    public String Mobile;
    @SerializedName("Designation")
    public String Designation;
    @SerializedName("Department")
    public String Department;
    @SerializedName("ManagerEmailAddress")
    public String ManagerEmailAddress;
    @SerializedName("ActivationCode")
    public String ActivationCode;
    @SerializedName("CompanyID")
    public int CompanyID;
    @SerializedName("EmployeeID")
    public int EmployeeID;
}
