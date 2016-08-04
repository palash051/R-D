package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.vipdashboard.app.activities.CollaborationDiscussionListActivity;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.ImageLoader;
import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.ContactUser;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.entities.UserRelationshipRoot;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonValues;

public class UserGroupListAdapter extends BaseAdapter implements IAsynchronousTask{
	Context context;
	UserGroupUnion userGroupUnion;
	ArrayList<UserGroupUnion> userGroupUnionList = null;
	ArrayList<UserGroupUnion> arraylist;
	
	LinearLayout llGroupMemberImage;
	ImageView ivLiveFeedLikeProfileImage1,ivLiveFeedLikeProfileImage2,ivLiveFeedLikeProfileImage3,ivLiveFeedLikeProfileImage4;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	
	ImageView ivUserGroup;
	int selectedFavId;
	boolean isCallFromFavourit;
	String friendId;

	public UserGroupListAdapter(Context _context, int resource,
			ArrayList<UserGroupUnion> _userGroupUnionList) {		
		context = _context;
		userGroupUnionList = _userGroupUnionList;
		arraylist = new ArrayList<UserGroupUnion>();
		arraylist.addAll(userGroupUnionList);		
	}
	
	@Override
	public int getCount() {
		return userGroupUnionList.size();
	}
	
	public void setList(ArrayList<UserGroupUnion> _userGroupUnionList){
		
		userGroupUnionList=_userGroupUnionList;
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		userGroupUnion = userGroupUnionList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View userItemView = inflater.inflate(R.layout.group_item_layout, null);	
		
		TextView tvGroupName = (TextView) userItemView
				.findViewById(R.id.tvGroupName);
		
		ImageView ivUserFavourites=(ImageView) userItemView.findViewById(R.id.ivUserFavourites);
		
		ivUserFavourites.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				userGroupUnion = (UserGroupUnion) v.getTag();
				if(userGroupUnion.isFavourite){
					isCallFromFavourit = false;
					friendId = String.valueOf(userGroupUnion.ID);
					LoadInformation();
				}else{
					isCallFromFavourit = true;
					friendId = String.valueOf(userGroupUnion.ID);
					LoadInformation();
				}
			}
		});
		
		ivUserFavourites.setVisibility(View.GONE);
		llGroupMemberImage = (LinearLayout) userItemView.findViewById(R.id.llGroupMemberImage);
		ivLiveFeedLikeProfileImage1 = (ImageView) userItemView.findViewById(R.id.ivLiveFeedLikeProfileImage1);
		ivLiveFeedLikeProfileImage2 = (ImageView) userItemView.findViewById(R.id.ivLiveFeedLikeProfileImage2);
		ivLiveFeedLikeProfileImage3 = (ImageView) userItemView.findViewById(R.id.ivLiveFeedLikeProfileImage3);
		ivLiveFeedLikeProfileImage4 = (ImageView) userItemView.findViewById(R.id.ivLiveFeedLikeProfileImage4);

		ivUserGroup = (ImageView) userItemView
				.findViewById(R.id.ivUserGroup);
		ImageView ivUserGroupStatus = (ImageView) userItemView
				.findViewById(R.id.ivUserGroupstatus);
		try{
			ivUserFavourites.setVisibility(View.VISIBLE);
			ivUserFavourites.setImageDrawable(context.getResources().getDrawable(R.drawable.user_favorite_normal));			
			if(userGroupUnion.Type.equals("U")){
				ContactUser user=CommonValues.getInstance().ConatactUserList.get(userGroupUnion.ID);							
				if(user!=null){
					tvGroupName.setText(user.Name);
					if(user.Image!=null)
						ivUserGroup.setImageBitmap(user.Image);	
					else
						ivUserGroup.setImageResource(R.drawable.user_icon);
				}			
			}else{			
				tvGroupName.setText(userGroupUnion.Name);
				ivUserGroup.setImageResource(R.drawable.user_group);
				ivUserFavourites.setVisibility(View.GONE);
				ivUserGroupStatus.setVisibility(View.GONE);
			}
			
			if(userGroupUnion.userOnlinestatus == CommonConstraints.OFFLINE){
				ivUserGroupStatus.setImageResource(R.drawable.user_status_offline);
			}else if(userGroupUnion.userOnlinestatus == CommonConstraints.AWAY){
				ivUserGroupStatus.setImageResource(R.drawable.user_status_away);
			}else if(userGroupUnion.userOnlinestatus == CommonConstraints.ONLINE){
				ivUserGroupStatus.setImageResource(R.drawable.user_status_online);
			}else if(userGroupUnion.userOnlinestatus == CommonConstraints.DO_NOT_DISTURB){
				ivUserGroupStatus.setImageResource(R.drawable.user_status_busy);
			}else if(userGroupUnion.userOnlinestatus == CommonConstraints.BUSY){
				ivUserGroupStatus.setImageResource(R.drawable.user_status_busy);
			}else{
				ivUserGroupStatus.setImageResource(R.drawable.user_status_offline);
			}
			
			if(userGroupUnion.isFavourite){
				ivUserFavourites.setImageDrawable(context.getResources().getDrawable(R.drawable.user_favorite_selectedmode));
			}
			
			ivUserFavourites.setTag(userGroupUnion);
			
			
			
			userItemView.setTag(userGroupUnion);
		}catch(Exception ex){
			
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
				if(wp.Name != null){
					if (wp.Name.toLowerCase(Locale.getDefault()).contains(charText)) {
						userGroupUnionList.add(wp);
					}
				}
			}
		}
		notifyDataSetChanged();
	}
	
	private void LoadInformation(){
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progressDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Processing");
		progressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		if(isCallFromFavourit){
			MyNetDatabase db=new MyNetDatabase(context);
			db.open();
			db.updateFavourite(Integer.valueOf(friendId), true);
			db.close();
			
			return userManager.SetMyFavouriteUser(friendId);
		}else{	
			MyNetDatabase db=new MyNetDatabase(context);
			db.open();
			db.updateFavourite(Integer.valueOf(friendId), false);
			db.close();
			return userManager.RemoveMyFavouriteUser(friendId);
		}		
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){			
			userGroupUnion.isFavourite=isCallFromFavourit;
			notifyDataSetChanged();
		}
	}
}
