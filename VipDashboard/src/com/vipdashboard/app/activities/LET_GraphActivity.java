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
import com.vipdashboard.app.entities.StatisticsLTEKPI;
import com.vipdashboard.app.entities.StatisticsLTEKPIs;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class LET_GraphActivity extends MainActionbarBase implements OnItemClickListener {

	/*RadioGroup radioGroup;
	RadioButton radioButton_barGraph, radioButton_lineGraph;
	ProgressBar pbGraph;
	DownloadableAsyncTask downloadableAsyncTask;

	private int PRESS_BAR_GRAPH = 0;
	private int PRESS_LINE_GRAPH = 1;
	private int SELECTED_GRAPG = PRESS_BAR_GRAPH;

	LinearLayout lineLayout, barLayout;*/
	
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
		titleText.setText("LTE Performance");
		
		ArrayList<Report> reportList = new ArrayList<Report>();
		
		reportList.add(new Report("Data Volume",1));
		reportList.add(new Report("RRC SR",2));
		reportList.add(new Report("RAB SR",3));
		reportList.add(new Report("Call Availability",4));
		
		adapter = new ReportAdapter(this, R.layout.lavel_item_layout, reportList);
		//lvNameText.setTag(adapter);
		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(this);
		
		//mSupportActionBar.setTitle("MyNet (Welcome " + CommonValues.getInstance().LoginUser.Name.toString() + ")");
		/*pbGraph = (ProgressBar) findViewById(R.id.pbGraph);

		radioGroup = (RadioGroup) findViewById(R.id.rgGroupStyle);
		radioButton_barGraph = (RadioButton) findViewById(R.id.barGraphButton);
		radioButton_lineGraph = (RadioButton) findViewById(R.id.lineGraphButton);

		barLayout = (LinearLayout) findViewById(R.id.barGraphLTEG);
		lineLayout = (LinearLayout) findViewById(R.id.lineGraphLTEG);

		runDownloadable();

		radioGroup.setOnClickListener(this);
		radioButton_barGraph.setOnClickListener(this);
		radioButton_lineGraph.setOnClickListener(this);
		runDownloadable();*/
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
			DailyKPIActivity.name = "Data Volume Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/LTE/DataVolume.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 2){
			DailyKPIActivity.name = "RRCSR Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/LTE/RRCSR.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 3){
			DailyKPIActivity.name = "RAB SR Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/LTE/RABSR.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(iD == 4){
			DailyKPIActivity.name = "Call Availability Report";
			DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/LTE/CellAvailability.aspx";
			Intent intent = new Intent(this,DailyKPIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
	}

	/*@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.barGraphButton) {
			SELECTED_GRAPG = PRESS_BAR_GRAPH;
			runDownloadable();
		} else if (id == R.id.lineGraphButton) {
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
		return statisticsReportManager.getLTEByCompanyID();
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			StatisticsLTEKPIs statisticsLTE = (StatisticsLTEKPIs) data;
			if (statisticsLTE != null && statisticsLTE.LTEKPIList.size() > 0) {
				int itemCount = 0;
				GraphViewData[] gvDatas_pSessionSetupSuccessRate = new GraphViewData[statisticsLTE.LTEKPIList
						.size()];
				GraphViewData[] gvDatas_pErabEstablishSuccessRate = new GraphViewData[statisticsLTE.LTEKPIList
						.size()];
				GraphViewData[] gvDatas_pRRCSuccessRate = new GraphViewData[statisticsLTE.LTEKPIList
						.size()];
				GraphViewData[] gvDatas_pIntraFreqHO = new GraphViewData[statisticsLTE.LTEKPIList
						.size()];
				GraphViewData[] gvDatas_pErabAbnormalRelease = new GraphViewData[statisticsLTE.LTEKPIList
						.size()];
				GraphViewData[] gvDatas_ULDataVolume = new GraphViewData[statisticsLTE.LTEKPIList
						.size()];
				GraphViewData[] gvDatas_DLDataVolume = new GraphViewData[statisticsLTE.LTEKPIList
						.size()];
				GraphViewData[] gvDatas_pmCellDowntimeAuto = new GraphViewData[statisticsLTE.LTEKPIList
						.size()];

				String[] horLevels = new String[statisticsLTE.LTEKPIList.size()];
				String[] horLevelsSet = new String[statisticsLTE.LTEKPIList
						.size() / 2];

				try {
					for (StatisticsLTEKPI statisticsLTEKPI : statisticsLTE.LTEKPIList) {
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
					for (int i = 0; i < statisticsLTE.LTEKPIList.size() / 2; i++) {
						horLevelsSet[i] = horLevels[itemCount];
						itemCount += 2;
					}
					
					GraphView graphView = null;

					if (SELECTED_GRAPG == 0) {
						lineLayout.setVisibility(LinearLayout.GONE);
						barLayout.setVisibility(LinearLayout.VISIBLE);

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
						LinearLayout layout = (LinearLayout) findViewById(R.id.graph1);
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
						layout = (LinearLayout) findViewById(R.id.graph2);
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
						layout = (LinearLayout) findViewById(R.id.graph3);
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
						layout = (LinearLayout) findViewById(R.id.graph4);
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
						layout = (LinearLayout) findViewById(R.id.graph5);
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
						layout = (LinearLayout) findViewById(R.id.graph6);
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
						layout = (LinearLayout) findViewById(R.id.graph7);
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
						layout = (LinearLayout) findViewById(R.id.graph8);
						layout.addView(graphView);

					} else {
						lineLayout.setVisibility(LinearLayout.VISIBLE);
						barLayout.setVisibility(LinearLayout.GONE);

						GraphViewSeries pSessionSetupSuccessRate = new GraphViewSeries(
								"PSSSR", new GraphViewSeriesStyle(Color.GREEN,
										3), gvDatas_pSessionSetupSuccessRate);
						graphView = new LineGraphView(this,
								"LTE (PSessionSetupSuccessRate)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pSessionSetupSuccessRate);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						// graphView.setManualYAxisBounds(6000, 2000);
						LinearLayout layout = (LinearLayout) findViewById(R.id.lgraph1);
						layout.addView(graphView);

						GraphViewSeries pErabEstablishSuccessRate = new GraphViewSeries(
								"PEESR", new GraphViewSeriesStyle(Color.GREEN,
										3), gvDatas_pErabEstablishSuccessRate);
						graphView = new LineGraphView(this,
								"LTE (PErab Establish SuccessRate)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pErabEstablishSuccessRate);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						// graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.lgraph2);
						layout.addView(graphView);

						GraphViewSeries pRRCSuccessRate = new GraphViewSeries(
								"PRRCSR", new GraphViewSeriesStyle(Color.GREEN,
										3), gvDatas_pRRCSuccessRate);
						graphView = new LineGraphView(this,
								"LTE (PRRC SuccessRate)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pRRCSuccessRate);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						// graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.lgraph3);
						layout.addView(graphView);

						GraphViewSeries pIntraFreqHO = new GraphViewSeries(
								"PIFHO", new GraphViewSeriesStyle(Color.GREEN,
										3), gvDatas_pIntraFreqHO);
						graphView = new LineGraphView(this, "LTE (PIntraFreqHO)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pIntraFreqHO);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						// graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.lgraph4);
						layout.addView(graphView);

						GraphViewSeries pErabAbnormalRelease = new GraphViewSeries(
								"PEAR",
								new GraphViewSeriesStyle(Color.GREEN, 3),
								gvDatas_pErabAbnormalRelease);
						graphView = new LineGraphView(this,
								"LTE (PErab Abnormal Release)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pErabAbnormalRelease);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						// graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.lgraph5);
						layout.addView(graphView);

						GraphViewSeries ULDataVolume = new GraphViewSeries(
								"ULDV",
								new GraphViewSeriesStyle(Color.GREEN, 3),
								gvDatas_ULDataVolume);
						graphView = new LineGraphView(this,
								"LTE (ULData Volume)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(ULDataVolume);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						// graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.lgraph6);
						layout.addView(graphView);

						GraphViewSeries DLDataVolume = new GraphViewSeries(
								"DLDV",
								new GraphViewSeriesStyle(Color.GREEN, 3),
								gvDatas_DLDataVolume);
						graphView = new LineGraphView(this,
								"LTE (DLData Volume)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(DLDataVolume);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						// graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.lgraph7);
						layout.addView(graphView);

						GraphViewSeries pmCellDowntimeAuto = new GraphViewSeries(
								"PMCDA", new GraphViewSeriesStyle(Color.GREEN,
										3), gvDatas_pmCellDowntimeAuto);
						graphView = new LineGraphView(this,
								"LTE (pmCell DowntimeAuto)");
						graphView.setHorizontalLabels(horLevelsSet);
						graphView.addSeries(pmCellDowntimeAuto);
						graphView.setShowLegend(true);
						graphView.setLegendAlign(LegendAlign.MIDDLE);
						graphView.setViewPort(0, 5);
						graphView.setScalable(true);
						// graphView.setManualYAxisBounds(1, 0);
						layout = (LinearLayout) findViewById(R.id.lgraph8);
						layout.addView(graphView);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}*/

}
