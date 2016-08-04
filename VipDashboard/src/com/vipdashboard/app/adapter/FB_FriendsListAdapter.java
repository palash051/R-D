package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.Locale;

import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.FacebookInvitationActivity;
import com.vipdashboard.app.entities.FB_Friend;
import com.vipdashboard.app.entities.FacebookFriends;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FB_FriendsListAdapter extends ArrayAdapter<FacebookFriends> implements OnClickListener {

	Context context;
	ArrayList<FacebookFriends> fb_Friendslist;
	ArrayList<FacebookFriends> userTempList;
	FacebookFriends fb_Friend;

	public FB_FriendsListAdapter(Context _context, int textViewResourceId,
			ArrayList<FacebookFriends> _objects) {
		super(_context, textViewResourceId, _objects);
		context = _context;
		fb_Friendslist = _objects;
		userTempList = new ArrayList<FacebookFriends>();
		userTempList.addAll(fb_Friendslist);
	}
	
	private class Holder{
		ImageView fb_personImage; 
		TextView fb_PersonName;
	}

	public int getCount() {
		return fb_Friendslist.size();
	}

	public FacebookFriends getItem(int position) {

		return fb_Friendslist.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		fb_Friend = fb_Friendslist.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View fb_FriendsListView = inflater.inflate(
				R.layout.fb_friends_list_adapter, null);
		
		ImageView fb_personImage = (ImageView) fb_FriendsListView.findViewById(R.id.ivFB_PersonImage);
		ImageView fb_sendAppRequest = (ImageView) fb_FriendsListView.findViewById(R.id.ivPlus);
		TextView fb_PersonName = (TextView) fb_FriendsListView.findViewById(R.id.tvFb_PersonName);
		
		String url="https://graph.facebook.com/"+fb_Friend.FriendsID+"/picture?type=small";
		ImageLoader il=new ImageLoader(context);
		il.DisplayImage(url, fb_personImage);
		
		fb_PersonName.setText(fb_Friend.FriendName);
		fb_sendAppRequest.setTag(fb_Friend.FriendsID);
		fb_sendAppRequest.setOnClickListener(this);
		fb_FriendsListView.setTag(fb_Friend);
		return fb_FriendsListView;
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.ivPlus){
			Intent intent = new Intent(context, FacebookInvitationActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("ID", view.getTag().toString());
			context.startActivity(intent);
		}
	}
	
	public void applyFilter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		fb_Friendslist.clear();
		if (charText.length() == 0) {
			fb_Friendslist.addAll(userTempList);
		} else {
			for (FacebookFriends user : userTempList) {
				/*if(user.TTrequests.size() > 0) {
					if (liveAlarm.TTrequests.get(0).TTNumber.toLowerCase(Locale.getDefault()).contains(charText)) {
						liveAlarmList.add(liveAlarm);
					}else if(liveAlarm.Priority.toLowerCase(Locale.getDefault()).contains(charText)){
						liveAlarmList.add(liveAlarm);
					}else if(liveAlarm.AlarmText.toLowerCase(Locale.getDefault()).contains(charText)){
						liveAlarmList.add(liveAlarm);
					}else if(liveAlarm.Status.toLowerCase(Locale.getDefault()).contains(charText)){
						liveAlarmList.add(liveAlarm);
					}
				}*/
				if(user.FriendName.toLowerCase(Locale.getDefault()).contains(charText)){
					fb_Friendslist.add(user);
				}/*else if(user.UserName.toLowerCase(Locale.getDefault()).contains(charText)){
					fb_Friendslist.add(user);
				}*/
			}
		}
		notifyDataSetChanged();
	}

}
