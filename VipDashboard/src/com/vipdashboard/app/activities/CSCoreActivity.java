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

public class CSCoreActivity extends MainActionbarBase implements OnItemClickListener {

	ListView listView;
	ReportAdapter adapter;
	TextView titleText;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_main);
		//listView = (ListView) findViewById(R.id.lvReportList);
		titleText = (TextView) findViewById(R.id.tvCollaborationMainTitle);
		titleText.setText("CS Core Report");
		
		ArrayList<Report> reportList = new ArrayList<Report>();
		
		reportList.add(new Report("CSSR",1));
		reportList.add(new Report("MMSC SR",2));
		
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
		 super.onResume();
	  MyNetApplication.activityResumed();
	  
	 }
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		int ID = Integer.parseInt(String.valueOf(view.getTag()));
		showGraph(ID);
		
	}
	private void showGraph(int iD) {
		if(iD == 1){
			DailyKPIActivity.name = "CSSR Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/CSSR.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 2){
			DailyKPIActivity.name = "MMSCSR Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/MMSC.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
	}
	

}
