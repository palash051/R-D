package com.vipdashboard.app.entities;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class User {
	@SerializedName("EmployeeID")
	public int EmployeeID;
	@SerializedName("Name")
	public String Name;
	@SerializedName("FirstName")
	public String FirstName;
	@SerializedName("LastName")
	public String LastName;
	@SerializedName("FullName")
	public String FullName;
	@SerializedName("UserID")
	public String UserID;
	@SerializedName("UserNumber")
	public int UserNumber;
	@SerializedName("Password")
	public String Password;
	@SerializedName("Email")
	public String Email;
	@SerializedName("Mobile")
	public String Mobile;
	@SerializedName("Designation")
	public String Designation;
	@SerializedName("Department")
	public String Department;
	@SerializedName("AppAccess")
	public String AppAccess;
	@SerializedName("UserLevel")
	public String UserLevel;
	@SerializedName("ROMAAccess")
	public String ROMAAccess;
	@SerializedName("NOAccess")
	public String NOAccess;
	@SerializedName("NPOAccess")
	public String NPOAccess;
	@SerializedName("ActivationCode")
	public String ActivationCode;
	@SerializedName("MDUserStatusID")
	public long MDUserStatusID;
	@SerializedName("UserOnlineAvailableStatusID")
	public long UserOnlineAvailableStatusID;
	@SerializedName("ManagerEmailAddress")
	public String ManagerEmailAddress;
	@SerializedName("IsApplicationUser")
	public boolean IsApplicationUser;
	@SerializedName("IsActive")
	public boolean IsActive;
	@SerializedName("DateOfBirth")
	public String DateOfBirth;
	@SerializedName("ProfilePicture")
	public byte[] ProfilePicture;
	@SerializedName("Gender")
	public String Gender;
	@SerializedName("JobType")
	public String JobType;
	@SerializedName("FBNo")
	public int FBNo;
	@SerializedName("Facebook_Person")
	public FacebokPerson Facebook_Person;
	@SerializedName("in_Number")
	public int in_Number;
	@SerializedName("GP_SLNO")
	public int GP_SLNO;
	@SerializedName("LinkedIn_Person")
	public LinkedInPerson LinkedIn_Person;
	@SerializedName("GP_Person")
	public GPPerson GP_Person;
	@SerializedName("CompanyID")
	public int CompanyID;
	@SerializedName("MCC")
	public String MCC;
	@SerializedName("MNC")
	public String MNC;
	@SerializedName("Country")
	public String Country;
	@SerializedName("Company")
	public Company Company;
	@SerializedName("GMTOffsetValue")
	public String GMTOffsetValue;
	@SerializedName("UserSettings")
	public List<UserSetting> userSettingList;
	
	
	public User() {
		Company = new Company();
		Facebook_Person = new FacebokPerson();
		LinkedIn_Person = new LinkedInPerson();
		GP_Person = new GPPerson();
	}

	public String FullName() {
		return FirstName + " " + LastName == null ? "" : LastName;
	}

}
