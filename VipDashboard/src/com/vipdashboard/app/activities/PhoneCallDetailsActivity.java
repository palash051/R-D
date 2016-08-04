package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.AllCallsAdapter;
import com.vipdashboard.app.adapter.AllSMSAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.entities.PhoneSignalStrenght;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class PhoneCallDetailsActivity extends Activity implements IAsynchronousTask {
	private GoogleMap map;
	ListView listView;
	ProgressBar pbar;
	DownloadableAsyncTask asyncTask;
	AllCallsAdapter allCallsAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_map);
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapPhone)).getMap();
		listView = (ListView) findViewById(R.id.listView);
		pbar = (ProgressBar) findViewById(R.id.pbPhoneMap);
	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
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
		initializeMap();
		
		super.onResume();
	}

	private void initializeMap() {
		LoadInformation();
		double defaultLatitude = 0, defaultLongitude = 0;
		LatLng Location = new LatLng(defaultLatitude, defaultLongitude);

		int count = 0;

		MyNetDatabase myNetDatabase = new MyNetDatabase(this);
		myNetDatabase.open();
		ArrayList<PhoneCallInformation> phoneList = myNetDatabase
				.getCallInfoList();
		myNetDatabase.close();
		String callType = "",strDuration="";
		int min=0,sec=0;
		
		
		Bitmap bmpDialed = BitmapFactory.decodeResource(getResources(),	R.drawable.call_dialed);
		Bitmap bmpReceived = BitmapFactory.decodeResource(getResources(),	R.drawable.call_received);
		Bitmap bmpMissed = BitmapFactory.decodeResource(getResources(),	R.drawable.call_missed);
			
		
		if (phoneList.size() > 0) {
			
			for (PhoneCallInformation phoneCallInformation : phoneList) {
				if(phoneCallInformation.Latitude==0 && phoneCallInformation.Longitude==0)
					continue;
				if (count == 0) {
					defaultLatitude = phoneCallInformation.Latitude;
					defaultLongitude = phoneCallInformation.Longitude;
				}
				Location = new LatLng(phoneCallInformation.Latitude,
						phoneCallInformation.Longitude);
				
				min=(int)phoneCallInformation.DurationInSec/60;
				sec=(int)phoneCallInformation.DurationInSec%60;
				strDuration="\r\nDuration:"+(min>0?min+"m":"")+sec+"s";
				
				if (phoneCallInformation.CallType == CallLog.Calls.INCOMING_TYPE){
					map.addMarker(new MarkerOptions()
					.position(Location)
					.title("Call:"
							+ "Incomming"								 
							+strDuration
							+ "\r\nTime:"
							+ CommonTask
									.convertDateToString(phoneCallInformation.CallTime)).icon(BitmapDescriptorFactory.fromBitmap(bmpReceived)));
				}
					
				
				else if (phoneCallInformation.CallType == CallLog.Calls.OUTGOING_TYPE){
					map.addMarker(new MarkerOptions()
					.position(Location)
					.title("Call:"
							+ "Outgoing"								 
							+strDuration
							+ "\r\nTime:"
							+ CommonTask
									.convertDateToString(phoneCallInformation.CallTime)).icon(BitmapDescriptorFactory.fromBitmap(bmpDialed)));
				}
					
				else{					
					map.addMarker(new MarkerOptions()
					.position(Location)
					.title("Call:"
							+ "Missed"
							+ "\r\nTime:"
							+ CommonTask
									.convertDateToString(phoneCallInformation.CallTime)).icon(BitmapDescriptorFactory.fromBitmap(bmpMissed)));
					
				}
				if(defaultLatitude > 0.0 && defaultLongitude > 0.0){
					
				}				
				count++;
			}

			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			LatLng Defaultlocation = new LatLng(defaultLatitude,
					defaultLongitude);

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Defaultlocation,
					14.0f));

			map.animateCamera(CameraUpdateFactory.zoomIn());

			map.animateCamera(CameraUpdateFactory.zoomTo(10));

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(Defaultlocation).zoom(15).bearing(90).tilt(30)
					.build();

			map.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

		}
	}

	private void LoadInformation() {
		if (asyncTask != null) {
			asyncTask.cancel(true);
		}
		asyncTask = new DownloadableAsyncTask(this);
		asyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		pbar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		pbar.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		MyNetDatabase myNetDatabase=new MyNetDatabase(this);		
		myNetDatabase.open();
		ArrayList<Object> obj = new ArrayList<Object>();
		ArrayList<PhoneCallInformation> phoneList = myNetDatabase
				.getCallInfoList();			
		obj.add(phoneList);		
		myNetDatabase.close();
		return obj;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			ArrayList<Object> obj = (ArrayList<Object>) data;			
			ArrayList<PhoneCallInformation> phoneList = (ArrayList<PhoneCallInformation>) obj.get(0);
			allCallsAdapter = new AllCallsAdapter(this, R.layout.all_sms_item_layout, phoneList);
			listView.setAdapter(allCallsAdapter);
		}
	}
}
