package com.vipdashboard.app.adapter;

import com.vipdashboard.app.R;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class AppBaseGridViewAdapter extends BaseAdapter{

	Context context;
	
	/*public Integer[] AppImage = {
			R.drawable.my_alarmmanager, R.drawable.my_dashboard, R.drawable.my_reports,
			R.drawable.my_troubletickets, R.drawable.my_remoteconnect, R.drawable.my_rolloutmanager,
			R.drawable.my_siteaudit, R.drawable.my_sitedatabase, R.drawable.my_onlineourses
	};*/
	
	public Integer[] AppImage = {
			R.drawable.my_alarmmanager, R.drawable.my_dashboard, R.drawable.my_reports,
			R.drawable.my_troubletickets,R.drawable.my_nro_manager, R.drawable.my_siteaudit, R.drawable.my_manuals
	};    
	
	/*public String[] AppImageTitle = {
			"Alarm Manager", "Dashboard","Reports",
			"Trouble Tickets","Remote Connect","Rollout Manager",
			"Site Audit", "Site Database", "Online Courses"
	};*/
	public String[] AppImageTitle = {
			"Alarm Manager", "Dashboard","Reports",
			"Trouble Tickets","NRO Manager","Site Audit", "Manuals"
	};
	
	public AppBaseGridViewAdapter(Context _conContext){
		context = _conContext;
	}

	@Override
	public int getCount() {
		return AppImage.length;
	}

	@Override
	public Object getItem(int position) {
		return AppImage[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {		
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View appBaseItemView = inflater.inflate(R.layout.appsbase_griditem_layout, null);
		
		
		ImageView imageView =(ImageView)appBaseItemView.findViewById(R.id.ivAppBaseGridItem);
        imageView.setBackgroundResource(AppImage[position]);
       // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
       // imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        TextView textView =(TextView)appBaseItemView.findViewById(R.id.tvAppBaseGridItemTitle);
        textView.setText(AppImageTitle[position]);
        return appBaseItemView;
	}

}
