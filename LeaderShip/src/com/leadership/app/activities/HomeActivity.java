package com.leadership.app.activities;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
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
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnClickListener, OnMarkerClickListener,IAsynchronousTask{
	
	ImageView rlFinencialInfo,rlNetworkInfo,rlSWMI,rlSummary;
	
	private GoogleMap map;
	DownloadableAsyncTask downloadableAsyncTask;
	String companyID;
	ProgressDialog progress;
	LinearLayout llUserMode;
	
	TextView tvUserMode,tvAdminUserMode,tvErricsonUserMode;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (!CommonTask.isOnline(this)) {
			showMessage("Network connection error.\nPlease check your internet connection.");
		}else{ 
			setContentView(R.layout.home_new);		
			initalization();
		}
	}
	
	private void showMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.app_name).setMessage(message)
				.setCancelable(false)
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(HomeActivity.this,
								LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		CommonValues.getInstance().SelectedCompany=null;
		initializeMap();
	}

	private void initalization() {
		rlFinencialInfo = (ImageView) findViewById(R.id.rlFinencialInfo);
		rlNetworkInfo= (ImageView) findViewById(R.id.rlNetworkInfo);
		rlSWMI= (ImageView) findViewById(R.id.rlSWMI);
		rlSummary= (ImageView) findViewById(R.id.rlSummary);
		
		llUserMode= (LinearLayout) findViewById(R.id.llUserMode);
		rlFinencialInfo.setOnClickListener(this);
		rlNetworkInfo.setOnClickListener(this);
		rlSWMI.setOnClickListener(this);
		rlSummary.setOnClickListener(this);
		
		tvUserMode= (TextView) findViewById(R.id.tvUserMode);
		tvAdminUserMode= (TextView) findViewById(R.id.tvAdminUserMode);
		tvErricsonUserMode= (TextView) findViewById(R.id.tvErricsonUserMode);
		
		tvUserMode.setOnClickListener(this);
		tvAdminUserMode.setOnClickListener(this);
		tvErricsonUserMode.setOnClickListener(this);
		
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapHomeCustomer)).getMap();
		
		map.setOnMarkerClickListener(this);
	}

	@Override
	public void onClick(View view) {
		
		if(view.getId() == R.id.rlFinencialInfo){
			if(CommonValues.getInstance().SelectedCompany==null || CommonValues.getInstance().SelectedCompany.CompanyID==0){
				Toast.makeText(this, "Please select a operator", Toast.LENGTH_SHORT).show();
				return;	
			}
			Intent intent = new Intent(this, LeadershipFinanceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.rlNetworkInfo){
			if(CommonValues.getInstance().SelectedCompany==null || CommonValues.getInstance().SelectedCompany.CompanyID==0){
				Toast.makeText(this, "Please select a operator", Toast.LENGTH_SHORT).show();
				return;	
			}
			Intent intent = new Intent(this, LeadershipNetworkKPIVoiceDataActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.rlSWMI){
			if(CommonValues.getInstance().SelectedCompany==null || CommonValues.getInstance().SelectedCompany.CompanyID==0){
				Toast.makeText(this, "Please select a operator", Toast.LENGTH_SHORT).show();
				return;	
			}
			Intent intent = new Intent(this, LeadershipSWMIActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.rlSummary){
			if(CommonValues.getInstance().SelectedCompany==null || CommonValues.getInstance().SelectedCompany.CompanyID==0){
				Toast.makeText(this, "Please select a operator", Toast.LENGTH_SHORT).show();
				return;	
			}
			Intent intent = new Intent(this, LeadershipSummaryActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.tvUserMode){			
			CommonValues.getInstance().LoginUser.UserMode=3;
			initializeMap();
		}else if(view.getId() == R.id.tvAdminUserMode){			
			CommonValues.getInstance().LoginUser.UserMode=1;
			initializeMap();
		}else if(view.getId() == R.id.tvErricsonUserMode){			
			CommonValues.getInstance().LoginUser.UserMode=2;
			initializeMap();
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
		
			if(CommonValues.getInstance().LoginUser.UserRoleID==3){
				llUserMode.setVisibility(View.GONE);
			}else{
				llUserMode.setVisibility(View.VISIBLE);
				if(CommonValues.getInstance().LoginUser.UserMode==1){
					tvAdminUserMode.setVisibility(View.GONE);
					tvUserMode.setVisibility(View.VISIBLE);
					tvErricsonUserMode.setVisibility(View.GONE);
				}else if(CommonValues.getInstance().LoginUser.UserMode==2){
					tvAdminUserMode.setVisibility(View.GONE);
					tvUserMode.setVisibility(View.VISIBLE);
					tvErricsonUserMode.setVisibility(View.GONE);
				}else{
					tvAdminUserMode.setVisibility(View.GONE);
					tvUserMode.setVisibility(View.GONE);
					tvErricsonUserMode.setVisibility(View.VISIBLE);
				}
			}
			
			map.clear();
	
			double defaultLatitude = 0, defaultLongitude = 0;		
				
			defaultLatitude = 34.15;
			defaultLongitude = 42.38;			
			LatLng Location = null;
			Bitmap defaultBitmap=null;
			
			if(CommonValues.getInstance().LoginUser.UserMode!=3){
				Location = new LatLng(33.3386, 44.3939);
				
				defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.zain);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Zain Arbia")
						.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
				
				
				Location = new LatLng(13.0000, 25.0);
				
				defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.japan);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Zain Sudan")
						.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
				
				Location = new LatLng(15.6333, 32.5333);
				defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pakistan);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Zain Bahrain")
						.icon(BitmapDescriptorFactory.fromBitmap(scaleImage(defaultBitmap))));
			
				Location = new LatLng(33.3250, 44.4220);
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
			}else{
				Location = new LatLng(33.3386, 44.3939);
				//defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.zain);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Operator 8"));
				
				
				Location = new LatLng(13.0000, 25.0);
				
				//defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.japan);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Operator 5"));
				
				Location = new LatLng(15.6333, 32.5333);
				//defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pakistan);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Operator 12"));
			
				Location = new LatLng(33.3250, 44.4220);
				//defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.japan);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Operator 1"));
				
				Location = new LatLng(29.240116, 47.971252);
				//defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.wataniya);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Operator 2"));
				
				Location = new LatLng(26, 50.549999);
				//defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.batelco);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Operator 3"));
				
				Location = new LatLng(36.8724, 42.9875);
				//defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.korek);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Operator 4"));
				
				Location = new LatLng(24.6408, 46.7728);
				//defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mobily);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Operator 6"));
				
				Location = new LatLng(21.516899, 39.2192);
				//defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.stc);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Operator 7"));
				
				Location = new LatLng(30.049999, 31.25);
				//defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.vodafone);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Operator 9"));
				
				Location = new LatLng(27, 30);
				//defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.etisalat);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Operator 10"));
				
				Location = new LatLng(39.1667, 35.6667);
				//defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.avea);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Operator 11"));
				
				/*Location = new LatLng(41.0186, 28.964701);
				//defaultBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.web_icon);		
				
				map.addMarker(new MarkerOptions().position(Location).title("Operator 14"));*/
			}
	
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
		int width=100,height=100;
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
		if(marker.getTitle().equals("Zain Arbia")|| marker.getTitle().equals("Operator 8")){
			companyID = String.valueOf(CommonConstraints.ZAIN_IRAQ);
			LoadInformation();		
		}else if(marker.getTitle().equals("Zain Sudan")|| marker.getTitle().equals("Operator 5")){
			companyID = String.valueOf(CommonConstraints.ZAIN_SUDAN);
			LoadInformation();		
		}else if(marker.getTitle().equals("Zain Bahrain")|| marker.getTitle().equals("Operator 12")){
			companyID = String.valueOf(CommonConstraints.ZAIN_BAHRAIN);
			LoadInformation();		
		}else if(marker.getTitle().equals("Turkcell")|| marker.getTitle().equals("Operator 1")){
			companyID = String.valueOf(CommonConstraints.TURKCELL);
			LoadInformation();		
		}else if(marker.getTitle().equals("Avea")|| marker.getTitle().equals("Operator 2")){
			companyID = String.valueOf(CommonConstraints.AVEA);
			LoadInformation();		
		}else if(marker.getTitle().equals("Etisalat Egypt")|| marker.getTitle().equals("Operator 3")){
			companyID = String.valueOf(CommonConstraints.ETISALAT_EGYPT);
			LoadInformation();		
		}else if(marker.getTitle().equals("Vodafone Egypt")|| marker.getTitle().equals("Operator 4")){
			companyID = String.valueOf(CommonConstraints.VODAFONE_EGYPT);
			LoadInformation();		
		}else if(marker.getTitle().equals("STC")|| marker.getTitle().equals("Operator 6")){
			companyID = String.valueOf(CommonConstraints.STC);
			LoadInformation();		
		}else if(marker.getTitle().equals("Mobily")|| marker.getTitle().equals("Operator 7")){
			companyID = String.valueOf(CommonConstraints.MOBILY);
			LoadInformation();		
		}else if(marker.getTitle().equals("Korek")|| marker.getTitle().equals("Operator 9")){
			companyID = String.valueOf(CommonConstraints.KOREK);
			LoadInformation();		
		}else if(marker.getTitle().equals("Batelco")|| marker.getTitle().equals("Operator 10")){
			companyID = String.valueOf(CommonConstraints.BATELCO);
			LoadInformation();		
		}else if(marker.getTitle().equals("Watanya Kuwait")|| marker.getTitle().equals("Operator 11")){
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
			if(CommonValues.getInstance().LoginUser.UserMode==3){
				CommonValues.getInstance().SelectedCompany.CompanyLogoId=R.drawable.ericsson;
				CommonValues.getInstance().SelectedCompany.CompanyName=CommonValues.getInstance().SelectedCompany.CompanyDescription;
				return;
			}			
			if(CommonValues.getInstance().SelectedCompany.CompanyID==CommonConstraints.ZAIN_IRAQ)
				CommonValues.getInstance().SelectedCompany.CompanyLogoId =R.drawable.zain;
			else if(CommonValues.getInstance().SelectedCompany.CompanyID==CommonConstraints.ZAIN_SUDAN)
				CommonValues.getInstance().SelectedCompany.CompanyLogoId=R.drawable.japan;
			else if(CommonValues.getInstance().SelectedCompany.CompanyID==CommonConstraints.ZAIN_SUDAN)
				CommonValues.getInstance().SelectedCompany.CompanyLogoId=R.drawable.japan;
			else if(CommonValues.getInstance().SelectedCompany.CompanyID==CommonConstraints.ZAIN_BAHRAIN)
				CommonValues.getInstance().SelectedCompany.CompanyLogoId=R.drawable.pakistan;
			else if(CommonValues.getInstance().SelectedCompany.CompanyID==CommonConstraints.ZAIN_IRAQ)
				CommonValues.getInstance().SelectedCompany.CompanyLogoId=R.drawable.japan;
			else if(CommonValues.getInstance().SelectedCompany.CompanyID==CommonConstraints.WATANYA_KUWAIT)
				CommonValues.getInstance().SelectedCompany.CompanyLogoId=R.drawable.wataniya;
			else if(CommonValues.getInstance().SelectedCompany.CompanyID==CommonConstraints.BATELCO)
				CommonValues.getInstance().SelectedCompany.CompanyLogoId=R.drawable.batelco;
			else if(CommonValues.getInstance().SelectedCompany.CompanyID==CommonConstraints.KOREK)
				CommonValues.getInstance().SelectedCompany.CompanyLogoId=R.drawable.korek;
			else if(CommonValues.getInstance().SelectedCompany.CompanyID==CommonConstraints.MOBILY)
				CommonValues.getInstance().SelectedCompany.CompanyLogoId=R.drawable.mobily;
			else if(CommonValues.getInstance().SelectedCompany.CompanyID==CommonConstraints.STC)
				CommonValues.getInstance().SelectedCompany.CompanyLogoId=R.drawable.stc;
			else if(CommonValues.getInstance().SelectedCompany.CompanyID==CommonConstraints.VODAFONE_EGYPT)
				CommonValues.getInstance().SelectedCompany.CompanyLogoId=R.drawable.vodafone;
			else if(CommonValues.getInstance().SelectedCompany.CompanyID==CommonConstraints.ETISALAT_EGYPT)
				CommonValues.getInstance().SelectedCompany.CompanyLogoId=R.drawable.etisalat;
			else if(CommonValues.getInstance().SelectedCompany.CompanyID==CommonConstraints.AVEA)
				CommonValues.getInstance().SelectedCompany.CompanyLogoId=R.drawable.avea;
			else if(CommonValues.getInstance().SelectedCompany.CompanyID==CommonConstraints.TURKCELL)
				CommonValues.getInstance().SelectedCompany.CompanyLogoId=R.drawable.web_icon;
			else{
				CommonValues.getInstance().SelectedCompany.CompanyLogoId=R.drawable.ericsson;
			}
		}
		else
		{
			Toast.makeText(this, "Operator information is not available", Toast.LENGTH_SHORT).show();
		}
	}
}
