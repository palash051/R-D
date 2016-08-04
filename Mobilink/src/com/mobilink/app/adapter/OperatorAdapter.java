package com.mobilink.app.adapter;

import java.util.ArrayList;

import com.mobilink.app.R;
import com.mobilink.app.activities.MobilinkOperatorCompareActivity;
import com.mobilink.app.entities.CompanySetup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class OperatorAdapter extends BaseAdapter {

	Context context;
	ArrayList<CompanySetup> companySetUpList;
	CompanySetup companySetup;

	public OperatorAdapter(Context _context, int resourceID,
			ArrayList<CompanySetup> _companySetupList) {
		context = _context;
		companySetUpList = _companySetupList;
	}

	@Override
	public int getCount() {
		return companySetUpList.size();
	}

	public ArrayList<CompanySetup> getItemList() {
		return companySetUpList;
	}

	@Override
	public CompanySetup getItem(int position) {
		return companySetUpList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		companySetup = companySetUpList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View operatorItem = inflater.inflate(R.layout.operator_item_layout,
				null);

		CheckedTextView OperatorName = (CheckedTextView) operatorItem
				.findViewById(R.id.usergroupListCheckedTextView);
		OperatorName.setText(companySetup.CompanyName.toUpperCase());
		operatorItem.setTag(companySetup);
		return operatorItem;
	}

}
