package com.vipdashboard.app.manager;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.util.Base64;
import android.webkit.URLUtil;

import com.vipdashboard.app.entities.LiveFeed;
import com.vipdashboard.app.entities.LiveFeedLikeLists;
import com.vipdashboard.app.entities.LiveFeeds;
import com.vipdashboard.app.entities.UserLastLocations;
import com.vipdashboard.app.interfaces.ILiveFeedManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;

public class LiveFeedManager implements ILiveFeedManager{
	@Override
	public LiveFeeds GetAllParentLiveFeed() {
		
		return (LiveFeeds) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetAllParentLiveFeedByUserNumber,CommonValues.getInstance().LoginUser.UserNumber,0), LiveFeeds.class);
	}
	
	@Override
	public LiveFeeds GetAllParentLiveFeed(int pageIndex) {
		return (LiveFeeds) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetAllParentLiveFeedByUserNumber,CommonValues.getInstance().LoginUser.UserNumber,pageIndex), LiveFeeds.class);
	}
	@Override
	public LiveFeeds GetAllParentLiveFeedByTime(int feedid) {
		return (LiveFeeds) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetLatestLiveFeedByTime,CommonValues.getInstance().LoginUser.UserNumber,feedid), LiveFeeds.class);
	}
	
	@Override
	public LiveFeeds SetLiveFeed(LiveFeed liveFeed,String filename, byte[] selectedFile) {		
		return SetLiveFeed(liveFeed,0,filename,selectedFile);
	}
	
	@Override
	public LiveFeeds SetLiveFeed(LiveFeed liveFeed,int parentFeedID,String filename, byte[] selectedFile) {
		try{
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("feedType", liveFeed.FeedType);
			jsonObject.put("feedText", liveFeed.FeedText);
			jsonObject.put("parentID", parentFeedID);
			jsonObject.put("latitude", liveFeed.Latitude);
			jsonObject.put("longitude", liveFeed.Longitude);
			jsonObject.put("fileName", filename);
			jsonObject.put("userNumber",liveFeed.UserNumber);
			jsonObject.put("link", URLUtil.isValidUrl(liveFeed.FeedText)?liveFeed.FeedText:"");
			jsonObject.put("byteValue",selectedFile!=null? Base64.encodeToString(selectedFile,Base64.NO_WRAP):"");
			
			return (LiveFeeds) JSONfunctions.retrieveDataFromJsonPost(CommonURL.getInstance().SetLiveFeed,jsonObject, LiveFeeds.class);
		}catch (Exception e) {
			
		}
		return null;
	}
	
	@Override
	public LiveFeeds SetLiveFeed(LiveFeed liveFeed,String filename, byte[] selectedFile,String voicelink) {
		try{
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("feedType", liveFeed.FeedType);
			jsonObject.put("feedText", liveFeed.FeedText);
			jsonObject.put("parentID", 0);
			jsonObject.put("latitude", liveFeed.Latitude);
			jsonObject.put("longitude", liveFeed.Longitude);
			jsonObject.put("fileName", filename);
			jsonObject.put("userNumber",liveFeed.UserNumber);
			jsonObject.put("link", voicelink);
			jsonObject.put("byteValue",selectedFile!=null? Base64.encodeToString(selectedFile,Base64.NO_WRAP):"");
			
			return (LiveFeeds) JSONfunctions.retrieveDataFromJsonPost(CommonURL.getInstance().SetLiveFeed,jsonObject, LiveFeeds.class);
		}catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public LiveFeeds GetAllChieldLiveFeed(int feedID) {
		return (LiveFeeds) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().getAllChieldLiveFeed,feedID), LiveFeeds.class);
	}
	
	@Override
	public LiveFeedLikeLists SetLikeList(int feedID) {
		return (LiveFeedLikeLists) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().SetLikeList,feedID,CommonValues.getInstance().LoginUser.UserNumber), LiveFeedLikeLists.class);
	}
	
	@Override
	public LiveFeedLikeLists SetUnLikeList(int feedID) {
		return (LiveFeedLikeLists) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().SetUnLikeList,feedID,CommonValues.getInstance().LoginUser.UserNumber), LiveFeedLikeLists.class);
	}

	@Override
	public LiveFeeds SetChieldLiveFeed(LiveFeed liveFeed, int feedID,
			String filename, byte[] selectedFile) {
		try{
			return (LiveFeeds) JSONfunctions.getJSONfromPostURL(
					String.format(CommonURL.getInstance().SetLiveFeed,
							liveFeed.FeedType,
							URLEncoder.encode(liveFeed.FeedText,CommonConstraints.EncodingCode),
							feedID,
							liveFeed.Latitude,
							liveFeed.Longitude,
							URLEncoder.encode(filename,CommonConstraints.EncodingCode),
							liveFeed.UserNumber,"",Base64.encodeToString(selectedFile,Base64.NO_WRAP)),
							selectedFile,"binary/octet-stream", LiveFeeds.class);
		}catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public UserLastLocations GetUserFriendsLastLocation() {
		UserLastLocations userLastLocations;
		userLastLocations = (UserLastLocations) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetUserFriendsLastLocation,
						CommonValues.getInstance().LoginUser.UserNumber), UserLastLocations.class);
		return userLastLocations;
	}

	@Override
	public UserLastLocations GetUserFamilyLastLocation() {
		UserLastLocations userLastLocations;
		userLastLocations = (UserLastLocations) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetUserFamilyLastLocation,
						CommonValues.getInstance().LoginUser.UserNumber), UserLastLocations.class);
		return userLastLocations;
	}

	
}
