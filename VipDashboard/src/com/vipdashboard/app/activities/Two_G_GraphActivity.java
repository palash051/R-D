package com.vipdashboard.app.activities;

import java.text.ParseException;
import java.util.ArrayList;

import android.app.LauncherActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ReportAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.classes.Report;
import com.vipdashboard.app.entities.Statistics2G;
import com.vipdashboard.app.entities.Statistics2Gs;
import com.vipdashboard.app.entities.Statistics3Gs;
import com.vipdashboard.app.entities.StatisticsLTEKPIs;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class Two_G_GraphActivity extends MainActionbarBase implements OnItemClickListener  {

	/*RadioGroup radioGroup;
	RadioButton radioButton_barGraph, radioButton_lineGraph;
	ProgressBar pbGraph;
	DownloadableAsyncTask downloadableAsyncTask;
	
	private int PRESS_BAR_GRAPH = 0;
	private int PRESS_LINE_GRAPH = 1;
	private int SELECTED_GRAPG = PRESS_BAR_GRAPH;
	
	LinearLayout lineLayout, barLayout;*/
	//int max = Integer.MIN_VALUE , min = Integer.MAX_VALUE;
	
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
		titleText.setText("2G Performance");
		
		ArrayList<Report> reportList = new ArrayList<Report>();
		
		reportList.add(new Report("Voice Traffic",1));
		reportList.add(new Report("CSSR",2));
		reportList.add(new Report("TCH Drop",3));
		reportList.add(new Report("HORS",4));
		reportList.add(new Report("Network Availability",5));
		reportList.add(new Report("More Reports",6));
		
		adapter = new ReportAdapter(this, R.layout.lavel_item_layout, reportList);
		//lvNameText.setTag(adapter);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(this);
		
		//mSupportActionBar.setTitle("MyNet (Welcome " + CommonValues.getInstance().LoginUser.Name.toString() + ")");
		/*pbGraph = (ProgressBar) findViewById(R.id.pbGraph);
		radioGroup = (RadioGroup) findViewById(R.id.rgGroupStyle);
		radioButton_barGraph = (RadioButton) findViewById(R.id.barGraphButton);
		radioButton_lineGraph = (RadioButton) findViewById(R.id.lineGraphButton);
		
		barLayout = (LinearLayout) findViewById(R.id.barLayout);
		lineLayout = (LinearLayout) findViewById(R.id.lineLayout);
		
		
		runDownloadable();
		
		radioGroup.setOnClickListener(this);
		radioButton_barGraph.setOnClickListener(this);
		radioButton_lineGraph.setOnClickListener(this);*/
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
	
	/*@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.barGraphButton){
			SELECTED_GRAPG = PRESS_BAR_GRAPH;
			runDownloadable();
		}else if(id == R.id.lineGraphButton){
			SELECTED_GRAPG = PRESS_LINE_GRAPH;
			runDownloadable();
		}
	}*/

	

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		int ID = Integer.parseInt(String.valueOf(view.getTag()));
		showGraph(ID);
	}

	private void showGraph(int iD) {
		if(iD == 1){
			DailyKPIActivity.name = "Voice Traffic Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/TCHTrafficReport.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 2){
			DailyKPIActivity.name = "CSSR Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/CSSR.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 3){
			DailyKPIActivity.name = "TCH Drop Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/TCHDrop.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 4){
			DailyKPIActivity.name = "HOSR Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/HOSR.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 5){
			DailyKPIActivity.name = "Network Availability Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/NetworkAvailability.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 6){
			Intent intent = new Intent(this,MoreReportsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
	}

	/*private void runDownloadable() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
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
		IStatisticsReportManager statisticsReportManager = new StatisticsReportManager();
		return statisticsReportManager.get2GByCompanyID();
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			Statistics2Gs statistics2Gs = (Statistics2Gs) data;
			if (statistics2Gs != null && statistics2Gs.twoGList.size() > 0) {
				int itemCount = 0;
				
				GraphViewData[] gvDatasTrafficTotal = new GraphViewData[statistics2Gs.twoGList.size()];
				GraphViewData[] gvDatasPCDR = new GraphViewData[statistics2Gs.twoGList.size()];
				GraphViewData[] gvDatas_pDrop_SDCCH = new GraphViewData[statistics2Gs.twoGList.size()];
				GraphViewData[] gvDatas_pCSSR = new GraphViewData[statistics2Gs.twoGList.size()];
				GraphViewData[] gvDatas_pCongestion_TCH_Perceived = new GraphViewData[statistics2Gs.twoGList.size()];
				GraphViewData[] gvDatas_pCongestion_SDCCH = new GraphViewData[statistics2Gs.twoGList.size()];
				GraphViewData[] gvDatas_pHSR_Outgoing = new GraphViewData[statistics2Gs.twoGList.size()];
				//GraphViewData[] gvDatas_pCongestion_SDCCH = new GraphViewData[statistics2Gs.twoGList.size()];
				
				String[] horLevels = new String[statistics2Gs.twoGList.size()];
				String[] horLevelsSet = new String[statistics2Gs.twoGList.size()/2];
				try {
					for (Statistics2G statistics2G : statistics2Gs.twoGList) {
						gvDatasTrafficTotal[itemCount] = new GraphViewData(
								itemCount, statistics2G.C_Traffic_Total);
						gvDatasPCDR[itemCount] = new GraphViewData(
								itemCount, statistics2G.pCDR);
						gvDatas_pDrop_SDCCH[itemCount] = new GraphViewData(
								itemCount, statistics2G.pDrop_SDCCH);
						gvDatas_pCSSR[itemCount] = new GraphViewData(
								itemCount, statistics2G.pCSSR);
						gvDatas_pCongestion_TCH_Perceived[itemCount] = new GraphViewData(
								itemCount, statistics2G.pCongestion_TCH_Perceived);
						gvDatas_pCongestion_SDCCH[itemCount] = new GraphViewData(
								itemCount, statistics2G.pCongestion_SDCCH);
						gvDatas_pHSR_Outgoing[itemCount] = new GraphViewData(
								itemCount, statistics2G.pHSR_Outgoing);
						
						horLevels[itemCount] = CommonTask
									.convertJsonDateToGraphTime(statistics2G.ReportDate);
						//Log.e("horLevels Item", horLevels[itemCount]);
						//if(Integer.parseInt(horLevels[itemCount]) > max) max = Integer.parseInt(horLevels[itemCount]);
						//if(Integer.parseInt(horLevels[itemCount]) < min) min = Integer.parseInt(horLevels[itemCount]);
						itemCount++;
						
					}
					itemCount = 0;
					for(int i=0;i<statistics2Gs.twoGList.size()/2;i++){
						horLevelsSet[i] = horLevels[itemCount];
						itemCount += 2;
					}
					
					GraphView graphView = null;
					
					if(SELECTED_GRAPG == 0){

						lineLayout.setVisibility(LinearLayout.GONE);
						barLayout.setVisibility(LinearLayout.VISIBLE);
						
						GraphViewSeries _2GTrafficTotalSeries = new GraphViewSeries("TT", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatasTrafficTotal );
						graphView=new BarGraphView(this , "2G (Traffic Total)");
						//graphView=new LineGraphView(this , "2G (Traffic Total)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_2GTrafficTotalSeries);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						graphView.setManualYAxisBounds(6000, 2000);
						LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
						layout.addView(graphView);
						
						GraphViewSeries _pCDRSeries = new GraphViewSeries("PCDR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatasPCDR );
						graphView=new BarGraphView(this , "2G (PCDRSeries)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_pCDRSeries);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.graph2);					
						layout.addView(graphView);
						
						
						GraphViewSeries _pDrop_SDCCH_Series = new GraphViewSeries("PDSDCCH", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatasPCDR );
						graphView=new BarGraphView(this , "2G (pDrop_SDCCH)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_pDrop_SDCCH_Series);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.graph3);					
						layout.addView(graphView);
						
						
						
						GraphViewSeries _pCSSRSeries = new GraphViewSeries("PCSSR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatasPCDR );
						graphView=new BarGraphView(this , "2G (PCSSR)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_pCSSRSeries);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.graph4);					
						layout.addView(graphView);
						
						GraphViewSeries _pCongestion_TCH_Perceived = new GraphViewSeries("PCTCH", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pCongestion_TCH_Perceived );
						graphView=new BarGraphView(this , "2G (PCTCH)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_pCongestion_TCH_Perceived);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.graph5);					
						layout.addView(graphView);
						
						GraphViewSeries _pCongestion_SDCCH = new GraphViewSeries("PCSDCCH", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pCongestion_SDCCH );
						graphView=new BarGraphView(this , "2G (PCDRSeries)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_pCongestion_SDCCH);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.graph6);					
						layout.addView(graphView);
						
						GraphViewSeries _pHSR_Outgoing = new GraphViewSeries("PHSRO", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pHSR_Outgoing );
						graphView=new BarGraphView(this , "2G (PCDRSeries)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_pHSR_Outgoing);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.graph7);					
						layout.addView(graphView);
					}else if(SELECTED_GRAPG == 1){
						
						barLayout.setVisibility(LinearLayout.GONE);
						lineLayout.setVisibility(LinearLayout.VISIBLE);
						
						GraphViewSeries _2GTrafficTotalSeries = new GraphViewSeries("TT", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatasTrafficTotal );
						graphView=new LineGraphView(this , "2G (Traffic Total)");
						//graphView=new LineGraphView(this , "2G (Traffic Total)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_2GTrafficTotalSeries);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						graphView.setManualYAxisBounds(6000, 2000);
						LinearLayout layout = (LinearLayout) findViewById(R.id.lgraph1);
						layout.addView(graphView);
						
						GraphViewSeries _pCDRSeries = new GraphViewSeries("PCDR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatasPCDR );
						graphView=new LineGraphView(this , "2G (PCDRSeries)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_pCDRSeries);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.lgraph2);					
						layout.addView(graphView);
						
						
						GraphViewSeries _pDrop_SDCCH_Series = new GraphViewSeries("PDSDCCH", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatasPCDR );
						graphView=new LineGraphView(this , "2G (PDrop_SDCCH_Series)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_pDrop_SDCCH_Series);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.lgraph3);					
						layout.addView(graphView);
						
						
						
						GraphViewSeries _pCSSRSeries = new GraphViewSeries("PCSSR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatasPCDR );
						graphView=new LineGraphView(this , "2G (PCSSRSeries)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_pCSSRSeries);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.lgraph4);					
						layout.addView(graphView);
						
						GraphViewSeries _pCongestion_TCH_Perceived = new GraphViewSeries("PCTCH", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pCongestion_TCH_Perceived );
						graphView=new LineGraphView(this , "2G (PCongestion TCH Perceived)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_pCongestion_TCH_Perceived);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.lgraph5);					
						layout.addView(graphView);
						
						GraphViewSeries _pCongestion_SDCCH = new GraphViewSeries("PCSDCCH", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pCongestion_SDCCH );
						graphView=new LineGraphView(this , "2G (PCongestion SDCCH)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_pCongestion_SDCCH);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.lgraph6);					
						layout.addView(graphView);
						
						GraphViewSeries _pHSR_Outgoing = new GraphViewSeries("PHSRO", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pHSR_Outgoing );
						graphView=new LineGraphView(this , "2G (PHSR Outgoing)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_pHSR_Outgoing);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.lgraph7);					
						layout.addView(graphView);
						
						
					}
				} catch (ParseException e) {

					e.printStackTrace();
				}

			}
		}
	}*/
}
