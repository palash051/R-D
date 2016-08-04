package com.vipdashboard.app.activities;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonValues;

public class VIPDBatteryDoctorTaptoDiagnoseActivity extends MainActionbarBase
		implements OnClickListener {

	TextView tvRemainingTime, ivSaveNow, tvRunningApps;
	ImageView ivGPSOff, ivGPSOn, ivScreenTimeoutOff, ivScreenTimeoutOn,
			ivBrigthnessOff, ivBrigthnessOn, imRunningAppsOff, imRunningAppsOn;

	RelativeLayout rlGPS, rlTimeout, rlBrigthness, rlRunningApps,
			rlRunningAppsList;

	ImageView ivRunningAppsOne, ivRunningAppsTwo, ivRunningAppsThree,
			ivRunningAppsFour, ivRunningAppsFive, ivRunningAppsSix;

	Boolean IsBrightnessNeedtoOptimize, IsGPSNeedtoOptimize,
			IsTimeoutNeedtoOptimize;

	private PackageManager packageManager;
	//Variable to store brightness value
	private int curBrightnessValue = 0;
	//Content resolver used as a handle to the system's settings
	private  ContentResolver cResolver;
	//Window object, that will store a reference to the current window
	private  Window window;
	public static int totalRunningApps = 0, runningApps = 0, killApps = 0;

	boolean IsMatchhappen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_doctor_tap_to_diagnose);
		Initialization();
		registerBatteryLevelReceiver();
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

		Bundle saveInstance = getIntent().getExtras();
		if (saveInstance != null) {
			IsBrightnessNeedtoOptimize = saveInstance
					.getBoolean("ISBRIGHTNESSNEEDTOOPTIMIZE");
			IsGPSNeedtoOptimize = saveInstance
					.getBoolean("ISGPSNEEDTOOPTIMIZE");
			IsTimeoutNeedtoOptimize = saveInstance
					.getBoolean("ISTIMEOUTNEEDTOOPTIMIZE");
		}

		totalRunningApps = 0;
		runningApps = 0;
		killApps = 0;
		
		CommonValues.getInstance().BatteryDoctorSavedMin=0;

		PanelVisible();
		getAllRunningApps();

		packageManager = this.getPackageManager();

		tvRunningApps.setText("Running Apps(" + runningApps + ")");

		if (runningApps == 0) {
			rlRunningAppsList.setVisibility(View.GONE);
		} else {
			rlRunningAppsList.setVisibility(View.VISIBLE);
		}

	}

	private void PanelVisible() {
		if (IsBrightnessNeedtoOptimize) {
			rlBrigthness.setVisibility(View.VISIBLE);
		} else {
			rlBrigthness.setVisibility(View.GONE);
		}
		if (IsGPSNeedtoOptimize) {
			rlGPS.setVisibility(View.VISIBLE);
		} else {
			rlGPS.setVisibility(View.GONE);
		}
		if (IsTimeoutNeedtoOptimize) {
			rlTimeout.setVisibility(View.VISIBLE);
		} else {
			rlTimeout.setVisibility(View.GONE);
		}

	}

	private void registerBatteryLevelReceiver() {
		IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(battery_receiver, filter);
	}

	private BroadcastReceiver battery_receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			boolean isPresent = intent.getBooleanExtra("present", false);
			String technology = intent.getStringExtra("technology");
			int plugged = intent.getIntExtra("plugged", -1);
			int scale = intent.getIntExtra("scale", -1);
			int health = intent.getIntExtra("health", 0);
			int status = intent.getIntExtra("status", 0);
			int rawlevel = intent.getIntExtra("level", -1);
			int level = 0;
			Bundle bundle = intent.getExtras();

			if (isPresent) {
				if (rawlevel >= 0 && scale > 0) {
					level = (rawlevel * 100) / scale;
				}
				// tvBatteryPerformance.setText(""+level+"%");

				float estimatedTime = (float) (level / 4.0);
				String value = String.valueOf(new DecimalFormat("##.##")
						.format(estimatedTime));
				if (estimatedTime <= 24.9) {
					String[] es = value.split("\\.");
					if (es.length < 2)
						tvRemainingTime.setText(es[0] + "h " + "00m");
					else
						tvRemainingTime.setText(es[0] + "h " + es[1] + "m");
				} else {
					String[] es = value.split("\\.");
					if (es.length < 2)
						tvRemainingTime.setText("1d " + " 00h");
					else
						tvRemainingTime.setText("1d " + es[1] + "h");
				}
			}
		}
	};

	private void Initialization() {
		tvRemainingTime = (TextView) findViewById(R.id.tvRemainingTime);
		ivSaveNow = (TextView) findViewById(R.id.ivSaveNow);
		tvRunningApps = (TextView) findViewById(R.id.tvRunningApps);
		ivGPSOff = (ImageView) findViewById(R.id.ivGPSOff);
		ivGPSOn = (ImageView) findViewById(R.id.ivGPSOn);
		ivScreenTimeoutOff = (ImageView) findViewById(R.id.ivScreenTimeoutOff);
		ivScreenTimeoutOn = (ImageView) findViewById(R.id.ivScreenTimeoutOn);
		ivBrigthnessOff = (ImageView) findViewById(R.id.ivBrigthnessOff);
		ivBrigthnessOn = (ImageView) findViewById(R.id.ivBrigthnessOn);
		imRunningAppsOff = (ImageView) findViewById(R.id.imRunningAppsOff);
		imRunningAppsOn = (ImageView) findViewById(R.id.imRunningAppsOn);

		ivRunningAppsOne = (ImageView) findViewById(R.id.ivRunningAppsOne);
		ivRunningAppsTwo = (ImageView) findViewById(R.id.ivRunningAppsTwo);
		ivRunningAppsThree = (ImageView) findViewById(R.id.ivRunningAppsThree);
		ivRunningAppsFour = (ImageView) findViewById(R.id.ivRunningAppsFour);
		ivRunningAppsFive = (ImageView) findViewById(R.id.ivRunningAppsFive);
		ivRunningAppsSix = (ImageView) findViewById(R.id.ivRunningAppsSix);

		rlGPS = (RelativeLayout) findViewById(R.id.rlGPS);
		rlTimeout = (RelativeLayout) findViewById(R.id.rlTimeout);
		rlBrigthness = (RelativeLayout) findViewById(R.id.rlBrigthness);
		rlRunningApps = (RelativeLayout) findViewById(R.id.rlRunningApps);
		rlRunningAppsList = (RelativeLayout) findViewById(R.id.rlRunningAppsList);

		rlGPS.setOnClickListener(this);
		rlTimeout.setOnClickListener(this);
		rlBrigthness.setOnClickListener(this);
		rlRunningApps.setOnClickListener(this);
		ivSaveNow.setOnClickListener(this);
		rlRunningAppsList.setOnClickListener(this);
	}

	private void KillAllRunningApps() {
		try {
			List<ApplicationInfo> packages;
			PackageManager pm;
			pm = getPackageManager();
			// get a list of installed apps.
			packages = pm.getInstalledApplications(0);

			ActivityManager mActivityManager = (ActivityManager) this
					.getSystemService(Context.ACTIVITY_SERVICE);

			for (ApplicationInfo packageInfo : packages) {
				if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1)
					continue;
				if (packageInfo.packageName
						.equals(CommonValues.APPLICATION_PACKAGE_NAME))
					continue;
				mActivityManager
						.killBackgroundProcesses(packageInfo.packageName);
				
				killApps++;
			}
		} catch (Exception e) {
			// Name Not FOund Exception
		}
	}

	private void getAllRunningApps() {
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();

		ivRunningAppsOne.setImageDrawable(null);
		ivRunningAppsTwo.setImageDrawable(null);
		ivRunningAppsThree.setImageDrawable(null);
		ivRunningAppsFour.setImageDrawable(null);
		ivRunningAppsFive.setImageDrawable(null);

		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(
						info.processName, PackageManager.GET_META_DATA));

				ApplicationInfo ai = pm.getApplicationInfo(info.processName,
						totalRunningApps);

				if (!String.valueOf(info.processName).equals(
						CommonValues.APPLICATION_PACKAGE_NAME)) {
					if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {

						if (VIPDBatteryDoctorRunningAppsActivity.selectedApplicationList != null) {
							for (int count = 0; count < VIPDBatteryDoctorRunningAppsActivity.selectedApplicationList
									.size(); count++) {
								String currentAppName = String
										.valueOf(VIPDBatteryDoctorRunningAppsActivity.selectedApplicationList
												.get(count));
								if (String
										.valueOf(ai.loadLabel(packageManager))
										.equals(currentAppName)) {
									IsMatchhappen = true;
									break;
								}
							}
						}

						if (IsMatchhappen) {
							IsMatchhappen = false;
							continue;
						}

						switch (runningApps) {
						case 0:
							ivRunningAppsOne.setVisibility(View.VISIBLE);
							ivRunningAppsOne.setImageDrawable(ai.loadIcon(pm));
							break;
						case 1:
							ivRunningAppsTwo.setVisibility(View.VISIBLE);
							ivRunningAppsTwo.setImageDrawable(ai.loadIcon(pm));
							break;
						case 2:
							ivRunningAppsThree.setVisibility(View.VISIBLE);
							ivRunningAppsThree
									.setImageDrawable(ai.loadIcon(pm));
							break;
						case 3:
							ivRunningAppsFour.setVisibility(View.VISIBLE);
							ivRunningAppsFour.setImageDrawable(ai.loadIcon(pm));
							break;
						case 4:
							ivRunningAppsFive.setVisibility(View.VISIBLE);
							ivRunningAppsFive.setImageDrawable(ai.loadIcon(pm));
							break;
						case 5:
							// ivRunningAppsSix.setVisibility(View.VISIBLE);
							// ivRunningAppsSix.setImageDrawable(ai.loadIcon(pm));
							break;
						}

						runningApps++;

					}
				}
				Log.e("LABEL", c.toString());
				totalRunningApps++;

				// android.os.Process.killProcess(info.pid);
			} catch (Exception e) {
				// Name Not FOund Exception
			}
		}

		if (totalRunningApps > 0) {
			rlRunningAppsList.setVisibility(View.VISIBLE);
		}
	}
	private void setScreenBrigthness() {
		try {
			curBrightnessValue=android.provider.Settings.System.getInt(
					getContentResolver(),
					android.provider.Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (curBrightnessValue >= 255 || curBrightnessValue >= 0 ) {
			curBrightnessValue = 0;
		} else {
			curBrightnessValue += 10;
		}

		android.provider.Settings.System.putInt(getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS,
				curBrightnessValue);

		

	}
	private void displayScreenBrightness() {

		try {
			curBrightnessValue = android.provider.Settings.System.getInt(
					getContentResolver(),
					android.provider.Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * curBrightnessValue = android.provider.Settings.System.getInt(
		 * getActivity().getContentResolver(),
		 * android.provider.Settings.System.SCREEN_BRIGHTNESS);
		 */

		if (curBrightnessValue > 0) {

			ChangeBrigthnessImageg();

		}
	}
		private void ChangeBrigthnessImageg() {
			 try {
				 curBrightnessValue=android.provider.Settings.System.getInt(
						getContentResolver(),
						android.provider.Settings.System.SCREEN_BRIGHTNESS);
			} catch (SettingNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	@Override
	public void onClick(View view) {

		int tempTime=0;
		if (view.getId() == R.id.rlGPS) {

			if (ivGPSOff.getVisibility() == View.VISIBLE) {
				ivGPSOff.setVisibility(View.GONE);
				ivGPSOn.setVisibility(View.VISIBLE);
			} else {
				ivGPSOff.setVisibility(View.VISIBLE);
				ivGPSOn.setVisibility(View.GONE);
			}
			
		}

		if (view.getId() == R.id.rlRunningAppsList) {
			Intent intent = new Intent(this,
					VIPDBatteryDoctorRunningAppsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}

		if (view.getId() == R.id.rlTimeout) {
			if (ivScreenTimeoutOff.getVisibility() == View.VISIBLE) {
				ivScreenTimeoutOff.setVisibility(View.GONE);
				ivScreenTimeoutOn.setVisibility(View.VISIBLE);
			} else {
				ivScreenTimeoutOff.setVisibility(View.VISIBLE);
				ivScreenTimeoutOn.setVisibility(View.GONE);
			}
		
		}
		if (view.getId() == R.id.rlBrigthness) {
			if (ivBrigthnessOff.getVisibility() == View.VISIBLE) {
				ivBrigthnessOff.setVisibility(View.GONE);
				ivBrigthnessOn.setVisibility(View.VISIBLE);
			} else {
				ivBrigthnessOff.setVisibility(View.VISIBLE);
				ivBrigthnessOn.setVisibility(View.GONE);
			}
			
			
		}
		if (view.getId() == R.id.rlRunningApps) {
			if (imRunningAppsOff.getVisibility() == View.VISIBLE) {
				imRunningAppsOff.setVisibility(View.GONE);
				imRunningAppsOn.setVisibility(View.VISIBLE);
			} else {
				imRunningAppsOff.setVisibility(View.VISIBLE);
				imRunningAppsOn.setVisibility(View.GONE);
			}
		}

		if (view.getId() == R.id.ivSaveNow) {
			if (ivGPSOn.getVisibility() == View.VISIBLE) {
				// Should Implement GPS stopped code here.
			}

			if (ivBrigthnessOn.getVisibility() == View.VISIBLE) {
				setScreenBrigthness();
		
				
				  
				  
				//old code
				/*int brightness = 204;

				// brightness set
				android.provider.Settings.System.putInt(getContentResolver(),
						android.provider.Settings.System.SCREEN_BRIGHTNESS,
						brightness);
				
				tempTime+=4;*/

			}
			if (ivScreenTimeoutOn.getVisibility() == View.VISIBLE) {
				int timeout = 60000;
				android.provider.Settings.System.putInt(getContentResolver(),
						android.provider.Settings.System.SCREEN_OFF_TIMEOUT,
						timeout);
				
				tempTime+=3;
			}
			if (imRunningAppsOn.getVisibility() == View.VISIBLE) {
				KillAllRunningApps();
			}

			
			
			CommonValues.getInstance().BatteryDoctorSavedMin=(int) Math.round(runningApps*8.2)+tempTime;
			
			Intent intent = new Intent(this, OptimizationResultActivity.class);
			OptimizationResultActivity.calledFromInfo="batterydoctor";
			OptimizationResultActivity.optimizationMessage=CommonValues.getInstance().BatteryDoctorSavedMin!=0?CommonValues.getInstance().BatteryDoctorSavedMin+" mins optimized.":"Already optimized.";
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			
			/*Intent intent = new Intent(this, VIPD_BatteryDoctor.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);*/
		}
	}
}
