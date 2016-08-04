package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class UserSetting {
	@SerializedName("UserSettingID")
	public int UserSettingID;
	@SerializedName("UserNumber")
	public int UserNumber;
	@SerializedName("LastUpdatedDate")
	public String LastUpdatedDate;
	@SerializedName("Remarks")
	public String Remarks;
	@SerializedName("IsSharLocation")
	public boolean IsSharLocation;
	@SerializedName("IsSeeMyCallHistory")
	public boolean IsSeeMyCallHistory;
	@SerializedName("IsCareConnectToMyDevice")
	public boolean IsCareConnectToMyDevice;
	@SerializedName("IsNotifyMeAnyPromotion")
	public boolean IsNotifyMeAnyPromotion;
	@SerializedName("IsNotifyNetworkIncidents")
	public boolean IsNotifyNetworkIncidents;
	@SerializedName("IsSyncDataWithTraffic")
	public boolean IsSyncDataWithTraffic;
	@SerializedName("IsSyncOnlyOnWiFi")
	public boolean IsSyncOnlyOnWiFi;
	@SerializedName("IsRecordVoiceMemo")
	public boolean IsRecordVoiceMemo;
	@SerializedName("IsPromptTextMemo")
	public boolean IsPromptTextMemo;
	
	@SerializedName("User")
	public User User;
}
