package com.mobilink.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class FinancialDatas {
	@SerializedName("FinancialDatas")
	public List<FinancialData> FinancialDataList;
	public FinancialDatas() {
		FinancialDataList=new ArrayList<FinancialData>();
	}
}
