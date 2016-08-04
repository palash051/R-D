package com.khareeflive.app.asynchronoustask;

import com.khareeflive.app.activities.ChatActivity;
import com.khareeflive.app.entities.Message;

import android.os.AsyncTask;

public class GetMessageTask extends AsyncTask<Void, Void, Message>{
	ChatActivity activity;
	public GetMessageTask(ChatActivity _activity) {
		this.activity = _activity;
	}
	@Override
	protected void onPreExecute() {
		
	}
	@Override
	protected Message doInBackground(Void... cap) {
		return activity.GetMessage();
	}
	
	@Override
	protected void onPostExecute(Message data) {
		activity.processGetMessage(data);
	}
}
