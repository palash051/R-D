package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.Locale;

import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.LiveAlarm;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

public class UserFriendListAdapter extends ArrayAdapter<User>{
	
	Context context;
	User user;
	ArrayList<User> userList;
	ArrayList<User> userTempList;
	boolean first_checked = false;
	ImageLoader imageLoader;

	public UserFriendListAdapter(Context _context, int textViewResourceId,
			ArrayList<User> _userlist) {
		super(_context, textViewResourceId, _userlist);
		context = _context;
		userList = _userlist;
		userTempList = new ArrayList<User>();
		userTempList.addAll(userList);
		imageLoader = new ImageLoader(context.getApplicationContext());
	}
	
	public int getCount() {
		return userList.size();
	}

	public User getItem(int position) {

		return userList.get(position);
	}

	public long getItemId(int position) {

		return 0;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		

		user = userList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View appBaseItemView = inflater.inflate(R.layout.my_profile_user_online_status, null);
		
		ImageView imageView =(ImageView)appBaseItemView.findViewById(R.id.userOnlineStatusImage);
		
		String profImg=CommonURL.getInstance().getImageServer+"DafaultImage/defaultuser.png";
		/*if(user.UserProfilePictures!=null &&  user.UserProfilePictures.size()>0){
			profImg=user.UserProfilePictures.get(0).PPPath;
			if(!profImg.contains("graph.facebook.com")){
				profImg=CommonURL.getInstance().getImageServer+profImg;
			}
		}*/
		profImg=profImg.replace("large", "small");
		imageLoader.DisplayImage(profImg,imageView);
        TextView textView =(TextView)appBaseItemView.findViewById(R.id.userOnlineStatusText);
        textView.setText(user.FullName);
        appBaseItemView.setTag(user.UserNumber);
        return appBaseItemView;
        
		
		
		/*//CheckedTextView ckUserList = (CheckedTextView) userItemView.findViewById(R.id.usergroupListCheckedTextView);
		ckUserList.setText(user.FullName);
		ckUserList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_icon, 0, 0, 0);
		userItemView.setTag(user.UserNumber);
		return userItemView;*/
		
		
		/*TextView name = (TextView) contactListItemView.findViewById(R.id.tvContactListName);
		TextView number = (TextView) contactListItemView.findViewById(R.id.tvContactListNumber);
		ImageView image = (ImageView) contactListItemView.findViewById(R.id.ivContactListImage);
		
		name.setText(contactList.getName().toString());
		number.setText(contactList.getNumber().toString());
		if(contactList.getPicture() != null){
			Bitmap picture = contactList.getPicture();
			image.setImageBitmap(picture);
		}
		contactListItemView.setTag(contactList);*/
	}
	
	public void applyFilter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		userList.clear();
		if (charText.length() == 0) {
			userList.addAll(userTempList);
		} else {
			for (User user : userTempList) {
				
				if(user.Name.toLowerCase(Locale.getDefault()).contains(charText)){
					userList.add(user);
				}else if(user.FullName.toLowerCase(Locale.getDefault()).contains(charText)){
					userList.add(user);
				}
			}
		}
		notifyDataSetChanged();
	}
	
	

}
