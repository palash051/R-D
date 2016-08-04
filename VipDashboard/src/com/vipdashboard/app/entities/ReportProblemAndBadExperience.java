package com.vipdashboard.app.entities;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class ReportProblemAndBadExperience {

	@SerializedName("SLNO")
	public int SLNO;
	@SerializedName("MobileNo")
	public String MobileNo;
	@SerializedName("Latitude")
	public double Latitude;
	@SerializedName("Longitude")
	public double Longitude;
	@SerializedName("RxLevel")
	public String RxLevel;
	@SerializedName("DeviceType")
	public String DeviceType;
	@SerializedName("Brand")
	public String Brand;
	@SerializedName("Problem")
	public String Problem;
	@SerializedName("ProblemTime")
	public String ProblemTime;
	@SerializedName("Status")
	public String Status;
	@SerializedName("Comment")
	public String Comment;
	@SerializedName("ProblemType")
	public boolean ProblemType;
	@SerializedName("UserNumber")
	public int UserNumber;
	@SerializedName("LatestFeedBack")
	public String LatestFeedBack;
	@SerializedName("TTNumber")
	public String TTNumber;
	@SerializedName("LocationName")
	public String LocationName;
	@SerializedName("MCC")
	public String MCC;
	@SerializedName("MNC")
	public String MNC;
	@SerializedName("LAC")
	public String LAC;
	@SerializedName("CID")
	public String CID;
	@SerializedName("IMSI")
	public String IMSI;
	@SerializedName("Failed")
	public int Failed;
	@SerializedName("IMEI")
	public String IMEI;
	@SerializedName("SIMID")
	public String SIMID;
	@SerializedName("ProblemDetailCategory")
	public String ProblemDetailCategory;
	@SerializedName("ProblemDetailSubCategory")
	public String ProblemDetailSubCategory;
	@SerializedName("Remarks")
	public String Remarks;
	@SerializedName("Extra1")
	public String Extra1;
	@SerializedName("Extra2")
	public String Extra2;
	@SerializedName("ReportTime")
	public String ReportTime;
	@SerializedName("AppsList")
	public String AppsList;
	@SerializedName("problemHeader")
	public String problemHeader;
	public Date ProblemDate;
	public Date ReportDate;
	
	public User User;
}
