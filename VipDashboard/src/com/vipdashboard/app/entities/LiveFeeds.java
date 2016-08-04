package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.vipdashboard.app.entities.LiveFeed;

public class LiveFeeds {
	@SerializedName("LiveFeeds")
	public List<LiveFeed> liveFeedList;
	public LiveFeeds() {
		liveFeedList=new ArrayList<LiveFeed>(); 
	}
}
