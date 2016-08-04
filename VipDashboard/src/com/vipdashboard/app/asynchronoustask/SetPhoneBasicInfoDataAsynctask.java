package com.vipdashboard.app.asynchronoustask;


import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.PhoneBasicInformation;
import com.vipdashboard.app.interfaces.IPhoneInformationService;
import com.vipdashboard.app.manager.PhoneInformationManager;

import android.content.Context;
import android.os.AsyncTask;

public class SetPhoneBasicInfoDataAsynctask extends AsyncTask<Void, Void, Object>{
	Context context;
	public SetPhoneBasicInfoDataAsynctask(Context context) {
		this.context = context;		
	}
	
	@Override
	protected void onPreExecute() {	
		
	}

	@Override
	protected Object doInBackground(Void... cap) {
		
		IPhoneInformationService service=new PhoneInformationManager();
		return service.SetPhoneBasicInfo(context);
	}

	@Override
	protected void onPostExecute(Object data) {
		if(data!=null){			
			MyNetService.phoneBasicInformation=(PhoneBasicInformation)data;
		}
		
	}
}
