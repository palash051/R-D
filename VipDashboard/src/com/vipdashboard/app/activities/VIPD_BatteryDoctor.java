package com.vipdashboard.app.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.BatterydoctorAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.customcontrols.CircularProgressBar;
import com.vipdashboard.app.customcontrols.CircularProgressBar.ProgressAnimationListener;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_BatteryDoctor extends MainActionbarBase implements
		OnClickListener, OnContactSelectedListener {
	TextView tvBatteryPerformance, tvRemainingTime;

	boolean running, isPressCharge, isPressedMore, isPressSave;
	int progress = 0;
	RelativeLayout rlCharge, rlMode, rlBatteryState, rlRank,rlSave;
	CircularProgressBar circularProgressBar;
	BatterydoctorAdapter adapter;

	ViewPager viewPager;
	boolean brightnessValue, timeoutValue, gpsValue, currentGPSState;
	boolean iscallfromOptimization, iscallfromdiagnose;
	ImageView ivSmartSaving;

	double gBatteryCapacity, CurrentPower;
	LinearLayout llSutter;
	
	public static boolean IsCalledFromOptimization=false;

	String Manufacturer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_doctor);
		Initalization();
		setProgressValue();
		registerBatteryLevelReceiver();
		getBatteryCapacity();

	}

	private void Initalization() {
		tvBatteryPerformance = (TextView) findViewById(R.id.tvBatteryPerformance);
		tvRemainingTime = (TextView) findViewById(R.id.tvRemainingTime);
		rlBatteryState = (RelativeLayout) findViewById(R.id.rlBatteryState);
		rlCharge = (RelativeLayout) findViewById(R.id.rlCharge);
		llSutter = (LinearLayout) findViewById(R.id.llSutter);
		rlMode = (RelativeLayout) findViewById(R.id.rlMode);
		//rlSave = (RelativeLayout) findViewById(R.id.rlSave);
		viewPager = (ViewPager) findViewById(R.id.Headerpager);
		ivSmartSaving = (ImageView) findViewById(R.id.ivSmartSaving);

		rlRank = (RelativeLayout) findViewById(R.id.rlRank);
		circularProgressBar = (CircularProgressBar) findViewById(R.id.circularprogressbar);

		adapter = new BatterydoctorAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);

		/*rlCharge.setOnTouchListener(this);
		rlMode.setOnTouchListener(this);*/
		rlCharge.setOnClickListener(this);
		rlMode.setOnClickListener(this);
		llSutter.setOnClickListener(this);
		ivSmartSaving.setOnClickListener(this);
		//rlSave.setOnClickListener(this);
		rlRank.setOnClickListener(this);
		rlBatteryState.setOnClickListener(this);
		circularProgressBar.setProgress(100);
		circularProgressBar.setOnClickListener(this);
	}

	private void setProgressValue() {
		final Runnable r = new Runnable() {
			public void run() {
				running = true;
				while (progress < 361) {
					progress++;
				}
				running = false;
			}
		};

		if (!running) {
			progress = 0;
			// progressWheel.resetCount();
			Thread s = new Thread(r);
			s.start();
		}
	}

	@Override
	protected void onPause() {
		MyNetApplication.activityResumed();
		super.onPause();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityPaused();
		super.onResume();
		//#A52631
		rlCharge.setBackgroundColor(Color.parseColor("#000099"));
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,
					"Mobile SIM card is not installed.\nPlease install it.");
		} else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask
					.DryConnectivityMessage(this,
							"No Internet Connection.\nPlease enable your connection first.");
		} else if (CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION) {
			if (!isFinishing()) {
			CommonTask.DryConnectivityMessage(this,
					CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode = CommonConstraints.NO_EXCEPTION;
			}
		}
		checkDefaultState();
		
		if(IsCalledFromOptimization)
		{
			registerBatteryLevelReceiver();
		}
	}

	private void checkDefaultState() {
		try {
			int brightness = 204;
			int timeout = 60000;

			circularProgressBar.setTitle("SAVE POWER");
			circularProgressBar.setTitleColor(Color.parseColor("#FFFFFF"));
			circularProgressBar.setSubTitle("Tap to diagnose");
			circularProgressBar.mProgressColorPaint.setColor(getResources()
					.getColor(R.color.circular_progress_default_progress));
			iscallfromOptimization = false;
			iscallfromdiagnose = true;

			int currentBrightness = Settings.System.getInt(
					getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
			int currentTimeOut = Settings.System.getInt(getContentResolver(),
					Settings.System.SCREEN_OFF_TIMEOUT);
			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				currentGPSState = true;
			} else {
				currentGPSState = false;
			}

			if (currentBrightness > brightness || currentTimeOut > timeout
					|| currentGPSState) {
				circularProgressBar.setTitle("Draining Fast");
				circularProgressBar.setSubTitle("Start Optimizing");
				circularProgressBar.mProgressColorPaint.setColor(Color
						.parseColor("#A52631"));
				if (currentBrightness > brightness) {
					brightnessValue = true;
				} else {
					brightnessValue = false;
				}
				if (currentTimeOut > timeout) {
					timeoutValue = true;
				} else {
					timeoutValue = false;
				}
				iscallfromOptimization = true;
				iscallfromdiagnose = false;
			} else {
				circularProgressBar.setTitle("SAVE POWER");
				circularProgressBar.setTitleColor(Color.parseColor("#FFFFFF"));
				circularProgressBar.setSubTitle("Tap to diagnose");
				circularProgressBar.mProgressColorPaint.setColor(getResources()
						.getColor(R.color.circular_progress_default_progress));
				iscallfromOptimization = false;
				iscallfromdiagnose = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
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

			double voltage = bundle.getInt("voltage", 0);

			if (voltage > 0) {
				/*
				 * BigDecimal temp=(BigDecimal )voltage / 1000; voltage =
				 * voltage.setScale(2, RoundingMode.CEILING);
				 */

				voltage = Math.round((voltage / 1000) * 10.0) / 10.0;
			}

			if (isPresent) {
				if (rawlevel >= 0 && scale > 0) {
					level = (rawlevel * 100) / scale;

					CurrentPower = (gBatteryCapacity / 100) * level;
				}

				tvBatteryPerformance.setText("" + level + "%");

				double processedCategory = CommonValues.getInstance().BATTERY_DOCTOR_DEFAULT;

				if (gBatteryCapacity == 2100) {
					processedCategory = CommonValues.getInstance().BATTERY_DOCTOR_TWO_ONE_ZERO_ZERO;
				} else if (gBatteryCapacity == 1000) {
					processedCategory = CommonValues.getInstance().BATTERY_DOCTOR_ONE_ZERO_ZERO_ZERO;
				} else if (CommonValues.getInstance().LoginUser.UserNumber == 316
						&& Manufacturer == "HTC") {
					processedCategory = CommonValues.getInstance().BATTERY_DOCTOR_IR;
				} else if (gBatteryCapacity > 2100) {
					processedCategory = CommonValues.getInstance().BATTERY_DOCTOR_UPPER_THAN_TWO_ONE_ZERO_ZERO;
				}

				/*
				 * if (voltage == 3.6) { processedCategory =
				 * CommonValues.getInstance
				 * ().BATTERY_DOCTOR_THREE_POINT_SIX_DEFAULT; } if (voltage ==
				 * 3.7) { processedCategory = CommonValues.getInstance().
				 * BATTERY_DOCTOR_THREE_POINT_SEVEN_DEFAULT; } else if (voltage
				 * == 3.8) { processedCategory = CommonValues.getInstance().
				 * BATTERY_DOCTOR_THREE_POINT_EIGHT_DEFAULT; } else if
				 * (processedCategory == 4.1) { processedCategory =
				 * CommonValues.
				 * getInstance().BATTERY_DOCTOR_FOUR_POINT_ONE_DEFAULT; } else
				 * if (processedCategory == 4.2) { processedCategory =
				 * CommonValues
				 * .getInstance().BATTERY_DOCTOR_FOUR_POINT_TWO_DEFAULT; }
				 */

				double estimatedTime = (CurrentPower * processedCategory)+CommonValues.getInstance().BatteryDoctorSavedMin;

				int hours = (int) (estimatedTime / 60); // since both are ints,
														// you get an int
				int minutes = (int) Math.ceil((estimatedTime % 60));
				// System.out.printf("%d:%02d", hours, minutes);

				tvRemainingTime.setText(hours + "h " + minutes + "m");

				CommonValues.getInstance().BatteryDoctorSavedMin=0;
				IsCalledFromOptimization=false;
				
				/*
				 * String value = String.valueOf(new DecimalFormat("##.##")
				 * .format(estimatedTime));
				 * 
				 * if (estimatedTime <= 24.9) { String[] es =
				 * value.split("\\."); if (es.length < 2)
				 * tvRemainingTime.setText(es[0] + "h " + "00m"); else
				 * tvRemainingTime.setText(es[0] + "h " + es[1] + "m"); } else {
				 * String[] es = value.split("\\."); if (es.length < 2)
				 * tvRemainingTime.setText("1d " + " 00h"); else
				 * tvRemainingTime.setText("1d " + es[1] + "h"); }
				 */
			}
		}
	};

	public void getBatteryCapacity() {
		Object mPowerProfile_ = null;
		// double batteryCapacity = 0;
		final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

		try {
			mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
					.getConstructor(Context.class).newInstance(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			gBatteryCapacity = (Double) Class.forName(POWER_PROFILE_CLASS)
					.getMethod("getAveragePower", java.lang.String.class)
					.invoke(mPowerProfile_, "battery.capacity");

			TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			Manufacturer = Build.MANUFACTURER;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String getHealthString(int health) {
		String healthString = "Unknown";

		switch (health) {
		case BatteryManager.BATTERY_HEALTH_DEAD:
			healthString = "Dead";
			break;
		case BatteryManager.BATTERY_HEALTH_GOOD:
			healthString = "Good";
			break;
		case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
			healthString = "Over Voltage";
			break;
		case BatteryManager.BATTERY_HEALTH_OVERHEAT:
			healthString = "Over Heat";
			break;
		case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
			healthString = "Failure";
			break;
		}

		return healthString;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.rlCharge) {
			Intent intent = new Intent(this,
					VIPD_BatteryDoctorChargeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlMode) {
			Intent intent = new Intent(this,
					VIPD_BatteryDoctorMoreActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.ivSmartSaving) {
			Intent intent = new Intent(this,
					VIPDBatteryDoctorSmartSavingActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlRank) {
			Intent intentBatteryUsage = new Intent(
					Intent.ACTION_POWER_USAGE_SUMMARY);
			startActivity(intentBatteryUsage);
		} else if (view.getId() == R.id.rlBatteryState) {
			Intent intent = new Intent(this,
					VIPDBatteryDoctorInfoActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);

		}
		if (view.getId() == R.id.llSutter) {
			if (viewPager.getVisibility() == View.VISIBLE) {
				viewPager.setVisibility(View.GONE);
				checkDefaultState();
			} else {
				viewPager.setVisibility(View.VISIBLE);
			}
		}/*
		 * else if (view.getId() == R.id.circularprogressbar) { if
		 * (iscallfromOptimization) { Intent intent = new
		 * Intent(VIPD_BatteryDoctor.this,
		 * VIPDBatteryDoctorTaptoDiagnoseActivity.class); ======= }
		 */
		/*
		 * else if (view.getId() == R.id.circularprogressbar) { if
		 * (iscallfromOptimization) { Intent intent = new
		 * Intent(VIPD_BatteryDoctor.this,
		 * VIPDBatteryDoctorTaptoDiagnoseActivity.class);
		 * intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		 * intent.putExtra("ISBRIGHTNESSNEEDTOOPTIMIZE", brightnessValue);
		 * intent.putExtra("ISGPSNEEDTOOPTIMIZE", currentGPSState);
		 * intent.putExtra("ISTIMEOUTNEEDTOOPTIMIZE", timeoutValue);
		 * startActivity(intent); } else if (iscallfromdiagnose) {
		 * circularProgressBar.animateProgressTo(0, 100, new
		 * ProgressAnimationListener() {
		 * 
		 * @Override public void onAnimationStart() {
		 * circularProgressBar.setTitle("Diagnosing");
		 * circularProgressBar.setTitleColor(Color .parseColor("#FFFFFF"));
		 * circularProgressBar.setSubTitle("Reating..."); }
		 * 
		 * @Override public void onAnimationProgress(int progress) { try {
		 * 
		 * circularProgressBar.setTitle("Diagnosing");
		 * circularProgressBar.setTitleColor(Color .parseColor("#FFFFFF"));
		 * circularProgressBar .setSubTitle("Brightness"); int brightness = 204;
		 * int currentBrightness = Settings.System .getInt(getContentResolver(),
		 * Settings.System.SCREEN_BRIGHTNESS); if (currentBrightness >
		 * brightness) { brightnessValue = true; } else { brightnessValue =
		 * false; }
		 * 
		 * circularProgressBar.setTitle("Diagnosing");
		 * circularProgressBar.setTitleColor(Color .parseColor("#FFFFFF"));
		 * circularProgressBar.setSubTitle("Timeout"); int timeout = 60000; int
		 * currentTimeOut = Settings.System .getInt(getContentResolver(),
		 * Settings.System.SCREEN_OFF_TIMEOUT); if (currentTimeOut > timeout) {
		 * timeoutValue = true; } else { timeoutValue = false; }
		 * 
		 * circularProgressBar.setTitle("Diagnosing");
		 * circularProgressBar.setTitleColor(Color .parseColor("#FFFFFF"));
		 * circularProgressBar.setSubTitle("GPS"); LocationManager
		 * locationManager = (LocationManager)
		 * getSystemService(Context.LOCATION_SERVICE); if (locationManager
		 * .isProviderEnabled(LocationManager.GPS_PROVIDER)) { currentGPSState =
		 * true; } else { currentGPSState = false; } } catch (Exception e) {
		 * e.printStackTrace(); } }
		 * 
		 * @Override public void onAnimationFinish() {
		 * circularProgressBar.setTitle("Good");
		 * circularProgressBar.setTitleColor(Color .parseColor("#FFFFFF"));
		 * circularProgressBar.setSubTitle("Done"); try { Thread.sleep(500); }
		 * catch (InterruptedException e) { e.printStackTrace(); } Intent intent
		 * = new Intent( VIPD_BatteryDoctor.this,
		 * VIPDBatteryDoctorTaptoDiagnoseActivity.class);
		 * intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		 * intent.putExtra("ISBRIGHTNESSNEEDTOOPTIMIZE", brightnessValue);
		 * intent.putExtra("ISGPSNEEDTOOPTIMIZE", currentGPSState);
		 * intent.putExtra("ISTIMEOUTNEEDTOOPTIMIZE", timeoutValue);
		 * startActivity(intent); } }); }
		 * 
		 * }
		 */

		else if (view.getId() == R.id.circularprogressbar) {
			if (iscallfromOptimization) {
				Intent intent = new Intent(VIPD_BatteryDoctor.this,
						VIPDBatteryDoctorTaptoDiagnoseActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra("ISBRIGHTNESSNEEDTOOPTIMIZE", brightnessValue);
				intent.putExtra("ISGPSNEEDTOOPTIMIZE", currentGPSState);
				intent.putExtra("ISTIMEOUTNEEDTOOPTIMIZE", timeoutValue);
				startActivity(intent);
			} else if (iscallfromdiagnose) {
				circularProgressBar.animateProgressTo(0, 100,
						new ProgressAnimationListener() {
							@Override
							public void onAnimationStart() {
								circularProgressBar.setTitle("Diagnosing");
								circularProgressBar.setTitleColor(Color
										.parseColor("#FFFFFF"));
								circularProgressBar.setSubTitle("Reating...");
							}

							@Override
							public void onAnimationProgress(int progress) {
								try {									
									circularProgressBar.setTitle("Diagnosing");
									circularProgressBar.setTitleColor(Color
											.parseColor("#FFFFFF"));
									circularProgressBar.setSubTitle("GPS");
									
									LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
									if (locationManager
											.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
										currentGPSState = true;
									} else {
										currentGPSState = false;
									}

									circularProgressBar.setTitle("Diagnosing");
									circularProgressBar.setTitleColor(Color
											.parseColor("#FFFFFF"));
									circularProgressBar.setSubTitle("Timeout");
									int timeout = 60000;
									int currentTimeOut = Settings.System
											.getInt(getContentResolver(),
													Settings.System.SCREEN_OFF_TIMEOUT);
									if (currentTimeOut > timeout) {
										timeoutValue = true;
									} else {
										timeoutValue = false;
									}
									circularProgressBar.setTitle("Diagnosing");
									circularProgressBar.setTitleColor(Color
											.parseColor("#FFFFFF"));
									circularProgressBar
											.setSubTitle("Brightness");
									int brightness = 204;
									int currentBrightness = Settings.System
											.getInt(getContentResolver(),
													Settings.System.SCREEN_BRIGHTNESS);
									if (currentBrightness > brightness) {
										brightnessValue = true;
									} else {
										brightnessValue = false;
									}

									
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onAnimationFinish() {
								circularProgressBar.setTitle("Good");
								circularProgressBar.setTitleColor(Color
										.parseColor("#FFFFFF"));
								circularProgressBar.setSubTitle("Done");
								try {
									Thread.sleep(500);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								Intent intent = new Intent(
										VIPD_BatteryDoctor.this,
										VIPDBatteryDoctorTaptoDiagnoseActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
								intent.putExtra("ISBRIGHTNESSNEEDTOOPTIMIZE",
										brightnessValue);
								intent.putExtra("ISGPSNEEDTOOPTIMIZE",
										currentGPSState);
								intent.putExtra("ISTIMEOUTNEEDTOOPTIMIZE",
										timeoutValue);
								startActivity(intent);
							}
						});
			}
		}

	}

	@Override
	public void onContactNameSelected(long contactId) {

	}

	@Override
	public void onContactNumberSelected(String contactNumber, String contactName) {

	}

	/*@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (view.getId() == R.id.rlCharge) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				rlCharge.setBackgroundColor(Color.parseColor("#FF9900"));
				break;
			case MotionEvent.ACTION_UP:
				Intent intent = new Intent(this,
						VIPD_BatteryDoctorChargeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				rlCharge.setBackgroundColor(Color.parseColor("#FF9900"));
				break;
			default:
				break;
			}
		} else if (view.getId() == R.id.rlMode) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				rlMode.setBackgroundColor(Color.parseColor("#FF9900"));
				break;
			case MotionEvent.ACTION_UP:
				Intent intent = new Intent(this,
						VIPD_BatteryDoctorMoreActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				rlMode.setBackgroundColor(Color.parseColor("#FF9900"));
				break;
			default:
				break;
			}
		}
		else if (view.getId() == R.id.rlSave) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				rlSave.setBackgroundColor(Color.parseColor("#FF9900"));
				break;
			case MotionEvent.ACTION_UP:
				Intent intent = new Intent(this,
						VIPD_BatteryDoctor.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				rlSave.setBackgroundColor(Color.parseColor("#FF9900"));
				break;
			default:
				break;
			}
		}
		return true;
	}*/
	//message_send_button
	//
}
