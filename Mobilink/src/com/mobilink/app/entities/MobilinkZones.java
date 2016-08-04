package com.mobilink.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MobilinkZones {
	@SerializedName("MLZone")
	public List< MobilinkZone> MobilinkZoneList;
	public MobilinkZones() {
		MobilinkZoneList=new ArrayList<MobilinkZone>();
	}
}
