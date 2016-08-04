package com.vipdashboard.app.adapter;

import java.util.ArrayList;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.Notification;
import com.vipdashboard.app.utils.CommonTask;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationCharListAdapter extends ArrayAdapter<Notification>{
	
	Context context;
	Notification notification;
	ArrayList<Notification> notificationList;

	public NotificationCharListAdapter(Context _context, int textViewResourceId,
			ArrayList<Notification> _NotificationList) {
		super(_context, textViewResourceId, _NotificationList);
		context = _context;
		notificationList = _NotificationList;
	}
	
	public int getCount() {
		return notificationList.size();
	}
	
	public Notification getLastItem(){
		return notificationList.get(notificationList.size()-1);
	}
	public void addItem(Notification item){
		notificationList.add(item);
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		notification = notificationList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View userItemView = inflater.inflate(R.layout.message_layout, null);
		try{
			TextView tvMessageTitle = (TextView) userItemView
					.findViewById(R.id.tvMessageTitle);
			TextView tvMessageText = (TextView) userItemView
					.findViewById(R.id.tvMessageText);
			TextView tvMessageTime = (TextView) userItemView
					.findViewById(R.id.tvMessageTime);
			RelativeLayout rlMessage = (RelativeLayout) userItemView
					.findViewById(R.id.rlMessage);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			rlMessage.setBackgroundResource(R.drawable.left_message_bg);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			
			rlMessage.setLayoutParams(params);
			Log.e("value of list are: ", notification.msgFromUser.Name + " " + CommonTask.toMessageDateAsString(notification.UpdatedDate).toString() + " " + notification.NotificationText);
			tvMessageTitle.setText(notification.msgFromUser.Name);
			tvMessageTime.setText(CommonTask.toMessageDateAsString(notification.UpdatedDate).toString());
			tvMessageText.setText(notification.NotificationText);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return userItemView;
	}

}
