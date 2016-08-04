/*package com.vipdashboard.app.activities;

import java.util.List;

import com.vipdashboard.app.R;

import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;


public class VIPD_AllAppListAdapter extends ArrayAdapter<RunningAppProcessInfo> {
	
	private final Context context;
	private final List<RunningAppProcessInfo> values;

	public VIPD_AllAppListAdapter(Context context, List<RunningAppProcessInfo> values) {
	//	super(context, R.layout.activity_vipd__testing, values);
		this.context = context;
		this.values = values;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
		int uid = (getItem(position)).uid;		
		
		Toast.makeText(context, "UID:" + uid , Toast.LENGTH_SHORT).show();
		
		
		//LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.main, parent, false);
		
		TextView appName = (TextView) rowView.findViewById(R.id.appNameText);
		appName.setText(values.get(position).processName);
		
		return rowView;
		
		return null;
	}
	
	
}*/