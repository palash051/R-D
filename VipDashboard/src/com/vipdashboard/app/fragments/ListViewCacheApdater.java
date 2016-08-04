package com.vipdashboard.app.fragments;

import java.util.List;

import com.vipdashboard.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewCacheApdater extends BaseAdapter {
	private LayoutInflater mInflater;

	private List<InstalledAppItem> listData;


	private class ViewHolder {
		ImageView image;
		TextView title;
		TextView description;
	}

	public ListViewCacheApdater(Context context, List<InstalledAppItem> listData) {
		this.mInflater = LayoutInflater.from(context);
		this.listData = listData;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.activity_cache_item,
					null);

			holder.image = (ImageView) convertView
					.findViewById(R.id.history_imageview_icon);
			holder.title = (TextView) convertView
					.findViewById(R.id.history_textview_title);
			holder.description = (TextView) convertView
					.findViewById(R.id.history_textview_cache);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();

		// ProcessItemBean bean = (ProcessItemBean) listData.get(position);

		holder.image.setBackgroundDrawable(listData.get(position).getImgApp());
		holder.title.setText(listData.get(position).getNameApp());
		holder.description.setText(listData.get(position).getCache());
		/*
		 * if (selectMap.get(position) != null) {
		 * holder.checkbox.setChecked(true); } else {
		 * holder.checkbox.setChecked(false); }
		 */

		return convertView;
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}
