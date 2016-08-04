package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class UserGroup {
	
	@SerializedName("ID")
    public int SLNo;
    @SerializedName("UserNumber")
    public int UserNumber;
    @SerializedName("GroupID")
    public int GroupID;
    @SerializedName("CreatedBy")
    public int CreatedBy;
    @SerializedName("RequestDate")
    public String RequestDate;
    @SerializedName("ConfirmedDate")
    public String ConfirmedDate;
    @SerializedName("Group")
    public Group Group;
    @SerializedName("User")
    public User User;

}
