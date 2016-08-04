package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class FB_FriendsRoot {
	@SerializedName("data")
	public List<FB_Friend> fb_FriendList;
	public FB_FriendsRoot() {
		fb_FriendList=new ArrayList<FB_Friend>(); 
	}
}
