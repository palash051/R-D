package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ReportAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.classes.Report;
import com.vipdashboard.app.entities.Statistics3G;
import com.vipdashboard.app.entities.Statistics3Gs;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class Three_G_GraphActivity extends MainActionbarBase implements OnItemClickListener {
	
	/*RadioGroup radioGroup;
	RadioButton radioButton_barGraph, radioButton_lineGraph;
	
	ProgressBar pbGraph;
	DownloadableAsyncTask downloadableAsyncTask;
	//int max = Integer.MIN_VALUE , min = Integer.MAX_VALUE;
	private int PRESS_BAR_GRAPH = 0;
	private int PRESS_LINE_GRAPH = 1;
	private int SELECTED_GRAPG = PRESS_BAR_GRAPH;
	
	LinearLayout lineLayout, barLayout;*/
	
	ListView listView;
	ReportAdapter adapter;
	TextView titleText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report_main);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		
		//listView = (ListView) findViewById(R.id.lvReportList);
		titleText = (TextView) findViewById(R.id.tvCollaborationMainTitle);
		titleText.setText("3G Performance");
		
		ArrayList<Report> reportList = new ArrayList<Report>();
		
		reportList.add(new Report("Voice Traffic",1));
		reportList.add(new Report("Data Volume",2));
		reportList.add(new Report("IRAT HOSR",3));
		reportList.add(new Report("PS HOSR",4));
		reportList.add(new Report("CS CSSR",5));
		reportList.add(new Report("Call Availability",6));
		
		adapter = new ReportAdapter(this, R.layout.lavel_item_layout, reportList);
		//lvNameText.setTag(adapter);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(this);
		
		//mSupportActionBar.setTitle("MyNet (Welcome " + CommonValues.getInstance().LoginUser.Name.toString() + ")");
		/*pbGraph = (ProgressBar) findViewById(R.id.pbGraph);
		barLayout = (LinearLayout) findViewById(R.id.barGraphThreeG);
		lineLayout = (LinearLayout) findViewById(R.id.lineGraphThreeG);
		radioGroup = (RadioGroup) findViewById(R.id.rgGroupStyle);
		radioButton_barGraph = (RadioButton) findViewById(R.id.barGraphButton);
		radioButton_lineGraph = (RadioButton) findViewById(R.id.lineGraphButton);
		
		
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
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		int ID = Integer.parseInt(String.valueOf(view.getTag()));
		showGraph(ID);
		
	}

	private void showGraph(int iD) {
		if(iD == 1){
			DailyKPIActivity.name = "Voice Traffic Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/VoiceTraffic.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 2){
			DailyKPIActivity.name = "Data Volume Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/DataVolume.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 3){
			DailyKPIActivity.name = "IRAT HOSR Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/IRATHOSR.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 4){
			DailyKPIActivity.name = "PS CSSR Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/PSCSSR.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 5){
			DailyKPIActivity.name = "CS CSSR Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/CS%20CSSR.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 6){
			DailyKPIActivity.name = "Call Availability Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/CellAvailability.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
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
	}

	private void runDownloadable() {
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
		return statisticsReportManager.get3GByCompanyID();
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			Statistics3Gs statistics3Gs = (Statistics3Gs) data;
			if (statistics3Gs != null && statistics3Gs.threeGList.size() > 0) {
				int itemCount = 0;
				
				GraphViewData[] gvDatas_Traffic_Speech_Erl = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_Volume_DL_R99 = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_Volume_UL_R99 = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_Volume_HS = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_pCSSR_Speech = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_pCSSR_PS = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_pCSSR_HS = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_pRRC_Conn_CS_SR = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_pRAB_Est_Speech_SR = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_pRRC_Conn_PS_SR = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_pRAB_Est_PacketInter_SR = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_pDrop_Speech = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_pPS_R99_DCR = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_pHS_DCR = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_pSoft_HO_SR = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_pIRAT_HO_Speech_SR = new GraphViewData[statistics3Gs.threeGList.size()];
				GraphViewData[] gvDatas_pmCellDowntimeAuto = new GraphViewData[statistics3Gs.threeGList.size()];
				
				String[] horLevels = new String[statistics3Gs.threeGList.size()];
				String[] horLevelsSet = new String[statistics3Gs.threeGList.size()/2];
				
				try{
					for (Statistics3G statistics3G : statistics3Gs.threeGList) {
						gvDatas_Traffic_Speech_Erl[itemCount] = new GraphViewData(
								itemCount, statistics3G.Traffic_Speech_Erl);
						gvDatas_Volume_DL_R99[itemCount] = new GraphViewData(
								itemCount, statistics3G.Volume_DL_R99);
						gvDatas_Volume_UL_R99[itemCount] = new GraphViewData(
								itemCount, statistics3G.Volume_UL_R99);
						gvDatas_Volume_HS[itemCount] = new GraphViewData(
								itemCount, statistics3G.Volume_HS);
						gvDatas_pCSSR_Speech[itemCount] = new GraphViewData(
								itemCount, statistics3G.pCSSR_Speech);
						gvDatas_pCSSR_PS[itemCount] = new GraphViewData(
								itemCount, statistics3G.pCSSR_PS);
						gvDatas_pCSSR_HS[itemCount] = new GraphViewData(
								itemCount, statistics3G.pCSSR_HS);
						gvDatas_pRRC_Conn_CS_SR[itemCount] = new GraphViewData(
								itemCount, statistics3G.pRRC_Conn_CS_SR);
						gvDatas_pRAB_Est_Speech_SR[itemCount] = new GraphViewData(
								itemCount, statistics3G.pRAB_Est_Speech_SR);
						gvDatas_pRRC_Conn_PS_SR[itemCount] = new GraphViewData(
								itemCount, statistics3G.pRRC_Conn_PS_SR);
						gvDatas_pRAB_Est_PacketInter_SR[itemCount] = new GraphViewData(
								itemCount, statistics3G.pRAB_Est_PacketInter_SR);
						gvDatas_pDrop_Speech[itemCount] = new GraphViewData(
								itemCount, statistics3G.pDrop_Speech);
						gvDatas_pPS_R99_DCR[itemCount] = new GraphViewData(
								itemCount, statistics3G.pPS_R99_DCR);
						gvDatas_pHS_DCR[itemCount] = new GraphViewData(
								itemCount, statistics3G.pHS_DCR);
						gvDatas_pSoft_HO_SR[itemCount] = new GraphViewData(
								itemCount, statistics3G.pSoft_HO_SR);
						gvDatas_pIRAT_HO_Speech_SR[itemCount] = new GraphViewData(
								itemCount, statistics3G.pIRAT_HO_Speech_SR);
						gvDatas_pmCellDowntimeAuto[itemCount] = new GraphViewData(
								itemCount, statistics3G.pmCellDowntimeAuto);
						
						horLevels[itemCount] = CommonTask
									.convertJsonDateToGraphTime(statistics3G.ReportDate);
						itemCount++;
						
					}
					
					itemCount = 0;
					for(int i=0;i<statistics3Gs.threeGList.size()/2;i++){
						horLevelsSet[i] = horLevels[itemCount];
						itemCount += 2;
					}
					
					if(SELECTED_GRAPG == 0){
						
						lineLayout.setVisibility(LinearLayout.GONE);
						barLayout.setVisibility(LinearLayout.VISIBLE);
						
						GraphViewSeries _3GTraffic_Speech_Erl = new GraphViewSeries("TSE", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_Traffic_Speech_Erl );
						GraphView graphView=new BarGraphView(this , "3G (Traffic Speech Erl)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_3GTraffic_Speech_Erl);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);				
						layout.addView(graphView);
						
						GraphViewSeries _3G_Volume_DL_R99 = new GraphViewSeries("VDLR99", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_Volume_DL_R99 );
						graphView=new BarGraphView(this , "3G (Volume DL R99)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_3G_Volume_DL_R99);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph2);				
						layout.addView(graphView);
						
						GraphViewSeries Volume_UL_R99 = new GraphViewSeries("VULR99", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_Volume_UL_R99 );
						graphView=new BarGraphView(this , "3G (Volume UL R99)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(Volume_UL_R99);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph3);				
						layout.addView(graphView);
						
						GraphViewSeries Volume_HS = new GraphViewSeries("VHS", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_Volume_HS );
						graphView=new BarGraphView(this , "3G (Volume HS)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(Volume_HS);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph4);				
						layout.addView(graphView);
						
						GraphViewSeries pCSSR_Speech = new GraphViewSeries("PCSSRS", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pCSSR_Speech );
						graphView=new BarGraphView(this , "3G (pCSSR Speech)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pCSSR_Speech);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph5);				
						layout.addView(graphView);
						
						GraphViewSeries pCSSR_PS = new GraphViewSeries("PCSSRPS", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pCSSR_PS );
						graphView=new BarGraphView(this , "3G (pCSSR PS)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pCSSR_PS);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph6);				
						layout.addView(graphView);
						
						GraphViewSeries pCSSR_HS = new GraphViewSeries("PCSSRHS", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pCSSR_HS );
						graphView=new BarGraphView(this , "3G (pCSSR HS)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pCSSR_HS);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph7);				
						layout.addView(graphView);
						
						GraphViewSeries pRRC_Conn_CS_SR = new GraphViewSeries("PRRC", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pRRC_Conn_CS_SR );
						graphView=new BarGraphView(this , "3G (pRRC SR)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pRRC_Conn_CS_SR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph8);				
						layout.addView(graphView);
						
						GraphViewSeries pRAB_Est_Speech_SR = new GraphViewSeries("PRABESSR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pRAB_Est_Speech_SR );
						graphView=new BarGraphView(this , "3G (pRAB SR)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pRAB_Est_Speech_SR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph9);				
						layout.addView(graphView);
						
						GraphViewSeries pRRC_Conn_PS_SR = new GraphViewSeries("PRRCSR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pRRC_Conn_PS_SR );
						graphView=new BarGraphView(this , "3G (pRRC CPSSR)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pRRC_Conn_PS_SR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph10);				
						layout.addView(graphView);
						
						GraphViewSeries pRAB_Est_PacketInter_SR = new GraphViewSeries("PRABEPSR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pRAB_Est_PacketInter_SR );
						graphView=new BarGraphView(this , "3G (pRAB EPSR)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pRAB_Est_PacketInter_SR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph11);				
						layout.addView(graphView);
						
						GraphViewSeries pDrop_Speech = new GraphViewSeries("PDS", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pDrop_Speech );
						graphView=new BarGraphView(this , "3G (pDrop Speech)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pDrop_Speech);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph12);				
						layout.addView(graphView);
						
						GraphViewSeries pPS_R99_DCR = new GraphViewSeries("PPSR99", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pPS_R99_DCR );
						graphView=new BarGraphView(this , "3G (pPS R99 DCR)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pPS_R99_DCR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph13);				
						layout.addView(graphView);
						
						GraphViewSeries pHS_DCR = new GraphViewSeries("PHSDCR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pHS_DCR );
						graphView=new BarGraphView(this , "3G (pHS DCR)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pHS_DCR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph14);				
						layout.addView(graphView);
						
						GraphViewSeries pSoft_HO_SR = new GraphViewSeries("PSHOSR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pSoft_HO_SR );
						graphView=new BarGraphView(this , "3G (pSoft HOSR)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pSoft_HO_SR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph15);				
						layout.addView(graphView);
						
						GraphViewSeries pIRAT_HO_Speech_SR = new GraphViewSeries("PIRAT", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pIRAT_HO_Speech_SR );
						graphView=new BarGraphView(this , "3G (pIRAT Speech)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pIRAT_HO_Speech_SR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph16);				
						layout.addView(graphView);
						
						GraphViewSeries pmCellDowntimeAuto = new GraphViewSeries("PMCDA", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pmCellDowntimeAuto );
						graphView=new BarGraphView(this , "3G (PM CellDowntimeAuto)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pmCellDowntimeAuto);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.graph17);				
						layout.addView(graphView);
					}else{
						
						lineLayout.setVisibility(LinearLayout.VISIBLE);
						barLayout.setVisibility(LinearLayout.GONE);
						
						GraphViewSeries _3GTraffic_Speech_Erl = new GraphViewSeries("TSE", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_Traffic_Speech_Erl );
						GraphView graphView=new LineGraphView(this , "3G (Traffic Speech Erl)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_3GTraffic_Speech_Erl);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						LinearLayout layout = (LinearLayout) findViewById(R.id.lgraph1);				
						layout.addView(graphView);
						
						GraphViewSeries _3G_Volume_DL_R99 = new GraphViewSeries("VDLR99", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_Volume_DL_R99 );
						graphView=new LineGraphView(this , "3G (Volume DL R99)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(_3G_Volume_DL_R99);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph2);				
						layout.addView(graphView);
						
						GraphViewSeries Volume_UL_R99 = new GraphViewSeries("VULR99", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_Volume_UL_R99 );
						graphView=new LineGraphView(this , "3G (Volume UL R99)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(Volume_UL_R99);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph3);				
						layout.addView(graphView);
						
						GraphViewSeries Volume_HS = new GraphViewSeries("VHS", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_Volume_HS );
						graphView=new LineGraphView(this , "3G (Volume HS)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(Volume_HS);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph4);				
						layout.addView(graphView);
						
						GraphViewSeries pCSSR_Speech = new GraphViewSeries("PCSSRS", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pCSSR_Speech );
						graphView=new LineGraphView(this , "3G (pCSSR Speech)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pCSSR_Speech);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph5);				
						layout.addView(graphView);
						
						GraphViewSeries pCSSR_PS = new GraphViewSeries("PCSSRPS", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pCSSR_PS );
						graphView=new LineGraphView(this , "3G (pCSSR PS)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pCSSR_PS);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph6);				
						layout.addView(graphView);
						
						GraphViewSeries pCSSR_HS = new GraphViewSeries("PCSSRHS", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pCSSR_HS );
						graphView=new LineGraphView(this , "3G (pCSSR HS)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pCSSR_HS);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph7);				
						layout.addView(graphView);
						
						GraphViewSeries pRRC_Conn_CS_SR = new GraphViewSeries("PRRC", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pRRC_Conn_CS_SR );
						graphView=new LineGraphView(this , "3G (pRRC SR)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pRRC_Conn_CS_SR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph8);				
						layout.addView(graphView);
						
						GraphViewSeries pRAB_Est_Speech_SR = new GraphViewSeries("PRABESSR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pRAB_Est_Speech_SR );
						graphView=new LineGraphView(this , "3G (pRAB SR)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pRAB_Est_Speech_SR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph9);				
						layout.addView(graphView);
						
						GraphViewSeries pRRC_Conn_PS_SR = new GraphViewSeries("PRRCSR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pRRC_Conn_PS_SR );
						graphView=new LineGraphView(this , "3G (pRRC CPSSR)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pRRC_Conn_PS_SR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph10);				
						layout.addView(graphView);
						
						GraphViewSeries pRAB_Est_PacketInter_SR = new GraphViewSeries("PRABEPSR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pRAB_Est_PacketInter_SR );
						graphView=new LineGraphView(this , "3G (pRAB EPSR)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pRAB_Est_PacketInter_SR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph11);				
						layout.addView(graphView);
						
						GraphViewSeries pDrop_Speech = new GraphViewSeries("PDS", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pDrop_Speech );
						graphView=new LineGraphView(this , "3G (pDrop Speech)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pDrop_Speech);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph12);				
						layout.addView(graphView);
						
						GraphViewSeries pPS_R99_DCR = new GraphViewSeries("PPSR99", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pPS_R99_DCR );
						graphView=new LineGraphView(this , "3G (pPS R99 DCR)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pPS_R99_DCR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph13);				
						layout.addView(graphView);
						
						GraphViewSeries pHS_DCR = new GraphViewSeries("PHSDCR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pHS_DCR );
						graphView=new LineGraphView(this , "3G (pHS DCR)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pHS_DCR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph14);				
						layout.addView(graphView);
						
						GraphViewSeries pSoft_HO_SR = new GraphViewSeries("PSHOSR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pSoft_HO_SR );
						graphView=new LineGraphView(this , "3G (pSoft HOSR)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pSoft_HO_SR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph15);				
						layout.addView(graphView);
						
						GraphViewSeries pIRAT_HO_Speech_SR = new GraphViewSeries("PIRAT", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pIRAT_HO_Speech_SR );
						graphView=new LineGraphView(this , "3G (pIRAT Speech)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pIRAT_HO_Speech_SR);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph16);				
						layout.addView(graphView);
						
						GraphViewSeries pmCellDowntimeAuto = new GraphViewSeries("PMCDA", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_pmCellDowntimeAuto );
						graphView=new LineGraphView(this , "3G (PM CellDowntimeAuto)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pmCellDowntimeAuto);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE); 
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						//graphView.setManualYAxisBounds(6000, 2000);
						layout = (LinearLayout) findViewById(R.id.lgraph17);				
						layout.addView(graphView);
					}
					
					
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}*/

}
