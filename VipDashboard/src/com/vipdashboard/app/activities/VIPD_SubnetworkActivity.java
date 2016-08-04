package com.vipdashboard.app.activities;

import java.net.URLEncoder;

import com.vipdashboard.app.R;
import com.vipdashboard.app.R.color;
import com.vipdashboard.app.R.drawable;
import com.vipdashboard.app.R.id;
import com.vipdashboard.app.R.layout;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.VIPDPMKPIHourlyDatas;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;
import com.vipdashboard.app.utils.MasterDataConstants;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class VIPD_SubnetworkActivity extends Activity implements OnClickListener, IAsynchronousTask {
	
	TextView tv2G,tv3G,tvLTE;
	DownloadableAsyncTask downloadableAsyncTask;
	
	int overviewID=0;
	String overviewTagID=null;
	TextView[] myTextViews;
	
	LinearLayout  llShowingTitle;
	LinearLayout linearLayout_gv,linearLayout,llShowingGaugeView;
	 TextView TitleView;
	 TextView TitleDate;
	
	int totalRowCount=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vipd__subnetwork);
		
		Initialization();
		
		
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
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) {
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
	}
	
	
	private void BuildTitle(int id) {
		
		 if(totalRowCount>0)
		    {
			 
			 if(((LinearLayout) llShowingTitle).getChildCount() > 0) 
				    ((LinearLayout) llShowingTitle).removeAllViews(); 
			 TitleView.setBackground(getResources().getDrawable(R.color.bottom_text_color));
			// "@color/bottom_text_color"
			 for(int i=0;i<totalRowCount;i++){
				 myTextViews[i].setBackgroundResource(Color.TRANSPARENT);
			 }
			 
			 
		    	switch(id){
		    	case 0:
		    		TitleView.setText("2G");
		    		TitleView.setTextSize(28);
		    		TitleView.setGravity(Gravity.CENTER);
		    		myTextViews[0].setBackground(getResources().getDrawable(R.drawable.tab_background_selected));
		    		llShowingTitle.addView(TitleView);
		    		TitleDate.setText("Update as of "+CommonTask.getCurrentDateTimeAsString());
		    		TitleDate.setTextSize(14);
		    		llShowingTitle.addView(TitleDate);
		    		break;
		    		
		    	case 1:
		    		TitleView.setText("3G");
		    		TitleView.setTextSize(28);
		    		TitleView.setGravity(Gravity.CENTER);
		    		myTextViews[1].setBackground(getResources().getDrawable(R.drawable.tab_background_selected));
		    		
		    		llShowingTitle.addView(TitleView);
		    		
		    		TitleDate.setText("Update as of "+CommonTask.getCurrentDateTimeAsString());
		    		TitleDate.setTextSize(14);
		    		llShowingTitle.addView(TitleDate);
		    		break;
		    		
		    		
		    	case 2:
		    		TitleView.setText("LTE");
		    		TitleView.setTextSize(28);
		    		TitleView.setGravity(Gravity.CENTER);
		    		myTextViews[2].setBackground(getResources().getDrawable(R.drawable.tab_background_selected));
		    		llShowingTitle.addView(TitleView);
		    		
		    		TitleDate.setText("Update as of "+CommonTask.getCurrentDateTimeAsString());
		    		TitleDate.setTextSize(14);
		    		llShowingTitle.addView(TitleDate);
		    		break;
		    	}
		    }
		 
		 LoadComponent();
	}
	

	
	private void LoadComponent() {
		downloadableAsyncTask = new DownloadableAsyncTask(VIPD_SubnetworkActivity.this);
		downloadableAsyncTask.execute();
	}
	@SuppressLint("ResourceAsColor")
	private void Initialization() {
		
		llShowingTitle =  (LinearLayout) findViewById(R.id.llShowingTitle);
		TitleView = new TextView(VIPD_SubnetworkActivity.this);
		TitleDate = new TextView(VIPD_SubnetworkActivity.this);
		
		llShowingGaugeView=  (LinearLayout) findViewById(R.id.llShowingGaugeView);
	    linearLayout =  (LinearLayout) findViewById(R.id.llShowingHeader);
		//linearLayout_gv =  (LinearLayout) findViewById(R.id.llShowingGaugeView);
		final int N = 3;
		
		totalRowCount=N;

		 myTextViews = new TextView[N];

		for (int i = 0; i < N; i++) {
		    final TextView rowTextView = new TextView(VIPD_SubnetworkActivity.this);
		    rowTextView.setId(i);
		    rowTextView.setTextSize(16);
		    if(i==0)
		    {
		    	/*rowTextView.setTypeface(Typeface.DEFAULT_BOLD);
		    	rowTextView.setTextColor(getResources().getColor(R.color.bottom_text_color));*/
		    rowTextView.setBackground(getResources().getDrawable(R.drawable.tab_background_selected));
		    }
		    rowTextView.setLayoutParams(new LinearLayout.LayoutParams(80, 80, 1f));
		    rowTextView.setText("This is row #" + i);
		    rowTextView.setOnClickListener(this);
		    linearLayout.addView(rowTextView);
		    myTextViews[i] = rowTextView;
		}
		
		BuildTitle(0);
	}
	@Override
	public void showProgressLoader() {
		// TODO Auto-generated method stub
	}


	@Override
	public void hideProgressLoader() {
		// TODO Auto-generated method stub
	}


	@Override
	public Object doBackgroundPorcess() {
		
		Object[] LineChartValue = new Object[200];
		
		int NumberOfChart=2;
		
		for(int i=0 ; i<NumberOfChart;i++){
			LineChartValue[i]=i;
		}
		
		
		
		String urlRqs3DPie = String.format(
				CommonURL.getInstance().GoogleChartServiceUsageNew,
				/*URLEncoder.encode("Calls received("+String.valueOf(incommingCallCount+missedSMSCount)+")", CommonConstraints.EncodingCode),
				URLEncoder.encode("Calls made("+String.valueOf(outgoingCallCount)+")", CommonConstraints.EncodingCode),
				URLEncoder.encode("Calls dropped(0)", CommonConstraints.EncodingCode),
				URLEncoder.encode("Calls setup failure(0)", CommonConstraints.EncodingCode),
				URLEncoder.encode("Messages("+String.valueOf(totalSMSCount)+")", CommonConstraints.EncodingCode),
				URLEncoder.encode("Data Connections("+String.valueOf(dataCount)+")", CommonConstraints.EncodingCode),						
				URLEncoder.encode("Active apps("+String.valueOf(am.getRunningAppProcesses().size())+")", CommonConstraints.EncodingCode),
				URLEncoder.encode("WIFI("+String.valueOf(wify)+")", CommonConstraints.EncodingCode),*/
				
				20,30,40,50,60,70,80,10,
				20,30,40,50,60,70,80,10,
				20,30,40,50,60,70,80,10
				);

		return JSONfunctions.LoadChart(urlRqs3DPie);
		
		// TODO Auto-generated method stub
		
		/*IStatisticsReportManager manager=new StatisticsReportManager();
		Object datax = manager.getPMKPIHourlyData(Integer.parseInt(overviewTagID));	
			
		String url=CommonURL.getInstance().GoogleLineChart;
		//String url=null;
		
		url=url+"chxl=0:|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24";
		
				url=url+"&chd=t:20,23,45,67,90|45,33,55,65,10";
		
		url="chxl=0:|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24";
		
		url=url+"&chd=t:20,23,45,67,90|45,33,55,65,10";
		
		//VIPDPMKPIHourlyDatas pMKPIHourlyDatas=(VIPDPMKPIHourlyDatas)datax;
		
		String processedString="";
		
		if(pMKPIHourlyDatas!=null && pMKPIHourlyDatas.VIPDPMKPIHourlyDatas.size()>0)
		{	
			for(int i=0;i<pMKPIHourlyDatas.VIPDPMKPIHourlyDatas.size();i++){
				processedString+=(int) Math.round(pMKPIHourlyDatas.VIPDPMKPIHourlyDatas.get(i).KPIValue)+",";
				
				if(i==15)
				{
					processedString = processedString.substring(0,processedString.length()-1);
					processedString+="|";
				}
			}
		}
		
		//processedString = processedString.substring(0,processedString.length()-1);
			
		//url=url+"&chd=t:"+processedString;
		
		return JSONfunctions.LoadChart(url);*/
	}


	@Override
	public void processDataAfterDownload(Object data) {
		if(((LinearLayout) llShowingGaugeView).getChildCount() > 0) 
		    ((LinearLayout) llShowingGaugeView).removeAllViews(); 
		
		if(data!=null)
		{	
			ImageView ivChartView=new ImageView(this);
			ivChartView.setId(56);
			ivChartView.setImageBitmap((Bitmap)data);
			llShowingGaugeView.addView(ivChartView);
		}	
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) {
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
		
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId()==0){
		BuildTitle(v.getId());	
		//DrawLineChart(v.getId());
		
		//BuildGauseView(v.getId());
	    }
		
		else if (v.getId()==1){
			BuildTitle(v.getId());
			//DrawLineChart(v.getId());
			//BuildGauseView(v.getId());
		    }
		
		else if (v.getId()==2) {
			BuildTitle(v.getId());	
			//DrawLineChart(v.getId());
			//BuildGauseView(v.getId());
			}
	}


	/*private Object DrawLineChart(int id) {
		IStatisticsReportManager manager=new StatisticsReportManager();
		Object datax = manager.getPMKPIHourlyData(Integer.parseInt(overviewTagID));	
			
		String url=CommonURL.getInstance().GoogleLineChart;
		String url=null;
		
		url=url+"chxl=0:|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24";
		
				url=url+"&chd=t:20,23,45,67,90|45,33,55,65,10";
		
		url="chxl=0:|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24";
		
		url=url+"&chd=t:20,23,45,67,90|45,33,55,65,10";
		
		//VIPDPMKPIHourlyDatas pMKPIHourlyDatas=(VIPDPMKPIHourlyDatas)datax;
		
		String processedString="";
		
		if(pMKPIHourlyDatas!=null && pMKPIHourlyDatas.VIPDPMKPIHourlyDatas.size()>0)
		{	
			for(int i=0;i<pMKPIHourlyDatas.VIPDPMKPIHourlyDatas.size();i++){
				processedString+=(int) Math.round(pMKPIHourlyDatas.VIPDPMKPIHourlyDatas.get(i).KPIValue)+",";
				
				if(i==15)
				{
					processedString = processedString.substring(0,processedString.length()-1);
					processedString+="|";
				}
			}
		}
		
		//processedString = processedString.substring(0,processedString.length()-1);
			
		//url=url+"&chd=t:"+processedString;
		
		return JSONfunctions.LoadChart(url);
	}*/
	
	
	/*public Object doBackgroundPorcess() {
		IStatisticsReportManager manager=new StatisticsReportManager();
		Object datax = manager.getPMKPIHourlyData(Integer.parseInt(overviewTagID));	
			
		String url=CommonURL.getInstance().GoogleLineChart;
		//url=url+"chxl=0:|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|21|22|23|24";
		
		//url=url+"&chd=t:20,23,45,67,90|45,33,55,65,10";
		
		VIPDPMKPIHourlyDatas pMKPIHourlyDatas=(VIPDPMKPIHourlyDatas)datax;
		
		String processedString="";
		
		if(pMKPIHourlyDatas!=null && pMKPIHourlyDatas.VIPDPMKPIHourlyDatas.size()>0)
		{	
			for(int i=0;i<pMKPIHourlyDatas.VIPDPMKPIHourlyDatas.size();i++){
				processedString+=(int) Math.round(pMKPIHourlyDatas.VIPDPMKPIHourlyDatas.get(i).KPIValue)+",";
				
				if(i==15)
				{
					processedString = processedString.substring(0,processedString.length()-1);
					processedString+="|";
				}
			}
		}
		
		processedString = processedString.substring(0,processedString.length()-1);
			
		url=url+"&chd=t:"+processedString;
		
		return JSONfunctions.LoadChart(url);
	}
	*/
	
	/*@Override
	public void processDataAfterDownload(Object data) {
		if(data!=null)
		{		
			
		}	
		ivChartView.setImageBitmap((Bitmap)data);
		}	
	*/


	private void BuildGauseView(int id) {
		
		if(((LinearLayout) linearLayout_gv).getChildCount() > 0) 
		    ((LinearLayout) linearLayout_gv).removeAllViews(); 
		
		 //LinearLayout linearLayout_gv =  (LinearLayout) findViewById(R.id.llShowingGaugeView);
		com.vipdashboard.app.customcontrols.GaugeView GaugeView = new 
       		 com.vipdashboard.app.customcontrols.GaugeView(this);
       		 GaugeView.setId(7);
       		 GaugeView.setTargetValue( 90);
       		 GaugeView.setLayoutParams(new LayoutParams(150,150));
       		linearLayout_gv.addView(GaugeView, linearLayout_gv.getChildCount()-1);
       		 GaugeView.setOnClickListener( this);
	}

}
