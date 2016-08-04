package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class NetworkAvailability {
	@SerializedName("SLNo")
    public int SLNo;
    @SerializedName("ReportDate")
    public String ReportDate;
    @SerializedName("Availability")
    public double Availability;
}
