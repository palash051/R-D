package com.vipdashboard.app.manager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.vipdashboard.app.entities.Collaborations;
import com.vipdashboard.app.entities.Lavel;
import com.vipdashboard.app.entities.Lavels;
import com.vipdashboard.app.entities.LiveAlarms;
import com.vipdashboard.app.entities.Notification;
import com.vipdashboard.app.entities.Notifications;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.entities.WebDataRequest;
import com.vipdashboard.app.entities.WebDataRequestEntityHolder;
import com.vipdashboard.app.interfaces.INotificationManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;

public class NotificationManager implements INotificationManager {
	@Override
	public Notifications GetLiveNotificationsByGroupId(int groupId) {
		return (Notifications) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetLiveNotificationsByGroupId,
						groupId), Notifications.class);
	}
	@Override
	public Notifications GetLiveNotificationsByMsgTo() {
		return (Notifications) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetLiveNotificationsByMsgTo,
						CommonValues.getInstance().LoginUser.UserNumber), Notifications.class);
	}
	@Override
	public Notification SendNotificationMessage(String messText,String lavelID,
			String usergroupsIDs) {
		
		try {
			return  (Notification) JSONfunctions.retrieveDataFromStream(String.format(CommonURL.getInstance().SendNotification,
					CommonValues.getInstance().LoginUser.UserNumber,
					URLEncoder.encode(messText, CommonConstraints.EncodingCode),
					lavelID,URLEncoder.encode(usergroupsIDs, CommonConstraints.EncodingCode)),Notification.class);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public List<Lavel> GetLevelConfigByCompanyID() {
		ArrayList<Lavel> lavelList=null;
		Lavels lavelRoot=(Lavels) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().GetLevelConfigByCompanyID, 
						CommonValues.getInstance().CompanyId),Lavels.class);
		if(lavelRoot!=null)
			lavelList=(ArrayList<Lavel>) lavelRoot.lavelList;
		return lavelList;
	}
	@Override
	public UserGroupUnions GetNotificationGroupUserList() {
		return (UserGroupUnions) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetNotificationGroupUserList,CommonValues.getInstance().LoginUser.UserNumber), UserGroupUnions.class);
	}
	
	@Override
	public Notifications GetLiveNotificationByUser(UserGroupUnion userGroupUnion, int LeadEarlinerCount) {
		return (Notifications) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetLiveNotificationByUser,
						CommonValues.getInstance().LoginUser.UserNumber,userGroupUnion.ID,userGroupUnion.Type,0,LeadEarlinerCount), Notifications.class);
	}
/*	@Override
	public List<Notification> GetLiveNotificationsByMsgTo() {
		ArrayList<Notification> NotificationList=null;
		Notifications notificationRoot=(Notifications) JSONfunctions.retrieveDataFromStream(
				String.format(CommonURL.getInstance().GetLiveNotificationsByMsgTo, 
						CommonValues.getInstance().LoginUser.UserNumber),Notifications.class);
		if(notificationRoot!=null)
			NotificationList=(ArrayList<Notification>) notificationRoot.notificationList;
		return NotificationList;
	}*/
	@Override
	public ArrayList<Object> GetLiveNotifications(String collaborateTime) {
		ArrayList<Object> complexNotifications =new ArrayList<Object>();
		
		Collaborations collaborations = (Collaborations) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetLiveCollaborationByUserDateTime,
						CommonValues.getInstance().LoginUser.UserNumber,collaborateTime), Collaborations.class);
		complexNotifications.add(collaborations);
		
		//New Web Request Info Update
		  
		  WebDataRequestEntityHolder webDataRequest = (WebDataRequestEntityHolder) JSONfunctions.retrieveDataFromStream(String
		    .format(CommonURL.getInstance().GetwebDataRequest,
		      CommonValues.getInstance().LoginUser.Mobile),
		      WebDataRequestEntityHolder.class);
		  
		  if(webDataRequest!=null&& webDataRequest.webDataRequest!=null)
		  {
		   complexNotifications.add(webDataRequest);
		  }
		
		return complexNotifications;
	}
	@Override
	public ArrayList<Object> GetSearchAlarmInformation(int count) {
		ArrayList<Object> complexData = new ArrayList<Object>();
		try{
			ArrayList<Lavel> lavelList=null;
			Lavels lavelRoot=(Lavels) JSONfunctions.retrieveDataFromStream(
					String.format(CommonURL.getInstance().GetLevelConfigByCompanyID, 
							CommonValues.getInstance().CompanyId),Lavels.class);
			if(lavelRoot!=null)
				lavelList=(ArrayList<Lavel>) lavelRoot.lavelList;
			complexData.add(lavelList);
			LiveAlarms liveAlarms =  (LiveAlarms) JSONfunctions.retrieveDataFromStream(String
					.format(CommonURL.getInstance().GetAllAlarmsByCompanyID,CommonValues.getInstance().CompanyId,count), LiveAlarms.class);
			complexData.add(liveAlarms);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return complexData;
	}
}
