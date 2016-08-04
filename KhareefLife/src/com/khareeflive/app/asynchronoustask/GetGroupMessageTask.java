package com.khareeflive.app.asynchronoustask;

import java.util.ArrayList;

import android.os.AsyncTask;

import com.khareeflive.app.activities.GroupChatActivity;
import com.khareeflive.app.entities.RoomWiseChat;

public class GetGroupMessageTask extends AsyncTask<Void, Void, ArrayList<RoomWiseChat>>{
	GroupChatActivity activity;
	public GetGroupMessageTask(GroupChatActivity _activity) {
		this.activity = _activity;
	}
	@Override
	protected void onPreExecute() {
		
	}
	@Override
	protected ArrayList<RoomWiseChat> doInBackground(Void... cap) {
		return activity.GetMessage();
	}
	
	@Override
	protected void onPostExecute(ArrayList<RoomWiseChat> data) {
		activity.processGetMessage(data);
	}

}
