package com.vipdashboard.app.asynchronoustask;

import java.util.Date;

import android.content.Context;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.widget.TextView;

import com.vipdashboard.app.activities.VIPD_ServiceTestActivity;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.utils.CommonTask;

public class ServiceTestAsyncTask extends AsyncTask<Void, Void, Object>{
	String url;
	TextView tvDelay, tvSpeed;
	long startTime,endTime,timediff,priviousDataSpeed,currentDataSpeed,speedDiff;
	Context context;
	public ServiceTestAsyncTask(Context _context,String _url,TextView _tvDelay,TextView _tvSpeed) {
		this.url = _url;
		context=_context;
		tvDelay=_tvDelay;
		tvSpeed=_tvSpeed;
		
	}
	
	@Override
	protected void onPreExecute() {	
		startTime=0;
		endTime=0;
		timediff=0;
		priviousDataSpeed=TrafficStats.getTotalRxBytes();
		//VIPD_ServiceTestActivity.tvTest.setText("Now testing "+ this.url);
	}

	@Override
	protected Object doInBackground(Void... cap) {
		
		startTime=System.currentTimeMillis();
		//VIPD_ServiceTestActivity.tvTest.setText("Now testing "+ this.url);
		return CommonTask.getBitmapImage(this.url);
	}

	@Override
	protected void onPostExecute(Object data) {	
		try{
			if(VIPD_ServiceTestActivity.tvTest != null)
				VIPD_ServiceTestActivity.tvTest.setText("Now testing "+ this.url);
			timediff=System.currentTimeMillis()-startTime;
			
			tvDelay.setText(String.valueOf(timediff));
			currentDataSpeed=TrafficStats.getTotalRxBytes();
			
			speedDiff=currentDataSpeed-priviousDataSpeed;
			
			int speed=(int) ((speedDiff/timediff)*1000)/1024;
			
			tvSpeed.setText(String.valueOf(speed));
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
			String CID = String.valueOf(location.getCid() % 0xffff);
			String LAC = String.valueOf(location.getLac() % 0xffff);
			
			VIPD_ServiceTestActivity.strSendData=VIPD_ServiceTestActivity.strSendData+url+"~"+timediff+"~"+speed+"~0~"+
					MyNetService.currentLocation.getLatitude()+"~"+MyNetService.currentLocation.getLongitude()+"~"+new Date().getTime()+
					"~"+LAC+"~"+CID+"|";
			
			VIPD_ServiceTestActivity.dataCounter++;
			
			
				if(VIPD_ServiceTestActivity.dataCounter==6){
					new  BrowserDataInfoAsycntask(context,VIPD_ServiceTestActivity.strSendData).execute();				
				}
			VIPD_ServiceTestActivity.count++;
			if(VIPD_ServiceTestActivity.count == 6){
				VIPD_ServiceTestActivity.tvTest.setText("Touch to start test");
				VIPD_ServiceTestActivity.ivSpeedTestButtonStart.setVisibility(View.VISIBLE);
				VIPD_ServiceTestActivity.ivSpeedTestButtonStop.setVisibility(View.GONE);
				VIPD_ServiceTestActivity.count = 0;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}
