package com.vipdashboard.app.entities;

import android.R.string;

import com.google.gson.annotations.SerializedName;

public class VIPDCauseOfTerminationEntity { 
	
	
	@SerializedName("SLNO")
    public int SLNO;
	@SerializedName("input_id")
	public string input_id;
	@SerializedName("output_id")
	 public string output_id;
	@SerializedName("addkey")
	 public string addkey;
	@SerializedName("source_id")
	 public string source_id;
	@SerializedName("filename")
	 public string filename ;
	@SerializedName("FrecordType")
	public string FrecordType;
	@SerializedName("FservedIMSI")
	 public string FservedIMSI;
	@SerializedName("FservedIMEI")
	 public string FservedIMEI;
	@SerializedName("FservedMSISDN")
	public string FservedMSISDN;
	@SerializedName("FcallingNumber")
	 public string FcallingNumber;
	@SerializedName("FconnectedNumber")
	 public string FconnectedNumber;
	@SerializedName("FrecordingEntity")
	 public string FrecordingEntity;
	@SerializedName("mscIncomingROUTEFrOUTEName")
	public string mscIncomingROUTEFrOUTEName;
	@SerializedName("mscOutgoingROUTEFrOUTEName")
	 public string mscOutgoingROUTEFrOUTEName;
	
	

}
