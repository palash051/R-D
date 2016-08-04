package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class FacebookContact {
	@SerializedName("FBC")
	public int FBC;
	@SerializedName("FBNo")
    public int FBNo;
	@SerializedName("MobileNumber")
    public String MobileNumber;
	@SerializedName("Alternate_Emails")
    public String Alternate_Emails;
	@SerializedName("ZIPCode")
    public int ZIPCode;
    @SerializedName("Country")
    public String Country;
    @SerializedName("Website")
    public String Website;
    @SerializedName("FacebookPerson")
    public FacebokPerson FacebookPerson;
}
