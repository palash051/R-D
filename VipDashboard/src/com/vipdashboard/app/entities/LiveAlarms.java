package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class LiveAlarms {
	@SerializedName("Alarms")
	public List<LiveAlarm> liveAlarmList;

	public LiveAlarms() {
		liveAlarmList = new ArrayList<LiveAlarm>();
	}
}
