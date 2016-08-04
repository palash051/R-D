package com.khareeflive.app.entities;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.khareeflive.app.R;

public class ChatGroupAdapter extends ArrayAdapter<ChatGroup> {
	Context context;
	ChatGroup ChatGroup;
	ArrayList<ChatGroup> chatGroupList;

	public ChatGroupAdapter(Context _context, int resource,
			ArrayList<ChatGroup> _chatGroupList) {
		super(_context, resource, _chatGroupList);
		context = _context;
		chatGroupList = _chatGroupList;
	}

	public int getCount() {
		return chatGroupList.size();
	}

	public ChatGroup getItem(int position) {

		return chatGroupList.get(position);
	}

	public long getItemId(int position) {

		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		ChatGroup = chatGroupList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View userItemView = inflater.inflate(R.layout.userlistiteminfo,
				null);
		TextView headingText = (TextView) userItemView
				.findViewById(R.id.tvUserName);
		headingText.setText(ChatGroup.message);
		userItemView.setTag(ChatGroup.message);
		return userItemView;
	}

}
