package com.khareeflive.app.asynchronoustask;

import com.khareeflive.app.activities.LatestUpdateActivity;
import com.khareeflive.app.activities.SplashActivity;

import android.os.AsyncTask;

public class SplashTask extends AsyncTask<Void, Void, Boolean>{
	SplashActivity splashactivity;
	
	public SplashTask(SplashActivity _splashactivity)
	{
		splashactivity=_splashactivity;
	}
	
	@Override
	protected void onPreExecute() {
		splashactivity.showLoader();
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		return splashactivity.GetDate();

	}

	@Override
	protected void onPostExecute(Boolean result) {
		splashactivity.hideLoader();
		if(result)
		{
			splashactivity.showData();
		}
	}
}
