package com.khareeflive.app.asynchronoustask;

import com.khareeflive.app.activities.NetworkPerformanceActivity;

import android.os.AsyncTask;

public class NetworkPerformanceTask extends AsyncTask<Void, Void, Boolean>{

	NetworkPerformanceActivity networkperformanceactivity;
	
	public NetworkPerformanceTask(NetworkPerformanceActivity _networkperformanceactivity)
	{
		networkperformanceactivity=_networkperformanceactivity;
	}
	
	@Override
	protected void onPreExecute() {
		networkperformanceactivity.showLoader();
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		return networkperformanceactivity.GetData();

	}

	@Override
	protected void onPostExecute(Boolean result) {
		networkperformanceactivity.hideLoader();
		if(result)
		{
			networkperformanceactivity.ShowData();
		}
	}

}
