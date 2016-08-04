package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class UserSettingsRoot {
	
	@SerializedName("UserSetting")
	public UserSetting userSettings;
	public UserSettingsRoot() {
		userSettings=new UserSetting();
	}
}
