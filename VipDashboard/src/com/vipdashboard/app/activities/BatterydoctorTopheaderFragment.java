package com.vipdashboard.app.activities;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.BatterydoctorAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BatterydoctorTopheaderFragment extends
		android.support.v4.app.Fragment implements OnClickListener {

	Context context;
	
	private OnContactSelectedListener mContactsListener;

	BatterydoctorAdapter adapter;

	WifiManager wifiManager;

	static int setTimeout = 0, curBrightnessValue = 0, rigtoneSound = 0;

	RelativeLayout rlWifi, rlDataConnection, rlBrigthness, rlRingToneType,
			rlViberation, rlGPS, rlTimeout, rlAirplaneMode;

	TextView tvOperationInfo;

	ImageView ivWifiNormal, ivWifiSelected, ivDataConnectionNormal,
			ivDataConnectionSelected, ivViberationNormal, ivViberationSelected,
			ivGPSNormal, ivGPSSelected, ivAirplaneModeNormal,
			ivAirplaneModeSelected;

	ImageView ivTimeoutFiftheenSeconds, ivTimeoutThirtySeconds,
			ivTimeoutOneMintue, ivTimeoutTwoMinutes, ivTimeoutTenMinutes,
			ivTimeoutThirtyMinutes;

	ImageView ivBrigthnessFirst, ivBrigthnessSecond, ivBrigthnessThird,
			ivBrigthnessFourth, ivBrigthnessFifth;

	ImageView ivRingToneTwo,
	ivRingToneThree,
	ivRingToneOne,
	ivRingToneSlient;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = (ViewGroup) inflater.inflate(
				R.layout.battery_doctor_header, container, false);

		tvOperationInfo = (TextView) root.findViewById(R.id.tvOperationInfo);
		ivWifiNormal = (ImageView) root.findViewById(R.id.ivWifiNormal);
		ivWifiSelected = (ImageView) root.findViewById(R.id.ivWifiSelected);
		ivDataConnectionNormal = (ImageView) root
				.findViewById(R.id.ivDataConnectionNormal);
		ivDataConnectionSelected = (ImageView) root
				.findViewById(R.id.ivDataConnectionSelected);
		ivViberationNormal = (ImageView) root
				.findViewById(R.id.ivViberationNormal);
		ivViberationSelected = (ImageView) root
				.findViewById(R.id.ivViberationSelected);
		ivGPSNormal = (ImageView) root.findViewById(R.id.ivGPSNormal);
		ivGPSSelected = (ImageView) root.findViewById(R.id.ivGPSSelected);

		ivTimeoutFiftheenSeconds = (ImageView) root
				.findViewById(R.id.ivTimeoutFiftheenSeconds);
		ivTimeoutThirtySeconds = (ImageView) root
				.findViewById(R.id.ivTimeoutThirtySeconds);
		ivTimeoutOneMintue = (ImageView) root
				.findViewById(R.id.ivTimeoutOneMintue);
		ivTimeoutTwoMinutes = (ImageView) root
				.findViewById(R.id.ivTimeoutTwoMinutes);
		ivTimeoutTenMinutes = (ImageView) root
				.findViewById(R.id.ivTimeoutTenMinutes);
		ivTimeoutThirtyMinutes = (ImageView) root
				.findViewById(R.id.ivTimeoutThirtyMinutes);

		ivAirplaneModeNormal = (ImageView) root
				.findViewById(R.id.ivAirplaneModeNormal);
		ivAirplaneModeSelected = (ImageView) root
				.findViewById(R.id.ivAirplaneModeSelected);

		rlWifi = (RelativeLayout) root.findViewById(R.id.rlWifi);
		rlDataConnection = (RelativeLayout) root
				.findViewById(R.id.rlDataConnection);
		rlBrigthness = (RelativeLayout) root.findViewById(R.id.rlBrigthness);
		rlRingToneType = (RelativeLayout) root
				.findViewById(R.id.rlRingToneType);
		rlViberation = (RelativeLayout) root.findViewById(R.id.rlViberation);
		rlGPS = (RelativeLayout) root.findViewById(R.id.rlGPS);
		rlTimeout = (RelativeLayout) root.findViewById(R.id.rlTimeout);
		rlAirplaneMode = (RelativeLayout) root
				.findViewById(R.id.rlAirplaneMode);

		ivBrigthnessFirst = (ImageView) root
				.findViewById(R.id.ivBrigthnessFirst);
		ivBrigthnessSecond = (ImageView) root
				.findViewById(R.id.ivBrigthnessSecond);
		ivBrigthnessThird = (ImageView) root
				.findViewById(R.id.ivBrigthnessThird);
		ivBrigthnessFourth = (ImageView) root
				.findViewById(R.id.ivBrigthnessFourth);
		ivBrigthnessFifth = (ImageView) root
				.findViewById(R.id.ivBrigthnessFifth);
		
		ivRingToneTwo= (ImageView) root
				.findViewById(R.id.ivRingToneTwo);
		ivRingToneThree= (ImageView) root
				.findViewById(R.id.ivRingToneThree);
		ivRingToneOne= (ImageView) root
				.findViewById(R.id.ivRingToneOne);
		ivRingToneSlient= (ImageView) root
				.findViewById(R.id.ivRingToneSlient);

		rlWifi.setOnClickListener(this);
		rlDataConnection.setOnClickListener(this);
		rlBrigthness.setOnClickListener(this);
		rlRingToneType.setOnClickListener(this);
		rlViberation.setOnClickListener(this);
		rlGPS.setOnClickListener(this);
		rlTimeout.setOnClickListener(this);
		rlAirplaneMode.setOnClickListener(this);

		return root;
	}

	@Override
	public void onClick(View view) {

		if (view.getId() == R.id.rlWifi
				&& ivWifiNormal.getVisibility() == View.VISIBLE) {
			tvOperationInfo.setText("Wifi :On");
			wifiManager.setWifiEnabled(true);
			ChangeWifiStatus();
		} else if (view.getId() == R.id.rlWifi
				&& ivWifiSelected.getVisibility() == View.VISIBLE) {
			tvOperationInfo.setText("Wifi :On");
			wifiManager.setWifiEnabled(false);
			ChangeWifiStatus();
		} else if (view.getId() == R.id.rlDataConnection
				&& ivDataConnectionNormal.getVisibility() == View.VISIBLE) {
			turnOnDataConnection();
			ivDataConnectionNormal.setVisibility(View.GONE);
			ivDataConnectionSelected.setVisibility(View.VISIBLE);
		} else if (view.getId() == R.id.rlDataConnection
				&& ivDataConnectionSelected.getVisibility() == View.VISIBLE) {
			turnOFFDataConnection();
			ivDataConnectionNormal.setVisibility(View.VISIBLE);
			ivDataConnectionSelected.setVisibility(View.GONE);
		} else if (view.getId() == R.id.rlBrigthness) {
			setScreenBrigthness();
		} else if (view.getId() == R.id.rlRingToneType) {
			maxVolume();
		} else if (view.getId() == R.id.rlViberation
				&& ivViberationNormal.getVisibility() == View.VISIBLE) {
			VibrateMode();
			ivViberationNormal.setVisibility(View.GONE);
			ivViberationSelected.setVisibility(View.VISIBLE);
			ivRingToneTwo.setVisibility(View.GONE);
			ivRingToneThree.setVisibility(View.GONE);
			ivRingToneOne.setVisibility(View.GONE);
			ivRingToneSlient.setVisibility(View.VISIBLE);
		} else if (view.getId() == R.id.rlViberation
				&& ivViberationSelected.getVisibility() == View.VISIBLE) {
			maxVolume();
			ivViberationNormal.setVisibility(View.VISIBLE);
			ivViberationSelected.setVisibility(View.GONE);
		} else if (view.getId() == R.id.rlGPS) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(intent);

		} else if (view.getId() == R.id.rlTimeout) {
			setTimeout(setTimeout);
		} else if (view.getId() == R.id.rlAirplaneMode) {
			startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
			/*Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
			startActivity(intent);*/
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		wifiManager = (WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE);
		GetcurentStatus();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);		
		try {			
			mContactsListener = (OnContactSelectedListener) activity;
		} catch (ClassCastException	e) {
			throw new ClassCastException(activity.toString() + " must implement OnContactSelectedListener");
		}
	}

	private void GetcurentStatus() {
		ChangeWifiStatus();
		GetRingStatus();
		displayGPSInfo();
		displayAirplaneMode();
		displayDataConnectionInfo();
		displayCurrnetTimeout();
		displayScreenBrightness();
	}

	private void ChangeWifiStatus() {

		boolean wifiEnabled = wifiManager.isWifiEnabled();

		if (wifiEnabled) {
			ivWifiNormal.setVisibility(View.GONE);
			ivWifiSelected.setVisibility(View.VISIBLE);

		} else {
			ivWifiNormal.setVisibility(View.VISIBLE);
			ivWifiSelected.setVisibility(View.GONE);
		}

		tvOperationInfo.setText("");

	}

	private void displayAirplaneMode() {
		int status = Settings.System.getInt(getActivity().getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0);

		if (status == 0) {
			ivAirplaneModeNormal.setVisibility(View.VISIBLE);
			ivAirplaneModeSelected.setVisibility(View.GONE);
		} else if (status == 1) {
			ivAirplaneModeNormal.setVisibility(View.GONE);
			ivAirplaneModeSelected.setVisibility(View.VISIBLE);
		}
	}

	private void displayGPSInfo() {
		LocationManager locationManager = (LocationManager) getActivity()
				.getSystemService(Context.LOCATION_SERVICE);

		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			ivGPSNormal.setVisibility(View.GONE);
			ivGPSSelected.setVisibility(View.VISIBLE);
		} else {
			ivGPSNormal.setVisibility(View.VISIBLE);
			ivGPSSelected.setVisibility(View.GONE);
		}
	}

	private void displayCurrnetTimeout() {
		int timeout = android.provider.Settings.System.getInt(getActivity()
				.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, -1);

		switch (timeout) {
		case 15000:
			ivTimeoutFiftheenSeconds.setVisibility(View.VISIBLE);
			ivTimeoutThirtySeconds.setVisibility(View.GONE);
			ivTimeoutOneMintue.setVisibility(View.GONE);
			ivTimeoutTwoMinutes.setVisibility(View.GONE);
			ivTimeoutTenMinutes.setVisibility(View.GONE);
			ivTimeoutThirtyMinutes.setVisibility(View.GONE);
			setTimeout = 0;
			break;
		case 30000:
			ivTimeoutFiftheenSeconds.setVisibility(View.GONE);
			ivTimeoutThirtySeconds.setVisibility(View.VISIBLE);
			ivTimeoutOneMintue.setVisibility(View.GONE);
			ivTimeoutTwoMinutes.setVisibility(View.GONE);
			ivTimeoutTenMinutes.setVisibility(View.GONE);
			ivTimeoutThirtyMinutes.setVisibility(View.GONE);
			setTimeout = 1;
			break;
		case 60000:
			ivTimeoutFiftheenSeconds.setVisibility(View.GONE);
			ivTimeoutThirtySeconds.setVisibility(View.GONE);
			ivTimeoutOneMintue.setVisibility(View.VISIBLE);
			ivTimeoutTwoMinutes.setVisibility(View.GONE);
			ivTimeoutTenMinutes.setVisibility(View.GONE);
			ivTimeoutThirtyMinutes.setVisibility(View.GONE);
			setTimeout = 2;
			break;
		case 120000:
			ivTimeoutFiftheenSeconds.setVisibility(View.GONE);
			ivTimeoutThirtySeconds.setVisibility(View.GONE);
			ivTimeoutOneMintue.setVisibility(View.GONE);
			ivTimeoutTwoMinutes.setVisibility(View.VISIBLE);
			ivTimeoutTenMinutes.setVisibility(View.GONE);
			ivTimeoutThirtyMinutes.setVisibility(View.GONE);
			setTimeout = 3;
			break;
		case 600000:
			ivTimeoutFiftheenSeconds.setVisibility(View.GONE);
			ivTimeoutThirtySeconds.setVisibility(View.GONE);
			ivTimeoutOneMintue.setVisibility(View.GONE);
			ivTimeoutTwoMinutes.setVisibility(View.GONE);
			ivTimeoutTenMinutes.setVisibility(View.VISIBLE);
			ivTimeoutThirtyMinutes.setVisibility(View.GONE);
			setTimeout = 4;
			break;
		case 1800000:
			ivTimeoutFiftheenSeconds.setVisibility(View.GONE);
			ivTimeoutThirtySeconds.setVisibility(View.GONE);
			ivTimeoutOneMintue.setVisibility(View.GONE);
			ivTimeoutTwoMinutes.setVisibility(View.GONE);
			ivTimeoutTenMinutes.setVisibility(View.GONE);
			ivTimeoutThirtyMinutes.setVisibility(View.VISIBLE);
			setTimeout = 5;
			break;
		default:
			ivTimeoutFiftheenSeconds.setVisibility(View.VISIBLE);
			ivTimeoutThirtySeconds.setVisibility(View.GONE);
			ivTimeoutOneMintue.setVisibility(View.GONE);
			ivTimeoutTwoMinutes.setVisibility(View.GONE);
			ivTimeoutTenMinutes.setVisibility(View.GONE);
			ivTimeoutThirtyMinutes.setVisibility(View.GONE);
			setTimeout = 0;
		}
	}

	private void displayDataConnectionInfo() {
		TelephonyManager telephonyManager = (TelephonyManager) getActivity()
				.getSystemService(Context.TELEPHONY_SERVICE);

		if (telephonyManager.getDataState() == TelephonyManager.DATA_SUSPENDED) {
			ivDataConnectionNormal.setVisibility(View.GONE);
			ivDataConnectionSelected.setVisibility(View.VISIBLE);
		} else {
			ivDataConnectionNormal.setVisibility(View.VISIBLE);
			ivDataConnectionSelected.setVisibility(View.GONE);
		}
	}

	private void displayScreenBrightness() {

		try {
			curBrightnessValue = android.provider.Settings.System.getInt(
					getActivity().getContentResolver(),
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

		} else {
			ivBrigthnessFirst.setVisibility(View.VISIBLE);
			ivBrigthnessSecond.setVisibility(View.GONE);
			ivBrigthnessThird.setVisibility(View.GONE);
			ivBrigthnessFourth.setVisibility(View.GONE);
			ivBrigthnessFifth.setVisibility(View.GONE);
		}
	}

	private void setScreenBrigthness() {
		try {
			curBrightnessValue=android.provider.Settings.System.getInt(
					getActivity().getContentResolver(),
					android.provider.Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (curBrightnessValue >= 255) {
			curBrightnessValue = 0;
		} else {
			curBrightnessValue += 51;
		}

		android.provider.Settings.System.putInt(getActivity()
				.getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS,
				curBrightnessValue);

		ChangeBrigthnessImageg();

	}

	private void ChangeBrigthnessImageg() {
		 try {
			 curBrightnessValue=android.provider.Settings.System.getInt(
					getActivity().getContentResolver(),
					android.provider.Settings.System.SCREEN_BRIGHTNESS);
		} catch (SettingNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (curBrightnessValue >= 51 && curBrightnessValue <= 101) {
			ivBrigthnessFirst.setVisibility(View.GONE);
			ivBrigthnessSecond.setVisibility(View.VISIBLE);
			ivBrigthnessThird.setVisibility(View.GONE);
			ivBrigthnessFourth.setVisibility(View.GONE);
			ivBrigthnessFifth.setVisibility(View.GONE);
		} else if (curBrightnessValue >= 102 && curBrightnessValue <= 153) {
			ivBrigthnessFirst.setVisibility(View.GONE);
			ivBrigthnessSecond.setVisibility(View.GONE);
			ivBrigthnessThird.setVisibility(View.VISIBLE);
			ivBrigthnessFourth.setVisibility(View.GONE);
			ivBrigthnessFifth.setVisibility(View.GONE);
		} else if (curBrightnessValue >= 153 && curBrightnessValue <= 203) {
			ivBrigthnessFirst.setVisibility(View.GONE);
			ivBrigthnessSecond.setVisibility(View.GONE);
			ivBrigthnessThird.setVisibility(View.GONE);
			ivBrigthnessFourth.setVisibility(View.VISIBLE);
			ivBrigthnessFifth.setVisibility(View.GONE);
		} else if (curBrightnessValue >= 204 && curBrightnessValue <= 254) {
			ivBrigthnessFirst.setVisibility(View.GONE);
			ivBrigthnessSecond.setVisibility(View.GONE);
			ivBrigthnessThird.setVisibility(View.GONE);
			ivBrigthnessFourth.setVisibility(View.VISIBLE);
			ivBrigthnessFifth.setVisibility(View.GONE);
		} else if (curBrightnessValue >= 255) {
			ivBrigthnessFirst.setVisibility(View.GONE);
			ivBrigthnessSecond.setVisibility(View.GONE);
			ivBrigthnessThird.setVisibility(View.GONE);
			ivBrigthnessFourth.setVisibility(View.GONE);
			ivBrigthnessFifth.setVisibility(View.VISIBLE);
		} else {
			ivBrigthnessFirst.setVisibility(View.VISIBLE);
			ivBrigthnessSecond.setVisibility(View.GONE);
			ivBrigthnessThird.setVisibility(View.GONE);
			ivBrigthnessFourth.setVisibility(View.GONE);
			ivBrigthnessFifth.setVisibility(View.GONE);
		}
	}

	private void setTimeout(int screenOffTimeout) {
		int time;
		switch (screenOffTimeout) {
		case 0:
			time = 15000;
			ivTimeoutFiftheenSeconds.setVisibility(View.VISIBLE);
			ivTimeoutThirtySeconds.setVisibility(View.GONE);
			ivTimeoutOneMintue.setVisibility(View.GONE);
			ivTimeoutTwoMinutes.setVisibility(View.GONE);
			ivTimeoutTenMinutes.setVisibility(View.GONE);
			ivTimeoutThirtyMinutes.setVisibility(View.GONE);
			break;
		case 1:
			time = 30000;
			ivTimeoutFiftheenSeconds.setVisibility(View.GONE);
			ivTimeoutThirtySeconds.setVisibility(View.VISIBLE);
			ivTimeoutOneMintue.setVisibility(View.GONE);
			ivTimeoutTwoMinutes.setVisibility(View.GONE);
			ivTimeoutTenMinutes.setVisibility(View.GONE);
			ivTimeoutThirtyMinutes.setVisibility(View.GONE);
			break;
		case 2:
			time = 60000;
			ivTimeoutFiftheenSeconds.setVisibility(View.GONE);
			ivTimeoutThirtySeconds.setVisibility(View.GONE);
			ivTimeoutOneMintue.setVisibility(View.VISIBLE);
			ivTimeoutTwoMinutes.setVisibility(View.GONE);
			ivTimeoutTenMinutes.setVisibility(View.GONE);
			ivTimeoutThirtyMinutes.setVisibility(View.GONE);
			break;
		case 3:
			time = 120000;
			ivTimeoutFiftheenSeconds.setVisibility(View.GONE);
			ivTimeoutThirtySeconds.setVisibility(View.GONE);
			ivTimeoutOneMintue.setVisibility(View.GONE);
			ivTimeoutTwoMinutes.setVisibility(View.VISIBLE);
			ivTimeoutTenMinutes.setVisibility(View.GONE);
			ivTimeoutThirtyMinutes.setVisibility(View.GONE);
			break;
		case 4:
			time = 600000;
			ivTimeoutFiftheenSeconds.setVisibility(View.GONE);
			ivTimeoutThirtySeconds.setVisibility(View.GONE);
			ivTimeoutOneMintue.setVisibility(View.GONE);
			ivTimeoutTwoMinutes.setVisibility(View.GONE);
			ivTimeoutTenMinutes.setVisibility(View.VISIBLE);
			ivTimeoutThirtyMinutes.setVisibility(View.GONE);
			break;
		case 5:
			time = 1800000;
			ivTimeoutFiftheenSeconds.setVisibility(View.GONE);
			ivTimeoutThirtySeconds.setVisibility(View.GONE);
			ivTimeoutOneMintue.setVisibility(View.GONE);
			ivTimeoutTwoMinutes.setVisibility(View.GONE);
			ivTimeoutTenMinutes.setVisibility(View.GONE);
			ivTimeoutThirtyMinutes.setVisibility(View.VISIBLE);
			break;
		default:
			ivTimeoutFiftheenSeconds.setVisibility(View.VISIBLE);
			ivTimeoutThirtySeconds.setVisibility(View.GONE);
			ivTimeoutOneMintue.setVisibility(View.GONE);
			ivTimeoutTwoMinutes.setVisibility(View.GONE);
			ivTimeoutTenMinutes.setVisibility(View.GONE);
			ivTimeoutThirtyMinutes.setVisibility(View.GONE);
			time = 15000;
			setTimeout = 0;
		}

		android.provider.Settings.System
				.putInt(getActivity().getContentResolver(),
						Settings.System.SCREEN_OFF_TIMEOUT, time);

		setTimeout++;
	}

	private void setMobileDataEnabled(Context context, boolean enabled) {
		/*
		 * final ConnectivityManager conman = (ConnectivityManager)
		 * context.getSystemService(Context.CONNECTIVITY_SERVICE); final Class
		 * conmanClass = Class.forName(conman.getClass().getName()); final Field
		 * connectivityManagerField = conmanClass.getDeclaredField("mService");
		 * connectivityManagerField.setAccessible(true); final Object
		 * connectivityManager = connectivityManagerField.get(conman); final
		 * Class connectivityManagerClass =
		 * Class.forName(connectivityManager.getClass().getName()); final Method
		 * setMobileDataEnabledMethod =
		 * connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled",
		 * Boolean.TYPE); setMobileDataEnabledMethod.setAccessible(true);
		 * 
		 * setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
		 */
	}

	private void GetRingStatus() {
		AudioManager am = (AudioManager) getActivity().getSystemService(
				Context.AUDIO_SERVICE);
		int rignMode = am.getRingerMode();
		
		rigtoneSound = am.getStreamVolume(AudioManager.STREAM_RING);

		switch (rignMode) {
		case 1: // vibarte
			ivViberationNormal.setVisibility(View.GONE);
			ivViberationSelected.setVisibility(View.VISIBLE);
			break;
		case 0:// Slient
			ivViberationNormal.setVisibility(View.VISIBLE);
			ivViberationSelected.setVisibility(View.GONE);
			break;
		case 2: // rigntone
			ivViberationNormal.setVisibility(View.VISIBLE);
			ivViberationSelected.setVisibility(View.GONE);
			break;
		}
		
		SetRignToneImage();

	}

	private void maxVolume() {
		/*
		 * AudioManager am= (AudioManager)
		 * getActivity().getSystemService(Context.AUDIO_SERVICE); int
		 * streamMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_RING);
		 * am.setStreamVolume(AudioManager.STREAM_RING, streamMaxVolume,
		 * AudioManager.FLAG_ALLOW_RINGER_MODES|AudioManager.FLAG_PLAY_SOUND);
		 */

		AudioManager am = (AudioManager) getActivity().getSystemService(
				Context.AUDIO_SERVICE);

		rigtoneSound += 2;

		int streamMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_RING);

		if (rigtoneSound > streamMaxVolume) {
			rigtoneSound = 0;
		}

		SetRignToneImage();

		am.setStreamVolume(AudioManager.STREAM_RING, rigtoneSound,
				AudioManager.FLAG_ALLOW_RINGER_MODES
						| AudioManager.FLAG_PLAY_SOUND);

		//am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	}

	private void SetRignToneImage() {
		
		if (rigtoneSound >= 1 && rigtoneSound <= 2) {
			ivRingToneTwo.setVisibility(View.GONE);
			ivRingToneThree.setVisibility(View.GONE);
			ivRingToneOne.setVisibility(View.VISIBLE);
			ivRingToneSlient.setVisibility(View.GONE);
			  ivViberationNormal.setVisibility(View.VISIBLE);
			    ivViberationSelected.setVisibility(View.GONE);
		} else if (rigtoneSound > 2 && rigtoneSound <= 4) {
			ivRingToneTwo.setVisibility(View.VISIBLE);
			ivRingToneThree.setVisibility(View.GONE);
			ivRingToneOne.setVisibility(View.GONE);
			ivRingToneSlient.setVisibility(View.GONE);
			ivViberationNormal.setVisibility(View.VISIBLE);
		    ivViberationSelected.setVisibility(View.GONE);
		} else if (rigtoneSound > 4 && rigtoneSound <= 6) {
			ivRingToneTwo.setVisibility(View.GONE);
			ivRingToneThree.setVisibility(View.VISIBLE);
			ivRingToneOne.setVisibility(View.GONE);
			ivRingToneSlient.setVisibility(View.GONE);
			ivViberationNormal.setVisibility(View.VISIBLE);
		    ivViberationSelected.setVisibility(View.GONE);
		} else if (rigtoneSound > 6 && rigtoneSound <= 8) {
			ivRingToneTwo.setVisibility(View.VISIBLE);
			ivRingToneThree.setVisibility(View.GONE);
			ivRingToneOne.setVisibility(View.GONE);
			ivRingToneSlient.setVisibility(View.GONE);
			ivViberationNormal.setVisibility(View.VISIBLE);
		    ivViberationSelected.setVisibility(View.GONE);
		}
		else
		{
			ivRingToneTwo.setVisibility(View.GONE);
			ivRingToneThree.setVisibility(View.GONE);
			ivRingToneOne.setVisibility(View.GONE);
			ivRingToneSlient.setVisibility(View.VISIBLE);	
		
			VibrateMode();
		    ivViberationNormal.setVisibility(View.GONE);
		    ivViberationSelected.setVisibility(View.VISIBLE);
		}
	}

	private void slientMode() {
		AudioManager am = (AudioManager) getActivity().getSystemService(
				Context.AUDIO_SERVICE);
		am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
	}

	private void VibrateMode() {
		AudioManager am = (AudioManager) getActivity().getSystemService(
				Context.AUDIO_SERVICE);
		am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
	}

	int bv = Build.VERSION.SDK_INT;

	private void turnOnDataConnection() {
		try {
			ConnectivityManager conman = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
			Class conmanClass = Class.forName(conman
					.getClass().getName());
			Field connectivityManagerField = conmanClass
					.getDeclaredField("mService");
			connectivityManagerField.setAccessible(true);
			Object connectivityManager = connectivityManagerField
					.get(conman);
			Class connectivityManagerClass = Class
					.forName(connectivityManager.getClass()
							.getName());
			Method setMobileDataEnabledMethod = connectivityManagerClass
					.getDeclaredMethod("setMobileDataEnabled",
							Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);
			setMobileDataEnabledMethod.invoke(connectivityManager,
					true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void turnOFFDataConnection() {
		try{
			ConnectivityManager conman = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
			Class conmanClass = Class.forName(conman
					.getClass().getName());
			Field connectivityManagerField = conmanClass
					.getDeclaredField("mService");
			connectivityManagerField.setAccessible(true);
			Object connectivityManager = connectivityManagerField
					.get(conman);
			Class connectivityManagerClass = Class
					.forName(connectivityManager.getClass()
							.getName());
			Method setMobileDataEnabledMethod = connectivityManagerClass
					.getDeclaredMethod("setMobileDataEnabled",
							Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);
			setMobileDataEnabledMethod.invoke(connectivityManager,
					false);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

}
