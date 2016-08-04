package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ReportAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.classes.Report;

public class MoreReportsActivity extends MainActionbarBase implements
		OnItemClickListener {
	ListView listView;
	ReportAdapter adapter;
	TextView titleText;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_main);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		titleText = (TextView) findViewById(R.id.tvCollaborationMainTitle);
		titleText.setText("More Reports");
		//listView = (ListView) findViewById(R.id.lvReportList);

		ArrayList<Report> reportList = new ArrayList<Report>();
		reportList.add(new Report("Management Dashboard", 1));
		reportList.add(new Report("Subscriber Details", 2));
		reportList.add(new Report("CSSR", 3));
		reportList.add(new Report("TCH Traffic", 4));
		reportList.add(new Report("TCH Drop", 5));
		reportList.add(new Report("BUSY Hour Traffic", 6));
		reportList.add(new Report("SDCCH Details", 7));
		reportList.add(new Report("Data Volume", 8));
		reportList.add(new Report("Packet Drop", 9));
		reportList.add(new Report("TCH Assignment Rate", 10));
		reportList.add(new Report("Network Availability", 11));
		reportList.add(new Report("SMS Details", 12));
		reportList.add(new Report("Paging Success Rate", 13));
		reportList.add(new Report("MMSC Details", 14));
		reportList.add(new Report("BSCMSC Processor", 15));
		reportList.add(new Report("GPRS", 16));
		adapter = new ReportAdapter(this, R.layout.lavel_item_layout,
				reportList);
		// lvNameText.setTag(adapter);
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
		if (iD == 1) {
			DailyKPIActivity.name = "Management Dashboard Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/ManagementDashboard.aspx";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (iD == 2) {
			DailyKPIActivity.name = "Subscriber Details Report";

			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/SubscriberReport.aspx";
			// DailyKPIActivity.URL =
			// "http://www.google.com/webhp?hl=en&output=html";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (iD == 3) {
			DailyKPIActivity.name = "CSSR Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/CSSR.aspx";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (iD == 4) {
			DailyKPIActivity.name = "TCHTraffic Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/TCHTrafficReport.aspx";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (iD == 5) {
			DailyKPIActivity.name = "TCHDrop Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/TCHDrop.aspx";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (iD == 6) {
			DailyKPIActivity.name = "BusyHour Traffic Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/BusyHourTraffic.aspx";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (iD == 7) {
			DailyKPIActivity.name = "SDCCH Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/SDCCH.aspx";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (iD == 8) {
			DailyKPIActivity.name = "DataVolume Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/DataVolume.aspx";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (iD == 9) {
			DailyKPIActivity.name = "PacketDrop Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/PacketDrop.aspx";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (iD == 10) {
			DailyKPIActivity.name = "TCH AssignmentSR Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/TCHAssignmentSR.aspx";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (iD == 11) {
			DailyKPIActivity.name = "Network Availability Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/NetworkAvailability.aspx";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (iD == 12) {
			DailyKPIActivity.name = "SMS Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/SMS.aspx";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (iD == 13) {
			DailyKPIActivity.name = "Paging SuccessRate Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/PagingSuccessRate.aspx";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (iD == 14) {
			DailyKPIActivity.name = "MMSC Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/MMSC.aspx";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (iD == 15) {
			DailyKPIActivity.name = "Processor Load Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/ProcessorLoad.aspx";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (iD == 16) {
			DailyKPIActivity.name = "GPRSPDP Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/GPRSPDP.aspx";
			Intent intent = new Intent(this, DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

}
