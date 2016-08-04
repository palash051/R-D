package com.vipdashboard.app.manager;

import java.net.URLEncoder;
import java.util.ArrayList;

import com.vipdashboard.app.entities.LiveAlarms;
import com.vipdashboard.app.entities.SiteInformations;
import com.vipdashboard.app.entities.TTStatu;
import com.vipdashboard.app.entities.TTStatuses;
import com.vipdashboard.app.interfaces.ILiveAlarmManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;

public class LiveAlarmManager implements ILiveAlarmManager{
	@Override
	public LiveAlarms GetCriticalLiveAlarmByCompanyID(int count) {
		return (LiveAlarms) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetCriticalLiveAlarmByCompanyID,CommonValues.getInstance().CompanyId,count), LiveAlarms.class);
	}
	@Override
	public LiveAlarms GetLatestUpdate() {
		return (LiveAlarms) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetLatestUpdate,CommonValues.getInstance().CompanyId), LiveAlarms.class);
	}

	@Override
	public TTStatuses GetTTStatusByTTCode(int code) {
		TTStatuses ttstatuses = (TTStatuses) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetTTStatusByTTCode,code), TTStatuses.class);
		return ttstatuses;
	}

	@Override
	public ArrayList<Object> GetTTStatusByTTCodeLatestUpdate(int code, int companyID, int siteid) {
		ArrayList<Object> complexData = new ArrayList<Object>();
		TTStatuses ttstatuses = (TTStatuses) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetTTStatusByTTCode,code), TTStatuses.class);
		complexData.add(ttstatuses);
		SiteInformations siteNotifications = (SiteInformations) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetSiteNotification,companyID,siteid), SiteInformations.class);
		complexData.add(siteNotifications);
		return complexData;
	}
	@Override
	public TTStatu SetTTComments(String TTcode,
			String Comments) {
		return (TTStatu) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().SetTTStatusComments,
						TTcode, Comments, CommonValues.getInstance().LoginUser.Name, CommonValues.getInstance().LoginUser.UserID), TTStatu.class);
	}
	@Override
	public LiveAlarms GetAllAlarmByCompanyID(int count) {
		return (LiveAlarms) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetAllAlarmsByCompanyID,CommonValues.getInstance().CompanyId,count), LiveAlarms.class);
	}
	@Override
	public TTStatu SetTTStatusCommend(String ttcode, String status,
			String comments, String workinguser) {
		TTStatu ttStatu = null;
		try{
			ttStatu =  (TTStatu) JSONfunctions.retrieveDataFromStream(String
					.format(CommonURL.getInstance().SetTTStatusComments,
							ttcode, status,
							URLEncoder.encode(comments, CommonConstraints.EncodingCode), workinguser ), TTStatu.class);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ttStatu;
	}
	@Override
	public LiveAlarms GetCeasedInLastHour() {
		return (LiveAlarms) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetCeasedTimeInLastHour,CommonValues.getInstance().CompanyId), LiveAlarms.class);
	}
}
