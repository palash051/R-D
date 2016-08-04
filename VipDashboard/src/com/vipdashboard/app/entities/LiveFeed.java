package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.vipdashboard.app.entities.LiveFeedAttachment;
import com.vipdashboard.app.entities.LiveFeedLikeList;
import com.vipdashboard.app.entities.LiveFeedWebLink;
import com.vipdashboard.app.entities.User;

public class LiveFeed {
	@SerializedName("FeedID")
	public int FeedID;
	@SerializedName("FeedType")
	public int FeedType;
	@SerializedName("FeedText")
	public String FeedText;
	@SerializedName("DateTime")
	public String DateTime;
	@SerializedName("ParentID")
	public int ParentID;
	@SerializedName("Latitude")
	public double Latitude;
	@SerializedName("Longitude")
	public double Longitude;
	@SerializedName("UserNumber")
	public int UserNumber;
	@SerializedName("CommentCount")
	public int CommentCount;	
	@SerializedName("User")
	public User FeedUser;	
	@SerializedName("LiveFeedLikeLists")
	public List<LiveFeedLikeList> LiveFeedLikeLists;
	@SerializedName("LiveFeedAttachments")
	public List<LiveFeedAttachment> LiveFeedAttachments;
	@SerializedName("LiveFeedWebLinks")
	public List<LiveFeedWebLink> LiveFeedWebLinks;
	public LiveFeed() {
		FeedUser=new User();
		LiveFeedLikeLists= new ArrayList<LiveFeedLikeList>();
		LiveFeedAttachments= new ArrayList<LiveFeedAttachment>();
		LiveFeedWebLinks= new ArrayList<LiveFeedWebLink>();
	}
}
