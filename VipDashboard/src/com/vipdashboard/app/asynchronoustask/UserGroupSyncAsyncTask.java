package com.vipdashboard.app.asynchronoustask;


import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;

import android.content.Context;
import android.os.AsyncTask;

public class UserGroupSyncAsyncTask extends AsyncTask<Void, Void, Object>{
	Context context;
	
	public UserGroupSyncAsyncTask(Context context){
		this.context = context;	
		
	}
	
	@Override
	protected void onPreExecute() {	
		
	}

	@Override
	protected Object doInBackground(Void... cap) {
		IUserManager manager=new UserManager();		
		manager.UpdateUserGroupSync(context);
		return null;
	}
	
	@Override
	protected void onPostExecute(Object data) {	
		
	}

}
