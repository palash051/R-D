package com.khareeflive.app.activities;

import java.util.ArrayList;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.khareeflive.app.R;
import com.khareeflive.app.asynchronoustask.DownloadableTask;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.entities.CSSR;
import com.khareeflive.app.entities.DROPData;
import com.khareeflive.app.entities.DataVolume;
import com.khareeflive.app.entities.IDownloadProcessorActicity;
import com.khareeflive.app.entities.TrafficReport;
import com.khareeflive.app.manager.LatestUpdateManager;

public class GraphActivity extends Activity implements
		IDownloadProcessorActicity {
	RelativeLayout rlTrafficReport, rlDROPData, rlCSSRData, rlDataVolume;
	private TextView networktrafic, networkdatavolume, networkcssrdata,
			networkdropdata;
	ProgressBar pbGraph;
	ArrayList<TrafficReport> trafficReportList;
	ArrayList<DataVolume> dataVolumeList;
	ArrayList<CSSR> cssrList;
	ArrayList<DROPData> dropDataList;
	DownloadableTask downloadableTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph);
		initialization();
	}

	private void initialization() {
		rlTrafficReport = (RelativeLayout) findViewById(R.id.rlTrafficReport);
		rlDROPData = (RelativeLayout) findViewById(R.id.rlDROPData);
		rlCSSRData = (RelativeLayout) findViewById(R.id.rlCSSRData);
		rlDataVolume = (RelativeLayout) findViewById(R.id.rlDataVolume);

		networktrafic = (TextView) findViewById(R.id.ednetworktrafic);
		networkdatavolume = (TextView) findViewById(R.id.ednetworkdatavolume);
		networkcssrdata = (TextView) findViewById(R.id.ednetworkcssrdata);
		networkdropdata = (TextView) findViewById(R.id.ednetworkdropdata);

		pbGraph = (ProgressBar) findViewById(R.id.pbGraph);
		if (downloadableTask != null) {
			downloadableTask.cancel(true);
		}
		downloadableTask = new DownloadableTask(this);
		downloadableTask.execute();
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
	public Object doBackgroundDownloadPorcess() {

		trafficReportList = LatestUpdateManager.GetTraffic();
		dataVolumeList = LatestUpdateManager.GetDataVolume();
		cssrList = LatestUpdateManager.GetCSSRData();
		dropDataList = LatestUpdateManager.GetDROPData();
		return true;
	}

	@Override
	public void processDownloadedData(Object data) {

		if ((Boolean) data) {
			designTrafficReport();
			designCssrReport();
			designDropReport();
			designDataVolumeReport();
			//ShowDataNetworkperformance();
		}
	}

	private void designTrafficReport() {
		if (trafficReportList != null) {
			GraphViewData[] gvd2G = new GraphViewData[trafficReportList.size()];
			GraphViewData[] gvd3G = new GraphViewData[trafficReportList.size()];
			GraphViewData gdata = null;			
			int count = 0;
			for (TrafficReport trafficReport : trafficReportList) {				
				gdata = new GraphViewData(count, trafficReport.traffic2G);				
				gvd2G[count] = gdata;
				gdata = new GraphViewData(count, trafficReport.traffic3G);				
				gvd3G[count] = gdata;				
				++count;
			}	
			GraphView graphView = new LineGraphView(this,"Traffic Report (2G & 3G)");
			
			GraphViewSeriesStyle style=new GraphViewSeriesStyle(Color.GREEN, 3);			
			GraphViewSeries graphViewSeries = new GraphViewSeries("2G",style,gvd2G);
			graphView.addSeries(graphViewSeries);
			
			style=new GraphViewSeriesStyle(Color.RED, 3);			
			graphViewSeries = new GraphViewSeries("3G",style,gvd3G);
			graphView.addSeries(graphViewSeries);			
			
			graphView.setShowLegend(true);
		    graphView.setLegendAlign(LegendAlign.TOP);
		    graphView.setGraphViewStyle(new GraphViewStyle(Color.BLACK, Color.BLACK, Color.LTGRAY));		    
		    graphView.setViewPort(0, 4);
		    graphView.redrawAll();
			rlTrafficReport.addView(graphView);
			
			
		}
	}

	private void designCssrReport() {
		if (cssrList != null) {
			GraphViewData[] gvd2G = new GraphViewData[cssrList.size()];
			GraphViewData[] gvd3G = new GraphViewData[cssrList.size()];
			GraphViewData[] gvd3GHs = new GraphViewData[cssrList.size()];
			GraphViewData[] gvdLTESSSR = new GraphViewData[cssrList.size()];

			GraphViewData gdata = null;
			int count = 0;

			for (CSSR cSSR : cssrList) {
				gdata = new GraphViewData(count, cSSR.CSSR2G);
				gvd2G[count] = gdata;
				
				gdata = new GraphViewData(count, cSSR.CSSR3G);
				gvd3G[count] = gdata;
				
				gdata = new GraphViewData(count, cSSR.CSSR3GHS);
				gvd3GHs[count] = gdata;
				
				gdata = new GraphViewData(count, cSSR.LTESSSR);
				gvdLTESSSR[count] = gdata;
				++count;				
			}
			
			GraphView graphView = new LineGraphView(this,"CSSR Report (2G,3G & LTE)");
			
			GraphViewSeriesStyle style=new GraphViewSeriesStyle(Color.GREEN, 3);			
			GraphViewSeries graphViewSeries = new GraphViewSeries("2G",style,gvd2G);
			graphView.addSeries(graphViewSeries);
			
			style=new GraphViewSeriesStyle(Color.RED, 3);			
			graphViewSeries = new GraphViewSeries("3G",style,gvd3G);
			graphView.addSeries(graphViewSeries);

			style=new GraphViewSeriesStyle(Color.BLUE, 3);			
			graphViewSeries = new GraphViewSeries("3GHS",style,gvd3GHs);
			graphView.addSeries(graphViewSeries);
			
			style=new GraphViewSeriesStyle(Color.MAGENTA, 3);			
			graphViewSeries = new GraphViewSeries("LTESSSR",style,gvdLTESSSR);
			graphView.addSeries(graphViewSeries);
			
			
			graphView.setShowLegend(true);
		    graphView.setLegendAlign(LegendAlign.TOP);
		    graphView.setGraphViewStyle(new GraphViewStyle(Color.BLACK, Color.BLACK, Color.LTGRAY));		    
		    graphView.setViewPort(0, 2);
		    graphView.redrawAll();
		    
			rlCSSRData.addView(graphView);
		}
	}

	private void designDropReport() {
		if (dropDataList != null) {
			GraphViewData[] gvdSDCCH2G = new GraphViewData[dropDataList.size()];
			GraphViewData[] gvdTCHDROP2G = new GraphViewData[dropDataList.size()];
			GraphViewData[] gvdDrop_Speech_3G = new GraphViewData[dropDataList.size()];
			GraphViewData[] gvdHSDrop_3G = new GraphViewData[dropDataList.size()];

			GraphViewData gdata = null;
			int count = 0;

			for (DROPData dROPData : dropDataList) {
				gdata = new GraphViewData(count, dROPData.SDCCH2G);
				gvdSDCCH2G[count] = gdata;
				
				gdata = new GraphViewData(count, dROPData.TCHDROP2G);
				gvdTCHDROP2G[count] = gdata;
				
				gdata = new GraphViewData(count, dROPData.Drop_Speech_3G);
				gvdDrop_Speech_3G[count] = gdata;
				
				gdata = new GraphViewData(count, dROPData.HSDrop_3G);
				gvdHSDrop_3G[count] = gdata;
				++count;
				
			}			
			
			GraphView graphView = new LineGraphView(this,"Drop Report (2G & 3G)");
			
			GraphViewSeriesStyle style=new GraphViewSeriesStyle(Color.GREEN, 3);			
			GraphViewSeries graphViewSeries = new GraphViewSeries("SDCCH2G",style,gvdSDCCH2G);
			graphView.addSeries(graphViewSeries);
			
			style=new GraphViewSeriesStyle(Color.RED, 3);			
			graphViewSeries = new GraphViewSeries("TCHDROP2G",style,gvdTCHDROP2G);
			graphView.addSeries(graphViewSeries);

			style=new GraphViewSeriesStyle(Color.BLUE, 3);			
			graphViewSeries = new GraphViewSeries("DropSpeech3G",style,gvdDrop_Speech_3G);
			graphView.addSeries(graphViewSeries);
			
			style=new GraphViewSeriesStyle(Color.MAGENTA, 3);			
			graphViewSeries = new GraphViewSeries("HSDrop3G",style,gvdHSDrop_3G);
			graphView.addSeries(graphViewSeries);
			
			
			graphView.setShowLegend(true);
		    graphView.setLegendAlign(LegendAlign.TOP);
		    graphView.setGraphViewStyle(new GraphViewStyle(Color.BLACK, Color.BLACK, Color.LTGRAY));		    
		    graphView.setViewPort(0, 2);
		    graphView.redrawAll();
			
			rlDROPData.addView(graphView);
		}
	}

	private void designDataVolumeReport() {
		if (dataVolumeList != null) {
			GraphViewData[] gvdLTEDLDV = new GraphViewData[dataVolumeList.size()];
			GraphViewData[] gvdDLDV3G = new GraphViewData[dataVolumeList.size()];
			GraphViewData[] gvdpDrops_TCH_BQ_DL = new GraphViewData[dataVolumeList.size()];
			

			GraphViewData gdata = null;
			int count = 0;

			for (DataVolume dataVolume : dataVolumeList) {
				gdata = new GraphViewData(count, dataVolume.LTEDLDV);
				gvdLTEDLDV[count] = gdata;
				
				gdata = new GraphViewData(count, dataVolume.DLDV3G);
				gvdDLDV3G[count] = gdata;
				
				gdata = new GraphViewData(count, dataVolume.pDrops_TCH_BQ_DL);
				gvdpDrops_TCH_BQ_DL[count] = gdata;
				
				++count;
				
			}
			
			GraphView graphView = new LineGraphView(this, "Data Volume");
			
			GraphViewSeriesStyle style=new GraphViewSeriesStyle(Color.GREEN, 3);			
			GraphViewSeries graphViewSeries = new GraphViewSeries("LTEDLDV",style,gvdLTEDLDV);
			graphView.addSeries(graphViewSeries);
			
			style=new GraphViewSeriesStyle(Color.RED, 3);			
			graphViewSeries = new GraphViewSeries("DLDV3G",style,gvdDLDV3G);
			graphView.addSeries(graphViewSeries);

			style=new GraphViewSeriesStyle(Color.BLUE, 3);			
			graphViewSeries = new GraphViewSeries("DropsTCHBQDL",style,gvdpDrops_TCH_BQ_DL);
			graphView.addSeries(graphViewSeries);
			
			graphView.setShowLegend(true);
		    graphView.setLegendAlign(LegendAlign.TOP);
		    graphView.setGraphViewStyle(new GraphViewStyle(Color.BLACK, Color.BLACK, Color.LTGRAY));		    
		    graphView.setViewPort(0, 2);
		    graphView.redrawAll();
		    
			rlDataVolume.addView(graphView);
		}
	}

	public void ShowDataNetworkperformance() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < trafficReportList.size(); i++) {
			sb.append("Report Time: " + trafficReportList.get(i).reportTime
					+ "\r\n" + "Traffic 2G: "
					+ trafficReportList.get(i).traffic2G + "\r\n"
					+ "Traffic 3G: " + trafficReportList.get(i).traffic3G
					+ "\r\n\r\n");
		}

		networktrafic.setText(sb.toString());

		sb = new StringBuilder();
		for (int i = 0; i < dataVolumeList.size(); i++) {
			sb.append("Report Time: " + dataVolumeList.get(i).reportTime
					+ "\r\n" + "LTEDLDV: " + dataVolumeList.get(i).LTEDLDV
					+ "\r\n" + "DLDV3G: " + dataVolumeList.get(i).DLDV3G
					+ "\r\n" + "pDrops_TCH_BQ_DL: "
					+ dataVolumeList.get(i).pDrops_TCH_BQ_DL + "\r\n\r\n");
		}

		networkdatavolume.setText(sb.toString());

		sb = new StringBuilder();
		for (int i = 0; i < cssrList.size(); i++) {
			sb.append("Report Time: " + cssrList.get(i).reportTime + "\r\n"
					+ "CSSR2G: " + cssrList.get(i).CSSR2G + "\r\n" + "CSSR3G: "
					+ cssrList.get(i).CSSR3G + "\r\n" + "CSSR3GHS: "
					+ cssrList.get(i).CSSR3GHS + "\r\n" + "LTESSSR: "
					+ cssrList.get(i).LTESSSR + "\r\n\r\n");
		}

		networkcssrdata.setText(sb.toString());

		sb = new StringBuilder();
		for (int i = 0; i < dropDataList.size(); i++) {
			sb.append("Report Time: " + dropDataList.get(i).reportTime + "\r\n"
					+ "SDCCH2G: " + dropDataList.get(i).SDCCH2G + "\r\n"
					+ "TCHDROP2G: " + dropDataList.get(i).TCHDROP2G + "\r\n"
					+ "Drop_Speech_3G: " + dropDataList.get(i).Drop_Speech_3G
					+ "\r\n" + "HSDrop_3G: " + dropDataList.get(i).HSDrop_3G
					+ "\r\n\r\n");
		}

		networkdropdata.setText(sb.toString());

	}
	
	@Override
	protected void onPause() {
		KhareefLiveApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		KhareefLiveApplication.activityResumed();
		super.onResume();
	}

}
