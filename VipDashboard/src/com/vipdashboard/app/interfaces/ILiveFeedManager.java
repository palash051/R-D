package com.vipdashboard.app.interfaces;

import java.io.File;

import com.vipdashboard.app.entities.UserLastLocations;
import com.vipdashboard.app.entities.LiveFeed;
import com.vipdashboard.app.entities.LiveFeedLikeLists;
import com.vipdashboard.app.entities.LiveFeeds;

public interface ILiveFeedManager {

	LiveFeeds GetAllParentLiveFeed();

	LiveFeeds SetLiveFeed(LiveFeed liveFeed,String filename, byte[] selectedFile);
	
	LiveFeeds GetAllChieldLiveFeed(int feedID);
	
	LiveFeeds SetChieldLiveFeed(LiveFeed liveFeed, int feedID, String filename, byte[] selectedFile);

	LiveFeedLikeLists SetLikeList(int feedID);

	LiveFeeds SetLiveFeed(LiveFeed liveFeed, int parentFeedID, String filename,
			byte[] selectedFile);

	LiveFeedLikeLists SetUnLikeList(int feedID);
	
	UserLastLocations GetUserFriendsLastLocation();
	
	UserLastLocations GetUserFamilyLastLocation();

	LiveFeeds GetAllParentLiveFeed(int pageIndex);

	

	LiveFeeds GetAllParentLiveFeedByTime(int feedid);

	LiveFeeds SetLiveFeed(LiveFeed liveFeed, String filename,
			byte[] selectedFile, String voicelink);

}
