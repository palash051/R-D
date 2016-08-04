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
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.Statistics2G;
import com.vipdashboard.app.entities.Statistics2Gs;
import com.vipdashboard.app.entities.Statistics3G;
import com.vipdashboard.app.entities.Statistics3Gs;
import com.vipdashboard.app.entities.StatisticsLTEKPI;
import com.vipdashboard.app.entities.StatisticsLTEKPIs;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonTask;

public class TechicalActivity extends MainActionbarBase implements IAsynchronousTask{
	ProgressBar pbGraph;
	DownloadableAsyncTask downloadableAsyncTask;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.techical_report);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		pbGraph = (ProgressBar) findViewById(R.id.pbTechicalGraph);
		runDownloaded();
	}
	
	private void runDownloaded() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(TechicalActivity.this);
		downloadableAsyncTask.execute();
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
				GraphViewData[] TCHDrop_Table = new GraphViewData[totalLength];
				
				String[] horLevels = new String[totalLength];
				String[] horLevelsSet = new String[totalLength/2];
				
				try{
					for (Statistics2G statistics2G : statistics2Gs.twoGList) {
						TT_Table[itemCount] = new GraphViewData(itemCount, statistics2G.C_Traffic_Total);
						PCSSR_Table[itemCount] = new GraphViewData(itemCount, statistics2G.pCSSR);
						TCHDrop_Table[itemCount] = new GraphViewData(itemCount, statistics2G.THNDROP);
						horLevels[itemCount] = CommonTask.convertJsonDateToGraphTime(statistics2G.ReportDate);
						itemCount++;
					}
					GraphViewSeries _2G_Data_Series = new GraphViewSeries("2G", new GraphViewSeriesStyle(Color.GREEN, 3), TT_Table);
					GraphViewSeries _2GpCSSR_Data_Series = new GraphViewSeries("2G", new GraphViewSeriesStyle(Color.GREEN, 3), PCSSR_Table);
					GraphViewSeries _2GDrop_Data_Series = new GraphViewSeries("2G", new GraphViewSeriesStyle(Color.GREEN, 3), TCHDrop_Table);
					
					itemCount = 0;
					TT_Table = new GraphViewData[totalLength];
					PCSSR_Table = new GraphViewData[totalLength];
					TCHDrop_Table = new GraphViewData[totalLength];
					for(Statistics3G statistics3G : satistics3Gs.threeGList){
						TT_Table[itemCount] = new GraphViewData(itemCount, statistics3G.Traffic_Speech_Erl);
						PCSSR_Table[itemCount] = new GraphViewData(itemCount, statistics3G.pCSSR_Speech);
						TCHDrop_Table[itemCount] = new GraphViewData(itemCount, statistics3G.pDrop_Speech);
						itemCount++;
					}
					GraphViewSeries _3G_Data_Series = new GraphViewSeries("3G", new GraphViewSeriesStyle(Color.BLUE, 3), TT_Table);
					GraphViewSeries _3GpCSSR_Data_Series = new GraphViewSeries("3G", new GraphViewSeriesStyle(Color.BLUE, 3), PCSSR_Table);
					GraphViewSeries _3GDrop_Data_Series = new GraphViewSeries("3G", new GraphViewSeriesStyle(Color.BLUE, 3), TCHDrop_Table);
					
					itemCount = 0;
					TT_Table = new GraphViewData[totalLength];
					PCSSR_Table = new GraphViewData[totalLength];
					TCHDrop_Table = new GraphViewData[totalLength];
					for (StatisticsLTEKPI statisticsLTE : statisticsLTEKPIs.LTEKPIList) {
						TT_Table[itemCount] = new GraphViewData(itemCount, statisticsLTE.DLDataVolume);
						PCSSR_Table[itemCount] = new GraphViewData(itemCount, statisticsLTE. pSessionSetupSuccessRate);
						TCHDrop_Table[itemCount] = new GraphViewData(itemCount, statisticsLTE. pErabAbnormalRelease);
						horLevels[itemCount] = CommonTask.convertJsonDateToGraphTime(statisticsLTE.ReportDate);
						itemCount++;
					}
					GraphViewSeries _LTE_Data_Series = new GraphViewSeries("LTE", new GraphViewSeriesStyle(Color.RED, 3), TT_Table);
					GraphViewSeries _LTEpCSSR_Data_Series = new GraphViewSeries("LTE", new GraphViewSeriesStyle(Color.RED, 3), PCSSR_Table);
					GraphViewSeries _LTEDrop_Data_Series = new GraphViewSeries("LTE", new GraphViewSeriesStyle(Color.RED, 3), TCHDrop_Table);
					
					itemCount = 0;
					for(int i=0;i<statistics2Gs.twoGList.size()/2;i++){
						horLevelsSet[i] = horLevels[itemCount];
						itemCount += 2;
					} 
					
					LineGraphView graphView;
					
					graphView = new LineGraphView(this, "Voice Traffic");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(_2G_Data_Series);
					graphView.addSeries(_3G_Data_Series);
					graphView.addSeries(_LTE_Data_Series);
					graphView.setShowLegend(true);
					//graphView.setDrawDataPoints(true);
					//graphView.setDataPointsRadius(10f);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					graphView.setViewPort(0, 4);
					graphView.setScalable(true);
					LinearLayout layout = (LinearLayout) findViewById(R.id.cgraph1);
					layout.addView(graphView);
					
					
					graphView = new LineGraphView(this, "CSSR");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(_2GpCSSR_Data_Series);
					graphView.addSeries(_3GpCSSR_Data_Series);
					graphView.addSeries(_LTEpCSSR_Data_Series);
					graphView.setShowLegend(true);
					//graphView.setDrawDataPoints(true);
					//graphView.setDataPointsRadius(10f);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					graphView.setViewPort(0, 4);
					graphView.setScalable(true);
					layout = (LinearLayout) findViewById(R.id.cgraph2);
					layout.addView(graphView);
					
					graphView = new LineGraphView(this, "TCH Drop");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(_2GDrop_Data_Series);
					graphView.addSeries(_3GDrop_Data_Series);
					graphView.addSeries(_LTEDrop_Data_Series);
					graphView.setShowLegend(true);
					//graphView.setDrawDataPoints(true);
					//graphView.setDataPointsRadius(10f);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					graphView.setViewPort(0, 4);
					graphView.setScalable(true);
					layout = (LinearLayout) findViewById(R.id.cgraph3);
					layout.addView(graphView);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				/*
				 * For 2G Graph
				 */
				
				try{
					itemCount = 0;
					
					GraphViewData[] gvDatasTrafficTotal = new GraphViewData[statistics2Gs.twoGList.size()];
					GraphViewData[] gvDatasPCDR = new GraphViewData[statistics2Gs.twoGList.size()];
					GraphViewData[] gvDatas_pDrop_SDCCH = new GraphViewData[statistics2Gs.twoGList.size()];
					GraphViewData[] gvDatas_pCSSR = new GraphViewData[statistics2Gs.twoGList.size()];
					GraphViewData[] gvDatas_pCongestion_TCH_Perceived = new GraphViewData[statistics2Gs.twoGList.size()];
					GraphViewData[] gvDatas_pCongestion_SDCCH = new GraphViewData[statistics2Gs.twoGList.size()];
					GraphViewData[] gvDatas_pHSR_Outgoing = new GraphViewData[statistics2Gs.twoGList.size()];
					
					horLevels = new String[statistics2Gs.twoGList.size()];
					horLevelsSet = new String[statistics2Gs.twoGList.size()/2];
					
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
						itemCount++;
						
					}
					itemCount = 0;
					for(int i=0;i<statistics2Gs.twoGList.size()/2;i++){
						horLevelsSet[i] = horLevels[itemCount];
						itemCount += 2;
					}
					
					GraphView graphView = null;
					
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
					LinearLayout layout = (LinearLayout) findViewById(R.id._2graph1);
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
					layout = (LinearLayout) findViewById(R.id._2graph2);					
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
					layout = (LinearLayout) findViewById(R.id._2graph3);					
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
					layout = (LinearLayout) findViewById(R.id._2graph4);					
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
					layout = (LinearLayout) findViewById(R.id._2graph5);					
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
					layout = (LinearLayout) findViewById(R.id._2graph6);					
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
					layout = (LinearLayout) findViewById(R.id._2graph7);					
					layout.addView(graphView);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				/*
				 * For 3G Graph
				 */
				try{
					itemCount = 0;
					
					GraphViewData[] gvDatas_Traffic_Speech_Erl = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_Volume_DL_R99 = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_Volume_UL_R99 = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_Volume_HS = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_pCSSR_Speech = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_pCSSR_PS = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_pCSSR_HS = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_pRRC_Conn_CS_SR = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_pRAB_Est_Speech_SR = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_pRRC_Conn_PS_SR = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_pRAB_Est_PacketInter_SR = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_pDrop_Speech = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_pPS_R99_DCR = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_pHS_DCR = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_pSoft_HO_SR = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_pIRAT_HO_Speech_SR = new GraphViewData[satistics3Gs.threeGList.size()];
					GraphViewData[] gvDatas_pmCellDowntimeAuto = new GraphViewData[satistics3Gs.threeGList.size()];
					
					horLevels = new String[satistics3Gs.threeGList.size()];
					horLevelsSet = new String[satistics3Gs.threeGList.size()/2];
					
					for (Statistics3G statistics3G : satistics3Gs.threeGList) {
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
					for(int i=0;i<satistics3Gs.threeGList.size()/2;i++){
						horLevelsSet[i] = horLevels[itemCount];
						itemCount += 2;
					}
					
					GraphViewSeries _3GTraffic_Speech_Erl = new GraphViewSeries("TSE", new GraphViewSeriesStyle(Color.GREEN, 3),gvDatas_Traffic_Speech_Erl );
					GraphView graphView=new BarGraphView(this , "3G (Traffic Speech Erl)");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(_3GTraffic_Speech_Erl);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.MIDDLE); 
					graphView.setViewPort(0, 5);
					graphView.setScalable(true);
					//graphView.setManualYAxisBounds(6000, 2000);
					LinearLayout layout = (LinearLayout) findViewById(R.id._3graph1);				
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
					layout = (LinearLayout) findViewById(R.id._3graph2);				
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
					layout = (LinearLayout) findViewById(R.id._3graph3);				
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
					layout = (LinearLayout) findViewById(R.id._3graph4);				
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
					layout = (LinearLayout) findViewById(R.id._3graph5);				
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
					layout = (LinearLayout) findViewById(R.id._3graph6);				
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
					layout = (LinearLayout) findViewById(R.id._3graph7);				
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
					layout = (LinearLayout) findViewById(R.id._3graph8);				
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
					layout = (LinearLayout) findViewById(R.id._3graph9);				
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
					layout = (LinearLayout) findViewById(R.id._3graph10);				
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
					layout = (LinearLayout) findViewById(R.id._3graph11);				
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
					layout = (LinearLayout) findViewById(R.id._3graph12);				
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
					layout = (LinearLayout) findViewById(R.id._3graph13);				
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
					layout = (LinearLayout) findViewById(R.id._3graph14);				
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
					layout = (LinearLayout) findViewById(R.id._3graph15);				
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
					layout = (LinearLayout) findViewById(R.id._3graph16);				
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
					layout = (LinearLayout) findViewById(R.id._3graph17);				
					layout.addView(graphView);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				/*
				 * For LTE Graph
				 */
				try{
					itemCount = 0;
					GraphViewData[] gvDatas_pSessionSetupSuccessRate = new GraphViewData[statisticsLTEKPIs.LTEKPIList
							.size()];
					GraphViewData[] gvDatas_pErabEstablishSuccessRate = new GraphViewData[statisticsLTEKPIs.LTEKPIList
							.size()];
					GraphViewData[] gvDatas_pRRCSuccessRate = new GraphViewData[statisticsLTEKPIs.LTEKPIList
							.size()];
					GraphViewData[] gvDatas_pIntraFreqHO = new GraphViewData[statisticsLTEKPIs.LTEKPIList
							.size()];
					GraphViewData[] gvDatas_pErabAbnormalRelease = new GraphViewData[statisticsLTEKPIs.LTEKPIList
							.size()];
					GraphViewData[] gvDatas_ULDataVolume = new GraphViewData[statisticsLTEKPIs.LTEKPIList
							.size()];
					GraphViewData[] gvDatas_DLDataVolume = new GraphViewData[statisticsLTEKPIs.LTEKPIList
							.size()];
					GraphViewData[] gvDatas_pmCellDowntimeAuto = new GraphViewData[statisticsLTEKPIs.LTEKPIList
							.size()];

					horLevels = new String[statisticsLTEKPIs.LTEKPIList.size()];
					horLevelsSet = new String[statisticsLTEKPIs.LTEKPIList
							.size() / 2];
					
					for (StatisticsLTEKPI statisticsLTEKPI : statisticsLTEKPIs.LTEKPIList) {
						gvDatas_pSessionSetupSuccessRate[itemCount] = new GraphViewData(
								itemCount,
								statisticsLTEKPI.pSessionSetupSuccessRate);
						gvDatas_pErabEstablishSuccessRate[itemCount] = new GraphViewData(
								itemCount,
								statisticsLTEKPI.pErabEstablishSuccessRate);
						gvDatas_pRRCSuccessRate[itemCount] = new GraphViewData(
								itemCount, statisticsLTEKPI.pRRCSuccessRate);
						gvDatas_pIntraFreqHO[itemCount] = new GraphViewData(
								itemCount, statisticsLTEKPI.pIntraFreqHO);
						gvDatas_pErabAbnormalRelease[itemCount] = new GraphViewData(
								itemCount,
								statisticsLTEKPI.pErabAbnormalRelease);
						gvDatas_ULDataVolume[itemCount] = new GraphViewData(
								itemCount, statisticsLTEKPI.ULDataVolume);
						gvDatas_DLDataVolume[itemCount] = new GraphViewData(
								itemCount, statisticsLTEKPI.DLDataVolume);
						gvDatas_pmCellDowntimeAuto[itemCount] = new GraphViewData(
								itemCount, statisticsLTEKPI.pmCellDowntimeAuto);
						horLevels[itemCount] = CommonTask
								.convertJsonDateToGraphTime(statisticsLTEKPI.ReportDate);
						itemCount++;
					}
					itemCount = 0;
					for (int i = 0; i < statisticsLTEKPIs.LTEKPIList.size() / 2; i++) {
						horLevelsSet[i] = horLevels[itemCount];
						itemCount += 2;
					}
					
					GraphView graphView = null;
					
					GraphViewSeries pSessionSetupSuccessRate = new GraphViewSeries(
							"PSSSR", new GraphViewSeriesStyle(Color.GREEN,
									3), gvDatas_pSessionSetupSuccessRate);
					graphView = new BarGraphView(this,
							"LTE (PSessionSetupSuccessRate)");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(pSessionSetupSuccessRate);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					graphView.setViewPort(0, 5);
					graphView.setScalable(true);
					// graphView.setManualYAxisBounds(6000, 2000);
					LinearLayout layout = (LinearLayout) findViewById(R.id.ltegraph1);
					layout.addView(graphView);

					GraphViewSeries pErabEstablishSuccessRate = new GraphViewSeries(
							"PEESR", new GraphViewSeriesStyle(Color.GREEN,
									3), gvDatas_pErabEstablishSuccessRate);
					graphView = new BarGraphView(this,
							"LTE (PErab Establish SuccessRate)");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(pErabEstablishSuccessRate);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					graphView.setViewPort(0, 5);
					graphView.setScalable(true);
					// graphView.setManualYAxisBounds(1, 0);
					layout = (LinearLayout) findViewById(R.id.ltegraph2);
					layout.addView(graphView);

					GraphViewSeries pRRCSuccessRate = new GraphViewSeries(
							"PRRCSR", new GraphViewSeriesStyle(Color.GREEN,
									3), gvDatas_pRRCSuccessRate);
					graphView = new BarGraphView(this,
							"LTE (PRRC SuccessRate)");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(pRRCSuccessRate);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					graphView.setViewPort(0, 5);
					graphView.setScalable(true);
					// graphView.setManualYAxisBounds(1, 0);
					layout = (LinearLayout) findViewById(R.id.ltegraph3);
					layout.addView(graphView);

					GraphViewSeries pIntraFreqHO = new GraphViewSeries(
							"PIFHO", new GraphViewSeriesStyle(Color.GREEN,
									3), gvDatas_pIntraFreqHO);
					graphView = new BarGraphView(this, "LTE (PIntraFreqHO)");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(pIntraFreqHO);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					graphView.setViewPort(0, 5);
					graphView.setScalable(true);
					// graphView.setManualYAxisBounds(1, 0);
					layout = (LinearLayout) findViewById(R.id.ltegraph4);
					layout.addView(graphView);

					GraphViewSeries pErabAbnormalRelease = new GraphViewSeries(
							"PEAR",
							new GraphViewSeriesStyle(Color.GREEN, 3),
							gvDatas_pErabAbnormalRelease);
					graphView = new BarGraphView(this,
							"LTE (PErab Abnormal Release)");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(pErabAbnormalRelease);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					graphView.setViewPort(0, 5);
					graphView.setScalable(true);
					// graphView.setManualYAxisBounds(1, 0);
					layout = (LinearLayout) findViewById(R.id.ltegraph5);
					layout.addView(graphView);

					GraphViewSeries ULDataVolume = new GraphViewSeries(
							"ULDV",
							new GraphViewSeriesStyle(Color.GREEN, 3),
							gvDatas_ULDataVolume);
					graphView = new BarGraphView(this,
							"LTE (ULData Volume)");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(ULDataVolume);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					graphView.setViewPort(0, 5);
					graphView.setScalable(true);
					// graphView.setManualYAxisBounds(1, 0);
					layout = (LinearLayout) findViewById(R.id.ltegraph6);
					layout.addView(graphView);

					GraphViewSeries DLDataVolume = new GraphViewSeries(
							"DLDV",
							new GraphViewSeriesStyle(Color.GREEN, 3),
							gvDatas_DLDataVolume);
					graphView = new BarGraphView(this,
							"LTE (DLData Volume)");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(DLDataVolume);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					graphView.setViewPort(0, 5);
					graphView.setScalable(true);
					// graphView.setManualYAxisBounds(1, 0);
					layout = (LinearLayout) findViewById(R.id.ltegraph7);
					layout.addView(graphView);

					GraphViewSeries pmCellDowntimeAuto = new GraphViewSeries(
							"PMCDA", new GraphViewSeriesStyle(Color.GREEN,
									3), gvDatas_pmCellDowntimeAuto);
					graphView = new BarGraphView(this,
							"LTE (pmCell DowntimeAuto)");
					graphView.setHorizontalLabels(horLevelsSet);
					graphView.addSeries(pmCellDowntimeAuto);
					graphView.setShowLegend(true);
					graphView.setLegendAlign(LegendAlign.MIDDLE);
					graphView.setViewPort(0, 5);
					graphView.setScalable(true);
					// graphView.setManualYAxisBounds(1, 0);
					layout = (LinearLayout) findViewById(R.id.ltegraph8);
					layout.addView(graphView);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}

}
