package com.vipdashboard.app.activities;

import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.customcontrols.GaugeView;
import com.vipdashboard.app.entities.PhoneDataInformation;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IPhoneInformationService;
import com.vipdashboard.app.manager.PhoneInformationManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class VIPD_SpeedTestActivity extends MainActionbarBase implements OnClickListener,IAsynchronousTask{
	
	TextView tvSpeedTestButton,tvSpeedTestLatencyWiFiavg,tvSpeedTestDownloadTitleWiFiavg,SpeedTestUploadWiFiavg;
	GaugeView gvSignalTestDownload,gvSignalTestUpload;
	ProgressDialog progressDialog;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressBar pbSpeedTest;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipd_speedtest);	
		
		Initialization();
		
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
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{	if (!isFinishing()) {
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
			}
	}

	

	private void Initialization() {			
		gvSignalTestDownload=(GaugeView)findViewById(R.id.gvSignalTestDownload);
		gvSignalTestUpload=(GaugeView)findViewById(R.id.gvSignalTestUpload);		
		tvSpeedTestButton = (TextView) findViewById(R.id.tvSpeedTestButton);
		tvSpeedTestLatencyWiFiavg = (TextView) findViewById(R.id.tvSpeedTestLatencyWiFiavg);
		tvSpeedTestDownloadTitleWiFiavg = (TextView) findViewById(R.id.tvSpeedTestDownloadTitleWiFiavg);
		SpeedTestUploadWiFiavg = (TextView) findViewById(R.id.SpeedTestUploadWiFiavg);
		pbSpeedTest = (ProgressBar) findViewById(R.id.pbSpeedTest);
		tvSpeedTestButton.setOnClickListener(this);
		
		gvSignalTestDownload.setTargetValue(0);
		gvSignalTestUpload.setTargetValue(0);
	}

	@Override
	public void onClick(View view) {
		try{
			if(view.getId() == R.id.tvSpeedTestButton){
				tvSpeedTestLatencyWiFiavg.setText("0");
				tvSpeedTestDownloadTitleWiFiavg.setText("0");
				SpeedTestUploadWiFiavg.setText("0");
				
				gvSignalTestDownload.setTargetValue(0);
				gvSignalTestUpload.setTargetValue(0);
				if (downloadableAsyncTask != null) {
					downloadableAsyncTask.cancel(true);
				}
				downloadableAsyncTask = new DownloadableAsyncTask(this);
				downloadableAsyncTask.execute();
			}
		}catch (Exception e) {
		}
	}

	@Override
	public void showProgressLoader() {
		/*progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		//progressDialog.set
		//progressDialog.setIndeterminate(true);
		//progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.custom_progrress_bar));
		
		progressDialog.setMessage("Please Wait...");
		progressDialog.show();*/
	}

	@Override
	public void hideProgressLoader() {
		//progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		IPhoneInformationService manager=new PhoneInformationManager();		
		return manager.SetDataSpeedInfo(this,true);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data!=null){
			PhoneDataInformation phoneDataInformation=(PhoneDataInformation)data;
			tvSpeedTestLatencyWiFiavg.setText(phoneDataInformation.CallCount>1000?String.valueOf((int)(phoneDataInformation.CallCount/1000)+" s"): phoneDataInformation.CallCount+" ms");
			tvSpeedTestDownloadTitleWiFiavg.setText(phoneDataInformation.DownLoadSpeed>1024?String.valueOf((double)(phoneDataInformation.DownLoadSpeed/1024)+" Mbps"): phoneDataInformation.DownLoadSpeed+" Kbps");
			SpeedTestUploadWiFiavg.setText(phoneDataInformation.UpLoadSpeed>1024?String.valueOf((double)(phoneDataInformation.UpLoadSpeed/1024)+" Mbps"): phoneDataInformation.UpLoadSpeed+" Kbps");
			gvSignalTestDownload.setTargetValue((phoneDataInformation.DownLoadSpeed/400)*100);
			gvSignalTestUpload.setTargetValue((phoneDataInformation.UpLoadSpeed/400)*100);
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
