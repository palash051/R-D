package com.vipdashboard.app.entities;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class UserProfilePictureListRoot {
	@SerializedName("UserProfilePictures")
	public List<UserProfilePicture> userProfilePictureList;
}
