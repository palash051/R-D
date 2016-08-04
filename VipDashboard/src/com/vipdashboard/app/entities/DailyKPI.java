package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class DailyKPI {
	@SerializedName("SLNo")
	public int SLNo;
	@SerializedName("ReportDate")
	public String ReportDate;
	@SerializedName("TotalTCHTraffic")
	public double TotalTCHTraffic;
	@SerializedName("TotalTCHTrafficEricsson")
	public double TotalTCHTrafficEricsson;
	@SerializedName("TotalTCHTrafficHuwaei")
	public double TotalTCHTrafficHuwaei;
	@SerializedName("BusyHourTrafficTotal")
	public double BusyHourTrafficTotal;
	@SerializedName("BusyHourTrafficEricsson")
	public double BusyHourTrafficEricsson;
	@SerializedName("BusyHourTrafficHuawei")
	public double BusyHourTrafficHuawei;
	@SerializedName("DailyDataTrafficTotal")
	public double DailyDataTrafficTotal;
	@SerializedName("DailyDataTrafficEricsson")
	public double DailyDataTrafficEricsson;
	@SerializedName("DailyDataTrafficHuawei")
	public double DailyDataTrafficHuawei;
	@SerializedName("TCHDropRateTotal")
	public double TCHDropRateTotal;
	@SerializedName("TCHDropRateEricsson")
	public double TCHDropRateEricsson;
	@SerializedName("TCHDropRateHuawei")
	public double TCHDropRateHuawei;
	@SerializedName("TCHAssignmentSRTotal")
	public double TCHAssignmentSRTotal;
	@SerializedName("TCHAssignmentSREricsson")
	public double TCHAssignmentSREricsson;
	@SerializedName("TCHAssignmentSRHuawei")
	public double TCHAssignmentSRHuawei;
	@SerializedName("ImmediateAssignmentSRTotal")
	public double ImmediateAssignmentSRTotal;
	@SerializedName("ImmediateAssignmentSREricsson")
	public double ImmediateAssignmentSREricsson;
	@SerializedName("ImmediateAssignmentSRHuawei")
	public double ImmediateAssignmentSRHuawei;
	@SerializedName("PagingSuccessRateTotal")
	public double PagingSuccessRateTotal;
	@SerializedName("PagingSuccessRateEricsson")
	public double PagingSuccessRateEricsson;
	@SerializedName("PagingSuccessRateHuawei")
	public double PagingSuccessRateHuawei;
	@SerializedName("RandomAccessSuccessRate")
	public double RandomAccessSuccessRate;
	@SerializedName("RACHSuccessRate")
	public double RACHSuccessRate;
	@SerializedName("CallSetupSuccessRateTotal")
	public double CallSetupSuccessRateTotal;
	@SerializedName("CallSetupSuccessRateEricsson")
	public double CallSetupSuccessRateEricsson;
	@SerializedName("CallSetupSuccessRateHuawei")
	public double CallSetupSuccessRateHuawei;
	@SerializedName("SDCCHDropRateTotal")
	public double SDCCHDropRateTotal;
	@SerializedName("SDCCHDropRateEricsson")
	public double SDCCHDropRateEricsson;
	@SerializedName("SDCCHDropRateHuawei")
	public double SDCCHDropRateHuawei;
	@SerializedName("SDCCHCongestionRateTotal")
	public double SDCCHCongestionRateTotal;
	@SerializedName("SDCCHCongestionRateEricsson")
	public double SDCCHCongestionRateEricsson;
	@SerializedName("SDCCHCongestionRateHuawei")
	public double SDCCHCongestionRateHuawei;
	@SerializedName("HOSuccessRateTotal")
	public double HOSuccessRateTotal;
	@SerializedName("HOSuccessRateEricsson")
	public double HOSuccessRateEricsson;
	@SerializedName("HOSuccessRateHuawei")
	public double HOSuccessRateHuawei;
	@SerializedName("GPRSAttachSuccessRate")
	public double GPRSAttachSuccessRate;
	@SerializedName("PDPContextSuccessRate")
	public double PDPContextSuccessRate;
	@SerializedName("PacketDropUL")
	public double PacketDropUL;
	@SerializedName("PacketDropDL")
	public double PacketDropDL;
	@SerializedName("NetworkAvailabilityEricsson")
	public double NetworkAvailabilityEricsson;
	@SerializedName("NetworkAvailabilityHuawei")
	public double NetworkAvailabilityHuawei;
	@SerializedName("CellAvailability")
	public double CellAvailability;
	@SerializedName("CellAvailabilityEricsson")
	public double CellAvailabilityEricsson;
	@SerializedName("CellAvailabilityHuawei")
	public double CellAvailabilityHuawei;
	@SerializedName("ProcessorLoadBSCCP")
	public double ProcessorLoadBSCCP;
	@SerializedName("ProcessorLoadMSCCP")
	public double ProcessorLoadMSCCP;
	@SerializedName("TotalSubscriber")
	public double TotalSubscriber;
	@SerializedName("TotalSubscriberPrepaid")
	public double TotalSubscriberPrepaid;
	@SerializedName("TotalSubscriberPostPaid")
	public double TotalSubscriberPostPaid;
	@SerializedName("VLRRegistredSubscriber")
	public double VLRRegistredSubscriber;
	@SerializedName("InternationalRoamingSubscriber")
	public double InternationalRoamingSubscriber;
	@SerializedName("TotalSMSVolume")
	public double TotalSMSVolume;
	@SerializedName("SMSVolumeReceived")
	public double SMSVolumeReceived;
	@SerializedName("SMSVolumeTerminating")
	public double SMSVolumeTerminating;
	@SerializedName("RadioCapacityUtilization")
	public double RadioCapacityUtilization;
	@SerializedName("MPDTotal")
	public double MPDTotal;
	@SerializedName("MPDEricsson")
	public double MPDEricsson;
	@SerializedName("MPDHuawei")
	public double MPDHuawei;
	@SerializedName("SMSSuccessRate")
	public double SMSSuccessRate;
	@SerializedName("SMSSuccessRateEricsson")
	public double SMSSuccessRateEricsson;
	@SerializedName("SMSSuccessRateHuawei")
	public double SMSSuccessRateHuawei;
	@SerializedName("SMSDropRate")
	public double SMSDropRate;
	@SerializedName("SMSDropRateEricson")
	public double SMSDropRateEricson;
	@SerializedName("SMSDropRateHuawei")
	public double SMSDropRateHuawei;
	@SerializedName("CoreCSSR")
	public double CoreCSSR;
	@SerializedName("CoreCSSREricsson")
	public double CoreCSSREricsson;
	@SerializedName("CoreCSSRHuawei")
	public double CoreCSSRHuawei;
	@SerializedName("MMSCSubmitSuccessRate")
	public double MMSCSubmitSuccessRate;
	@SerializedName("MMSCSubmitSuccessRateEricsson")
	public double MMSCSubmitSuccessRateEricsson;
	@SerializedName("MMSCSubmitSuccessRateHuawei")
	public double MMSCSubmitSuccessRateHuawei;
	@SerializedName("SMSCLicenseUtilization")
	public double SMSCLicenseUtilization;
	@SerializedName("SMSMOMTSuccessRate")
	public double SMSMOMTSuccessRate;
	@SerializedName("SMSCMOMTAttempt")
	public double SMSCMOMTAttempt;
}
