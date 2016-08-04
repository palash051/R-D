package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ReportAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.classes.Report;

public class RadioAccessNetworkActivity extends MainActionbarBase implements OnItemClickListener {
	ListView listView;
	ReportAdapter adapter;
	TextView titleText;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_main);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		
		//listView = (ListView) findViewById(R.id.lvReportList);
		titleText = (TextView) findViewById(R.id.tvCollaborationMainTitle);
		titleText.setText("Radio Access Network");
		
		ArrayList<Report> reportList = new ArrayList<Report>();
		
		reportList.add(new Report("2G Performance",1));
		reportList.add(new Report("3G Performance",2));
		reportList.add(new Report("LTE Performance",3));
		
		adapter = new ReportAdapter(this, R.layout.lavel_item_layout, reportList);
		//lvNameText.setTag(adapter);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(this);
		
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
	 }
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		int ID = Integer.parseInt(String.valueOf(view.getTag()));
		showGraph(ID);
	}
	private void showGraph(int iD) {
		if(iD == 1){
			Intent intent = new Intent(this,Two_G_GraphActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 2){
			Intent intent = new Intent(this,Three_G_GraphActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 3){
			Intent intent = new Intent(this,LET_GraphActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

}
