package com.leadership.app.entities;

import com.google.gson.annotations.SerializedName;

public class UserInfo {

	@SerializedName("ID")
	public int ID;
	@SerializedName("User_id")
	public String User_id;
	@SerializedName("FullName")
	public String FullName;
	@SerializedName("Password")
	public String Password;
	@SerializedName("EmailAddress")
	public String EmailAddress;
	@SerializedName("MobileNo")
	public String MobileNo;
	@SerializedName("Country")
	public String Country;
	@SerializedName("Department")
	public String Department;
	@SerializedName("Designation")
	public String Designation;
	@SerializedName("ReportTo")
	public String ReportTo;
	@SerializedName("AppRole")
	public String AppRole;
	@SerializedName("MobileDevice")
	public String MobileDevice;
	@SerializedName("DeviceType")
	public String DeviceType;
	@SerializedName("DeviceBrand")
	public String DeviceBrand;
	@SerializedName("DeviceSize")
	public String DeviceSize;
	@SerializedName("UserRoleID")
	public int UserRoleID;
	@SerializedName("CreateDate")
	public String CreateDate;
	@SerializedName("Status")
	public String Status;
	public int UserMode;
}
