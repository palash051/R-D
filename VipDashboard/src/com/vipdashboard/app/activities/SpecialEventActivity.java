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

public class SpecialEventActivity extends MainActionbarBase implements IAsynchronousTask, OnItemClickListener{
	DownloadableAsyncTask downloadAsync;
	ProgressBar pbGraph;
	ListView dailyKPIReport;
	ReportAdapter adapter;
	int listPosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.special_report);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		dailyKPIReport = (ListView) findViewById(R.id.lvDailyKPIReportList);
		pbGraph = (ProgressBar) findViewById(R.id.pbDailyKPI);
		/*String[] report = new String[]{"MMSC Success Rate","Route Utilization","Core","Radio","MMS"};
		ArrayList<String> reportList = new ArrayList<String>();
		for(int i=0;i<report.length;i++){
			reportList.add(report[i]);
		}
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,reportList);*/
		ArrayList<Report> reportList = new ArrayList<Report>();
		reportList.add(new Report("MMSC Success Rate",1));
		reportList.add(new Report("Route Utilization",2));
		reportList.add(new Report("Core",3));
		reportList.add(new Report("Radio",4));
		reportList.add(new Report("MMS",5));
		
		adapter = new ReportAdapter(this, R.layout.lavel_item_layout,reportList);
		
		dailyKPIReport.setAdapter(adapter);
		dailyKPIReport.setOnItemClickListener(this);
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
		//pbGraph.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		//pbGraph.setVisibility(View.GONE);
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
			
			LineGraphView lineGraphView = null;
			try{
				if(listPosition == 1){
				for (DailyKPI dailyKPI : dailyKPIs.dailyKPIList) {
					total_Data[totalCount] = new GraphViewData(totalCount, dailyKPI.MMSCSubmitSuccessRate);
					total_Ericssion[totalCount] = new GraphViewData(totalCount, dailyKPI.MMSCSubmitSuccessRateEricsson);
					total_Huwaei[totalCount] = new GraphViewData(totalCount, dailyKPI.MMSCSubmitSuccessRateHuawei);
					horLevels[totalCount] = CommonTask.convertJsonDateToDailyKPI(dailyKPI.ReportDate);
					totalCount++;
				}
				
				GraphViewSeries totalData = new GraphViewSeries("total", new GraphViewSeriesStyle(Color.GREEN, 3), total_Data);
				GraphViewSeries ericssionData = new GraphViewSeries("Ericssion", new GraphViewSeriesStyle(Color.BLUE, 3), total_Ericssion);
				GraphViewSeries huwaeiData = new GraphViewSeries("huwaei", new GraphViewSeriesStyle(Color.RED, 3), total_Huwaei);
				
				lineGraphView = new LineGraphView(this, "MMSC Success");
				lineGraphView.setHorizontalLabels(horLevels);
				lineGraphView.addSeries(totalData);
				lineGraphView.addSeries(ericssionData);
				lineGraphView.addSeries(huwaeiData);
				lineGraphView.setShowLegend(true);
				lineGraphView.setLegendAlign(LegendAlign.MIDDLE);
				//lineGraphView.setDrawDataPoints(true);
				//lineGraphView.setDataPointsRadius(10f);
				lineGraphView.setViewPort(0, 4);
				lineGraphView.setScalable(true);
				
				Intent intent = new Intent(this, DailyKPIReportActivity.class);
				DailyKPIReportActivity.lineGraphView = lineGraphView;
				startActivity(intent);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

}
