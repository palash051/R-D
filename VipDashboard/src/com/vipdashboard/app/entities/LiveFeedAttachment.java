package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class LiveFeedAttachment {
	@SerializedName("AtchID")
    public int AtchID ;
    @SerializedName("FeedID")
    public int FeedID ;
    @SerializedName("FilePath")
    public String FilePath ;
    public String FileStreamBase64 ;

}
