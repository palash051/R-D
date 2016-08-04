package com.vipdashboard.app.adapter;

import java.text.ParseException;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.Notification;
import com.vipdashboard.app.utils.CommonTask;

public class NotificationListAdapter extends ArrayAdapter<Notification>{
	
	Context context;
	Notification notification;
	ArrayList<Notification> notificationList;

	public NotificationListAdapter(Context _context, int resource,
			ArrayList<Notification> _collaborationList) {
		super(_context, resource, _collaborationList);
		context = _context;
		notificationList = _collaborationList;
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
		View userItemView = inflater.inflate(R.layout.notification_list_item_layout, null);
		try {
			TextView tvMessageTitle = (TextView) userItemView
					.findViewById(R.id.tvNotificationTitle);
			TextView tvMessageText = (TextView) userItemView
					.findViewById(R.id.tvNotificationText);
			TextView tvMessageTime = (TextView) userItemView
					.findViewById(R.id.tvNotificationTime);
			tvMessageTitle.setText(notification.msgFromUser.Name);
			tvMessageText.setText(notification.NotificationText);
			tvMessageTime.setText(CommonTask.toMessageTimeAsString(notification.UpdatedDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return userItemView;
	}

}
