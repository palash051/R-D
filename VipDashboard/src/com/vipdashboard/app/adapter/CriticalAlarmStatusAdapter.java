package com.vipdashboard.app.adapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.Lavel;
import com.vipdashboard.app.entities.TTStatus;
import com.vipdashboard.app.utils.CommonTask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CriticalAlarmStatusAdapter extends ArrayAdapter<TTStatus>{
	
	Context context;
	TTStatus TTstatus;
	ArrayList<TTStatus> TTstatusList;

	public CriticalAlarmStatusAdapter(Context _context, int textViewResourceId,
			ArrayList<TTStatus> _objects) {
		super(_context, textViewResourceId, _objects);
		context = _context;
		TTstatusList = _objects;
	}
	
	public int getCount() {
		return TTstatusList.size();
	}

	
	public TTStatus getItem(int position) {

		return TTstatusList.get(position);
	}

	
	public long getItemId(int position) {

		return 0;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		TTstatus = TTstatusList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View lavelItemView = inflater.inflate(R.layout.critical_alarm_status_item, null);
		//CheckedTextView text = (CheckedTextView) lavelItemView.findViewById(R.id.checkedTextView);
		//text.setTag(lavel.LevelID);
		//text.setText(lavel.LevelName);
		TextView lavelText = (TextView) lavelItemView.findViewById(R.id.tvCriticalAlarmList);
		//String name = lavel.LevelName;
		try {
			lavelText.setText(TTstatus.UserName
					+ " , "
					+ CommonTask
							.convertJsonDateToTTStatusTime(TTstatus.StatusUpdateTime)
					+ " , " + TTstatus.Status + " : "
					+ TTstatus.Comments);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lavelItemView.setTag(TTstatus.TTCode);
		
		return lavelItemView;
	}

}
