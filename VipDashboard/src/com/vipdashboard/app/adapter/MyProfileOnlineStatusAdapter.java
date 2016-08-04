package com.vipdashboard.app.adapter;

import java.util.ArrayList;

import com.vipdashboard.app.R;
import com.vipdashboard.app.classes.UserStatus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyProfileOnlineStatusAdapter extends BaseAdapter {
	
Context context;
	
	/*public Integer[] AppImage = {
			R.drawable.my_alarmmanager, R.drawable.my_dashboard, R.drawable.my_reports,
			R.drawable.my_troubletickets, R.drawable.my_remoteconnect, R.drawable.my_rolloutmanager,
			R.drawable.my_siteaudit, R.drawable.my_sitedatabase, R.drawable.my_onlineourses
	};*/
	
	public Integer[] AppImage = {
			R.drawable.user_status_online, R.drawable.user_status_away, R.drawable.user_status_busy,
			R.drawable.user_status_offline	};    
	
	/*public String[] AppImageTitle = {
			"Alarm Manager", "Dashboard","Reports",
			"Trouble Tickets","Remote Connect","Rollout Manager",
			"Site Audit", "Site Database", "Online Courses"
	};*/
	public String[] AppImageTitle = {
			"Online", "Away","Do Not Disturb",
			"Invisible"	};
	
	public int[] AppImagePosition = {
		1,2,3,4	
	};
	
	UserStatus userStatus;
	ArrayList<UserStatus> userStatusList;
	
	public MyProfileOnlineStatusAdapter(Context _conContext, int resource, ArrayList<UserStatus> _list){
		context = _conContext;
		userStatusList = _list;
	}

	@Override
	public int getCount() {
		return userStatusList.size();
	}

	@Override
	public UserStatus getItem(int position) {
		return userStatusList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {		
		userStatus = userStatusList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View appBaseItemView = inflater.inflate(R.layout.my_profile_user_online_status, null);
		
		
		ImageView imageView =(ImageView)appBaseItemView.findViewById(R.id.userOnlineStatusImage);
        imageView.setBackgroundResource(userStatus.getResourceID());
       // imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
       // imageView.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        TextView textView =(TextView)appBaseItemView.findViewById(R.id.userOnlineStatusText);
        textView.setText(userStatus.getName());
        appBaseItemView.setTag(userStatus.getID());
        return appBaseItemView;
	}

}
