package com.khareeflive.app.asynchronoustask;

import android.os.AsyncTask;

import com.khareeflive.app.activities.ChatActivity;

public class SendMessageTask extends AsyncTask<Void, Void, Boolean>{
	ChatActivity activity;
	public SendMessageTask(ChatActivity _activity) {
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
