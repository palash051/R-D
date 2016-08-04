package com.vipdashboard.app.interfaces;

import java.util.ArrayList;

import com.vipdashboard.app.entities.PMKPIDatas;
import com.vipdashboard.app.entities.ReportProblemAndBadExperienceRoot;
import com.vipdashboard.app.entities.ReportProblemAndBadExperiences;
import com.vipdashboard.app.entities.Statistics2Gs;
import com.vipdashboard.app.entities.Statistics3Gs;
import com.vipdashboard.app.entities.StatisticsLTEKPIs;
import com.vipdashboard.app.entities.VIPDCDRDataPercentage;
import com.vipdashboard.app.entities.VIPDCDRDataPercentageRoot;
import com.vipdashboard.app.entities.VIPDCustomerIssue;
import com.vipdashboard.app.entities.VIPDCustomerIssueRoot;
import com.vipdashboard.app.entities.VIPDPMKPIHourlyDatas;
import com.vipdashboard.app.entities.VIPDSubscriberServiceType;
import com.vipdashboard.app.entities.VIPDSubscriberServiceTypes;

public interface IStatisticsReportManager {
	Statistics2Gs get2GByCompanyID();

	Statistics3Gs get3GByCompanyID();

	StatisticsLTEKPIs getLTEByCompanyID();

	ArrayList<Object> getComplexData();

	PMKPIDatas getPMKPIDatas(int KIPType);

	PMKPIDatas getParentPMKPIDatas(int pKpiID);

	VIPDPMKPIHourlyDatas getPMKPIHourlyData(int kpiID);

	VIPDSubscriberServiceTypes getSubscriberServiceType(String mobileNo);

	VIPDCustomerIssueRoot GetCustomerIssueByMobileNo(String mobileNo);

	VIPDCustomerIssueRoot SetCustomerIssueByCustomerIssueID(Object data);
	
	ReportProblemAndBadExperiences SetReportProblemAndBadExperience(String mobileNo, String latitude, String longitude, 
								String RxLevel, String deviceType, String brand, String problem, String problemTime,
								String status, String comment, String problemType,String latestFeedBack, String ttNumber,
								String locationName, String MCC, String MNC, String LAC, String CID, String IMSI, String Failed,
								String IMEI, String SIMID, String category, String subCategory,String Extra_Tow,String problemHeader);
	ReportProblemAndBadExperienceRoot GetAllReportProblemAndBadExperience(String mobileNo);

	VIPDCDRDataPercentageRoot getCauseofTermination(String mobileNo);
	
	ReportProblemAndBadExperienceRoot GetTotalNumberOfDroppedCall(String number,String formate, String type);
	
	ArrayList<Object> getComplexDataForCallandSMS(String mobileNumber);
	
}
