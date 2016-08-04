package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class VIPDCDRDataPercentage {
	
	@SerializedName("normalRelease")
    public double normalRelease;
	
	@SerializedName("partialRecord")
    public double partialRecord;
	
	@SerializedName("partialRecordCallReestablishment")
    public double partialRecordCallReestablishment;
	
	@SerializedName("unsuccessfulCallAttempt")
    public double unsuccessfulCallAttempt;
	
	
	@SerializedName("stableCallAbnormalTermination")
    public double stableCallAbnormalTermination;
	
	@SerializedName("cAMELInitCallRelease")
    public double cAMELInitCallRelease;
	
	@SerializedName("cAMELCPHCallConfigurationChange")
    public double cAMELCPHCallConfigurationChange;
	
	@SerializedName("unauthorizedRequestingNetwork")
    public double unauthorizedRequestingNetwork;
	
	@SerializedName("unauthorizedLCSClient")
    public double unauthorizedLCSClient;
	
	@SerializedName("positionMethodFailure")
    public double positionMethodFailure;
	
	@SerializedName("unknownOrUnreachableLCSClient")
    public double unknownOrUnreachableLCSClient;

}
