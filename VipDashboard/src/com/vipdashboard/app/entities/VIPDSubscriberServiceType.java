package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class VIPDSubscriberServiceType {
    
	@SerializedName("SLNo")
	public double SLNo;
	@SerializedName("C2G")
	public double C2G;
	@SerializedName("C3G")
	public double C3G;
	@SerializedName("MMS")
	public double MMS;
	@SerializedName("MobileNumber")
	public String MobileNumber;
	@SerializedName("SMS")
	public double SMS;
	@SerializedName("VideoCall")
	public double VideoCall;
	
	
	/*public String getPackageName() {
	      return SMS;
	   }*/
	
	
	public Object get(int i) {
		// TODO Auto-generated method stub
		return SMS;
	}
	
}
