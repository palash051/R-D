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
import android.webkit.WebView;
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
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.classes.Report;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.MasterDataConstants;

public  class VIPDNetworkPerformance extends MainActionbarBase implements OnItemClickListener, OnQueryTextListener {
	/*public static Fragment newInstance(Context context) {
	AlarmMainFragment f = new AlarmMainFragment();		
	return f;
}*/
//Button bShowCriticalAlarms,bCeasedLastHour;

/*@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	ViewGroup root = (ViewGroup) inflater.inflate(
			R.layout.alarm_main,container, false);
	
	
	
	return root;
}*/

ListView lvAlarmMenu;
TextView lvNameText;
SearchView searchView;
ReportAdapter adapter;

@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.vipd_network_performance);
	//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
	
	lvAlarmMenu = (ListView) findViewById(R.id.lvAlarmTitleList);
	lvNameText = (TextView) findViewById(R.id.text);
	searchView = (SearchView) findViewById(R.id.svCollaborationList);
	
	AutoCompleteTextView search_text = (AutoCompleteTextView) searchView.findViewById(searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
	search_text.setTextColor(Color.BLACK);
	search_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
	
	
	WebView webView = (WebView) findViewById(R.id.webView1);
	webView.getSettings().setJavaScriptEnabled(true);
	/*webView.loadUrl("http://www.google.com");*/
	
	 String customHtml = "<html><body><h1>Hello, WebView</h1></body></html>";
	   webView.loadData(customHtml, "text/html", "UTF-8");
	
	
	searchView.setOnQueryTextListener(this);
	
	String[] menuListvalue = new String[]{"All Alarms","Critical Alarms","Ceased In Last Hour","Search Alarm"};
	ArrayList<String> menuList = new ArrayList<String>();
	for(int i=0;i<menuListvalue.length;i++){
		menuList.add(menuListvalue[i]);
	}
	ArrayList<Report> reportList = new ArrayList<Report>();
	reportList.add(new Report("PLMN",MasterDataConstants.NetworkPerformace.PLMN));
	reportList.add(new Report("Region",MasterDataConstants.NetworkPerformace.REGION));
	reportList.add(new Report("Platinum Cluster",MasterDataConstants.NetworkPerformace.PLATINUM_CLUSTER));
	adapter = new ReportAdapter(this, R.layout.lavel_item_layout, reportList);
	lvNameText.setTag(adapter);
	lvAlarmMenu.setAdapter(adapter);
	
	lvAlarmMenu.setOnItemClickListener(this);
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
	else if(!CommonTask.isMyServiceRunning(this))
		startService(new Intent(this, MyNetService.class));
	else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
		if (!isFinishing()) {
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
		}
		}
}

@Override
public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
	int ID = Integer.parseInt(String.valueOf(view.getTag()));
	if(ID == MasterDataConstants.NetworkPerformace.PLMN){
		Intent intent = new Intent(this,DashboradActivity.class);
		intent.putExtra("overviewID", ID);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_SHORT).show();
	}else if(ID == MasterDataConstants.NetworkPerformace.REGION){
		Intent intent = new Intent(this,DashboradActivity.class);
		intent.putExtra("overviewID", ID);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}else if(ID == MasterDataConstants.NetworkPerformace.PLATINUM_CLUSTER){
		Intent intent = new Intent(this,DashboradActivity.class);
		intent.putExtra("overviewID", ID);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		//Toast.makeText(getApplicationContext(),getResources().getString(R.string.apllication_default_msg) ,Toast.LENGTH_SHORT).show();
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
