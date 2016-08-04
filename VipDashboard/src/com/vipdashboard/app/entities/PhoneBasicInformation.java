package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

import android.telephony.TelephonyManager;

public class PhoneBasicInformation {
	@SerializedName("PhoneId")
	public int PhoneId;
	@SerializedName("Model")
	public String Model;
	@SerializedName("DeciceID")
	public String DeciceID;
	@SerializedName("DeviceType")
	public String DeviceType;
	@SerializedName("MACID")
	public String MACID;
	@SerializedName("MobileNo")
	public String MobileNo;
	@SerializedName("OperatorName")
	public String OperatorName;
	@SerializedName("OperatorCountryCode")
	public String OperatorCountryCode;
	@SerializedName("OperatorCountry")
	public String OperatorCountry;
	@SerializedName("SIMID")
	public String SIMID;
	@SerializedName("NetworkType")
	public String NetworkType;
	@SerializedName("LastSyncTime")
	public String LastSyncTime;
	public String LocationName;
	
	public static String getNetworkTypeString(int type){
        String typeString = "Unknown";
        
        switch(type)
        {
                case TelephonyManager.NETWORK_TYPE_EDGE:        typeString = "EDGE"; break;
                case TelephonyManager.NETWORK_TYPE_GPRS:        typeString = "GPRS"; break;
                case TelephonyManager.NETWORK_TYPE_UMTS:        typeString = "UMTS"; break;
                case TelephonyManager.NETWORK_TYPE_1xRTT:        typeString = "1xRTT"; break;
                case TelephonyManager.NETWORK_TYPE_CDMA:        typeString = "CDMA"; break;
                case TelephonyManager.NETWORK_TYPE_EHRPD:        typeString = "EHRPD"; break;
                case TelephonyManager.NETWORK_TYPE_EVDO_0:        typeString = "EVDO_0"; break;
                case TelephonyManager.NETWORK_TYPE_EVDO_A:        typeString = "EVDO_A"; break;
                case TelephonyManager.NETWORK_TYPE_EVDO_B:        typeString = "EVDO_B"; break;
                case TelephonyManager.NETWORK_TYPE_HSDPA:        typeString = "HSDPA"; break;
                case TelephonyManager.NETWORK_TYPE_HSPA:        typeString = "HSPA"; break;
                case TelephonyManager.NETWORK_TYPE_HSPAP:        typeString = "HSPAP"; break;
                case TelephonyManager.NETWORK_TYPE_HSUPA:        typeString = "HSUPA"; break;
                case TelephonyManager.NETWORK_TYPE_IDEN:        typeString = "IDEN"; break;
                case TelephonyManager.NETWORK_TYPE_LTE:        typeString = "LTE"; break;
                default: 
                	typeString = "UNKNOWN"; break;
        }
        
        return typeString;
    }
}
