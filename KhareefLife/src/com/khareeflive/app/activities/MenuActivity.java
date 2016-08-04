package com.khareeflive.app.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.khareeflive.app.R;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.utils.CommonValues;

public class MenuActivity extends Activity implements OnClickListener {

	private Button latestupdate, warroom, network, directory, offers,
			partnership, btNetworkUpdate, bt2GUpdate, bt3GUpdate, btLTEUpdate,
			btNOCRoster, btShowGraph, btnsitedown, btuserguide;
	private Intent intent;
	TextView tvUserWelcome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		initializeControl();

	}

	private void initializeControl() {
		latestupdate = (Button) findViewById(R.id.btLatestUpdate);
		warroom = (Button) findViewById(R.id.btWarroom);
		/* network = (Button) findViewById(R.id.btNetwork); */
		directory = (Button) findViewById(R.id.btDirectory);
		offers = (Button) findViewById(R.id.btOffers);
		partnership = (Button) findViewById(R.id.btPartnership);
		/*
		 * btNetworkUpdate = (Button) findViewById(R.id.btNetworkUpdate);
		 * bt2GUpdate = (Button) findViewById(R.id.bt2GUpdate); bt3GUpdate =
		 * (Button) findViewById(R.id.bt3GUpdate); btLTEUpdate = (Button)
		 * findViewById(R.id.btLTEUpdate);
		 */
		btNOCRoster = (Button) findViewById(R.id.btNOCRoster);
		btShowGraph = (Button) findViewById(R.id.btShowGraph);
		btnsitedown = (Button) findViewById(R.id.btSiteDown);
		btuserguide = (Button) findViewById(R.id.btuserguide);

		tvUserWelcome = (TextView) findViewById(R.id.tvUserWelcome);
		tvUserWelcome.setText("Welcome "
				+ CommonValues.getInstance().LoginUser.userName);

		latestupdate.setOnClickListener(this);
		warroom.setOnClickListener(this);
		/* network.setOnClickListener(this); */
		directory.setOnClickListener(this);
		offers.setOnClickListener(this);
		partnership.setOnClickListener(this);
		/*
		 * btNetworkUpdate.setOnClickListener(this);
		 * bt2GUpdate.setOnClickListener(this);
		 * bt3GUpdate.setOnClickListener(this);
		 * btLTEUpdate.setOnClickListener(this);
		 */
		btNOCRoster.setOnClickListener(this);
		btShowGraph.setOnClickListener(this);
		btnsitedown.setOnClickListener(this);
		btuserguide.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btSiteDown:
			intent = new Intent(v.getContext(), SiteDownActivity.class);
			startActivity(intent);
			break;

		case R.id.btLatestUpdate:
			intent = new Intent(v.getContext(), LatestUpdateActivity.class);
			startActivity(intent);
			break;

		case R.id.btWarroom:

			intent = new Intent(v.getContext(), WarRoomGroupActivity.class);

			startActivity(intent);
			break;

		/*
		 * case R.id.btNetwork:
		 * 
		 * intent = new Intent(v.getContext(),
		 * NetworkPerformanceActivity.class);
		 * 
		 * startActivity(intent); break;
		 */

		case R.id.btDirectory:

			intent = new Intent(v.getContext(), DirectoryActivity.class);

			startActivity(intent);
			break;
		/*
		 * case R.id.btNetworkUpdate:
		 * 
		 * intent = new Intent(v.getContext(), NetworkUpdateActivity.class);
		 * 
		 * startActivity(intent); break;
		 */
		/*
		 * case R.id.bt2GUpdate:
		 * 
		 * intent = new Intent(v.getContext(), Update2GActivity.class);
		 * 
		 * startActivity(intent); break;
		 */
		/*
		 * case R.id.bt3GUpdate:
		 * 
		 * intent = new Intent(v.getContext(), Update3GActivity.class);
		 * 
		 * startActivity(intent); break;
		 */
		/*
		 * case R.id.btLTEUpdate:
		 * 
		 * intent = new Intent(v.getContext(), LTEUpdateActivity.class);
		 * 
		 * startActivity(intent); break;
		 */
		case R.id.btNOCRoster:

			intent = new Intent(v.getContext(), NOCRosterActivity.class);

			startActivity(intent);
			break;
		case R.id.btShowGraph:

			intent = new Intent(v.getContext(), NewGraphActivity.class);

			startActivity(intent);
			break;

		case R.id.btOffers:

			intent = new Intent(v.getContext(), OfferActivity.class);

			startActivity(intent);
			break;

		case R.id.btPartnership:

			/*
			 * intent = new Intent(v.getContext(), ManualActivity.class);
			 * 
			 * startActivity(intent);
			 */
			ShowPDF();
			break;

		case R.id.btuserguide:
			ShowUserGuidePDF();
			break;
		}

	}

	public void ShowUserGuidePDF() {

		File fileBrochure = new File("/sdcard/User Guide_V1.0.pdf");
		if (!fileBrochure.exists()) {
			CopyAssetsbrochure("User Guide_V1.0.pdf");
		}

		/** PDF reader code */
		File file = new File("/sdcard/User Guide_V1.0.pdf");

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			getApplicationContext().startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(MenuActivity.this, "NO Pdf Viewer",
					Toast.LENGTH_SHORT).show();
		}

	}

	public void ShowPDF() {

		File fileBrochure = new File("/sdcard/Operation Manual_V1.0.pdf");
		if (!fileBrochure.exists()) {
			CopyAssetsbrochure("Operation Manual_V1.0.pdf");
		}

		/** PDF reader code */
		File file = new File("/sdcard/Operation Manual_V1.0.pdf");

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			getApplicationContext().startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(MenuActivity.this, "NO Pdf Viewer",
					Toast.LENGTH_SHORT).show();
		}

	}

	private void CopyAssetsbrochure(String filename) {
		AssetManager assetManager = getAssets();
		String[] files = null;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			Log.e("tag", e.getMessage());
		}
		for (int i = 0; i < files.length; i++) {
			String fStr = files[i];
			if (fStr.equalsIgnoreCase(filename)) {
				InputStream in = null;
				OutputStream out = null;
				try {
					in = assetManager.open(files[i]);
					out = new FileOutputStream("/sdcard/" + files[i]);
					copyFile(in, out);
					in.close();
					in = null;
					out.flush();
					out.close();
					out = null;
					break;
				} catch (Exception e) {
					Log.e("tag", e.getMessage());
				}
			}
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	@Override
	protected void onPause() {
		KhareefLiveApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		KhareefLiveApplication.activityResumed();
		super.onResume();
	}
}
