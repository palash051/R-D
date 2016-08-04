package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.widget.TextView;





import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.customcontrols.GaugeView;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneDataInformation;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_ExperienceKPIActivity extends MainActionbarBase {
	GaugeView gvMyExperienceSignalStrenght,gvMyExperienceDataSpeed,gvMyExperienceDropCalls,gvMyExperienceTotalCalls;
	TextView tvMyExperienceSignalStrenghtValue,tvMyExperienceTotalCalls,tvMyExperienceSetupSuccessValue,tvMyExperienceDropCallsValue,tvMyExperienceDataSpeedDownload,tvMyExperienceDataSpeedUpload;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.experience_kpi);	
		
		
		initialization();
	}
	private void initialization() {
		gvMyExperienceSignalStrenght=(GaugeView)findViewById(R.id.gvMyExperienceSignalStrenght);		
		gvMyExperienceDataSpeed=(GaugeView)findViewById(R.id.gvMyExperienceDataSpeed);
		gvMyExperienceTotalCalls=(GaugeView)findViewById(R.id.gvMyExperienceTotalCalls);
		gvMyExperienceDropCalls=(GaugeView)findViewById(R.id.gvMyExperienceDropCalls);
		tvMyExperienceSignalStrenghtValue =(TextView)findViewById(R.id.tvMyExperienceSignalStrenghtValue);
		tvMyExperienceTotalCalls =(TextView)findViewById(R.id.tvMyExperienceTotalCalls);
		tvMyExperienceSetupSuccessValue =(TextView)findViewById(R.id.tvMyExperienceSetupSuccessValue);		
		tvMyExperienceDropCallsValue =(TextView)findViewById(R.id.tvMyExperienceDropCallsValue);
		tvMyExperienceDataSpeedDownload=(TextView)findViewById(R.id.tvMyExperienceDataSpeedDownload);
		tvMyExperienceDataSpeedUpload=(TextView)findViewById(R.id.tvMyExperienceDataSpeedUpload);
	}
	@Override
	protected void onResume() {		
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
			if (!isFinishing()) 
			{
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
			}
		startSignalLevelListener();
		gvMyExperienceDropCalls.setTargetValue(0);
		gvMyExperienceSignalStrenght.setTargetValue(0);
		setSpedometer();
	}
	
	private void setSpedometer() {
		
		int incommingCallCount = 0, outgoingCallCount = 0, missedCallCount = 0, totalCallCount = 0,dropCallCount = 0,dataCount=0;
		
		MyNetDatabase myNetDatabase = new MyNetDatabase(this);
		myNetDatabase.open();
		ArrayList<PhoneCallInformation> phoneCallList = myNetDatabase.getTotalCallInfo(2);
		PhoneDataInformation phoneDataInformation = myNetDatabase.getAgvDownLoadUploadSpeed(2);
		myNetDatabase.close();
		
		

		for (PhoneCallInformation phoneCallInformation : phoneCallList) {
			if (phoneCallInformation.CallType == CallLog.Calls.INCOMING_TYPE) {
				incommingCallCount = phoneCallInformation.CallCount;
			} else if (phoneCallInformation.CallType == CallLog.Calls.OUTGOING_TYPE) {
				outgoingCallCount = phoneCallInformation.CallCount;
			} else {
				missedCallCount = phoneCallInformation.CallCount;
			}
			totalCallCount = totalCallCount + phoneCallInformation.CallCount;
		}
		
		
		int  totalInPar = 0, successCallinPar = 0, dropCallinPar = 0;

		if (totalCallCount > 0) {
			totalInPar = ((incommingCallCount + missedCallCount) * 100) / totalCallCount;

			successCallinPar = ((totalCallCount - dropCallCount) * 100) / totalCallCount;

			dropCallinPar = (dropCallCount * 100) / totalCallCount;
		}

		gvMyExperienceTotalCalls.setTargetValue(successCallinPar);	

		gvMyExperienceDropCalls.setTargetValue(dropCallinPar);

		tvMyExperienceTotalCalls.setText("Call Setup: " + totalCallCount);

		tvMyExperienceSetupSuccessValue.setText(String.valueOf( successCallinPar )+"%");
		
		
		tvMyExperienceDropCallsValue.setText(dropCallinPar + "%");
		
		
		if (phoneDataInformation != null
				&& phoneDataInformation.DownLoadSpeed > 0) {
			int currentSpeedInPar = (MyNetService.currentDownloadSpeedInKbPS * 100)
					/ phoneDataInformation.DownLoadSpeed;
			gvMyExperienceDataSpeed.setTargetValue(currentSpeedInPar);
			tvMyExperienceDataSpeedDownload
					.setText("Download: "
							+ (phoneDataInformation.DownLoadSpeed > 8 ? phoneDataInformation.DownLoadSpeed
									/ 8 + "KBPS"
									: phoneDataInformation.DownLoadSpeed
											+ "kbps"));
			tvMyExperienceDataSpeedUpload
					.setText("Upload: "
							+ (phoneDataInformation.UpLoadSpeed > 8 ? phoneDataInformation.UpLoadSpeed
									/ 8 + "KBPS"
									: phoneDataInformation.UpLoadSpeed + "kbps"));
		} else {
			gvMyExperienceDataSpeed.setTargetValue(0);
			tvMyExperienceDataSpeedDownload.setText("Download: 0kbps");
			tvMyExperienceDataSpeedUpload.setText("Upload: 0kbps");
		}
		
	}
	
	private void startSignalLevelListener() {

		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		int events = PhoneStateListener.LISTEN_SIGNAL_STRENGTH
				| PhoneStateListener.LISTEN_DATA_ACTIVITY
				| PhoneStateListener.LISTEN_CELL_LOCATION
				| PhoneStateListener.LISTEN_CALL_STATE
				| PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR
				| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
				| PhoneStateListener.LISTEN_SERVICE_STATE;

		tm.listen(phoneListener, events);

	}
	
	
	
	private final PhoneStateListener phoneListener = new PhoneStateListener() {
		/*
		 * call fwding
		 */
		public void onCallForwardingIndicatorChanged(boolean cfi) {

			super.onCallForwardingIndicatorChanged(cfi);
		}

		/*
		 * Call State Changed
		 */
		public void onCallStateChanged(int state, String incomingNumber) {

		}

		/*
		 * Cell location changed event handler
		 */
		public void onCellLocationChanged(CellLocation location) {

		}

		/*
		 * Cellphone data connection status
		 */
		public void onDataConnectionStateChanged(int state) {

		}

		/*
		 * Data activity handler
		 */
		public void onDataActivity(int direction) {

		}

		/*
		 * Cellphone Service status
		 */
		public void onServiceStateChanged(ServiceState serviceState) {

		}

		/*
		 * 
		 * */
		public void onSignalStrengthChanged(int asu) {
			if(asu==99)
				asu=0;			
			int signalValue= asu>0?( -113 + (asu * 2)):asu;
			int currentsignal=((100+signalValue)*100)/(100-51);
			gvMyExperienceSignalStrenght.setTargetValue(currentsignal);
			if (currentsignal > 75)
				tvMyExperienceSignalStrenghtValue.setText("Excellent("+signalValue+"dBm)");
			else if (currentsignal > 50)
				tvMyExperienceSignalStrenghtValue.setText("Good("+signalValue+"dBm)");
			else if (currentsignal > 25)
				tvMyExperienceSignalStrenghtValue.setText("Moderate("+signalValue+"dBm)");
			else
				tvMyExperienceSignalStrenghtValue.setText("Weak("+signalValue+"dBm)");
			super.onSignalStrengthChanged(asu);
		}

	};


}
