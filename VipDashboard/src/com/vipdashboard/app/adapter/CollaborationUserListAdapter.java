package com.vipdashboard.app.adapter;

import java.util.ArrayList;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.ImageLoader;

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

public class CollaborationUserListAdapter extends BaseAdapter implements IAsynchronousTask{
	
	Context context;
	UserGroupUnion userGroupUnion;
	ArrayList<UserGroupUnion> userGroupUnionList = null;
	private ImageLoader imageLoader;
	ImageOptions imgOptions;	
	LinearLayout llGroupMemberImage;
	ImageView ivLiveFeedLikeProfileImage1,ivLiveFeedLikeProfileImage2,ivLiveFeedLikeProfileImage3,ivLiveFeedLikeProfileImage4;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	private AQuery aq;
	ImageView ivUserGroup;
	int selectedFavId;
	boolean isCallFromFavourit;
	String friendId;
	
	public CollaborationUserListAdapter(Context _context, int resource,
			ArrayList<UserGroupUnion> _userGroupUnionList){
		context = _context;
		userGroupUnionList = _userGroupUnionList;
		imageLoader = new ImageLoader(context.getApplicationContext());		
		imgOptions = CommonValues.getInstance().defaultImageOptions; 		
		imgOptions.targetWidth=100;
		imgOptions.ratio=0;//AQuery.RATIO_PRESERVE;
		imgOptions.round = 8;
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

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		userGroupUnion = userGroupUnionList.get(position);	
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View userItemView = inflater.inflate(R.layout.group_item_layout, null);
		if(userGroupUnion.Type.equals("U")){			 
			aq = new AQuery(userItemView);
			AQuery  aq = new AQuery(userItemView);
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
			ivUserGroup = (ImageView) userItemView.findViewById(R.id.ivUserGroup);
			ImageView ivUserGroupStatus = (ImageView) userItemView.findViewById(R.id.ivUserGroupstatus);
			
			ivUserFavourites.setVisibility(View.VISIBLE);
			ivUserFavourites.setImageDrawable(context.getResources().getDrawable(R.drawable.user_favorite_normal));
			
			
			ContentResolver cr = context.getContentResolver();
			Cursor cursor = cr.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, " replace(data1,' ', '') like '%" + userGroupUnion.Name.substring(3) +"%'", null, null);
			
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				tvGroupName.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
				Bitmap bit=CommonTask.fetchContactImageThumbnail(context,cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID)));
				if(bit!=null)
					ivUserGroup.setImageBitmap(bit);
				else{
					ivUserGroup.setImageResource(R.drawable.user_icon);
				}
			}else{
				tvGroupName.setText("Unknown("+userGroupUnion.Name+")");
				ivUserGroup.setImageResource(R.drawable.user_icon);
				
			}
			cursor.close();
			
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
		}
		return userItemView;
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
