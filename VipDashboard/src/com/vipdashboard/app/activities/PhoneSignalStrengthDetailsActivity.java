package com.vipdashboard.app.activities;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.vipdashboard.app.adapter.AllSMSAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.PhoneDataInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.entities.PhoneSignalStrenght;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.utils.CommonTask;

public class PhoneSignalStrengthDetailsActivity extends Activity implements IAsynchronousTask {
	private GoogleMap map;
	ListView listView;
	ProgressBar pbar;
	DownloadableAsyncTask asyncTask;
	private boolean isCallFromSignalStrength;
	private boolean isCallFromSMS;
	private boolean isCallFromDataSpeed;
	AllSMSAdapter allSMSAdapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_map);
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapPhone)).getMap();
		listView = (ListView) findViewById(R.id.listView);
		pbar = (ProgressBar) findViewById(R.id.pbPhoneMap);
		if(savedInstanceState.containsKey("MyExperienceSignalStrength")){
			listView.setVisibility(ListView.GONE);
			isCallFromSignalStrength = savedInstanceState.getBoolean("MyExperienceSignalStrength");
			
		}else if(savedInstanceState.containsKey("MyExperienceSMS")){
			listView.setVisibility(ListView.VISIBLE);
			isCallFromSMS = savedInstanceState.getBoolean("MyExperienceSMS");
		}else if(savedInstanceState.containsKey("MyExperienceDataSpeed")){
			listView.setVisibility(ListView.GONE);
			isCallFromDataSpeed = savedInstanceState.getBoolean("MyExperienceDataSpeed");
		}
	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		initializeMap();
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		super.onResume();
	}
	
	private void initializeMap() {
		
		double defaultLatitude=0,defaultLongitude=0;
		LatLng Location = new LatLng(defaultLatitude, defaultLongitude);
		int count=0;
		
		MyNetDatabase myNetDatabase=new MyNetDatabase(this);		
		myNetDatabase.open();
		if(isCallFromSignalStrength){
			ArrayList<PhoneSignalStrenght> phoneList=myNetDatabase.getSignalStrenghtList();
			if(phoneList.size()>0){
				
				for (PhoneSignalStrenght phoneSignalStrenght : phoneList) {
					if(count==0){
						defaultLatitude=phoneSignalStrenght.Latitude;
						defaultLongitude=phoneSignalStrenght.Longitude;
					}
					Location =new LatLng(phoneSignalStrenght.Latitude, phoneSignalStrenght.Longitude);
					if(defaultLatitude > 0.0 && defaultLongitude > 0.0){
						map.addMarker(new MarkerOptions().position(Location).title("Signal:"+ ((int)(phoneSignalStrenght.SignalLevel*100)/31)+"%\r\nTime:"+CommonTask.convertDateToString( phoneSignalStrenght.Time)));
					}					
					count++;
				}	
				
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				
				LatLng Defaultlocation = new LatLng(defaultLatitude, defaultLongitude);
		
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(Defaultlocation, 14.0f));
		
				map.animateCamera(CameraUpdateFactory.zoomIn());
		
				map.animateCamera(CameraUpdateFactory.zoomTo(10));
		
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(Defaultlocation).zoom(15).bearing(90).tilt(30).build();
		
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			
			}
		}else if(isCallFromSMS){
			LoadInforamtion();
			ArrayList<PhoneSMSInformation> phoneList=myNetDatabase.getSMSInfoList();
			if(phoneList.size()>0){
				Bitmap bmpOut = BitmapFactory.decodeResource(getResources(),	R.drawable.messages_sent);
				Bitmap bmpIn = BitmapFactory.decodeResource(getResources(),	R.drawable.messages_received);
				for (PhoneSMSInformation phoneSMS : phoneList) {
					if(count==0){
						defaultLatitude=phoneSMS.Latitude;
						defaultLongitude=phoneSMS.Longitude;
					}
					Location =new LatLng(phoneSMS.Latitude, phoneSMS.Longitude);
					if(defaultLatitude > 0.0 && defaultLongitude > 0.0){
						map.addMarker(new MarkerOptions().position(Location).title(phoneSMS.SMSBody).icon(BitmapDescriptorFactory.fromBitmap(phoneSMS.SMSType==1? bmpIn:bmpOut)));
					}
					count++;
				}	
				
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				
				LatLng Defaultlocation = new LatLng(defaultLatitude, defaultLongitude);
		
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(Defaultlocation, 14.0f));
		
				map.animateCamera(CameraUpdateFactory.zoomIn());
		
				map.animateCamera(CameraUpdateFactory.zoomTo(10));
		
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(Defaultlocation).zoom(15).bearing(90).tilt(30).build();
		
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			
			}
		}else if(isCallFromDataSpeed){
			ArrayList<PhoneDataInformation> phoneList=myNetDatabase.getDataInfoList();
			if(phoneList.size()>0){
				
				for (PhoneDataInformation phoneDateInformation : phoneList) {
					if(count==0){
						defaultLatitude=phoneDateInformation.Latitude;
						defaultLongitude=phoneDateInformation.Longitude;
					}
					Location =new LatLng(phoneDateInformation.Latitude, phoneDateInformation.Longitude);
					if(defaultLatitude > 0.0 && defaultLongitude > 0.0){
						map.addMarker(new MarkerOptions().position(Location).title("DownloadSpeed: "+phoneDateInformation.DownLoadSpeed+", UploadSpeed: "+phoneDateInformation.UpLoadSpeed));
					}
					count++;
				}	
				
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				
				LatLng Defaultlocation = new LatLng(defaultLatitude, defaultLongitude);
		
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(Defaultlocation, 14.0f));
		
				map.animateCamera(CameraUpdateFactory.zoomIn());
		
				map.animateCamera(CameraUpdateFactory.zoomTo(10));
		
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(Defaultlocation).zoom(15).bearing(90).tilt(30).build();
		
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			
			}
		}
		myNetDatabase.close();
	}
	
	private void LoadInforamtion(){
		if (asyncTask != null) {
			asyncTask.cancel(true);
		}
		asyncTask = new DownloadableAsyncTask(this);
		asyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		//pbar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		//pbar.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		MyNetDatabase myNetDatabase=new MyNetDatabase(this);		
		myNetDatabase.open();
		ArrayList<Object> obj = new ArrayList<Object>();
		if(isCallFromSMS){
			ArrayList<PhoneSMSInformation> phoneList=myNetDatabase.getSMSInfoList();			
			obj.add(phoneList);
		}		
		myNetDatabase.close();
		return obj;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			if(isCallFromSMS){
				isCallFromSMS = false;
				/*ArrayList<Object> obj = (ArrayList<Object>) data;			
				ArrayList<PhoneSMSInformation> smsINformation = (ArrayList<PhoneSMSInformation>) obj.get(0);
				allSMSAdapter = new AllSMSAdapter(this, R.layout.all_sms_item_layout, smsINformation);
				listView.setAdapter(allSMSAdapter);*/
			}
		}
	}
}
