package com.vipdashboard.app.activities;

import java.text.ParseException;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.actionbarsherlock.view.MenuItem;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.Statistics2G;
import com.vipdashboard.app.entities.Statistics2Gs;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class GraphActivity extends MainActionbarBase implements
		IAsynchronousTask {

	ProgressBar pbGraph;
	DownloadableAsyncTask downloadableAsyncTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graphs);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		//mSupportActionBar.setTitle("MyNet (Welcome " + CommonValues.getInstance().LoginUser.Name.toString() + ")");
		pbGraph = (ProgressBar) findViewById(R.id.pbGraph);
		runDownloadable();
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

	

	private void runDownloadable() {
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
				String[] horLevels = new String[5];
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
						if(itemCount<5)
							horLevels[itemCount] = CommonTask
									.convertJsonDateToGraphTime(statistics2G.ReportDate);
						itemCount++;

					}
					
					GraphViewSeries _2GTrafficTotalSeries = new GraphViewSeries("TT", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatasTrafficTotal );
					GraphView graphView=new BarGraphView(this , "2G (Traffic Total)");
					
					graphView.setHorizontalLabels(horLevels);
					graphView.addSeries(_2GTrafficTotalSeries);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.TOP); 
					//graphView.setScalable(true);
					//graphView.setViewPort(1, 14);
					//graphView.setScrollable(true);
					graphView.setManualYAxisBounds(6000, 2000);
					LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);					
					layout.addView(graphView);
					
					
					GraphViewSeries _pCDRSeries = new GraphViewSeries("PCDR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatasPCDR );
					graphView=new BarGraphView(this , "2G (PCDR)");
					
					graphView.setHorizontalLabels(horLevels);
					graphView.addSeries(_pCDRSeries);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.TOP); 
					//graphView.setScalable(true);
					//graphView.setViewPort(1, 14);
					//graphView.setScrollable(true);
					graphView.setManualYAxisBounds(1, 0);
					layout = (LinearLayout) findViewById(R.id.graph2);					
					layout.addView(graphView);
					
					
					GraphViewSeries _pDrop_SDCCH_Series = new GraphViewSeries("PDSDCCH", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatasPCDR );
					graphView=new BarGraphView(this , "2G (pDrop_SDCCH)");
					
					graphView.setHorizontalLabels(horLevels);
					graphView.addSeries(_pDrop_SDCCH_Series);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.TOP); 
					//graphView.setScalable(true);
					//graphView.setViewPort(1, 14);
					//graphView.setScrollable(true);
					graphView.setManualYAxisBounds(1, 0);
					layout = (LinearLayout) findViewById(R.id.graph3);					
					layout.addView(graphView);
					
					
					
					GraphViewSeries _pCSSRSeries = new GraphViewSeries("PCSSR", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatasPCDR );
					graphView=new BarGraphView(this , "2G (PCSSR)");
					
					graphView.setHorizontalLabels(horLevels);
					graphView.addSeries(_pCSSRSeries);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.TOP); 
					//graphView.setScalable(true);
					//graphView.setViewPort(1, 14);
					//graphView.setScrollable(true);
					graphView.setManualYAxisBounds(1, 0);
					layout = (LinearLayout) findViewById(R.id.graph4);					
					layout.addView(graphView);
					
				} catch (ParseException e) {

					e.printStackTrace();
				}

			}
		}
	}

}
