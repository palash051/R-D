package com.mobilink.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class KPICustoms {
	@SerializedName("KPICustoms")
	public List<KPICustom> KPICustomList;
	public KPICustoms() {
		KPICustomList=new ArrayList<KPICustom>();
	}
}
