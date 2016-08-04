package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;
import com.vipdashboard.app.entities.User;

public class LiveFeedLikeList {
	@SerializedName("LikeListID")
    public int LikeListID ;
    @SerializedName("FeedID")
    public int FeedID ;
    @SerializedName("UserNumber")
    public int UserNumber ;
    @SerializedName("User")
    public User User ;
}
