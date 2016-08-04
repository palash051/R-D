package com.leadership.app.activities;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.leadership.app.R;
import com.leadership.app.asynchronoustask.DownloadableAsyncTask;
import com.leadership.app.entities.CompanyHolder;
import com.leadership.app.entities.CompanySetup;
import com.leadership.app.interfaces.IAsynchronousTask;
import com.leadership.app.interfaces.ICompanySetUP;
import com.leadership.app.manager.CompanySetUpManager;
import com.leadership.app.utils.CommonConstraints;
import com.leadership.app.utils.CommonTask;
import com.leadership.app.utils.CommonValues;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class AppbaseCompanyListActivity extends Activity implements OnClickListener, OnMarkerClickListener,IAsynchronousTask{
	ImageView ivNext;
	private GoogleMap map;
	DownloadableAsyncTask downloadableAsyncTask;
	String companyID;
	ProgressDialog progress;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (!CommonTask.isOnline(this)) {
			showMessage("Network connection error.\nPlease check your internet connection.");
		}else{ 
			setContentView(R.layout.appbase_companylist);
		
			initalization();
		}
	}
	
	private void initalization() {
		ivNext = (ImageView) findViewById(R.id.ivNext);
		ivNext.setOnClickListener(this);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapAppsCustomer)).getMap();
		
		map.setOnMarkerClickListener(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initializeMap();
	}
	
	@Override
	public void onClick(View view) {
		if(CommonValues.getInstance().SelectedCompany==null || CommonValues.getInstance().SelectedCompany.CompanyID==0)
			return;
		if(view.getId() == R.id.ivNext){
			AppbaseReportActivity.isCompare=true;
			Intent intent = new Intent(this, AppbaseReportActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
	}
	
	private void LoadInformation() {
		if (!CommonTask.isOnline(this)) {
			CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
			return;
		}
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}
	
	private void initializeMap() {
		try{
		
			map.clear();
	
			double defaultLatitude = 0, defaultLongitude = 0;		
				
			defaultLatitude = 34.15;
			defaultLongitude = 42.38;			
			LatLng Location = new LatLng(33.3386, 44.3939);
			Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.zain);		
			
			map.addMarker(new MarkerOptions().position(Location).title("Zain Arbia")
					.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
			
			
			Location = new LatLng(25.7069, 28.229401);
			defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.japan);		
			
			map.addMarker(new MarkerOptions().position(Location).title("Zain Sudan")
					.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
			
			Location = new LatLng(26.236099, 50.583099);
			defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pakistan);		
			
			map.addMarker(new MarkerOptions().position(Location).title("Zain Bahrain")
					.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
			
			Location = new LatLng(34.15, 42.38);
			defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.japan);		
			
			map.addMarker(new MarkerOptions().position(Location).title("Zain Iraq")
					.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
			
			Location = new LatLng(29.240116, 47.971252);
			defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wataniya);		
			
			map.addMarker(new MarkerOptions().position(Location).title("Watanya Kuwait")
					.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
			
			Location = new LatLng(26, 50.549999);
			defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.batelco);		
			
			map.addMarker(new MarkerOptions().position(Location).title("Batelco")
					.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
			
			Location = new LatLng(36.8724, 42.9875);
			defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.korek);		
			
			map.addMarker(new MarkerOptions().position(Location).title("Korek")
					.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
			
			Location = new LatLng(24.6408, 46.7728);
			defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mobily);		
			
			map.addMarker(new MarkerOptions().position(Location).title("Mobily")
					.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
			
			Location = new LatLng(21.516899, 39.2192);
			defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.stc);		
			
			map.addMarker(new MarkerOptions().position(Location).title("STC")
					.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
			
			Location = new LatLng(30.049999, 31.25);
			defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.vodafone);		
			
			map.addMarker(new MarkerOptions().position(Location).title("Vodafone Egypt")
					.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
			
			Location = new LatLng(27, 30);
			defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.etisalat);		
			
			map.addMarker(new MarkerOptions().position(Location).title("Etisalat Egypt")
					.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
			
			Location = new LatLng(39.1667, 35.6667);
			defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.avea);		
			
			map.addMarker(new MarkerOptions().position(Location).title("Avea")
					.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
			
			Location = new LatLng(41.0186, 28.964701);
			defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.web_icon);		
			
			map.addMarker(new MarkerOptions().position(Location).title("Turkcell")
					.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
	
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	
			LatLng Defaultlocation = new LatLng(defaultLatitude, defaultLongitude);
	
			map.moveCamera(CameraUpdateFactory
					.newLatLngZoom(Defaultlocation, 14.0f));
	
			map.animateCamera(CameraUpdateFactory.zoomIn());
	
			map.animateCamera(CameraUpdateFactory.zoomTo(10));
	
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(Defaultlocation).zoom(4).bearing(90).tilt(30).build();
	
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}catch (Exception e) {
			
		}
	}
	
	private Bitmap scaleImage(Bitmap originalImage){
		int width=200,height=200;
		Bitmap background = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		float originalWidth = originalImage.getWidth(), originalHeight = originalImage.getHeight();
		Canvas canvas = new Canvas(background);
		float scale = width/originalWidth;
		float xTranslation = 0.0f, yTranslation = (height - originalHeight * scale)/2.0f;
		Matrix transformation = new Matrix();
		transformation.postTranslate(xTranslation, yTranslation);
		transformation.preScale(scale, scale);
		Paint paint = new Paint();
		paint.setFilterBitmap(true);
		canvas.drawBitmap(originalImage, transformation, paint);
		return background;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if(marker.getTitle().equals("Zain Arbia")){
			companyID = String.valueOf(CommonConstraints.ZAIN_IRAQ);
			LoadInformation();		
		}else if(marker.getTitle().equals("Zain Sudan")){
			companyID = String.valueOf(CommonConstraints.ZAIN_SUDAN);
			LoadInformation();		
		}else if(marker.getTitle().equals("Zain Bahrain")){
			companyID = String.valueOf(CommonConstraints.ZAIN_BAHRAIN);
			LoadInformation();		
		}else if(marker.getTitle().equals("Turkcell")){
			companyID = String.valueOf(CommonConstraints.TURKCELL);
			LoadInformation();		
		}else if(marker.getTitle().equals("Avea")){
			companyID = String.valueOf(CommonConstraints.AVEA);
			LoadInformation();		
		}else if(marker.getTitle().equals("Etisalat Egypt")){
			companyID = String.valueOf(CommonConstraints.ETISALAT_EGYPT);
			LoadInformation();		
		}else if(marker.getTitle().equals("Vodafone Egypt")){
			companyID = String.valueOf(CommonConstraints.VODAFONE_EGYPT);
			LoadInformation();		
		}else if(marker.getTitle().equals("STC")){
			companyID = String.valueOf(CommonConstraints.STC);
			LoadInformation();		
		}else if(marker.getTitle().equals("Mobily")){
			companyID = String.valueOf(CommonConstraints.MOBILY);
			LoadInformation();		
		}else if(marker.getTitle().equals("Korek")){
			companyID = String.valueOf(CommonConstraints.KOREK);
			LoadInformation();		
		}else if(marker.getTitle().equals("Batelco")){
			companyID = String.valueOf(CommonConstraints.BATELCO);
			LoadInformation();		
		}else if(marker.getTitle().equals("Watanya Kuwait")){
			companyID = String.valueOf(CommonConstraints.WATANYA_KUWAIT);
			LoadInformation();		
		}
		return false;
	}

	@Override
	public void showProgressLoader() {
		progress= ProgressDialog.show(this, "","Please wait", true);
		progress.setCancelable(false);
		progress.setIcon(null);	
		
	}

	@Override
	public void hideProgressLoader() {
		progress.dismiss();	
	}
	@Override
	public Object doBackgroundPorcess() {
		ICompanySetUP companySetUP = new CompanySetUpManager();
		return companySetUP.GetCompanySetupByCompanyID(companyID);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			CompanyHolder companyHolder = (CompanyHolder) data;
			CommonValues.getInstance().SelectedCompany = (CompanySetup) companyHolder.companySetup;
		}
		else
		{
			Toast.makeText(this, "Operator information is not available", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void showMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.app_name).setMessage(message)
				.setCancelable(false)
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(AppbaseCompanyListActivity.this,
								LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
}
