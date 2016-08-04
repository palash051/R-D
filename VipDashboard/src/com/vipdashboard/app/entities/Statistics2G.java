package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class Statistics2G {
	@SerializedName("SLno")
    public int SLno ;
    @SerializedName("CompanyID")
    public int CompanyID ;
    @SerializedName("ReportDate")
    public String ReportDate ;
    @SerializedName("C_Downtime")
    public double C_Downtime ;
    @SerializedName("C_Drop_SDCCH_BQ")
    public double C_Drop_SDCCH_BQ ;
    @SerializedName("C_Drops_SDCCH_Oth")
    public double C_Drops_SDCCH_Oth ;
    @SerializedName("C_Drops_SDCCH_SS")
    public double C_Drops_SDCCH_SS ;
    @SerializedName("C_Drops_TCH")
    public double C_Drops_TCH ;
    @SerializedName("C_Drops_SDCCH_TA")
    public double C_Drops_SDCCH_TA ;
    @SerializedName("C_Drops_TCH_BQ")
    public double C_Drops_TCH_BQ ;
    @SerializedName("C_Drops_TCH_Oth")
    public double C_Drops_TCH_Oth ;
    @SerializedName("C_Drops_TCH_SS")
    public double C_Drops_TCH_SS ;
    @SerializedName("C_Drops_TCH_Sudden")
    public double C_Drops_TCH_Sudden ;
    @SerializedName("C_Drops_TCH_TA")
    public double C_Drops_TCH_TA ;
    @SerializedName("C_HO_Better_Cell_Ass_Failures")
    public double C_HO_Better_Cell_Ass_Failures ;
    @SerializedName("C_MHT_SDCCH")
    public double C_MHT_SDCCH ;
    @SerializedName("C_MHT_TCH")
    public double C_MHT_TCH ;
    @SerializedName("C_Paging_Total")
    public double C_Paging_Total ;
    @SerializedName("C_RACH_Attempts")
    public double C_RACH_Attempts ;
    @SerializedName("C_Traffic_FR")
    public double C_Traffic_FR ;
    @SerializedName("C_Traffic_FR_OL")
    public double C_Traffic_FR_OL ;
    @SerializedName("C_Traffic_FR_UL")
    public double C_Traffic_FR_UL ;
    @SerializedName("C_Traffic_TCH_HR")
    public double C_Traffic_TCH_HR ;
    @SerializedName("C_Traffic_TCH_HR_OL")
    public double C_Traffic_TCH_HR_OL ;
    @SerializedName("C_Traffic_TCH_HR_UL")
    public double C_Traffic_TCH_HR_UL ;
    @SerializedName("C_Traffic_TCH_OL")
    public double C_Traffic_TCH_OL ;
    @SerializedName("C_Traffic_Total")
    public double C_Traffic_Total ;
    @SerializedName("C_Traffic_UL")
    public double C_Traffic_UL ;
    @SerializedName("pAvailability_SDCCH")
    public double pAvailability_SDCCH ;
    @SerializedName("pAvailability_TCH")
    public double pAvailability_TCH ;
    @SerializedName("pCDR")
    public double pCDR ;
    @SerializedName("pCDR_Alternative_OL")
    public double pCDR_Alternative_OL ;
    @SerializedName("pCDR_Alternative_UL")
    public double pCDR_Alternative_UL ;
    @SerializedName("pCDR_Excluding_Oth")
    public double pCDR_Excluding_Oth ;
    @SerializedName("pCDR_OL")
    public double pCDR_OL ;
    @SerializedName("pCDR_Original")
    public double pCDR_Original ;
    @SerializedName("pCongestion_TCH_Perceived")
    public double pCongestion_TCH_Perceived ;
    @SerializedName("pCongestion_TCH_Soft")
    public double pCongestion_TCH_Soft ;
    @SerializedName("pCSSR")
    public double pCSSR ;
    @SerializedName("pCSSR_Alternative")
    public double pCSSR_Alternative ;
    @SerializedName("pDrop_SDCCH")
    public double pDrop_SDCCH ;
    @SerializedName("pDrops_TCH_BQ")
    public double pDrops_TCH_BQ ;
    @SerializedName("pDrops_TCH_BQ_DL")
    public double pDrops_TCH_BQ_DL ;
    @SerializedName("pDrops_TCH_BQ_UL")
    public double pDrops_TCH_BQ_UL ;
    @SerializedName("pHO_Better_Cell_Ass_SR")
    public double pHO_Better_Cell_Ass_SR ;
    @SerializedName("pHSR_Outgoing")
    public double pHSR_Outgoing ;
    @SerializedName("pImmediate_Ass_CS_SR")
    public double pImmediate_Ass_CS_SR ;
    @SerializedName("pImmediate_Ass_PS_SR")
    public double pImmediate_Ass_PS_SR ;
    @SerializedName("pPCU_Failure_Rate")
    public double pPCU_Failure_Rate ;
    @SerializedName("pTCH_Assignment_SR")
    public double pTCH_Assignment_SR ;
    @SerializedName("pTCH_Drops_Oth")
    public double pTCH_Drops_Oth ;
    @SerializedName("pTCH_Drops_SS")
    public double pTCH_Drops_SS ;
    @SerializedName("pTCH_Drops_SS_BL")
    public double pTCH_Drops_SS_BL ;
    @SerializedName("pTCH_Drops_SS_DL")
    public double pTCH_Drops_SS_DL ;
    @SerializedName("pTCH_Drops_SS_UL")
    public double pTCH_Drops_SS_UL ;
    @SerializedName("ALLPDCHACTACC")
    public double ALLPDCHACTACC ;
    @SerializedName("ALLPDCHPCUATT")
    public double ALLPDCHPCUATT ;
    @SerializedName("ALLPDCHPCUFAIL")
    public double ALLPDCHPCUFAIL ;
    @SerializedName("CCONGS")
    public double CCONGS ;
    @SerializedName("CNDROP")
    public double CNDROP ;
    @SerializedName("CNRELCONG")
    public double CNRELCONG ;
    @SerializedName("PAGETOOOLD")
    public double PAGETOOOLD ;
    @SerializedName("PAGPCHCONG")
    public double PAGPCHCONG ;
    @SerializedName("TCONGSHO")
    public double TCONGSHO ;
    @SerializedName("TCONGSAS")
    public double TCONGSAS ;
    @SerializedName("THNDROP")
    public double THNDROP ;
    @SerializedName("THCONGSAS")
    public double THCONGSAS ;
    @SerializedName("THCONGSHO")
    public double THCONGSHO ;
    @SerializedName("TFNDROP")
    public double TFNDROP ;
    @SerializedName("TFNDROPSUB")
    public double TFNDROPSUB ;
    @SerializedName("THNDROPSUB")
    public double THNDROPSUB ;
    @SerializedName("pCongestion_SDCCH")
    public double pCongestion_SDCCH ;
}
