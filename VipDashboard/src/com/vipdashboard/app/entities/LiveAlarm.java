package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class LiveAlarm {
	public LiveAlarm() {
		this.SiteInformation = new SiteInformation();
		this.TTrequests = new ArrayList<TTrequest>();
	}

	@SerializedName("AlarmID")
	public int AlarmID;
	@SerializedName("CompanyID")
	public int CompanyID;
	@SerializedName("SiteID")
	public int SiteID;
	@SerializedName("SystemID")
	public int SystemID;
	@SerializedName("EventTime")
	public String EventTime;
	@SerializedName("CeaseTime")
	public String CeaseTime;
	@SerializedName("AlarmText")
	public String AlarmText;
	@SerializedName("Priority")
	public String Priority;
	@SerializedName("Status")
	public String Status;
	@SerializedName("Description")
	public String Description;
	@SerializedName("Comments")
	public String Comments;
	@SerializedName("isAck")
	public boolean isAck;
	@SerializedName("AckBy")
	public String AckBy;
	@SerializedName("SiteInformation")
	public SiteInformation SiteInformation;
	@SerializedName("TTrequests")
	public List<TTrequest> TTrequests;

}
