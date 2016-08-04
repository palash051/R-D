package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class UserSettingEntityHolder {
	
	@SerializedName("UserSettings")
	public List<UserSetting> userSettingList;
	
	public UserSettingEntityHolder() {
		userSettingList = new ArrayList<UserSetting>();
	}
}
