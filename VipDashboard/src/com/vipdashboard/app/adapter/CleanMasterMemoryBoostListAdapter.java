package com.vipdashboard.app.adapter;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.VIPDBatteryDoctorRunningAppsActivity;
import com.vipdashboard.app.utils.CommonTask;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CleanMasterMemoryBoostListAdapter extends
		ArrayAdapter<ApplicationInfo> {

	private Context context;
	private PackageManager packageManager;
	private ArrayList<ApplicationInfo> applicationInfoList;
	private ApplicationInfo applicationInfo;

	ImageView ivRunningAppsOn, ivRunningAppsOff;
	RelativeLayout rlRunningApps;
	int imagesize=0;

	public CleanMasterMemoryBoostListAdapter(Context _context,
			int textViewResourceId, ArrayList<ApplicationInfo> _objects) {
		super(_context, textViewResourceId, _objects);
		context = _context;
		applicationInfoList = _objects;
		packageManager = context.getPackageManager();
		imagesize=CommonTask.dpToPx(context,80);
	}

	@Override
	public int getCount() {
		return ((null != applicationInfoList) ? applicationInfoList.size() : 0);
	}

	@Override
	public ApplicationInfo getItem(int position) {
		return ((null != applicationInfoList) ? applicationInfoList
				.get(position) : null);
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
		View view = layoutInflater.inflate(
				R.layout.vipd_appmanager_uninstall_item, null);
		if (null != applicationInfo) {

			CheckedTextView checkedTextView = (CheckedTextView) view
					.findViewById(R.id.usergroupListCheckedTextView);
			File file = new File(applicationInfo.sourceDir);
			double size = file.length() / (1024 * 1024);
			String text = applicationInfo.loadLabel(packageManager).toString()
					+ "\n"
					+ String.valueOf(new DecimalFormat("##.##").format(size))
					+ "MB";
			checkedTextView.setText(text);
			Drawable drimage = applicationInfo.loadIcon(packageManager);
			Bitmap b = ((BitmapDrawable) drimage).getBitmap();
			Bitmap bitmapResized = Bitmap
					.createScaledBitmap(b, imagesize, imagesize, false);
			@SuppressWarnings("deprecation")
			Drawable dr = new BitmapDrawable(bitmapResized);
			checkedTextView.setCompoundDrawablesWithIntrinsicBounds(dr, null,
					null, null);
			view.setTag(applicationInfo);
		}
		return view;
	}
}
