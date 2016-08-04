package com.vipdashboard.app.interfaces;

import java.util.ArrayList;
import java.util.List;

import com.vipdashboard.app.entities.Collaborations;
import com.vipdashboard.app.entities.Lavel;
import com.vipdashboard.app.entities.Notification;
import com.vipdashboard.app.entities.Notifications;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;

public interface INotificationManager {
	 Notifications GetLiveNotificationsByGroupId(int groupId);
	 Notifications GetLiveNotificationsByMsgTo();
	 Notification SendNotificationMessage(String messText, String labelID,String usergroupIDs);
	 List<Lavel> GetLevelConfigByCompanyID();
	 UserGroupUnions GetNotificationGroupUserList();
	Notifications GetLiveNotificationByUser(UserGroupUnion userGroupUnion, int LoadEarlierCount);
	ArrayList<Object> GetLiveNotifications(String collaborationTime);
	
	ArrayList<Object> GetSearchAlarmInformation(int count);
}
