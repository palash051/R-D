package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.classes.Report;

public class ReportItemAdapter extends ArrayAdapter<Report>{
	Context context;
	Report report;
	ArrayList<Report> reportlist;
	ArrayList<Report> reportSearchItem;

	public ReportItemAdapter(Context _context, int textViewResourceId,
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
		View lavelItemView = inflater.inflate(R.layout.report_item_layout, null);
		//CheckedTextView text = (CheckedTextView) lavelItemView.findViewById(R.id.checkedTextView);
		//text.setTag(lavel.LevelID);
		//text.setText(lavel.LevelName);
		TextView lavelText = (TextView) lavelItemView.findViewById(R.id.tvLavelList);
		//String name = lavel.LevelName;
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
