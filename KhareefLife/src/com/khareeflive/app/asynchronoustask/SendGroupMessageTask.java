package com.khareeflive.app.asynchronoustask;

import android.os.AsyncTask;

import com.khareeflive.app.activities.GroupChatActivity;

public class SendGroupMessageTask extends AsyncTask<Void, Void, Boolean>{
	GroupChatActivity activity;
	public SendGroupMessageTask(GroupChatActivity _activity) {
		this.activity = _activity;
	}
	@Override
	protected void onPreExecute() {
		
	}
	@Override
	protected Boolean doInBackground(Void... cap) {
		return activity.SendMessage();
	}
	
	@Override
	protected void onPostExecute(Boolean data) {
		activity.procesSendData(data);
	}
}
