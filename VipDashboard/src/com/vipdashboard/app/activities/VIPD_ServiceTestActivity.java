package com.vipdashboard.app.activities;

import android.content.Context;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.BrowserDataInfoAsycntask;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.asynchronoustask.ServiceTestAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.ServiceTestsRoot;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IPhoneInformationService;
import com.vipdashboard.app.manager.PhoneInformationManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_ServiceTestActivity extends MainActionbarBase implements OnClickListener,IAsynchronousTask{
	
	TextView tvSpeedTestButton,tvServiceTestButton,tvFacebookDelayValue,tvFacebookDownloadSpeedValue,
	tvYoutubeDelayValue,tvYoutubeDownloadSpeedValue,tvBingDelayValue,
	tvBingDownloadSpeedValue,tvTwitterDelayValue,tvTwitterDownloadSpeedValue,tvYahooDelayValue, 
	tvYahooDownloadSpeedValue,tvWikipediaDelayValue,tvWikipediaDownloadSpeedValue,tvDownloadavgspeedValue,tvWIFIDataValue,tvServiceTestFacebook,
	tvServiceTestYoutube,tvServiceTestBing,tvServiceTestTwitter,tvServiceTestYahoo,tvServiceTestWiki;
	public static ImageView ivSpeedTestButtonStop, ivSpeedTestButtonStart;
	public static TextView tvTest;
	public static String strSendData;
	public static  int dataCounter=0;
	public static int count = 0;
	DownloadableAsyncTask downloadableAsyncTask;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.vipd_servicetest);
		
		tvServiceTestButton=(TextView)findViewById(R.id.tvServiceTestButton);
		tvSpeedTestButton=(TextView)findViewById(R.id.tvSpeedTestButton);	
		
		tvFacebookDownloadSpeedValue=(TextView)findViewById(R.id.tvFacebookDownloadSpeedValue);
		tvFacebookDelayValue=(TextView)findViewById(R.id.tvFacebookDelayValue);
		tvYoutubeDelayValue=(TextView)findViewById(R.id.tvYoutubeDelayValue);
		tvYoutubeDownloadSpeedValue=(TextView)findViewById(R.id.tvYoutubeDownloadSpeedValue);
		tvBingDelayValue=(TextView)findViewById(R.id.tvBingDelayValue);
		tvBingDownloadSpeedValue=(TextView)findViewById(R.id.tvBingDownloadSpeedValue);
		tvTwitterDelayValue=(TextView)findViewById(R.id.tvTwitterDelayValue);
		tvTwitterDownloadSpeedValue=(TextView)findViewById(R.id.tvTwitterDownloadSpeedValue);
		tvYahooDelayValue=(TextView)findViewById(R.id.tvYahooDelayValue);
		tvYahooDownloadSpeedValue=(TextView)findViewById(R.id.tvYahooDownloadSpeedValue);
		tvWikipediaDelayValue=(TextView)findViewById(R.id.tvWikipediaDelayValue);
		tvWikipediaDownloadSpeedValue=(TextView)findViewById(R.id.tvWikipediaDownloadSpeedValue);
		tvDownloadavgspeedValue=(TextView)findViewById(R.id.tvDownloadavgspeedValue);
		tvWIFIDataValue=(TextView)findViewById(R.id.tvWIFIDataValue);
		
		tvServiceTestFacebook=(TextView)findViewById(R.id.tvServiceTestFacebook);
		tvServiceTestYoutube=(TextView)findViewById(R.id.tvServiceTestYoutube);
		tvServiceTestBing=(TextView)findViewById(R.id.tvServiceTestBing);
		tvServiceTestTwitter=(TextView)findViewById(R.id.tvServiceTestTwitter);
		tvServiceTestYahoo=(TextView)findViewById(R.id.tvServiceTestYahoo);
		tvServiceTestWiki=(TextView)findViewById(R.id.tvServiceTestWiki);
		
		ivSpeedTestButtonStop = (ImageView)findViewById(R.id.ivSpeedTestButtonStop);
		ivSpeedTestButtonStart = (ImageView)findViewById(R.id.ivSpeedTestButtonStart);
		
		tvTest = (TextView)findViewById(R.id.tvTest);
		
		//tvServiceTestButton.setOnClickListener(this);
		//tvSpeedTestButton.setOnClickListener(this);
		
		ivSpeedTestButtonStop.setOnClickListener(this);
		ivSpeedTestButtonStart.setOnClickListener(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MyNetApplication.activityPaused();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MyNetApplication.activityResumed();
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) {
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
			}
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) {
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}
	
	private void clearField(){
		tvFacebookDownloadSpeedValue.setText("0");
		tvFacebookDelayValue.setText("0");
		tvYoutubeDelayValue.setText("0");
		tvYoutubeDownloadSpeedValue.setText("0");
		tvBingDelayValue.setText("0");
		tvBingDownloadSpeedValue.setText("0");
		tvTwitterDelayValue.setText("0");
		tvTwitterDownloadSpeedValue.setText("0");
		tvYahooDelayValue.setText("0");
		tvYahooDownloadSpeedValue.setText("0");
		tvWikipediaDelayValue.setText("0");
		tvWikipediaDownloadSpeedValue.setText("0");
		tvDownloadavgspeedValue.setText("0");
		tvWIFIDataValue.setText("0");
	}
	
	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.ivSpeedTestButtonStart){	
			try{
				arrengeServiceTest();
				clearField();
				strSendData="";
				dataCounter=0;
				WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				if (wifiInfo != null) {
					tvDownloadavgspeedValue.setText(String.valueOf( wifiInfo.getLinkSpeed())+" Mbps"); 
				}
				tvWIFIDataValue.setText(String.valueOf((TrafficStats.getTotalRxBytes()-TrafficStats.getMobileRxBytes())/1048576));
				
				new  ServiceTestAsyncTask(this,tvServiceTestFacebook.getText().toString(),tvFacebookDelayValue,tvFacebookDownloadSpeedValue).execute();
				new  ServiceTestAsyncTask(this,tvServiceTestYoutube.getText().toString(),tvYoutubeDelayValue,tvYoutubeDownloadSpeedValue).execute();
				new  ServiceTestAsyncTask(this,tvServiceTestBing.getText().toString(),tvBingDelayValue,tvBingDownloadSpeedValue).execute();
				new  ServiceTestAsyncTask(this,tvServiceTestTwitter.getText().toString(),tvTwitterDelayValue,tvTwitterDownloadSpeedValue).execute();
				new  ServiceTestAsyncTask(this,tvServiceTestYahoo.getText().toString(),tvYahooDelayValue,tvYahooDownloadSpeedValue).execute();
				new  ServiceTestAsyncTask(this,tvServiceTestWiki.getText().toString(),tvWikipediaDelayValue,tvWikipediaDownloadSpeedValue).execute();
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else if(view.getId() == R.id.ivSpeedTestButtonStop){
			arrengeSpeedTest();
		}
	}
	
	private void arrengeSpeedTest(){
		/*tvSpeedTestButton.setBackgroundColor(getResources().getColor(R.color.header_text));
		tvServiceTestButton.setBackgroundColor(getResources().getColor(R.color.value_text));*/
		ivSpeedTestButtonStart.setVisibility(View.VISIBLE);
		ivSpeedTestButtonStop.setVisibility(View.GONE);
	}
	
	private void arrengeServiceTest(){
		/*tvServiceTestButton.setBackgroundColor(getResources().getColor(R.color.header_text));
		tvSpeedTestButton.setBackgroundColor(getResources().getColor(R.color.value_text));*/
		ivSpeedTestButtonStart.setVisibility(View.GONE);
		ivSpeedTestButtonStop.setVisibility(View.VISIBLE);
	}

	@Override
	public void showProgressLoader() {
		
		
	}

	@Override
	public void hideProgressLoader() {
		
		
	}

	@Override
	public Object doBackgroundPorcess() {
		
		IPhoneInformationService manager=new PhoneInformationManager();		
		return manager.getServiceTest();
	}

	@Override
	public void processDataAfterDownload(Object data) {
		
		if(data!=null){
			ServiceTestsRoot serviceTestsRoot =(ServiceTestsRoot)data;
			if(serviceTestsRoot.ServiceTestList.size()>0){
				
				tvServiceTestFacebook.setText(serviceTestsRoot.ServiceTestList.get(0).ServiceTestLink);
				tvServiceTestYoutube.setText(serviceTestsRoot.ServiceTestList.get(1).ServiceTestLink);
				tvServiceTestBing.setText(serviceTestsRoot.ServiceTestList.get(2).ServiceTestLink);
				tvServiceTestTwitter.setText(serviceTestsRoot.ServiceTestList.get(3).ServiceTestLink);
				tvServiceTestYahoo.setText(serviceTestsRoot.ServiceTestList.get(4).ServiceTestLink);
				tvServiceTestWiki.setText(serviceTestsRoot.ServiceTestList.get(5).ServiceTestLink);
				
				
			}
			else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		}
	}
}
