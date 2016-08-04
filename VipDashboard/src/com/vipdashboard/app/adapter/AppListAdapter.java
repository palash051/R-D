package com.vipdashboard.app.adapter;

import java.util.ArrayList;

import com.vipdashboard.app.R;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppListAdapter extends ArrayAdapter<ApplicationInfo>{
	
	private Context context;
	private PackageManager packageManager;
	private ArrayList<ApplicationInfo> applicationInfoList;
	private ApplicationInfo applicationInfo;

	public AppListAdapter(Context _context, int textViewResourceId,
			ArrayList<ApplicationInfo> _objects) {
		super(_context, textViewResourceId, _objects);
		context = _context;
		applicationInfoList = _objects;
		packageManager = context.getPackageManager();
	}
	
	@Override
    public int getCount() {
        return ((null != applicationInfoList) ? applicationInfoList.size() : 0);
    }
 
    @Override
    public ApplicationInfo getItem(int position) {
        return ((null != applicationInfoList) ? applicationInfoList.get(position) : null);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	applicationInfo = applicationInfoList.get(position);
    	LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
        View view = layoutInflater.inflate(R.layout.app_list_item_layout, null);
        
        if (null != applicationInfo) {
            TextView appName = (TextView) view.findViewById(R.id.tvAppListName);
            ImageView iconview = (ImageView) view.findViewById(R.id.ivAppImage);
 
            appName.setText(applicationInfo.loadLabel(packageManager));
            iconview.setImageDrawable(applicationInfo.loadIcon(packageManager));
            view.setTag(applicationInfo);
        }
        return view;
    }

}
