package com.vipdashboard.app.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.customcontrols.GaugeView;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneDataInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.entities.ReportProblemAndBadExperience;
import com.vipdashboard.app.entities.ReportProblemAndBadExperienceRoot;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class ExperienceNetworkSummaryActivity extends
MainActionbarBase implements OnClickListener,IAsynchronousTask {

	private GoogleMap map;
	int successCallinPar = 0, dropCallinPar = 0;
	TextView tvCompanyName, tvCompanyCountry;
	TextView tvExperinceFilterHour, tvExperinceFilterToday,
			tvExperinceFilterYesterday, tvExperinceFilterWeek,welcomeText;
	GaugeView gvMyExperienceSignalStrenght, gvMyExperienceTotalCalls,
			gvMyExperienceSMS, gvMyExperienceSetupSuccess,
			gvMyExperienceDropCalls, gvMyExperienceDataSpeed;
	TextView tvMyExperienceTotalCalls, tvMyExperienceTotalCallsCalled,
			tvMyExperienceTotalCallsReceived, tvMyExperienceTotalCallsMissed,
			tvMyExperienceSignalStrenghtValue, tvMyExperienceSMSSent,
			tvMyExperienceSMSReceived, tvMyExperienceSMSFailed,
			tvMyExperienceSMS, tvMyExperienceSetupSuccessValue,
			tvMyExperienceDropCallsValue, tvMyExperienceDataSpeedDownload,
			tvMyExperienceDataSpeedUpload;
	private static final int DOWNLOAD_MAP = 0;
	int downloadType = DOWNLOAD_MAP;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	int incommingCallCount = 0, outgoingCallCount = 0, missedCallCount = 0,
			totalCallCount = 0, dropCallCount = 0, incommingSMSCount = 0,
			outgoingSMSCount = 0, missedSMSCount = 0, totalSMSCount = 0,
			dataCount = 0, successRateCount = 0;
	Bitmap profilePicture;
	LinearLayout llMyExperienceDetails;
	ImageView bFacebook;
	boolean isCallFromFacebookPost;
	String type;
	LinearLayout selfCareExperienceLayout;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selfcare_myexperience);
		
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
			{
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
			}
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		tvCompanyName.setText(tMgr.isNetworkRoaming()==true?tMgr.getNetworkOperatorName().toString()+"(Roaming)":tMgr.getNetworkOperatorName().toString());
		//Locale l = new Locale("", tMgr.getSimCountryIso().toString());
		//tvCompanyCountry.setText(l.getDisplayCountry());
		tvCompanyCountry.setText(MyNetService.currentCountryName);
		downloadType = DOWNLOAD_MAP;
		arrangeTodayTab();
		setSpedometer(CommonConstraints.INFO_TYPE_TODAY);
		type = String.valueOf(CommonConstraints.TODAY);
		LoadInformation();
	}

	private void Initialization() {
		welcomeText = (TextView) findViewById(R.id.tvSelfcareMyExperinceUserTitle);
		llMyExperienceDetails = (LinearLayout) findViewById(R.id.llMyExperienceDetails);
		tvExperinceFilterHour = (TextView) findViewById(R.id.tvExperinceFilterHour);
		tvExperinceFilterToday = (TextView) findViewById(R.id.tvExperinceFilterToday);
		tvExperinceFilterYesterday = (TextView) findViewById(R.id.tvExperinceFilterYesterday);
		tvExperinceFilterWeek = (TextView) findViewById(R.id.tvExperinceFilterWeek);
		
		tvCompanyName = (TextView) findViewById(R.id.tvCompanyName);
		tvCompanyCountry = (TextView) findViewById(R.id.tvCompanyCountry);

		tvExperinceFilterHour.setOnClickListener(this);
		tvExperinceFilterToday.setOnClickListener(this);
		tvExperinceFilterYesterday.setOnClickListener(this);
		tvExperinceFilterWeek.setOnClickListener(this);

		/*map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapSelfcareMyExperince)).getMap();*/

		gvMyExperienceSignalStrenght = (GaugeView) findViewById(R.id.gvMyExperienceSignalStrenght);
		gvMyExperienceTotalCalls = (GaugeView) findViewById(R.id.gvMyExperienceTotalCalls);
		gvMyExperienceSMS = (GaugeView) findViewById(R.id.gvMyExperienceSMS);
		gvMyExperienceSetupSuccess = (GaugeView) findViewById(R.id.gvMyExperienceSetupSuccess);
		gvMyExperienceDropCalls = (GaugeView) findViewById(R.id.gvMyExperienceDropCalls);
		gvMyExperienceDataSpeed = (GaugeView) findViewById(R.id.gvMyExperienceDataSpeed);

		tvMyExperienceTotalCallsCalled = (TextView) findViewById(R.id.tvMyExperienceTotalCallsCalled);
		tvMyExperienceTotalCallsReceived = (TextView) findViewById(R.id.tvMyExperienceTotalCallsReceived);
		tvMyExperienceTotalCalls = (TextView) findViewById(R.id.tvMyExperienceTotalCalls);
		tvMyExperienceSignalStrenghtValue = (TextView) findViewById(R.id.tvMyExperienceSignalStrenghtValue);
		tvMyExperienceSMS = (TextView) findViewById(R.id.tvMyExperienceSMS);
		tvMyExperienceSMSSent = (TextView) findViewById(R.id.tvMyExperienceSMSSent);
		tvMyExperienceSMSReceived = (TextView) findViewById(R.id.tvMyExperienceSMSReceived);
		tvMyExperienceSetupSuccessValue = (TextView) findViewById(R.id.tvMyExperienceSetupSuccessValue);
		tvMyExperienceDropCallsValue = (TextView) findViewById(R.id.tvMyExperienceDropCallsValue);
		tvMyExperienceDataSpeedDownload = (TextView) findViewById(R.id.tvMyExperienceDataSpeedDownload);
		tvMyExperienceDataSpeedUpload = (TextView) findViewById(R.id.tvMyExperienceDataSpeedUpload);
		tvMyExperienceTotalCallsMissed = (TextView) findViewById(R.id.tvMyExperienceTotalCallsMissed);
		tvMyExperienceSMSFailed = (TextView) findViewById(R.id.tvMyExperienceSMSFailed);
	}

	private void setSpedometer(int type) {
		try {
			incommingCallCount = 0;
			outgoingCallCount = 0;
			missedCallCount = 0;
			totalCallCount = 0;
			dropCallCount = 0;
			incommingSMSCount = 0;
			outgoingSMSCount = 0;
			missedSMSCount = 0;
			totalSMSCount = 0;
			dataCount = 0;
			MyNetDatabase myNetDatabase = new MyNetDatabase(this);
			int signal = 0;
			myNetDatabase.open();
			signal = (int) (myNetDatabase.getAgvSignalStrenght(type) * 100) / 31;

			ArrayList<PhoneCallInformation> phoneCallList = myNetDatabase
					.getTotalCallInfo(type);
			ArrayList<PhoneSMSInformation> phoneSmsList = myNetDatabase
					.getTotalSMSInfo(type);
			PhoneDataInformation phoneDataInformation = myNetDatabase
					.getAgvDownLoadUploadSpeed(type);

			dataCount = myNetDatabase.getTotalDataCount();

			int hour = 0, min = 0, sec = 0, duration = 0;
			duration = myNetDatabase.getMaxCallDuration();
			sec = duration % 60;
			min = duration / 60;
			if (min > 59) {
				hour = min / 60;
				min = min % 60;
			}
			NumberFormat formatter = new DecimalFormat("00");
			String durationText = formatter.format(hour) + ":"
					+ formatter.format(min) + ":" + formatter.format(sec);


			hour = 0;
			min = 0;
			sec = 0;
			duration = 0;
			duration = myNetDatabase.getMinCallDuration();
			sec = duration % 60;
			min = duration / 60;
			if (min > 59) {
				hour = min / 60;
				min = min % 60;
			}
			formatter = new DecimalFormat("00");
			durationText = formatter.format(hour) + ":" + formatter.format(min)
					+ ":" + formatter.format(sec);
			hour = 0;
			min = 0;
			sec = 0;
			duration = 0;
			duration = myNetDatabase.getAvgCallDuration();
			sec = duration % 60;
			min = duration / 60;
			if (min > 59) {
				hour = min / 60;
				min = min % 60;
			}
			formatter = new DecimalFormat("00");
			durationText = formatter.format(hour) + ":" + formatter.format(min)
					+ ":" + formatter.format(sec);

			myNetDatabase.close();

			for (PhoneCallInformation phoneCallInformation : phoneCallList) {
				if (phoneCallInformation.CallType == CallLog.Calls.INCOMING_TYPE) {
					incommingCallCount = phoneCallInformation.CallCount;
				} else if (phoneCallInformation.CallType == CallLog.Calls.OUTGOING_TYPE) {
					outgoingCallCount = phoneCallInformation.CallCount;
				} else {
					missedCallCount = phoneCallInformation.CallCount;
				}
				totalCallCount = totalCallCount
						+ phoneCallInformation.CallCount;
			}

			gvMyExperienceSignalStrenght.setTargetValue(signal);
			if (signal > 75)
				tvMyExperienceSignalStrenghtValue.setText("Excellent");
			else if (signal > 50)
				tvMyExperienceSignalStrenghtValue.setText("Good");
			else if (signal > 25)
				tvMyExperienceSignalStrenghtValue.setText("Moderate");
			else
				tvMyExperienceSignalStrenghtValue.setText("Weak");

			int totalInPar = 0;

			if (totalCallCount > 0) {
				totalInPar = ((incommingCallCount + missedCallCount) * 100)
						/ totalCallCount;

				/*successCallinPar = ((totalCallCount - dropCallCount) * 100)
						/ totalCallCount;

				dropCallinPar = (dropCallCount * 100) / totalCallCount;*/
			}

			gvMyExperienceTotalCalls.setTargetValue(totalInPar);

			gvMyExperienceSetupSuccess.setTargetValue(successCallinPar);

			gvMyExperienceDropCalls.setTargetValue(dropCallinPar);

			tvMyExperienceTotalCalls.setText("Total Calls: " + totalCallCount);

			tvMyExperienceTotalCallsCalled.setText(String
					.valueOf(outgoingCallCount));

			tvMyExperienceTotalCallsMissed.setText(String
					.valueOf(missedCallCount));

			tvMyExperienceTotalCallsReceived.setText(String
					.valueOf(incommingCallCount));

			/*tvMyExperienceSetupSuccessValue.setText(successCallinPar + "%");
			tvMyExperienceDropCallsValue.setText(dropCallinPar + "%");*/

			for (PhoneSMSInformation phoneSMSInformation : phoneSmsList) {
				if (phoneSMSInformation.SMSType == 1) {
					incommingSMSCount = phoneSMSInformation.SMSCount;
				} else if (phoneSMSInformation.SMSType == 2) {
					outgoingSMSCount = phoneSMSInformation.SMSCount;
				} else {
					missedSMSCount = phoneSMSInformation.SMSCount;
				}
				totalSMSCount = totalSMSCount + phoneSMSInformation.SMSCount;
			}
			totalInPar = 0;
			if (totalSMSCount > 0) {
				totalInPar = (incommingSMSCount * 100) / totalSMSCount;
			}
			gvMyExperienceSMS.setTargetValue(totalInPar);

			tvMyExperienceSMS.setText("SMS: " + totalSMSCount);

			tvMyExperienceSMSSent.setText(String.valueOf(outgoingSMSCount));
			tvMyExperienceSMSFailed.setText(String.valueOf(missedSMSCount));
			tvMyExperienceSMSReceived
					.setText(String.valueOf(incommingSMSCount));

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
										: phoneDataInformation.UpLoadSpeed
												+ "kbps"));
			} else {
				gvMyExperienceDataSpeed.setTargetValue(0);
				tvMyExperienceDataSpeedDownload.setText("Download: 0kbps");
				tvMyExperienceDataSpeedUpload.setText("Upload: 0kbps");
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.tvExperinceFilterHour) {
			arrangeLastHourTab();
			setSpedometer(CommonConstraints.INFO_TYPE_LASTHOUR);
			type = String.valueOf(CommonConstraints.THIS_HOUR);
			LoadInformation();
		} else if (view.getId() == R.id.tvExperinceFilterToday) {
			arrangeTodayTab();
			setSpedometer(CommonConstraints.INFO_TYPE_TODAY);
			type = String.valueOf(CommonConstraints.TODAY);
			LoadInformation();
		} else if (view.getId() == R.id.tvExperinceFilterYesterday) {
			arrangeYesterdayTab();
			setSpedometer(CommonConstraints.INFO_TYPE_YESTERDAY);
			type = String.valueOf(CommonConstraints.YESTERDAY);
			LoadInformation();
		} else if (view.getId() == R.id.tvExperinceFilterWeek) {
			arrangeWeekTab();
			setSpedometer(CommonConstraints.INFO_TYPE_WEEK);
			type = String.valueOf(CommonConstraints.THIS_WEEK);
			LoadInformation();
		}
	}

	private void networkSummarySnapShot() {
		try{
			Bitmap bmOverlay = null;
			llMyExperienceDetails.setDrawingCacheEnabled(true);
			bmOverlay = llMyExperienceDetails.getDrawingCache();
			String external_path = Environment
					.getExternalStorageDirectory().getPath()
					+ "/MyNet/";
			String filePath = String.format(CommonValues.getInstance().LoginUser.UserNumber+".jpg");
			File cduFileDir = new File(external_path);
			if (!cduFileDir.exists())
				cduFileDir.mkdir();
			File pictureFile = new File(cduFileDir, filePath);
			ExperinceLiveActivity.filename = pictureFile.getName();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmOverlay.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			ExperinceLiveActivity.selectedFile = stream.toByteArray();
			LoadInformation();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void arrangeLastHourTab() {
		tvExperinceFilterHour.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		tvExperinceFilterToday.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterYesterday.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterWeek.setBackgroundColor(getResources().getColor(
				R.color.value_text));
	}
	
	private void arrangeTodayTab() {
		tvExperinceFilterHour.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterToday.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		tvExperinceFilterYesterday.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterWeek.setBackgroundColor(getResources().getColor(
				R.color.value_text));	
	}
	
	private void arrangeYesterdayTab() {
		tvExperinceFilterHour.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterToday.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterYesterday.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		tvExperinceFilterWeek.setBackgroundColor(getResources().getColor(
				R.color.value_text));	
	}
	private void arrangeWeekTab() {
		tvExperinceFilterHour.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterToday.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterYesterday.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvExperinceFilterWeek.setBackgroundColor(getResources().getColor(
				R.color.header_text));	
	}
	
	public void LoadInformation(){
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		/*progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(true);
		progressDialog.setMessage("Please Wait...");
		progressDialog.show();*/
	}

	@Override
	public void hideProgressLoader() {
		/*progressDialog.dismiss();*/
	}

	@Override
	public Object doBackgroundPorcess() {
		MyNetDatabase database = new MyNetDatabase(this);
		database.open();
		ArrayList<ReportProblemAndBadExperience> probList = database.getCountOfDropAndBlockCalls(Integer.parseInt(type));
		database.close();
		return probList;
		//IStatisticsReportManager statisticsReportManager = new StatisticsReportManager();
		//return statisticsReportManager.GetTotalNumberOfDroppedCall(CommonValues.getInstance().LoginUser.Mobile,"Dropped Call,Blocked Call",type);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			successCallinPar = 100;dropCallinPar = 0;
			ArrayList<ReportProblemAndBadExperience> reportProblemAndBadExperiences = (ArrayList<ReportProblemAndBadExperience>) data;
			if(reportProblemAndBadExperiences != null && reportProblemAndBadExperiences.size()>0){
				for(int i=0;i<reportProblemAndBadExperiences.size();i++){
					if(reportProblemAndBadExperiences.get(i).Problem.equals("Dropped Call")){
						if(totalCallCount > 0){
							dropCallCount = reportProblemAndBadExperiences.get(i).Failed;
							dropCallinPar = ((totalCallCount-dropCallCount)*100)/totalCallCount;
							if(dropCallinPar < 0 )
								dropCallinPar = 0;
							dropCallinPar = 100 - dropCallinPar;
						}
					}
					if(reportProblemAndBadExperiences.get(i).Problem.equals("Blocked Call")){ 
						if(totalCallCount > 0){
							successRateCount = reportProblemAndBadExperiences.get(i).Failed;
							successCallinPar = ((totalCallCount-successRateCount)*100)/totalCallCount;
							if(successCallinPar < 0)
								successCallinPar = 0; 
						}
					}
				}
			}
			if(totalCallCount == 0){
				gvMyExperienceSetupSuccess.setTargetValue(0);

				gvMyExperienceDropCalls.setTargetValue(0);
				
				tvMyExperienceSetupSuccessValue.setText("0%");
				tvMyExperienceDropCallsValue.setText("0%");

			}else{
				gvMyExperienceSetupSuccess.setTargetValue(successCallinPar);
	
				gvMyExperienceDropCalls.setTargetValue(dropCallinPar);
				
				tvMyExperienceSetupSuccessValue.setText(successCallinPar + "%");
				tvMyExperienceDropCallsValue.setText(dropCallinPar + "%");
			}
		}
	}

	private void initializeMap() {
		try{
		
		Bitmap defaultBitmap, defaultBitmapUser;

		double defaultLatitude = 0, defaultLongitude = 0;

		if (MyNetService.currentLocation != null) {
			LatLng Location = new LatLng(
					MyNetService.currentLocation.getLatitude(),
					MyNetService.currentLocation.getLongitude());
			defaultLatitude = Location.latitude;
			defaultLongitude = Location.longitude;
			if(profilePicture != null){
				defaultBitmapUser = profilePicture;
			}else{
				defaultBitmapUser = BitmapFactory.decodeResource(getResources(),
						R.drawable.user_icon);
			}
			defaultBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.google_custom_pin);
			
			defaultBitmapUser = Bitmap.createScaledBitmap(defaultBitmapUser, defaultBitmap.getWidth() - 12,
					defaultBitmap.getHeight() - 40, true);
			Bitmap bmp = Bitmap.createBitmap(defaultBitmap.getWidth(), defaultBitmap.getHeight(),
					defaultBitmap.getConfig());
			Canvas canvas = new Canvas(bmp);
			canvas.drawBitmap(defaultBitmap, new Matrix(), null);
			canvas.drawBitmap(defaultBitmapUser, 8, 10, null);
			map.clear();
			map.addMarker(new MarkerOptions().position(Location).title(CommonValues.getInstance().LoginUser.FullName()
							+ ", Mobile:"
							+ MyNetService.phoneBasicInformation.MobileNo)
					.icon(BitmapDescriptorFactory.fromBitmap(bmp)));

		}

		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		LatLng Defaultlocation = new LatLng(defaultLatitude, defaultLongitude);

		map.moveCamera(CameraUpdateFactory
				.newLatLngZoom(Defaultlocation, 14.0f));

		map.animateCamera(CameraUpdateFactory.zoomIn());

		map.animateCamera(CameraUpdateFactory.zoomTo(10));

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(Defaultlocation).zoom(17).bearing(90).tilt(30).build();

		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

}
