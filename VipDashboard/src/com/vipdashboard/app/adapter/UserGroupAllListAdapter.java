package com.vipdashboard.app.adapter;



import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.vipdashboard.app.R;

import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.ContactUser;
import com.vipdashboard.app.entities.Group;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.ImageLoader;

public class UserGroupAllListAdapter extends BaseAdapter {
	Context context;
	UserGroupUnion userGroupUnion;
	ArrayList<UserGroupUnion> userGroupUnionList = null;
	ArrayList<UserGroupUnion> arraylist;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	ViewHolder holder;
	int groupID;
	

	public UserGroupAllListAdapter(Context _context, int resource,
			ArrayList<UserGroupUnion> _userGroupUnionList) {
		// super(_context, resource, _userGroupUnionList);
		context = _context;
		userGroupUnionList = _userGroupUnionList;
		arraylist = new ArrayList<UserGroupUnion>();
		arraylist.addAll(userGroupUnionList);		
	}

	@Override
	public int getCount() {
		return userGroupUnionList.size();
	}

	public void setList(ArrayList<UserGroupUnion> _userGroupUnionList) {

		userGroupUnionList = _userGroupUnionList;
		notifyDataSetChanged();
	}

	@Override
	public UserGroupUnion getItem(int position) {
		return userGroupUnionList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		TextView tvGroupName;
		TextView tvlastTime;
		TextView tvSubMessage;
		ImageView ivUserGroup;
		LinearLayout llUserProfilePicture;
		ImageView ivLiveFeedLikeProfileImage1;
		ImageView ivLiveFeedLikeProfileImage2;
		ImageView ivLiveFeedLikeProfileImage3;
		ImageView ivLiveFeedLikeProfileImage4;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View userItemView = convertView;
		try {
			userGroupUnion = userGroupUnionList.get(position);
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				userItemView = inflater.inflate(
						R.layout.group_item_lastmessage, null);
				holder = new ViewHolder();
				holder.tvGroupName = (TextView) userItemView
						.findViewById(R.id.tvGroupName);
				holder.tvlastTime = (TextView) userItemView
						.findViewById(R.id.tvDate);
				holder.tvSubMessage = (TextView) userItemView
						.findViewById(R.id.tvSubMessage);
				holder.ivUserGroup = (ImageView) userItemView
						.findViewById(R.id.ivUserGroup);
				holder.llUserProfilePicture = (LinearLayout) userItemView
						.findViewById(R.id.llGroupMemberImage);
				holder.ivLiveFeedLikeProfileImage1 = (ImageView) userItemView
						.findViewById(R.id.ivLiveFeedLikeProfileImage1);
				holder.ivLiveFeedLikeProfileImage2 = (ImageView) userItemView
						.findViewById(R.id.ivLiveFeedLikeProfileImage2);
				holder.ivLiveFeedLikeProfileImage3 = (ImageView) userItemView
						.findViewById(R.id.ivLiveFeedLikeProfileImage3);
				holder.ivLiveFeedLikeProfileImage4 = (ImageView) userItemView
						.findViewById(R.id.ivLiveFeedLikeProfileImage4);
				userItemView.setTag(holder);
			} else {
				holder = (ViewHolder) userItemView.getTag();
			}

			if(userGroupUnion.Type.equals("U")){
				ContactUser user=CommonValues.getInstance().ConatactUserList.get(userGroupUnion.ID);							
				if(user!=null){
					holder.tvGroupName.setText(user.Name);
					if(user.Image!=null)
						holder.ivUserGroup.setImageBitmap(user.Image);	
					else
						holder.ivUserGroup.setImageResource(R.drawable.user_icon);
				}
			}else{
				if(CommonValues.getInstance().XmppConnectedGroup!=null){
					Group grp=CommonValues.getInstance().XmppConnectedGroup.get(userGroupUnion.ID);
					holder.tvGroupName.setText(grp.Name);
				}
				holder.ivUserGroup.setImageResource(R.drawable.user_group);
			}
			if (userGroupUnion.LastMessage.length() > 20) {
				String text = userGroupUnion.LastMessage.substring(0, 20);
				text = text.replaceAll("\n", " ");
				holder.tvSubMessage.setText(text + "...");
			} else {
				holder.tvSubMessage.setText(userGroupUnion.LastMessage);
			}			
			holder.tvlastTime.setText(DateUtils.getRelativeTimeSpanString(CommonTask.convertJsonDateToLong(userGroupUnion.PostedDate), new Date().getTime(), 0));
			if (userGroupUnion.Type.equals("G")) {				
				MyNetDatabase db =new MyNetDatabase(context);
				db.open();
				UserGroupUnions users =db.GetGroupMember(userGroupUnion.ID);				
				db.close();
				
				if(users!=null && users.userGroupUnionList!=null){
					int counter=1;
					holder.llUserProfilePicture.setVisibility(View.VISIBLE);
					for (UserGroupUnion userGroupUnion : users.userGroupUnionList) {
						ContactUser user=CommonValues.getInstance().ConatactUserList.get(userGroupUnion.ID);												
						if(counter==1){		
							holder.ivLiveFeedLikeProfileImage1.setVisibility(View.VISIBLE);
							if(user.Image!=null)
								holder.ivLiveFeedLikeProfileImage1.setImageBitmap(user.Image);	
							else
								holder.ivLiveFeedLikeProfileImage1.setImageResource(R.drawable.user_icon);							
						}
						else if(counter==2 ){								
							holder.ivLiveFeedLikeProfileImage2.setVisibility(View.VISIBLE);
							if(user.Image!=null)
								holder.ivLiveFeedLikeProfileImage2.setImageBitmap(user.Image);	
							else
								holder.ivLiveFeedLikeProfileImage2.setImageResource(R.drawable.user_icon);
						}else if(counter==3 ){								
							holder.ivLiveFeedLikeProfileImage3.setVisibility(View.VISIBLE);
							if(user.Image!=null)
								holder.ivLiveFeedLikeProfileImage3.setImageBitmap(user.Image);	
							else
								holder.ivLiveFeedLikeProfileImage3.setImageResource(R.drawable.user_icon);
						}else if(counter==4){								
							holder.ivLiveFeedLikeProfileImage4.setVisibility(View.VISIBLE);
							if(user.Image!=null)
								holder.ivLiveFeedLikeProfileImage4.setImageBitmap(user.Image);	
							else
								holder.ivLiveFeedLikeProfileImage4.setImageResource(R.drawable.user_icon);
							break;
						}
						counter++;
					}
						
				}
			} 
			userItemView.setTag(userGroupUnion);
		} catch (Exception ex) {

		}
		return userItemView;

	}

	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		userGroupUnionList.clear();
		if (charText.length() == 0) {
			userGroupUnionList.addAll(arraylist);
		} else {
			for (UserGroupUnion wp : arraylist) {
				if (wp.Name != null) {
					if (wp.Name.toLowerCase(Locale.getDefault()).contains(
							charText)) {
						userGroupUnionList.add(wp);
					}
				}
			}
		}
		notifyDataSetChanged();
	}
}
