package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.vipdashboard.app.R;
import com.vipdashboard.app.classes.Report;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReportAdapter extends ArrayAdapter<Report>{
	
	Context context;
	Report report;
	ArrayList<Report> reportlist;
	ArrayList<Report> reportSearchItem;

	public ReportAdapter(Context _context, int textViewResourceId,
			ArrayList<Report> _objects) {
		super(_context, textViewResourceId, _objects);
		context = _context;
		reportlist = _objects;
		reportSearchItem = new ArrayList<Report>();
		reportSearchItem.addAll(reportlist);
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {	
		report = reportlist.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View lavelItemView = inflater.inflate(R.layout.lavel_item_layout, null);
		//CheckedTextView text = (CheckedTextView) lavelItemView.findViewById(R.id.checkedTextView);
		//text.setTag(lavel.LevelID);
		//text.setText(lavel.LevelName);
		TextView lavelText = (TextView) lavelItemView.findViewById(R.id.tvLavelList);
		ImageView ivLavelList = (ImageView) lavelItemView.findViewById(R.id.ivLavelList);
		//String name = lavel.LevelName;
		ivLavelList.setVisibility(View.GONE);
		
		//ImageView of Status made invisible as per Imtiaz vaiya's design like What's Apps 19-05-2014 Raju Dutta.
		/*if(report.getID() == CommonConstraints.OFFLINE){
			ivLavelList.setImageResource(R.drawable.user_status_offline);
		}else if(report.getID() == CommonConstraints.AWAY){
			ivLavelList.setImageResource(R.drawable.user_status_away);
		}else if(report.getID() == CommonConstraints.ONLINE){
			ivLavelList.setImageResource(R.drawable.user_status_online);
		}else if(report.getID() == CommonConstraints.DO_NOT_DISTURB){
			ivLavelList.setImageResource(R.drawable.user_status_busy);
		}else if(report.getID() == CommonConstraints.BUSY){
			ivLavelList.setImageResource(R.drawable.user_status_busy);
		}else{
			ivLavelList.setImageResource(R.drawable.user_status_offline);
		}*/
		
		
		
		lavelText.setText(report.getName().trim());
		//CommonTask.makeLinkedTextview(context, lavelText, report.getName());
		lavelItemView.setTag(report.getID());
		
		return lavelItemView;

	}
	
	public void Filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		reportlist.clear();
		if (charText.length() == 0) {
			reportlist.addAll(reportSearchItem);
		} else {
			for (Report report : reportSearchItem) {
				/*if(user.TTrequests.size() > 0) {
					if (liveAlarm.TTrequests.get(0).TTNumber.toLowerCase(Locale.getDefault()).contains(charText)) {
						liveAlarmList.add(liveAlarm);
					}else if(liveAlarm.Priority.toLowerCase(Locale.getDefault()).contains(charText)){
						liveAlarmList.add(liveAlarm);
					}else if(liveAlarm.AlarmText.toLowerCase(Locale.getDefault()).contains(charText)){
						liveAlarmList.add(liveAlarm);
					}else if(liveAlarm.Status.toLowerCase(Locale.getDefault()).contains(charText)){
						liveAlarmList.add(liveAlarm);
					}
				}*/
				if(report.getName().toLowerCase(Locale.getDefault()).contains(charText)){
					reportlist.add(report);
				}
			}
		}
		notifyDataSetChanged();
	}
	
	

}
