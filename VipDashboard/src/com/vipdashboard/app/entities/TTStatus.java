package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class TTStatus {
	@SerializedName("TTSttID")
    public int TTSttID;
    @SerializedName("TTCode")
    public int TTCode;
    @SerializedName("NetworkProblemTime")
    public String NetworkProblemTime;
    @SerializedName("Originator")
    public String Originator;
    @SerializedName("UserName")
    public String UserName;
    @SerializedName("Status")
    public String Status;
    @SerializedName("StatusUpdateTime")
    public String StatusUpdateTime;
    @SerializedName("Severity")
    public String Severity;
    @SerializedName("Comments")
    public String Comments;
    @SerializedName("Region")
    public String Region;
    @SerializedName("WorkingUser")
    public String WorkingUser;
    @SerializedName("WULat")
    public double WULat;
    @SerializedName("WULong")
    public double WULong;
}
