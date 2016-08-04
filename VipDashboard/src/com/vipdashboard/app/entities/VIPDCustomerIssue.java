package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class VIPDCustomerIssue {
    
	@SerializedName("CustomerIssueID")
	public int CustomerIssueID;
	@SerializedName("TTReferenceNumber")
	public String TTReferenceNumber;
	@SerializedName("MobileNo")
	public String MobileNo;
	@SerializedName("RequestedDate")
	public String RequestedDate;
	@SerializedName("SubmittedDate")
	public String SubmittedDate;
	@SerializedName("IssueStatusID")
	public int IssueStatusID;
	@SerializedName("Coarse")
	public String Coarse;
	@SerializedName("LAC")
	public String LAC;
	@SerializedName("RNC")
	public String RNC;
	@SerializedName("RXValue")
	public String RXValue;
	@SerializedName("Remarks")
	public String Remarks;
	@SerializedName("ExtraField")
	public String ExtraField;
	@SerializedName("CustomerIssueStatu")
	public String CustomerIssueStatu;
	
}