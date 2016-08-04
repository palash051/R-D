package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class StatisticsLTEKPI {
	@SerializedName("SLno")
    public int SLno ;
   @SerializedName("CompanyID")
    public int CompanyID ;
   @SerializedName("ReportDate")
    public String ReportDate ;
   @SerializedName("DLDataVolume")
    public double DLDataVolume ;
   @SerializedName("ULDataVolume")
    public double ULDataVolume ;
   @SerializedName("pErabEstablishSuccessRate")
    public double pErabEstablishSuccessRate ;
   @SerializedName("pErabAbnormalRelease")
    public double pErabAbnormalRelease ;
   @SerializedName("pSessionSetupSuccessRate")
    public double pSessionSetupSuccessRate ;
   @SerializedName("pIntraFreqHO")
    public double pIntraFreqHO ;
   @SerializedName("pRRCSuccessRate")
    public double pRRCSuccessRate ;
   @SerializedName("pRachSuccess")
    public double pRachSuccess ;
   @SerializedName("pmHoPrepSuccLteIntraF")
    public double pmHoPrepSuccLteIntraF ;
   @SerializedName("pmHoExeSuccLteIntraF")
    public double pmHoExeSuccLteIntraF ;
   @SerializedName("pmHoExeAttLteIntraF")
    public double pmHoExeAttLteIntraF ;
   @SerializedName("pmErabEstabSuccInit")
    public double pmErabEstabSuccInit ;
   @SerializedName("pmErabEstabAttInit")
    public double pmErabEstabAttInit ;
   @SerializedName("pmErabRelAbnormalEnbAct")
    public double pmErabRelAbnormalEnbAct ;
   @SerializedName("pmErabRelNormalEnb")
    public double pmErabRelNormalEnb ;
   @SerializedName("pmRrcConnEstabSucc")
    public double pmRrcConnEstabSucc ;
   @SerializedName("pmRrcConnEstabAtt")
    public double pmRrcConnEstabAtt ;
   @SerializedName("pmHoPrepAttLteIntraF")
    public double pmHoPrepAttLteIntraF ;
   @SerializedName("pmZtemporary9")
    public double pmZtemporary9 ;
   @SerializedName("pmS1SigConnEstabSucc")
    public double pmS1SigConnEstabSucc ;
   @SerializedName("pmS1SigConnEstabAtt")
    public double pmS1SigConnEstabAtt ;
   @SerializedName("pmUeCtxtRelCsfbGsm")
    public double pmUeCtxtRelCsfbGsm ;
   @SerializedName("pmUeCtxtRelCsfbGsmEm")
    public double pmUeCtxtRelCsfbGsmEm ;
   @SerializedName("pmUeCtxtRelCsfbWcdma")
    public double pmUeCtxtRelCsfbWcdma ;
   @SerializedName("NumofCSFBReleasetoWCDMA")
    public double NumofCSFBReleasetoWCDMA ;
   @SerializedName("pmUeCtxtRelCsfbWcdmaEm")
    public double pmUeCtxtRelCsfbWcdmaEm ;
   @SerializedName("pmLicConnectedUsersMax")
    public double pmLicConnectedUsersMax ;
   @SerializedName("pmCellDowntimeAuto")
    public double pmCellDowntimeAuto ;
   @SerializedName("pmCellDowntimeMan")
    public double pmCellDowntimeMan ;
   @SerializedName("pmRaAttCbra")
    public double pmRaAttCbra ;
   @SerializedName("pmRaSuccCbra")
    public double pmRaSuccCbra ;
}
