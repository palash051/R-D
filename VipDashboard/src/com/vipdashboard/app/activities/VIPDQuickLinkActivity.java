package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ReportAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.classes.Report;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.MasterDataConstants;

public  class VIPDQuickLinkActivity extends MainActionbarBase implements OnItemClickListener, OnQueryTextListener {

ListView lvQuickLinksList;
TextView lvNameText;
SearchView searchView;
ReportAdapter adapter;

@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.vipdtapquickmenu);
	//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
	
	lvQuickLinksList = (ListView) findViewById(R.id.lvQuickLinksList);
	lvNameText = (TextView) findViewById(R.id.text);
	searchView = (SearchView) findViewById(R.id.svCollaborationList);
	
	AutoCompleteTextView search_text = (AutoCompleteTextView) searchView.findViewById(searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
	search_text.setTextColor(Color.BLACK);
	search_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
	
	searchView.setOnQueryTextListener(this);
	
	ArrayList<Report> reportList = new ArrayList<Report>();
	reportList.add(new Report("My performance",MasterDataConstants.QuickLink.MY_PERFORMANCE));
	reportList.add(new Report("Usage history",MasterDataConstants.QuickLink.USAGE_HISTORY));
	reportList.add(new Report("My data usage",MasterDataConstants.QuickLink.MY_DATA_USAGE));
	reportList.add(new Report("Last drop call",MasterDataConstants.QuickLink.LAST_DROP_CALL));
	reportList.add(new Report("Network summary",MasterDataConstants.QuickLink.NETWORK_SUMMARY));
	
	
	adapter = new ReportAdapter(this, R.layout.lavel_item_layout, reportList);
	
	lvNameText.setTag(adapter);
	lvQuickLinksList.setAdapter(adapter);
	lvQuickLinksList.setOnItemClickListener(this);
}

@Override
protected void onPause() {
	MyNetApplication.activityPaused();
	super.onPause();
}

@Override
protected void onResume() {
	MyNetApplication.activityResumed();
	super.onResume();
	TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
		if (!isFinishing()) 
		CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
	}
	else if (!CommonTask.isOnline(this)) {
		if (!isFinishing()) 
		CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
	}
	 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{	if (!isFinishing()) {
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
		}
		}
}

@Override
public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
	int ID = Integer.parseInt(String.valueOf(view.getTag()));
	
	switch(ID)
	{
	case MasterDataConstants.QuickLink.MY_PERFORMANCE:
		Intent objectOne = new Intent(this,VIPDNetworkUsageviewActivity.class);
		objectOne.putExtra("overviewID", ID);
		objectOne.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(objectOne);
		break;
	case MasterDataConstants.QuickLink.NETWORK_SUMMARY:
		Intent objectTwo = new Intent(this,VIPDNetworkUsageviewActivity.class);
		objectTwo.putExtra("overviewID", ID);
		objectTwo.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(objectTwo);
		break;
	case MasterDataConstants.QuickLink.LAST_DROP_CALL:
		Intent objThree= new Intent(this,DemoScreenActivity.class);
		objThree.putExtra("overviewID", ID);
		objThree.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(objThree);
		break;
	case MasterDataConstants.QuickLink.MY_DATA_USAGE:
		Intent objFour= new Intent(this,VIPDMobileUsageActivity.class);
		objFour.putExtra("overviewID", ID);
		objFour.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(objFour);
		break;
	case MasterDataConstants.QuickLink.USAGE_HISTORY:
		Intent objfive= new Intent(this,VIPD_ServiceUsages.class);
		objfive.putExtra("overviewID", ID);
		objfive.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(objfive);
		break;
	}
}
@Override
public boolean onQueryTextChange(String newText) {
	adapter.Filter(newText);
	return true;
}
@Override
public boolean onQueryTextSubmit(String query) {
	
	return false;
}
}
