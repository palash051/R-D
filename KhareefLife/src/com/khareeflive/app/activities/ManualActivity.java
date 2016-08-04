package com.khareeflive.app.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.khareeflive.app.R;
import com.khareeflive.app.base.KhareefLiveApplication;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ManualActivity extends Activity {
	ProgressBar pbmanual;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manual);
		initializeControl();
		ShowPDF();

	}

	private void initializeControl() {
		pbmanual = (ProgressBar) findViewById(R.id.pbmanual);
	}

	public void ShowPDF() {

		initializeControl();
		pbmanual.setVisibility(View.VISIBLE);

		File fileBrochure = new File("/sdcard/KhareefEvent.pdf");
		if (!fileBrochure.exists()) {
			CopyAssetsbrochure();
		}

		/** PDF reader code */
		File file = new File("/sdcard/KhareefEvent.pdf");

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/pdf");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			getApplicationContext().startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(ManualActivity.this, "NO Pdf Viewer",
					Toast.LENGTH_SHORT).show();
		}

		pbmanual.setVisibility(View.GONE);
	}

	private void CopyAssetsbrochure() {
		AssetManager assetManager = getAssets();
		String[] files = null;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			Log.e("tag", e.getMessage());
		}
		for (int i = 0; i < files.length; i++) {
			String fStr = files[i];
			if (fStr.equalsIgnoreCase("KhareefEvent.pdf")) {
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
