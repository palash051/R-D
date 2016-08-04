package com.vipdashboard.app.asynchronoustask;

import android.content.Context;
import android.os.AsyncTask;
import com.vipdashboard.app.interfaces.IPhoneInformationService;
import com.vipdashboard.app.manager.PhoneInformationManager;

public class BrowserDataInfoAsycntask extends AsyncTask<Void, Void, Object>{
	String strData;	
	Context context;
	public BrowserDataInfoAsycntask(Context _context,String _strData) {
		this.strData = _strData;
		context=_context;		
	}
	
	@Override
	protected void onPreExecute() {	
		
	}

	@Override
	protected Object doInBackground(Void... cap) {
		IPhoneInformationService manager=new PhoneInformationManager();		
		return manager.setBrowserDataInfoPost(context,strData);
	}

	@Override
	protected void onPostExecute(Object data) {	
		
	}
}
