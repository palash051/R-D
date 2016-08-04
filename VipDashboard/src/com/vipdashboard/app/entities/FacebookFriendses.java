package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class FacebookFriendses {
	@SerializedName("Facebook_Friends")
	public FacebookFriends facebokFriends;

	public FacebookFriendses() {
		facebokFriends = new FacebookFriends();
	}
}
