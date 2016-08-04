package com.vipdashboard.app.interfaces;

import java.util.ArrayList;

import com.vipdashboard.app.entities.LiveAlarms;
import com.vipdashboard.app.entities.TTStatu;
import com.vipdashboard.app.entities.TTStatuses;

public interface ILiveAlarmManager {
	LiveAlarms GetCriticalLiveAlarmByCompanyID(int count);
	LiveAlarms GetLatestUpdate();
	TTStatuses GetTTStatusByTTCode(int code);
	TTStatu SetTTComments(String TTcode, String Comments);
	TTStatu SetTTStatusCommend(String ttcode, String status, String comments, String workinguser);
	ArrayList<Object> GetTTStatusByTTCodeLatestUpdate(int code, int companyID, int siteid);
	LiveAlarms GetAllAlarmByCompanyID(int count);
	LiveAlarms GetCeasedInLastHour();
}
