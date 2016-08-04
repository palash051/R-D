package com.vipdashboard.app.adapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.LiveAlarm;
import com.vipdashboard.app.utils.CommonTask;

public class LiveAlarmListAdapter extends ArrayAdapter<LiveAlarm>{
	Context context;
	LiveAlarm liveAlarm;
	ArrayList<LiveAlarm> liveAlarmList;
	ArrayList<LiveAlarm> liveAlarmSearchItemList;

	public LiveAlarmListAdapter(Context _context, int resource,
			ArrayList<LiveAlarm> _liveAlarmList) {
		super(_context, resource, _liveAlarmList);
		context = _context;
		liveAlarmList = _liveAlarmList;
		liveAlarmSearchItemList = new ArrayList<LiveAlarm>();
		liveAlarmSearchItemList.addAll(liveAlarmList);
	}
	public int getCount() {
		return liveAlarmList.size();
	}

	public LiveAlarm getItem(int position) {

		return liveAlarmList.get(position);
	}
	
	public LiveAlarm getLastItem(){
		return liveAlarmList.get(liveAlarmList.size()-1);
	}

	public long getItemId(int position) {

		return 0;
	}
	public LiveAlarm getItemByAlarmId(int Id) {
		
		for (LiveAlarm liveAlarm : liveAlarmList) {
			if(liveAlarm.AlarmID==Id){
				return liveAlarm;
			}
		}

		return null;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {	
		View liveAlarmItemView=null;
		try {
			liveAlarm = liveAlarmList.get(position);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			liveAlarmItemView = inflater.inflate(R.layout.alarm_list_item, null);
			TextView tvSeverity = (TextView) liveAlarmItemView
					.findViewById(R.id.tvAlarmSeverity);
			TextView tvLiveAlarmName = (TextView) liveAlarmItemView
					.findViewById(R.id.tvAlarmText);
			TextView tvStatus = (TextView) liveAlarmItemView
					.findViewById(R.id.tvAlarmStatus);
			TextView tvSystem = (TextView) liveAlarmItemView.findViewById(R.id.tvSystemCore);
			TextView tvEventTime = (TextView) liveAlarmItemView
					.findViewById(R.id.tvDateandTime);
			
			
			RelativeLayout rlTTRequest=(RelativeLayout) liveAlarmItemView
					.findViewById(R.id.rlTTRequest);
			RelativeLayout rlAlarmManagerAdapter = (RelativeLayout) liveAlarmItemView
					.findViewById(R.id.rlAlarmManager);
			
			if(liveAlarm.TTrequests!=null && liveAlarm.TTrequests.size()>0){
				rlTTRequest.setVisibility(View.VISIBLE);
				TextView tvTTNumber = (TextView) liveAlarmItemView
						.findViewById(R.id.tvTTNumber);
					tvTTNumber.setText(liveAlarm.TTrequests.get(0).TTNumber);
					tvSystem.setText(""+liveAlarm.TTrequests.get(0).Department);
				/*rlTTRequest.setTag(liveAlarm.TTrequests.get(0));
				rlTTRequest.setOnClickListener(new RelativeLayout.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						TTRequestDetailsFragment.TTrequest=(TTrequest) v.getTag();
						((AlarmActivity)context). startFragment();
					}
				});*/
				
			}else{
				rlTTRequest.setVisibility(View.GONE);
				tvLiveAlarmName.setTextColor(Color.rgb(62, 72, 204));
				tvStatus.setTextColor(Color.rgb(62, 72, 204));
				tvEventTime.setTextColor(Color.rgb(62, 72, 204));
				tvSystem.setTextColor(Color.rgb(62, 72, 204));
				rlAlarmManagerAdapter.setBackgroundColor(Color.YELLOW);
			}
			
			tvSeverity.setText(liveAlarm.Priority);
			tvLiveAlarmName.setText(liveAlarm.AlarmText);
			tvStatus.setText(liveAlarm.Status);
			
			tvEventTime.setText( CommonTask.convertJsonDateToLiveAlarm(liveAlarm.EventTime));
			
			liveAlarmItemView.setTag(liveAlarm.AlarmID);
			
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		return liveAlarmItemView;
	}
	
	public void AlarmListFilter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		liveAlarmList.clear();
		if (charText.length() == 0) {
			liveAlarmList.addAll(liveAlarmSearchItemList);
		} else {
			for (LiveAlarm liveAlarm : liveAlarmSearchItemList) {
				if(liveAlarm.TTrequests.size() > 0) {
					if (liveAlarm.TTrequests.get(0).TTNumber.toLowerCase(Locale.getDefault()).contains(charText)) {
						liveAlarmList.add(liveAlarm);
					}else if(liveAlarm.Priority.toLowerCase(Locale.getDefault()).contains(charText)){
						liveAlarmList.add(liveAlarm);
					}else if(liveAlarm.AlarmText.toLowerCase(Locale.getDefault()).contains(charText)){
						liveAlarmList.add(liveAlarm);
					}else if(liveAlarm.Status.toLowerCase(Locale.getDefault()).contains(charText)){
						liveAlarmList.add(liveAlarm);
					}else if(liveAlarm.TTrequests.get(0).Department.toLowerCase(Locale.getDefault()).contains(charText)){
						liveAlarmList.add(liveAlarm);
					}
				}
			}
		}
		notifyDataSetChanged();
	}
}
