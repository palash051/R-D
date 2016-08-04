package com.vipdashboard.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class AllAppListAdapter extends BaseAdapter{
	Context context;
	ArrayList<ApplicationInfo> applicationInfoList;
	PackageManager packageManager;
	public AllAppListAdapter(Context _context, ArrayList<ApplicationInfo> _application){
		context = _context;
		applicationInfoList = _application;
		packageManager = _context.getPackageManager();
	}

	@Override
	public int getCount() {
	
		return applicationInfoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		
		return applicationInfoList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ApplicationInfo appinfo = applicationInfoList.get(arg0);
		ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(appinfo.loadIcon(packageManager));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(75, 75));
		return imageView;
	}

}
