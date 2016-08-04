package com.khareeflive.app.entities;

import java.util.ArrayList;
import com.khareeflive.app.R;
import com.khareeflive.app.activities.DirectoryActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UserListAdapter extends ArrayAdapter<LoginAuthentication> {
	Context context;
	LoginAuthentication loginAuthentication;
	ArrayList<LoginAuthentication> loginAuthenList;

	public UserListAdapter(Context _context, int resource,
			ArrayList<LoginAuthentication> _loginAuthenList) {
		super(_context, resource, _loginAuthenList);
		context = _context;
		loginAuthenList = _loginAuthenList;
	}

	public int getCount() {
		return loginAuthenList.size();
	}

	public LoginAuthentication getItem(int position) {

		return loginAuthenList.get(position);
	}

	public long getItemId(int position) {

		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		loginAuthentication = loginAuthenList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View userItemView = inflater.inflate(R.layout.userlistiteminfo,
				null);
		TextView headingText = (TextView) userItemView
				.findViewById(R.id.tvUserName);
		if(context.getClass()==DirectoryActivity.class )
			headingText.setText(loginAuthentication.userID +"\r\n          " + loginAuthentication.mobile);
		else
			headingText.setText(loginAuthentication.userID);
		userItemView.setTag(loginAuthentication);
		return userItemView;
	}

}
