package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.NetworkAvailability;
import com.vipdashboard.app.entities.NetworkAvailabilitys;
import com.vipdashboard.app.entities.NetworkDataTraffic;
import com.vipdashboard.app.entities.NetworkDataTraffics;
import com.vipdashboard.app.entities.Statistics2G;
import com.vipdashboard.app.entities.Statistics2Gs;
import com.vipdashboard.app.entities.Statistics3G;
import com.vipdashboard.app.entities.Statistics3Gs;
import com.vipdashboard.app.entities.StatisticsLTEKPI;
import com.vipdashboard.app.entities.StatisticsLTEKPIs;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.INetworkManager;
import com.vipdashboard.app.manager.NetworkManager;
import com.vipdashboard.app.utils.CommonTask;

public class ManagementActivity extends MainActionbarBase implements IAsynchronousTask{
	ProgressBar pbGraph;
	DownloadableAsyncTask downloadableAsyncTask;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.management);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		pbGraph = (ProgressBar) findViewById(R.id.pbManagementGraph);
		LoadDownloadData();
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

	private void LoadDownloadData() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
		
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
		INetworkManager networkManager = new NetworkManager();
		return networkManager.getComplesData();
	}
	@Override
	public void processDataAfterDownload(Object data) {
		if(data!=null){
			ArrayList<Object> complexData=(ArrayList<Object>)data;
			
			if(complexData.size()>0){
				
				NetworkAvailabilitys networkAvailabilitys = (NetworkAvailabilitys) complexData.get(0);
				NetworkDataTraffics networkDataTraffics = (NetworkDataTraffics) complexData.get(1);
				Statistics2Gs statistics2Gs=(Statistics2Gs) complexData.get(2);
				Statistics3Gs satistics3Gs=(Statistics3Gs) complexData.get(3);
				StatisticsLTEKPIs statisticsLTEKPIs=(StatisticsLTEKPIs) complexData.get(4);
				
				int totalCount = 0, netWorkDataTraffic2G_count = 0, netWorkDataTraffic3G_count=0, netWorkDataTrafficLTE_count=0;
				
				int ntotalLength = networkAvailabilitys.networkAvailableList.size();
				GraphViewData[] neyworkAvailableData = new GraphViewData[ntotalLength];
				
				int statisticalTotal = statistics2Gs.twoGList.size();
				GraphViewData[] voiceTrafic = new GraphViewData[statisticalTotal];
				GraphViewData[] CSSR = new GraphViewData[statisticalTotal];
				
				int networkDataLength = networkDataTraffics.networkDataTrafficList.size();
				
				
				for (NetworkDataTraffic networkDataTraffic : networkDataTraffics.networkDataTrafficList) {
					if(networkDataTraffic.NetworkType.equals("2G")){
						netWorkDataTraffic2G_count++;
					}else if(networkDataTraffic.NetworkType.equals("3G")){
						netWorkDataTraffic3G_count++;
					}else{
						netWorkDataTrafficLTE_count++;
					}
				}
				GraphViewData[] netWorkDataTraffic2G = new GraphViewData[netWorkDataTraffic2G_count];
				GraphViewData[] netWorkDataTraffic3G = new GraphViewData[netWorkDataTraffic3G_count];
				GraphViewData[] netWorkDataTrafficLTE = new GraphViewData[netWorkDataTrafficLTE_count];
				
				
				try{
					totalCount = 0;
					String[] horLevels = new String[ntotalLength];
					String[] horLevelsSet = new String[ntotalLength/2];
					for (NetworkAvailability networkAvailability : networkAvailabilitys.networkAvailableList){
						neyworkAvailableData[totalCount] = new GraphViewData(ntotalLength, networkAvailability.Availability);
						horLevels[totalCount] = CommonTask.convertJsonDateToGraphTime(networkAvailability.ReportDate);
						totalCount++;
					}
					totalCount = 0;
					for(int i=0;i<ntotalLength/2;i++){
						horLevelsSet[i] = horLevels[totalCount];
						totalCount += 2;
					}
					GraphView graphView = null;
					GraphViewSeries _2GTrafficTotalSeries = new GraphViewSeries("NA", new GraphViewSeriesStyle(Color.GREEN, 3),neyworkAvailableData );
					graphView=new BarGraphView(this , "NetWork Availability");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(_2GTrafficTotalSeries);
					graphView.setShowLegend(true);
					//((BarGraphView)graphView).setDrawValuesOnTop(true);					
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					graphView.setViewPort(0, 5);
					graphView.setScalable(true);
					LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
					layout.addView(graphView);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				/*
				 * voice Traffic
				 */
				
				try{
					totalCount = 0;
					String[] horLevels = new String[statisticalTotal];
					String[] horLevelsSet = new String[statisticalTotal/2];
					
					for (Statistics2G statistics2G : statistics2Gs.twoGList) {
						voiceTrafic[totalCount] = new GraphViewData(totalCount, statistics2G.C_Traffic_Total);
						horLevels[totalCount] = CommonTask.convertJsonDateToGraphTime(statistics2G.ReportDate);
						totalCount++;
					}
					GraphViewSeries _2G_Data_Series = new GraphViewSeries("2G", new GraphViewSeriesStyle(Color.GREEN, 3), voiceTrafic);
					totalCount = 0;
					voiceTrafic = new GraphViewData[statisticalTotal];
					for(Statistics3G statistics3G : satistics3Gs.threeGList){
						voiceTrafic[totalCount] = new GraphViewData(totalCount, statistics3G.Traffic_Speech_Erl);
						totalCount++;
					}
					GraphViewSeries _3G_Data_Series = new GraphViewSeries("3G", new GraphViewSeriesStyle(Color.BLUE, 3), voiceTrafic);
					
					totalCount = 0;
					voiceTrafic = new GraphViewData[statisticalTotal];
					for (StatisticsLTEKPI statisticsLTE : statisticsLTEKPIs.LTEKPIList) {
						voiceTrafic[totalCount] = new GraphViewData(totalCount, statisticsLTE.DLDataVolume);
						horLevels[totalCount] = CommonTask.convertJsonDateToGraphTime(statisticsLTE.ReportDate);
						totalCount++;
					}
					GraphViewSeries _LTE_Data_Series = new GraphViewSeries("LTE", new GraphViewSeriesStyle(Color.RED, 3), voiceTrafic);
					
					totalCount = 0;
					for(int i=0;i<statistics2Gs.twoGList.size()/2;i++){
						horLevelsSet[i] = horLevels[totalCount];
						totalCount += 2;
					} 
					
					LineGraphView graphView = null;
					
					graphView = new LineGraphView(this, "Voice Traffic");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(_2G_Data_Series);
					graphView.addSeries(_3G_Data_Series);
					graphView.addSeries(_LTE_Data_Series);
					//graphView.setDrawDataPoints(true);
					//graphView.setDataPointsRadius(10f);
					graphView.setDrawBackground(true);
							
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					//graphView.setDrawDataPoints(true);
					//graphView.setDataPointsRadius(10f);
					//graphView.setDrawDataPoints(true);
					graphView.setViewPort(0, 4);
					graphView.setScalable(true);
					LinearLayout layout = (LinearLayout) findViewById(R.id.graph2);
					layout.addView(graphView);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				/*
				 * NetWork Data Traffic
				 */
				try{
					totalCount = 0;
					int _2gCount = 0, _3GCount = 0, lteCount = 0;
					String[] horLevels = new String[networkDataLength];
					String[] horLevelsSet = new String[networkDataLength/2];
					
					for (NetworkDataTraffic networkDataTraffic : networkDataTraffics.networkDataTrafficList) {
						if(networkDataTraffic.NetworkType.equals("2G")){
							netWorkDataTraffic2G[_2gCount] = new GraphViewData(_2gCount, networkDataTraffic.TotalDataTraffic);
							_2gCount++;
						}else if(networkDataTraffic.NetworkType.equals("3G")){
							netWorkDataTraffic3G[_3GCount] = new GraphViewData(_3GCount, networkDataTraffic.TotalDataTraffic);
							_3GCount++;
						}else{
							netWorkDataTrafficLTE[lteCount] = new GraphViewData(lteCount, networkDataTraffic.TotalDataTraffic);
							lteCount++;
						}
						horLevels[totalCount] = CommonTask.convertJsonDateToGraphTime(networkDataTraffic.ReportDate);
						totalCount++;
					}
					
					totalCount = 0;
					for(int i=0;i<networkDataLength/2;i++){
						horLevelsSet[i] = horLevels[totalCount];
						totalCount += 2;
					} 
					
					GraphViewSeries _2g_Data_Series = new GraphViewSeries("2G", new GraphViewSeriesStyle(Color.GREEN, 3), netWorkDataTraffic2G);
					GraphViewSeries _3g_Data_Series = new GraphViewSeries("3G", new GraphViewSeriesStyle(Color.BLUE, 3), netWorkDataTraffic3G);
					GraphViewSeries _LTE_Data_Series = new GraphViewSeries("LTE", new GraphViewSeriesStyle(Color.RED, 3), netWorkDataTrafficLTE);
					
					LineGraphView graphView = null;
					
					graphView = new LineGraphView(this, "Network Data Traffic");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(_2g_Data_Series);
					graphView.addSeries(_3g_Data_Series);
					graphView.addSeries(_LTE_Data_Series);
					graphView.setShowLegend(true);
					//graphView.setDrawDataPoints(true);
					//graphView.setDataPointsRadius(10f);
					graphView.setDrawBackground(true);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					//graphView.setDrawDataPoints(true);
					//graphView.setDataPointsRadius(10f);
					graphView.setViewPort(0, 4);
					graphView.setScalable(true);
					LinearLayout layout = (LinearLayout) findViewById(R.id.graph3);
					layout.addView(graphView);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				/*
				 * CSSR Report
				 */
				try{
					totalCount = 0;
					String[] horLevels = new String[statisticalTotal];
					String[] horLevelsSet = new String[statisticalTotal/2];
					
					for (Statistics2G statistics2G : statistics2Gs.twoGList) {
						CSSR[totalCount] = new GraphViewData(totalCount, statistics2G.pCSSR);
						horLevels[totalCount] = CommonTask.convertJsonDateToGraphTime(statistics2G.ReportDate);
						totalCount++;
					}
					GraphViewSeries _2G_Data_Series = new GraphViewSeries("2G", new GraphViewSeriesStyle(Color.GREEN, 3), CSSR);
					totalCount = 0;
					CSSR = new GraphViewData[statisticalTotal];
					for(Statistics3G statistics3G : satistics3Gs.threeGList){
						CSSR[totalCount] = new GraphViewData(totalCount, statistics3G.pCSSR_Speech);
						totalCount++;
					}
					GraphViewSeries _3G_Data_Series = new GraphViewSeries("3G", new GraphViewSeriesStyle(Color.BLUE, 3), CSSR);
					
					totalCount = 0;
					CSSR = new GraphViewData[statisticalTotal];
					for (StatisticsLTEKPI statisticsLTE : statisticsLTEKPIs.LTEKPIList) {
						CSSR[totalCount] = new GraphViewData(totalCount, statisticsLTE. pSessionSetupSuccessRate);
						totalCount++;
					}
					GraphViewSeries _LTE_Data_Series = new GraphViewSeries("LTE", new GraphViewSeriesStyle(Color.RED, 3), CSSR);
					
					totalCount = 0;
					for(int i=0;i<statistics2Gs.twoGList.size()/2;i++){
						horLevelsSet[i] = horLevels[totalCount];
						totalCount += 2;
					} 
					
					LineGraphView graphView = null;
					
					graphView = new LineGraphView(this, "CSSR");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(_2G_Data_Series);
					graphView.addSeries(_3G_Data_Series);
					graphView.addSeries(_LTE_Data_Series);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					//graphView.setDrawDataPoints(true);
					//graphView.setDataPointsRadius(10f);
					graphView.setViewPort(0, 4);
					graphView.setScalable(true);
					LinearLayout layout = (LinearLayout) findViewById(R.id.graph4);
					layout.addView(graphView);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
