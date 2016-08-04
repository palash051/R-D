package com.vipdashboard.app.fragments;

import java.util.ArrayList;
import java.util.List;

import com.vipdashboard.app.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class BaseExpandableListResidualFileAdapter extends
		BaseExpandableListAdapter {
	LayoutInflater mInflater = null;

	private Context context;
	private ArrayList<GroupHeaderDetail> groupList;
	private List<List<ResidualFileItem>> childList;

	private class ViewHolder {
		TextView txtFileName;
		CheckBox chbWillDelete;

	}

	public BaseExpandableListResidualFileAdapter(Context context,
			ArrayList<GroupHeaderDetail> groupList,
			List<List<ResidualFileItem>> children) {
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		this.groupList = groupList;
		this.childList = children;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// ArrayList<ResidualFileItem> residualList =
		// groupList.get(groupPosition)
		// .getResidualFileItemsList();
		// return residualList.get(childPosition);
		if (childList != null && childList.size() > groupPosition
				&& childList.get(groupPosition) != null) {
			if (childList.get(groupPosition).size() > childPosition)
				return childList.get(groupPosition).get(childPosition);
		}

		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {

			view = mInflater.inflate(R.layout.activity_residualfile_item, null);
			final ViewHolder holder = new ViewHolder();
			// convertView = mInflater.inflate(
			// R.layout.activity_residualfile_item, null);
			//
			holder.txtFileName = (TextView) view
					.findViewById(R.id.txt_residual_fileName);
			holder.chbWillDelete = (CheckBox) view
					.findViewById(R.id.checkbox_residualdile);
			holder.chbWillDelete
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							ResidualFileItem item = (ResidualFileItem) holder.chbWillDelete
									.getTag();
							item.setWillBeDelete(buttonView.isChecked());
						}
					});
			view.setTag(holder);
			holder.chbWillDelete.setTag(childList.get(groupPosition).get(
					childPosition));

		} else {
			view = convertView;
			((ViewHolder) view.getTag()).chbWillDelete.setTag(childList.get(
					groupPosition).get(childPosition));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.chbWillDelete.setChecked(childList.get(groupPosition)
				.get(childPosition).isWillBeDelete());
		holder.txtFileName.setText(childList.get(groupPosition)
				.get(childPosition).getFileName());

		// holder.txtFileName
		// .setText(listResidualFile.get(position).getFileName());
		// holder.chbWillDelete.setChecked(listResidualFile.get(position)
		// .isWillBeDelete());
		// holder.chbWillDelete.setTag(appItemBean);
		return view;

	}

	@Override
	public int getChildrenCount(int groupPosition) {

		if (childList != null && childList.size() > groupPosition
				&& childList.get(groupPosition) != null)
			return childList.get(groupPosition).size();

		return 0;
		// ArrayList<ResidualFileItem> ResidualList =
		// groupList.get(groupPosition)
		// .getResidualFileItemsList();
		// return ResidualList.size();

	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groupList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isLastChild, View view,
			ViewGroup parent) {

		GroupHeaderDetail GroupHeaderDetail = (GroupHeaderDetail) getGroup(groupPosition);
		if (view == null) {
			LayoutInflater inf = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inf.inflate(R.layout.expand_list_group_heading, null);
		}

		TextView heading = (TextView) view.findViewById(R.id.heading);
		heading.setText(GroupHeaderDetail.getName().trim());
		heading.setTextColor(Color.parseColor("#000000"));
		TextView numFiles = (TextView) view.findViewById(R.id.num_files);
		numFiles.setText("" + childList.get(groupPosition).size());

		return view;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
