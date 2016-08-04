package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Notifications {
	@SerializedName("Notifications")
	public List<Notification> notificationList;

	public Notifications() {
		notificationList = new ArrayList<Notification>();
	}
}
