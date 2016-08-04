package com.vipdashboard.app.manager;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import com.vipdashboard.app.classes.AppList;
import com.vipdashboard.app.entities.PMKPIDatas;
import com.vipdashboard.app.entities.ReportProblemAndBadExperience;
import com.vipdashboard.app.entities.ReportProblemAndBadExperienceRoot;
import com.vipdashboard.app.entities.ReportProblemAndBadExperiences;
import com.vipdashboard.app.entities.Statistics2Gs;
import com.vipdashboard.app.entities.Statistics3Gs;
import com.vipdashboard.app.entities.StatisticsLTEKPIs;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.entities.UserRoot;
import com.vipdashboard.app.entities.Users;
import com.vipdashboard.app.entities.VIPDCDRDataPercentage;
import com.vipdashboard.app.entities.VIPDCDRDataPercentageRoot;
import com.vipdashboard.app.entities.VIPDCustomerIssue;
import com.vipdashboard.app.entities.VIPDCustomerIssueRoot;
import com.vipdashboard.app.entities.VIPDPMKPIHourlyDatas;
import com.vipdashboard.app.entities.VIPDSubscriberServiceType;
import com.vipdashboard.app.entities.VIPDSubscriberServiceTypes;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;

public class StatisticsReportManager implements IStatisticsReportManager {

	@Override
	public Statistics2Gs get2GByCompanyID() {
		return (Statistics2Gs) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().Get2GByCompanyID,
						CommonValues.getInstance().CompanyId),
				Statistics2Gs.class);
	}

	@Override
	public Statistics3Gs get3GByCompanyID() {
		return (Statistics3Gs) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().Get3GByCompanyID,
						CommonValues.getInstance().CompanyId),
				Statistics3Gs.class);
	}

	@Override
	public StatisticsLTEKPIs getLTEByCompanyID() {
		return (StatisticsLTEKPIs) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetLTEByCompanyID,
						CommonValues.getInstance().CompanyId),
				StatisticsLTEKPIs.class);
	}

	@Override
	public ArrayList<Object> getComplexData() {

		ArrayList<Object> ComplexData = new ArrayList<Object>();

		Statistics2Gs statistics2Gs = (Statistics2Gs) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().Get2GByCompanyID,
						CommonValues.getInstance().CompanyId),
						Statistics2Gs.class);
		ComplexData.add(statistics2Gs);

		Statistics3Gs satistics3Gs = (Statistics3Gs) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().Get3GByCompanyID,
						CommonValues.getInstance().CompanyId),
						Statistics3Gs.class);

		ComplexData.add(satistics3Gs);

		StatisticsLTEKPIs statisticsLTEKPIs = (StatisticsLTEKPIs) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().GetLTEByCompanyID,
						CommonValues.getInstance().CompanyId),
						StatisticsLTEKPIs.class);
		ComplexData.add(statisticsLTEKPIs);

		return ComplexData;

	}

	@Override
	public PMKPIDatas getPMKPIDatas(int kpiType) {
		return (PMKPIDatas) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().GetDailyKPI, kpiType),
				PMKPIDatas.class);
	}

	@Override
	public PMKPIDatas getParentPMKPIDatas(int pKpiID) {
		return (PMKPIDatas) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().GetParentKPI, pKpiID),
				PMKPIDatas.class);
	}

	@Override
	public VIPDPMKPIHourlyDatas getPMKPIHourlyData(int kpiID) {
		return (VIPDPMKPIHourlyDatas) JSONfunctions.retrieveDataFromStream(
				String.format(
						CommonURL.getInstance().GetKPIHourlyDataByKPIIDLast48,
						kpiID), VIPDPMKPIHourlyDatas.class);
	}

	@Override
	public VIPDSubscriberServiceTypes getSubscriberServiceType(String mobileNo) {
		return (VIPDSubscriberServiceTypes) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().GetSubscriberServiceType,
						mobileNo), VIPDSubscriberServiceTypes.class);
	}

	@Override
	public VIPDCustomerIssueRoot GetCustomerIssueByMobileNo(String mobileNo) {
		return (VIPDCustomerIssueRoot) JSONfunctions.retrieveDataFromStream(
				String.format(
						CommonURL.getInstance().GetCustomerIssueByMobileNo,
						mobileNo), VIPDCustomerIssueRoot.class);
	}

	@Override
	public VIPDCustomerIssueRoot SetCustomerIssueByCustomerIssueID(Object data) {

		VIPDCustomerIssueRoot vIPDCustomerIssueRoot = (VIPDCustomerIssueRoot) data;

		return (VIPDCustomerIssueRoot) JSONfunctions
				.retrieveDataFromStream(
						String.format(
								CommonURL.getInstance().SetCustomerIssueByCustomerIssueID,
								vIPDCustomerIssueRoot.vIPDCustomerIssue.CustomerIssueID,
								"TT500000", "1399999999999", "13499999999999",
								"880N", "93", "383", "90.66", "yes"),
						VIPDCustomerIssueRoot.class);
	}

	@Override
	public ReportProblemAndBadExperienceRoot GetAllReportProblemAndBadExperience(
			String mobileNo) {
		return (ReportProblemAndBadExperienceRoot) JSONfunctions
				.retrieveDataFromStream(
						String.format(
								CommonURL.getInstance().GetAllReportProblemAndBadExperience,
								mobileNo),
						ReportProblemAndBadExperienceRoot.class);
	}

	@Override
	public VIPDCDRDataPercentageRoot getCauseofTermination(String mobileNo) {
		return (VIPDCDRDataPercentageRoot) JSONfunctions
				.retrieveDataFromStream(
						String.format(
								CommonURL.getInstance().GetCasueOfTermination,
								mobileNo), VIPDCDRDataPercentageRoot.class);
	}

	@Override
	public ReportProblemAndBadExperiences SetReportProblemAndBadExperience(
			String mobileNo, String latitude, String longitude, String RxLevel,
			String deviceType, String brand, String problem,
			String problemTime, String status, String comment,
			String problemType, String latestFeedBack, String ttNumber,
			String locationName, String MCC, String MNC, String LAC,
			String CID, String IMSI, String Failed, String IMEI, String SIMID,
			String catagory, String subCatagory, String Extra_Tow,
			String problemHeader) {
		ReportProblemAndBadExperiences reportProblemAndBadExperiences = null;
		try {
			reportProblemAndBadExperiences = (ReportProblemAndBadExperiences) JSONfunctions
					.retrieveDataFromStream(
							String.format(
									CommonURL.getInstance().SetReportProblemAndBadExperience,
									URLEncoder.encode(mobileNo,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(latitude,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(longitude,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(RxLevel,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(deviceType,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(brand,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(problem,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(problemTime,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(String.valueOf(System
											.currentTimeMillis()),
											CommonConstraints.EncodingCode),
									URLEncoder.encode(status,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(comment,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(problemType,
											CommonConstraints.EncodingCode),
									CommonValues.getInstance().LoginUser.UserNumber,
									URLEncoder.encode(latestFeedBack,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(ttNumber,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(locationName,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(MCC,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(MNC,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(LAC,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(CID,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(IMSI,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(Failed,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(IMEI,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(SIMID,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(catagory,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(subCatagory,
											CommonConstraints.EncodingCode),
									URLEncoder.encode("",
											CommonConstraints.EncodingCode),
									URLEncoder.encode("",
											CommonConstraints.EncodingCode),
									URLEncoder.encode(Extra_Tow,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(AppList.AppName,
											CommonConstraints.EncodingCode),
									URLEncoder.encode(problemHeader,
											CommonConstraints.EncodingCode)),
							ReportProblemAndBadExperiences.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return reportProblemAndBadExperiences;
	}

	@Override
	public ReportProblemAndBadExperienceRoot GetTotalNumberOfDroppedCall(
			String number, String formate, String type) {
		ReportProblemAndBadExperienceRoot reportProblemAndBadExperienceRoot = null;
		try {
			reportProblemAndBadExperienceRoot = (ReportProblemAndBadExperienceRoot) JSONfunctions
					.retrieveDataFromStream(
							String.format(
									CommonURL.getInstance().GetTotalNumberOfDroppedCall,
									number, URLEncoder.encode(formate,
											CommonConstraints.EncodingCode),
									type),
							ReportProblemAndBadExperienceRoot.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportProblemAndBadExperienceRoot;
	}

	@Override
	public ArrayList<Object> getComplexDataForCallandSMS(String mobileNumber) {
		ArrayList<Object> ComplexData = new ArrayList<Object>();

		User user=null;
		UserRoot userRoot=(UserRoot) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().GetUserByMobileNo,
						mobileNumber),UserRoot.class);
		if(userRoot!=null)
			user=userRoot.user;
		ComplexData.add(user);

		return ComplexData;
	}

}
