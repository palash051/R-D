package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.actionbarsherlock.view.MenuItem;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.Statistics2G;
import com.vipdashboard.app.entities.Statistics2Gs;
import com.vipdashboard.app.entities.Statistics3G;
import com.vipdashboard.app.entities.Statistics3Gs;
import com.vipdashboard.app.entities.StatisticsLTEKPIs;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class ComplexGraphActivity extends MainActionbarBase implements IAsynchronousTask{
	
	ProgressBar pbGraph;
	DownloadableAsyncTask downloadableAsyncTask;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complex_graphs);
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
		return statisticsReportManager.getComplexData();
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data!=null){
			ArrayList<Object> complexData=(ArrayList<Object>)data;
			
			if(complexData.size()>0){
				int itemCount = 0;
				
				Statistics2Gs statistics2Gs=(Statistics2Gs) complexData.get(0);
				Statistics3Gs satistics3Gs=(Statistics3Gs) complexData.get(1);
				StatisticsLTEKPIs statisticsLTEKPIs=(StatisticsLTEKPIs) complexData.get(2);
				
				int totalLength = statistics2Gs.twoGList.size();
				
				GraphViewData[] TT_Table = new GraphViewData[totalLength];
				GraphViewData[] PCSSR_Table = new GraphViewData[totalLength];
				
				
				String[] horLevels = new String[totalLength];
				String[] horLevelsSet = new String[totalLength/2];
				
				try{
					for (Statistics2G statistics2G : statistics2Gs.twoGList) {
						TT_Table[itemCount] = new GraphViewData(itemCount, statistics2G.C_Traffic_Total);
						PCSSR_Table[itemCount] = new GraphViewData(itemCount, statistics2G.pCSSR);
						
						horLevels[itemCount] = CommonTask.convertJsonDateToGraphTime(statistics2G.ReportDate);
						itemCount++;
					}
					GraphViewSeries _2G_Data_Series = new GraphViewSeries("2G", new GraphViewSeriesStyle(Color.GREEN, 3), TT_Table);
					GraphViewSeries _2GpCSSR_Data_Series = new GraphViewSeries("2G", new GraphViewSeriesStyle(Color.GREEN, 3), PCSSR_Table);
					itemCount = 0;
					TT_Table = new GraphViewData[totalLength];
					PCSSR_Table = new GraphViewData[totalLength];
					
					for(Statistics3G statistics3G : satistics3Gs.threeGList){
						TT_Table[itemCount] = new GraphViewData(itemCount, statistics3G.Traffic_Speech_Erl);
						PCSSR_Table[itemCount] = new GraphViewData(itemCount, statistics3G.pCSSR_Speech);
						itemCount++;
					}
					GraphViewSeries _3G_Data_Series = new GraphViewSeries("3G", new GraphViewSeriesStyle(Color.BLUE, 3), TT_Table);
					GraphViewSeries _3GpCSSR_Data_Series = new GraphViewSeries("3G", new GraphViewSeriesStyle(Color.BLUE, 3), PCSSR_Table);
					itemCount = 0;
					for(int i=0;i<statistics2Gs.twoGList.size()/2;i++){
						horLevelsSet[i] = horLevels[itemCount];
						itemCount += 2;
					} 
					
					GraphView graphView;
					
					graphView = new LineGraphView(this, "Traffic Total");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(_2G_Data_Series);
					graphView.addSeries(_3G_Data_Series);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					graphView.setLegendWidth(75);
					//graphView.getGraphViewStyle().setNumHorizontalLabels(3);
					//graphView.getGraphViewStyle().
					graphView.setViewPort(0, 4);
					graphView.setScalable(true);
					LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
					layout.addView(graphView);
					
					
					graphView = new LineGraphView(this, "PCSSR");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(_2GpCSSR_Data_Series);
					graphView.addSeries(_3GpCSSR_Data_Series);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					graphView.setLegendWidth(75);
					//graphView.getGraphViewStyle().setNumHorizontalLabels(3);
					//graphView.getGraphViewStyle().
					graphView.setViewPort(0, 4);
					graphView.setScalable(true);
					layout = (LinearLayout) findViewById(R.id.graph2);
					layout.addView(graphView);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}

}
