package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ReportAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.classes.Report;
import com.vipdashboard.app.entities.DailyKPI;
import com.vipdashboard.app.entities.DailyKPIs;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IReportManager;
import com.vipdashboard.app.manager.ReportManager;
import com.vipdashboard.app.utils.CommonTask;

public class VASReportActivity extends MainActionbarBase implements OnItemClickListener {
	/*DownloadableAsyncTask downloadAsync;
	ProgressBar pbGraph;
	ListView dailyKPIReport;
	ReportAdapter adapter;
	int listPosition;*/

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
		titleText.setText("VAS Report");
		
		ArrayList<Report> reportList = new ArrayList<Report>();
		
		reportList.add(new Report("SMS Success Rate",1));
		reportList.add(new Report("SMS Drop Rate",2));
		reportList.add(new Report("SMS License Utilization",3));
		reportList.add(new Report("SMS Attempts",4));
		reportList.add(new Report("SMS Details",5));
		
		adapter = new ReportAdapter(this, R.layout.lavel_item_layout, reportList);
		//lvNameText.setTag(adapter);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(this);
		
		/*dailyKPIReport = (ListView) findViewById(R.id.lvDailyKPIReportList);
		pbGraph = (ProgressBar) findViewById(R.id.pbDailyKPI);
		String[] report = new String[]{"SMS Licence Utilization","SMS MO/MT Success Rate","SMS MO/MT Attempt"};
		ArrayList<String> reportList = new ArrayList<String>();
		for(int i=0;i<report.length;i++){
			reportList.add(report[i]);
		}
		ArrayList<Report> reportList = new ArrayList<Report>();
		reportList.add(new Report("SMS Licence Utilization",1));
		reportList.add(new Report("SMS MO/MT Success Rate",2));
		reportList.add(new Report("SMS MO/MT Attempt",3));
		adapter = new ReportAdapter(this, R.layout.lavel_item_layout,reportList);
		dailyKPIReport.setAdapter(adapter);
		dailyKPIReport.setOnItemClickListener(this);*/
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
			DailyKPIActivity.name = "SMS Success Rate Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/SMSSuccessRate.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 2){
			DailyKPIActivity.name = "SMS Drop Rate Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/SMSDropRate.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 3){
			DailyKPIActivity.name = "SMS Licence Utilization Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/SMSUtilization.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 4){
			DailyKPIActivity.name = "SMS Attempt Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/SMSAttempt.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 5){
			DailyKPIActivity.name = "SMS Details Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/SMS.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

	/*@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		listPosition = Integer.parseInt(String.valueOf(view.getTag()));
		LoadGraphData();
	}


	private void LoadGraphData(){
		if (downloadAsync != null) {
			downloadAsync.cancel(true);
		}
		downloadAsync = new DownloadableAsyncTask(this);
		downloadAsync.execute();
	}

	@Override
	public void showProgressLoader() {
		pbGraph.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		pbGraph.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		IReportManager reportManager = new ReportManager();
		return reportManager.getDailyKPI();
	}


	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			DailyKPIs dailyKPIs = (DailyKPIs) data;
			int totalCount=0;
			int length = dailyKPIs.dailyKPIList.size();			
			GraphViewData[] total_Data = new GraphViewData[length];
			GraphViewData[] total_Ericssion = new GraphViewData[length];
			GraphViewData[] total_Huwaei = new GraphViewData[length];
			String[] horLevels = new String[length];
			String[] horLevelsSet = new String[length/2];
			
			LineGraphView lineGraphView = null;
			
			try{
				if(listPosition == 1){
					for (DailyKPI dailyKPI : dailyKPIs.dailyKPIList) {
						total_Data[totalCount] = new GraphViewData(totalCount, dailyKPI.SMSCLicenseUtilization);
						//total_Ericssion[totalCount] = new GraphViewData(totalCount, dailyKPI.BusyHourTrafficEricsson);
						//total_Huwaei[totalCount] = new GraphViewData(totalCount, dailyKPI.BusyHourTrafficHuawei);
						horLevels[totalCount] = CommonTask.convertJsonDateToDailyKPI(dailyKPI.ReportDate);
						totalCount++;
					}
					
					GraphViewSeries totalData = new GraphViewSeries("SMS",new GraphViewSeriesStyle(Color.GREEN, 3), total_Data);
					//GraphViewSeries ericssionData = new GraphViewSeries("Ericssion", new GraphViewSeriesStyle(Color.BLUE, 3), total_Ericssion);
					//GraphViewSeries huwaeiData = new GraphViewSeries("huwaei", new GraphViewSeriesStyle(Color.RED, 3), total_Huwaei);
					
					lineGraphView = new LineGraphView(this, "SMS Utilization");
					lineGraphView.setHorizontalLabels(horLevels);
					lineGraphView.addSeries(totalData);
					//lineGraphView.addSeries(ericssionData);
					//lineGraphView.addSeries(huwaeiData);
					lineGraphView.setShowLegend(true);
					lineGraphView.setLegendAlign(LegendAlign.MIDDLE);
					lineGraphView.setDrawDataPoints(true);
					lineGraphView.setDataPointsRadius(10f);
					lineGraphView.setViewPort(0, 4);
					lineGraphView.setScalable(true);
					
					Intent intent = new Intent(this, DailyKPIReportActivity.class);
					DailyKPIReportActivity.lineGraphView = lineGraphView;
					startActivity(intent);
				}else if(listPosition == 2){
					for (DailyKPI dailyKPI : dailyKPIs.dailyKPIList) {
						total_Data[totalCount] = new GraphViewData(totalCount, dailyKPI.SMSMOMTSuccessRate);
						//total_Ericssion[totalCount] = new GraphViewData(totalCount, dailyKPI.BusyHourTrafficEricsson);
						//total_Huwaei[totalCount] = new GraphViewData(totalCount, dailyKPI.BusyHourTrafficHuawei);
						horLevels[totalCount] = CommonTask.convertJsonDateToDailyKPI(dailyKPI.ReportDate);
						totalCount++;
					}
					
					GraphViewSeries totalData = new GraphViewSeries("SMS MO/MT",new GraphViewSeriesStyle(Color.GREEN, 3), total_Data);
					//GraphViewSeries ericssionData = new GraphViewSeries("Ericssion", new GraphViewSeriesStyle(Color.BLUE, 3), total_Ericssion);
					//GraphViewSeries huwaeiData = new GraphViewSeries("huwaei", new GraphViewSeriesStyle(Color.RED, 3), total_Huwaei);
					
					lineGraphView = new LineGraphView(this, "SMS MO/MT Success");
					lineGraphView.setHorizontalLabels(horLevels);
					lineGraphView.addSeries(totalData);
					//lineGraphView.addSeries(ericssionData);
					//lineGraphView.addSeries(huwaeiData);
					lineGraphView.setShowLegend(true);
					lineGraphView.setLegendAlign(LegendAlign.MIDDLE);
					lineGraphView.setDrawDataPoints(true);
					lineGraphView.setDataPointsRadius(10f);
					lineGraphView.setViewPort(0, 4);
					lineGraphView.setScalable(true);
					
					Intent intent = new Intent(this, DailyKPIReportActivity.class);
					DailyKPIReportActivity.lineGraphView = lineGraphView;
					startActivity(intent);
				}else if(listPosition == 3){
					for (DailyKPI dailyKPI : dailyKPIs.dailyKPIList) {
						total_Data[totalCount] = new GraphViewData(totalCount, dailyKPI.SMSCMOMTAttempt);
						//total_Ericssion[totalCount] = new GraphViewData(totalCount, dailyKPI.BusyHourTrafficEricsson);
						//total_Huwaei[totalCount] = new GraphViewData(totalCount, dailyKPI.BusyHourTrafficHuawei);
						horLevels[totalCount] = CommonTask.convertJsonDateToDailyKPI(dailyKPI.ReportDate);
						totalCount++;
					}
					
					GraphViewSeries totalData = new GraphViewSeries("SMS MO/MT Attempt",new GraphViewSeriesStyle(Color.GREEN, 3), total_Data);
					//GraphViewSeries ericssionData = new GraphViewSeries("Ericssion", new GraphViewSeriesStyle(Color.BLUE, 3), total_Ericssion);
					//GraphViewSeries huwaeiData = new GraphViewSeries("huwaei", new GraphViewSeriesStyle(Color.RED, 3), total_Huwaei);
					
					lineGraphView = new LineGraphView(this, "SMS MO/MT Attempt");
					lineGraphView.setHorizontalLabels(horLevels);
					lineGraphView.addSeries(totalData);
					//lineGraphView.addSeries(ericssionData);
					//lineGraphView.addSeries(huwaeiData);
					lineGraphView.setShowLegend(true);
					lineGraphView.setLegendAlign(LegendAlign.MIDDLE);
					lineGraphView.setDrawDataPoints(true);
					lineGraphView.setDataPointsRadius(10f);
					lineGraphView.setViewPort(0, 4);
					lineGraphView.setScalable(true);
					
					Intent intent = new Intent(this, DailyKPIReportActivity.class);
					DailyKPIReportActivity.lineGraphView = lineGraphView;
					startActivity(intent);
				}
			}catch(Exception ex){
				
			}
		}
	}*/

}
