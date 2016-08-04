package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class TTrequest {
	/*public TTrequest(){
		this.TTstatus = new ArrayList<TTStatus>();
		this.TTsummary = new ArrayList<TTSummary>();
	}*/
	
	@SerializedName("TTCode")
	public int TTCode;
	@SerializedName("CompanyID")
	public int CompanyID;
	@SerializedName("AlarmID")
	public int AlarmID;
	@SerializedName("SiteID")
	public int SiteID;
	@SerializedName("TTNumber")
	public String TTNumber;
	@SerializedName("CreateDateTime")
	public String CreateDateTime;
	@SerializedName("NetworkProblemDateTime")
	public String NetworkProblemDateTime;
	@SerializedName("Severity")
	public String Severity;
	@SerializedName("ProblemText")
	public String ProblemText;
	@SerializedName("ProblemDescription")
	public String ProblemDescription;
	@SerializedName("Status")
	public String Status;
	@SerializedName("OriginatorName")
	public String OriginatorName;
	@SerializedName("OriginatorGroup")
	public String OriginatorGroup;
	@SerializedName("NetworkElements")
	public String NetworkElements;
	@SerializedName("AffectedCell")
	public String AffectedCell;
	@SerializedName("AffectedService")
	public String AffectedService;
	@SerializedName("Customer")
	public String Customer;
	@SerializedName("BSCName")
	public String BSCName;
	@SerializedName("SiteName")
	public String SiteName;
	@SerializedName("EscalateGroup")
	public String EscalateGroup;
	@SerializedName("UserList")
	public String UserList;
	@SerializedName("UserListEmail")
	public String UserListEmail;
	@SerializedName("UserListMobile")
	public String UserListMobile;
	@SerializedName("OriginatorFullName")
	public String OriginatorFullName;
	@SerializedName("ProblemType")
	public String ProblemType;
	@SerializedName("ProblemCeaseTime")
	public String ProblemCeaseTime;
	@SerializedName("TicketImpact")
	public String TicketImpact;
	@SerializedName("TicketImpactStartDate")
	public String TicketImpactStartDate;
	@SerializedName("TicketImpactEndDate")
	public String TicketImpactEndDate;
	@SerializedName("Region")
	public String Region;
	@SerializedName("WorkingUser")
	public String WorkingUser;
	@SerializedName("Department")
	public String Department;
	@SerializedName("ReAssignUserList")
	public String ReAssignUserList;
	@SerializedName("ReAssignEmail")
	public String ReAssignEmail;
	@SerializedName("ReAssignMobile")
	public String ReAssignMobile;
	@SerializedName("WorkOrderNo")
	public String WorkOrderNo;
	@SerializedName("Eventtime")
	public String Eventtime;
	@SerializedName("AlarmText")
	public String AlarmText;
	@SerializedName("CeaseTime")
	public String CeaseTime;
	@SerializedName("FaultArea")
	public String FaultArea;
	@SerializedName("WorkingUserName")
	public String WorkingUserName;
	@SerializedName("NoofSiteDown")
	public int NoofSiteDown;
	@SerializedName("NoofTxDown")
	public int NoofTxDown;
	@SerializedName("TotalCell")
	public int TotalCell;
	@SerializedName("TTClosureType")
	public String TTClosureType;
	@SerializedName("TTClosureDescription")
	public String TTClosureDescription;
	@SerializedName("SolutionDescription")
	public String SolutionDescription;
	@SerializedName("WeekNumber")
	public String WeekNumber;
	@SerializedName("FaultDuration")
	public String FaultDuration;
	@SerializedName("MaxSLA")
	public int MaxSLA;
	@SerializedName("FinalClosureType")
	public String FinalClosureType;
	@SerializedName("FinalClosureDescription")
	public String FinalClosureDescription;
	@SerializedName("BIGFIVE")
	public String BIGFIVE;
	@SerializedName("IssueType")
	public String IssueType;
	@SerializedName("MajorIssue")
	public String MajorIssue;
	@SerializedName("MajorTimeSpent")
	public int MajorTimeSpent;
	/*@SerializedName("TTsummary")
	public List<TTSummary> TTsummary;
	@SerializedName("TTstatus")
	public List<TTStatus> TTstatus;*/

}
