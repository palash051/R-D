package com.vipdashboard.app.fragments;

import java.util.List;

import com.vipdashboard.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ListViewResidualFileAdaper extends BaseAdapter {
	LayoutInflater mInflater = null;
	private List<ResidualFileItem> listResidualFile;

	private class ViewHolder {
		TextView txtFileName;
		CheckBox chbWillDelete;

	}

	public ListViewResidualFileAdaper(Context context,
			List<ResidualFileItem> listResidual) {
		this.mInflater = LayoutInflater.from(context);
		this.listResidualFile = listResidual;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.activity_residualfile_item, null);

			holder.txtFileName = (TextView) convertView
					.findViewById(R.id.txt_residual_fileName);
			holder.chbWillDelete = (CheckBox) convertView
					.findViewById(R.id.checkbox_residualdile);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ResidualFileItem appItemBean = (ResidualFileItem) listResidualFile
				.get(position);
		holder.chbWillDelete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				CheckBox checkBox = (CheckBox) v;
				ResidualFileItem appItemBean = (ResidualFileItem) checkBox
						.getTag();
				int posi = listResidualFile.indexOf(appItemBean);
				listResidualFile.get(posi).willBeDelete = holder.chbWillDelete
						.isChecked();
			}
		});
		holder.txtFileName
				.setText(listResidualFile.get(position).getFileName());
		holder.chbWillDelete.setChecked(listResidualFile.get(position)
				.isWillBeDelete());
		holder.chbWillDelete.setTag(appItemBean);
		return convertView;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listResidualFile.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listResidualFile.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
