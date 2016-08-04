package com.vipdashboard.app.asynchronoustask;


import com.vipdashboard.app.interfaces.IPhoneInformationService;
import com.vipdashboard.app.manager.PhoneInformationManager;

import android.content.Context;
import android.os.AsyncTask;

public class SpeedTestAsyncTask extends AsyncTask<Void, Void, Object>{
	Context context;
	
	public SpeedTestAsyncTask(Context context){
		this.context = context;	
		
	}
	
	@Override
	protected void onPreExecute() {	
	}

	@Override
	protected Object doInBackground(Void... cap) {
		IPhoneInformationService manager=new PhoneInformationManager();		
		return manager.SetDataSpeedInfo(context,false);
	}
	
	@Override
	protected void onPostExecute(Object data) {	
		
	}

}
