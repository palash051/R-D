package com.khareeflive.app.activities;

import java.util.ArrayList;

import com.khareeflive.app.R;
import com.khareeflive.app.asynchronoustask.NetworkPerformanceTask;
import com.khareeflive.app.asynchronoustask.NetworkUpdateTask;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.entities.CSSR;
import com.khareeflive.app.entities.DROPData;
import com.khareeflive.app.entities.DataVolume;
import com.khareeflive.app.entities.TrafficReport;
import com.khareeflive.app.manager.LatestUpdateManager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NetworkPerformanceActivity extends Activity{

	private TextView networktrafic,networkdatavolume,networkcssrdata,networkdropdata;
	private NetworkPerformanceTask networkperformancetask;
	ArrayList<TrafficReport> trafficReportList;
	ArrayList<DataVolume> dataVolumeList;
	ArrayList<CSSR> cssrList;
	ArrayList<DROPData> dropDataList;
	ProgressBar pbNetworkperformance;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.networkperformance);	
		initializeControl();
		
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
	
	private void initializeControl()
	{
		networktrafic=(TextView) findViewById(R.id.ednetworktrafic);
		networkdatavolume=(TextView) findViewById(R.id.ednetworkdatavolume);
		networkcssrdata=(TextView) findViewById(R.id.ednetworkcssrdata);
		networkdropdata=(TextView) findViewById(R.id.ednetworkdropdata);
		pbNetworkperformance=(ProgressBar)findViewById(R.id.pbnetperformance);
		if (networkperformancetask != null) {
			networkperformancetask.cancel(true);
		}			
		networkperformancetask = new NetworkPerformanceTask(this);
		networkperformancetask.execute();
		
	}
	
	public Boolean GetData()
	{
		try
		{
			trafficReportList=LatestUpdateManager.GetTraffic();
			dataVolumeList=LatestUpdateManager.GetDataVolume();
			cssrList=LatestUpdateManager.GetCSSRData();
			dropDataList=LatestUpdateManager.GetDROPData();
			return true;
			
		}
		catch (Exception e)
		{
			return false;
		}
	}	
	public void ShowData()
	{
		StringBuilder sb=new StringBuilder();
		for (int i = 0; i < trafficReportList.size(); i++) {
			sb.append("Report Time: " +
					trafficReportList.get(i).reportTime + 
					"\r\n" +
					"Traffic 2G: " + 
					trafficReportList.get(i).traffic2G + 
					"\r\n" +
					"Traffic 3G: " + 
					trafficReportList.get(i).traffic3G
					+"\r\n\r\n");
		}
		
		networktrafic.setText(sb.toString());
		
		sb=new StringBuilder();
		for (int i = 0; i < dataVolumeList.size(); i++) {
			sb.append("Report Time: " +
					dataVolumeList.get(i).reportTime + 
					"\r\n" +
					"LTEDLDV: " + 
					dataVolumeList.get(i).LTEDLDV + 
					"\r\n" +
					"DLDV3G: " + 
					dataVolumeList.get(i).DLDV3G +
					"\r\n" +
					"pDrops_TCH_BQ_DL: " + 
					dataVolumeList.get(i).pDrops_TCH_BQ_DL
					+"\r\n\r\n");
		}
		
		networkdatavolume.setText(sb.toString());
		
		sb=new StringBuilder();
		for (int i = 0; i < cssrList.size(); i++) {
			sb.append("Report Time: " +
					cssrList.get(i).reportTime + 
					"\r\n" +
					"CSSR2G: " + 
					cssrList.get(i).CSSR2G + 
					"\r\n" +
					"CSSR3G: " + 
					cssrList.get(i).CSSR3G +
					"\r\n" +
					"CSSR3GHS: " + 
					cssrList.get(i).CSSR3GHS +
					"\r\n" +
					"LTESSSR: " + 
					cssrList.get(i).LTESSSR
					+"\r\n\r\n");
		}
		
		networkcssrdata.setText(sb.toString());
		
		sb=new StringBuilder();
		for (int i = 0; i < dropDataList.size(); i++) {
			sb.append("Report Time: " +
					dropDataList.get(i).reportTime + 
					"\r\n" +
					"SDCCH2G: " + 
					dropDataList.get(i).SDCCH2G + 
					"\r\n" +
					"TCHDROP2G: " + 
					dropDataList.get(i).TCHDROP2G +
					"\r\n" +
					"Drop_Speech_3G: " + 
					dropDataList.get(i).Drop_Speech_3G +
					"\r\n" +
					"HSDrop_3G: " + 
					dropDataList.get(i).HSDrop_3G
					+"\r\n\r\n");
		}
		
		networkdropdata.setText(sb.toString());
		
	}
	
	public void showLoader(){
		pbNetworkperformance.setVisibility(View.VISIBLE);
	}
	public void hideLoader(){
		pbNetworkperformance.setVisibility(View.GONE);
	}
}
