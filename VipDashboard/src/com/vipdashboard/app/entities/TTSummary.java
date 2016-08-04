package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class TTSummary {
	 @SerializedName("TTSumID")
     public int TTSumID;
     @SerializedName("TTCode")
     public int TTCode;
     @SerializedName("TTCreateStartTime")
     public String TTCreateStartTime;
     @SerializedName("OpenTime")
     public String OpenTime;
     @SerializedName("ResolvedTime")
     public String ResolvedTime;
     @SerializedName("RejectedTime")
     public String RejectedTime;
     @SerializedName("ReassignTime")
     public String ReassignTime;
     @SerializedName("NetworkProblemTime")
     public String NetworkProblemTime;
     @SerializedName("NetworkCeaseTime")
     public String NetworkCeaseTime;
     @SerializedName("TTImpactStartTime")
     public String TTImpactStartTime;
     @SerializedName("TTImpactCloseTime")
     public String TTImpactCloseTime;
     @SerializedName("TTStartTime")
     public String TTStartTime;
     @SerializedName("TTCloseTime")
     public String TTCloseTime;
     @SerializedName("TTSuspendedStartTime")
     public String TTSuspendedStartTime;
     @SerializedName("TTSuspendedCloseTime")
     public String TTSuspendedCloseTime;
     @SerializedName("TTCreationCloseTime")
     public String TTCreationCloseTime;
     @SerializedName("MaxSLA")
     public double MaxSLA;
     @SerializedName("TravelTime")
     public String TravelTime;
     @SerializedName("RestoreTime")
     public String RestoreTime;
     @SerializedName("WithdrawnTime")
     public String WithdrawnTime;
     @SerializedName("ForwardTime")
     public String ForwardTime;
     @SerializedName("ForwardOpenTime")
     public String ForwardOpenTime;
     @SerializedName("TravelDescription")
     public String TravelDescription;
     @SerializedName("DeferredTime")
     public String DeferredTime;
     @SerializedName("AlarmEventTime")
     public String AlarmEventTime;
     @SerializedName("AlarmCeaseTime")
     public String AlarmCeaseTime;
}
