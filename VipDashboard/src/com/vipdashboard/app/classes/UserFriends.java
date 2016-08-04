package com.vipdashboard.app.classes;

import java.util.ArrayList;

import com.vipdashboard.app.adapter.CareFriendsContactList;
import com.vipdashboard.app.adapter.FB_FriendsListAdapter;
import com.vipdashboard.app.adapter.UserFriendListAdapter;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.ContactListFragment;
import com.vipdashboard.app.activities.ContactsListFragment;
import com.vipdashboard.app.activities.FacebookInvitationActivity;
import com.vipdashboard.app.activities.MyProfileActivity;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.FacebookFriends;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.entities.UserFriendListRoot;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonTask;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class UserFriends implements OnClickListener,IAsynchronousTask, OnItemClickListener{
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progress;
	Context context;
	UserFriendListAdapter userFriendListAdapter; 
	ListView lvMyNetFriends;
	public static ListView lvFBFriends;
	TextView tvMyNetFriends,tvFBFriends,tvContacts,tvfbFriendsListNote,tvInviteFriends;
	Boolean bContactList,bCareFriends,bFacebookFriends;
	ArrayList<ContactList> contactListInformarion;
	public static LinearLayout llBottomAddFriend;
	Bitmap bitmap;
	int contactCount=0,cursorCount=-1;
	FrameLayout frameLayout;
	boolean isCallFromSearchItem;
	CareFriendsContactList contactListadapter;
	
	public UserFriends(Context _context, TextView _tvMyNetFriends, TextView _tvFBFriends, TextView _tvContacts, 
			TextView _tvfbFriendsListNote, ListView _lvMyNetFriends, ListView _lvFBFriends, FrameLayout _frameLayout, 
			TextView _tvInviteFriends, LinearLayout _llBottomAddFriend) {
		context = _context;
		tvMyNetFriends = _tvMyNetFriends;
		tvFBFriends = _tvFBFriends;
		tvContacts = _tvContacts;
		tvfbFriendsListNote = _tvfbFriendsListNote;
		lvMyNetFriends = _lvMyNetFriends;
		lvFBFriends = _lvFBFriends;
		frameLayout = _frameLayout;
		tvInviteFriends = _tvInviteFriends;
		llBottomAddFriend = _llBottomAddFriend;
		
		tvFBFriends.setOnClickListener(this);
		tvContacts.setOnClickListener(this);
		tvInviteFriends.setOnClickListener(this);
		//lvContacts.setOnItemClickListener(this);
	}
	
	public void showFriends(){
		ArrangeScrollView(2);
		
	}	

	private void ArrangeScrollView(int selectedScroll) {
		
		// Should change here 
		switch(selectedScroll)
		{
		case 2:
			bContactList=false;
			bCareFriends=false;
			bFacebookFriends=true;
			MyProfileActivity.searchFilterFlag=MyProfileActivity.SEARCH_FILTER_FB_FRIENDS;
			//llBottomAddFriend.setVisibility(View.VISIBLE);
			tvfbFriendsListNote.setVisibility(TextView.VISIBLE);
			lvFBFriends.setVisibility(View.VISIBLE);
			//lvContacts.setVisibility(View.GONE);
			//tvInviteFriends.setVisibility(View.GONE);
			frameLayout.setVisibility(View.GONE);
			tvInviteFriends.setText("Invite Many");
			tvInviteFriends.setVisibility(View.VISIBLE);
			tvFBFriends.setBackground(context.getResources().getDrawable(R.drawable.tab_background_selected));
			tvContacts.setBackgroundResource(Color.TRANSPARENT);
			
			//add fb_friends into list
			
			MyNetDatabase database = new MyNetDatabase(context);
			database.open();
			ArrayList<FacebookFriends> facebookFriendsList = database.getFacebookFriends();
			database.close();
			FB_FriendsListAdapter fb_FriendsListAdapter = new FB_FriendsListAdapter(context, R.layout.fb_friends_list_adapter, 
					facebookFriendsList);
			lvFBFriends.setAdapter(fb_FriendsListAdapter);
			
			if(facebookFriendsList.size()>0){
				llBottomAddFriend.setVisibility(View.VISIBLE);
				tvfbFriendsListNote.setVisibility(TextView.GONE);
			}
			
			
			
			
			/*MyNetDatabase database = new MyNetDatabase(context);
			database.open();
			FacebokPerson facebokPerson = database.getFacebokPerson();
			database.close();
			Log.e("issync", String.valueOf(facebokPerson.isSync));
			Log.e("list",String.valueOf(lvFBFriends.getCount()));
			if(facebokPerson.isSync == 1 && FB_FriendsActivity.fb_FriendsListAdapter == null){
				database.open();
				database.updateFacebookPerson(0);
				database.close();
				lvFBFriends.setAdapter(FB_FriendsActivity.fb_FriendsListAdapter);
				
			}
			database.open();
			facebokPerson = database.getFacebokPerson();
			database.close();
			if(facebokPerson.isSync == 0){
				Intent intent = new Intent(context, FB_FriendsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				context.startActivity(intent);
				
			}else{
				lvFBFriends.setAdapter(FB_FriendsActivity.fb_FriendsListAdapter);
			}*/
			/*if(FB_FriendsActivity.fb_FriendsListAdapter != null){
				llBottomAddFriend.setVisibility(View.VISIBLE);
			}*/
			
			//tvfbFriendsListNote.setVisibility(TextView.GONE);
			break;
		case 3:
			bContactList=true;
			bCareFriends=false;
			bFacebookFriends=false;
			MyProfileActivity.searchFilterFlag=MyProfileActivity.SEARCH_FILTER_CONTACTS;
			llBottomAddFriend.setVisibility(View.GONE);
			tvfbFriendsListNote.setVisibility(TextView.GONE);
			//lvContacts.setVisibility(View.VISIBLE);
			frameLayout.setVisibility(View.VISIBLE);
			lvFBFriends.setVisibility(View.GONE);
			tvInviteFriends.setVisibility(View.GONE);
			tvContacts.setBackground(context.getResources().getDrawable(R.drawable.tab_background_selected));
			tvFBFriends.setBackgroundResource(Color.TRANSPARENT);
			bContactList=true;
			//LoadInformation();
			BindContactList();
			break;
		}
		
	}
	
	private void BindContactList(){	
		if(!isCallFromSearchItem){
			isCallFromSearchItem = true;
			FragmentManager fragmentManager = ((MyProfileActivity) context).getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			ContactListFragment fragment = new ContactListFragment();
			fragmentTransaction.add(R.id.fragment_container, fragment);
			fragmentTransaction.commit();
		}
	}

	private void LoadInformation() {
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tvFBFriends) {
			ArrangeScrollView(2);
		}		
		else if (v.getId() == R.id.tvContacts) {
			ArrangeScrollView(3);
		}else if(v.getId() == R.id.tvInviteFriends){
			if(bCareFriends){
				//AddFriendAndFamily addFriendAndFamily = new AddFriendAndFamily(context);
				//addFriendAndFamily.getUserList();
			}else if(bFacebookFriends){
				Intent intent = new Intent(context, FacebookInvitationActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				context.startActivity(intent);
			}			
		}
	}

	@Override
	public void showProgressLoader() {
		progress = new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);
		progress.setCancelable(false);
		progress.setMessage("Processing...");
		progress.show();
	}

	@Override
	public void hideProgressLoader() {
		progress.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		//MyNetDatabase database = new MyNetDatabase(context);
		IUserManager userManager = new UserManager();
		
		if(bContactList){
			contactListInformarion = new ArrayList<ContactList>();
			ContentResolver cr = context.getContentResolver();
			 String[] projection = new String[] {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
		                ContactsContract.CommonDataKinds.Phone.NUMBER,
		                ContactsContract.CommonDataKinds.Phone.PHOTO_ID};
			 
			Cursor cursor = cr.query(
					android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					projection, 
					null, 
					null, 
					android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
			
			cursorCount= cursor.getCount();
			if(contactCount!=cursorCount)
			{
				if(cursor.getCount() > 0){
					cursor.moveToFirst();
					do{
						//String id=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
						String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
						String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						bitmap=CommonTask.fetchContactImageThumbnail(context,cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID)));
						contactListInformarion.add(new ContactList(number, name,bitmap));
					}while(cursor.moveToNext());
				}
				cursor.close();
			}
			return contactListInformarion;
		}
		else if(bCareFriends){
			//return userManager.getUserMyNetFriends();
		}
		
		return null;
	}
	

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){			
			if(bContactList){
				if(contactCount!=cursorCount)
				{
					contactListadapter = new CareFriendsContactList(context, R.layout.care_friends_contact, contactListInformarion);
					//lvContacts.setAdapter(contactListadapter);
					contactCount=contactListadapter.getCount();
				}
			}
			else if(bCareFriends){
				UserFriendListRoot userFriendListRoot=(UserFriendListRoot)data;
				userFriendListAdapter = new UserFriendListAdapter(context, R.layout.user_friend_list,new ArrayList<User>(userFriendListRoot.userList));
				lvMyNetFriends.setAdapter(userFriendListAdapter);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		if(bCareFriends){
		/*	Intent intent = new Intent(context, IndividualProfileViewActivity.class);
			intent.putExtra("UserNumber", userFriendListAdapter.getItem(arg2).UserNumber);
			intent.putExtra("UserName", userFriendListAdapter.getItem(arg2).LastName);
			intent.putExtra("PhoneNumber", userFriendListAdapter.getItem(arg2).Mobile);
			intent.putExtra("UserID", userFriendListAdapter.getItem(arg2).UserID);*/
			//intent.putExtra("Name", userFriendListAdapter.getItem(arg2).Name);
			
			
			//intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			//context.startActivity(intent);
		}
	}
}
