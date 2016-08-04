package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.vipdashboard.app.entities.LiveFeedLikeList;

public class LiveFeedLikeLists {
	@SerializedName("LiveFeedLikeList")
	public List<LiveFeedLikeList> liveFeedLikeList;
	public LiveFeedLikeLists() {
		liveFeedLikeList=new ArrayList<LiveFeedLikeList>(); 
	}
}
