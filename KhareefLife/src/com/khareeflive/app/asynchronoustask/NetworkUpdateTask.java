package com.khareeflive.app.asynchronoustask;

import com.khareeflive.app.activities.NetworkUpdateActivity;

import android.os.AsyncTask;

public class NetworkUpdateTask extends AsyncTask<Void, Void, Boolean>{

	NetworkUpdateActivity _networkupdateactivity;
	
	public NetworkUpdateTask(NetworkUpdateActivity networkupdateActivity)
	{
		_networkupdateactivity=networkupdateActivity;
	}
	@Override
	protected void onPreExecute() {
		
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		return _networkupdateactivity.GetData();

	}

	@Override
	protected void onPostExecute(Boolean result) {
		if(result)
		{
			_networkupdateactivity.ShowData();
		}
	}
}
