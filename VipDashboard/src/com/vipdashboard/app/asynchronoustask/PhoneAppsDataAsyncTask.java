package com.vipdashboard.app.asynchronoustask;

import android.content.Context;
import android.os.AsyncTask;

import com.vipdashboard.app.interfaces.IPhoneInformationService;
import com.vipdashboard.app.manager.PhoneInformationManager;

public class PhoneAppsDataAsyncTask extends AsyncTask<Void, Void, Object>{
	Context context;
	public PhoneAppsDataAsyncTask(Context context){
		this.context = context;	
	}
	
	@Override
	protected void onPreExecute() {	
	}

	@Override
	protected Object doInBackground(Void... cap) {
		IPhoneInformationService manager=new PhoneInformationManager();
		
		return manager.processPhoneAppsData(context);
		
	}
	
	@Override
	protected void onPostExecute(Object data) {	
		
	}
}
