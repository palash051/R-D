package com.vipdashboard.app.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;

public class CallMemoActivity extends Activity implements OnClickListener, IAsynchronousTask, LocationListener {

	TextView tvSendSocial, tvSaveToCallLog, tvCallerNumber, tvCallerName,
			tvCallDuration, tvCallLocation;
	RelativeLayout socialLayout;
	EditText etCallMemoText;
	public static PhoneCallInformation phoneCallInformation;
	String number, duration;
	LinearLayout llcallmemo;
	ImageView postToFacebook;
	DownloadableAsyncTask asyncTask;
	ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_memo);
		Initilization();
	}

	private void Initilization() {
		llcallmemo = (LinearLayout) findViewById(R.id.llcallmemo);
		postToFacebook = (ImageView) findViewById(R.id.CallMemofacebookImage);
		tvSendSocial = (TextView) findViewById(R.id.tvSocialPost);
		tvSaveToCallLog = (TextView) findViewById(R.id.tvSaveInCallLog);
		socialLayout = (RelativeLayout) findViewById(R.id.rlCallMemoSocialPost);
		etCallMemoText = (EditText) findViewById(R.id.etCallMemoMessage);
		tvCallerNumber = (TextView) findViewById(R.id.tvCallerNumber);
		tvCallerName = (TextView) findViewById(R.id.tvCallerName);
		tvCallDuration = (TextView) findViewById(R.id.tvCallDuration);
		tvCallLocation = (TextView) findViewById(R.id.tvCallLocation);
		tvSendSocial.setOnClickListener(this);
		tvSaveToCallLog.setOnClickListener(this);
		postToFacebook.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tvSocialPost) {
			socialLayout.setVisibility(RelativeLayout.VISIBLE);
		} else if (v.getId() == R.id.tvSaveInCallLog) {
			if (etCallMemoText.getText().toString().equals(""))
				return;
			if (phoneCallInformation != null) {
				phoneCallInformation.TextCallMemo = etCallMemoText.getText()
						.toString();
				MyNetDatabase mynetDatabase = new MyNetDatabase(this);
				mynetDatabase.open();
				mynetDatabase.updatePhoneCallInformation(phoneCallInformation);
				mynetDatabase.close();
				onBackPressed();
			} else {
				onBackPressed();
			}
		} else if (v.getId() == R.id.CallMemofacebookImage) {
			if (phoneCallInformation != null) {
				phoneCallInformation.TextCallMemo = etCallMemoText.getText()
						.toString();
				MyNetDatabase mynetDatabase = new MyNetDatabase(this);
				mynetDatabase.open();
				mynetDatabase.updatePhoneCallInformation(phoneCallInformation);
				mynetDatabase.close();
			}
			MamoSnapShot();
		}
	}

	private void MamoSnapShot() {
		try {
			Bitmap bmOverlay = null;
			llcallmemo.setDrawingCacheEnabled(true);
			bmOverlay = llcallmemo.getDrawingCache();
			String external_path = Environment.getExternalStorageDirectory()
					.getPath() + "/MyNet/";
			String filePath = String
					.format(CommonValues.getInstance().LoginUser.UserNumber
							+ ".jpg");
			File cduFileDir = new File(external_path);
			if (!cduFileDir.exists())
				cduFileDir.mkdir();
			File pictureFile = new File(cduFileDir, filePath);
			ExperinceLiveActivity.filename = pictureFile.getName();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmOverlay.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			ExperinceLiveActivity.selectedFile = stream.toByteArray();
			LoadInformation();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void LoadInformation() {
		if (asyncTask != null)
			asyncTask.cancel(true);
		asyncTask = new DownloadableAsyncTask(this);
		asyncTask.execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initValue();
	}

	private void initValue() {
		String addressText = "";
		if (phoneCallInformation != null) {
			String name = CommonTask.getContentName(this,
					phoneCallInformation.Number);
			if (name != "") {
				tvCallerName.setText(name);
			}
			tvCallerNumber.setText(phoneCallInformation.Number);

			int min = 0, sec = 0, hour = 0;
			String durationText = "";
			NumberFormat formatter = new DecimalFormat("00");
			sec = phoneCallInformation.DurationInSec % 60;
			min = phoneCallInformation.DurationInSec / 60;
			if (min > 59) {
				hour = min / 60;
				min = min % 60;
			}
			durationText = formatter.format(hour) + ":" + formatter.format(min)
					+ ":" + formatter.format(sec);
			tvCallDuration.setText("Duration : " + durationText);
		}
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, this);
		android.location.Location loc = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (loc != null) {
			LatLng Location = new LatLng(loc.getLatitude(), loc.getLongitude());
			double latitude = Location.latitude, longitude = Location.longitude;
			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			try {
				List<Address> addresses = geocoder.getFromLocation(latitude,
						longitude, 1);
				if (addresses != null && addresses.size() > 0) {
					Address address = addresses.get(0);
					for (int lineIndex = 0; lineIndex < address
							.getMaxAddressLineIndex(); lineIndex++) {
						addressText = addressText
								+ address.getAddressLine(lineIndex) + ", ";
					}
					addressText = addressText + address.getLocality() + ", "
							+ address.getCountryName();
					tvCallLocation.setText("Location : " + addressText);
					Location = new LatLng(latitude, longitude);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onBackPressed() {
		phoneCallInformation = null;
		this.finish();
		if (!MyNetApplication.isApplicationAlive()) {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onLocationChanged(Location arg0) {

	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}

	@Override
	public void showProgressLoader() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(true);
		progressDialog.setMessage("Please Wait...");
		progressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		return userManager.SetFBPostPicture(ExperinceLiveActivity.selectedFile,
				CommonValues.getInstance().LoginUser.UserNumber);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			String path = "";
			path += CommonURL.getInstance().getImageServer
					+ String.valueOf(data);
			ExperinceLiveActivity.filename = "";
			ExperinceLiveActivity.selectedFile = null;

			Intent intent = new Intent(this, FB_ShareActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("PIC_LINK", path);
			intent.putExtra("DESCRIPTION", "Call Memo");
			startActivity(intent);
			onBackPressed();
		}
	}
}
