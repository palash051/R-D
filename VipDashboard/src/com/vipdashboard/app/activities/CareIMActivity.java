package com.vipdashboard.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.CareIMAdapter;
import com.vipdashboard.app.asynchronoustask.UserGroupSyncAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.fragments.ChatToContactFragment;

public class CareIMActivity extends MainActionbarBase implements OnClickListener{
	CareIMAdapter adapter;
	ViewPager viewPager;
	RelativeLayout rlChatToFavourites,rlChatToRecents,rlChatToContacts,rlChatUserStatus;
	ImageView ivChatToFavourites,ivChatToRecents,ivChatToContacts,ivChatUserStatus;
	TextView tvManualSync,tvIMTitle;
	ImageButton ibNewContact;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.care_im_main);
		Initalization();
	}
	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		super.onResume();
		viewPager.setCurrentItem(1);
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}
	
	/*@Override
	public void onBackPressed() {		
		if(viewPager.getCurrentItem()==0)			
			super.onBackPressed();
		else if(viewPager.getCurrentItem()==1)
			viewPager.setCurrentItem(0);
		else if(viewPager.getCurrentItem()==2)
			viewPager.setCurrentItem(1);
		else if(viewPager.getCurrentItem()==3)
			viewPager.setCurrentItem(2);
	}*/
	private void Initalization() {
		rlChatToFavourites=(RelativeLayout)findViewById(R.id.rlChatToFavourites);
		rlChatToRecents=(RelativeLayout)findViewById(R.id.rlChatToRecents);
		rlChatToContacts=(RelativeLayout)findViewById(R.id.rlChatToContacts);
		rlChatUserStatus=(RelativeLayout)findViewById(R.id.rlChatUserStatus);
		
		ivChatToFavourites=(ImageView)findViewById(R.id.ivChatToFavourites);
		ivChatToRecents=(ImageView)findViewById(R.id.ivChatToRecents);
		ivChatToContacts=(ImageView)findViewById(R.id.ivChatToContacts);
		ivChatUserStatus=(ImageView)findViewById(R.id.ivChatUserStatus);
		
		ibNewContact=(ImageButton)findViewById(R.id.ibNewContact);
		
		tvManualSync=(TextView)findViewById(R.id.tvManualSync);
		tvIMTitle=(TextView)findViewById(R.id.tvIMTitle);
		
		rlChatToFavourites.setOnClickListener(this);
		rlChatToRecents.setOnClickListener(this);
		rlChatToContacts.setOnClickListener(this);
		rlChatUserStatus.setOnClickListener(this);
		
		tvManualSync.setOnClickListener(this);
		
		ibNewContact.setOnClickListener(this);
		
		
		viewPager = (ViewPager) findViewById(R.id.vpCareIm);
		adapter = new CareIMAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {	
				if(position==0){
					initializeFavouritesTab();
				}else if(position==1){
					initializeRecentTab();
				}else if(position==2){
					initializeContactTab();
				}else if(position==3){
					initializeStatusTab();
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}
	@Override
	public void onClick(View v) {	
		int vId=v.getId();
		if(vId==R.id.rlChatToFavourites){
			viewPager.setCurrentItem(0);
		}else if(vId==R.id.rlChatToRecents){
			viewPager.setCurrentItem(1);
		}else if(vId==R.id.rlChatToContacts){
			viewPager.setCurrentItem(2);
		}else if(vId==R.id.rlChatUserStatus){
			viewPager.setCurrentItem(3);
		}else if(vId==R.id.tvManualSync){
			if(viewPager.getCurrentItem() == 2){
				new UserGroupSyncAsyncTask(this).execute();
				 Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.vpCareIm + ":" + viewPager.getCurrentItem());
				 if (page != null) {
			          ((ChatToContactFragment)page).LoadUserList();     
			     } 
			}else{
				Intent intent = new Intent(this,AddGroupActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		}else if(vId==R.id.ibNewContact){
			Intent intent = new Intent(this,CollaborationNewContactActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}
	private void initializeFavouritesTab() {
		ivChatToFavourites.setSelected(true);
		ivChatToRecents.setSelected(false);
		ivChatToContacts.setSelected(false);
		ivChatUserStatus.setSelected(false);
		ibNewContact.setVisibility(View.GONE);
		tvManualSync.setVisibility(View.GONE);
		tvIMTitle.setText("Chat to Favourites");
	}
	private void initializeRecentTab() {
		ivChatToFavourites.setSelected(false);
		ivChatToRecents.setSelected(true);
		ivChatToContacts.setSelected(false);
		ivChatUserStatus.setSelected(false);
		ibNewContact.setVisibility(View.VISIBLE);
		tvManualSync.setVisibility(View.VISIBLE);
		tvManualSync.setText("New Group");
		tvIMTitle.setText("Chat to Recent");
	}
	private void initializeContactTab() {
		ivChatToFavourites.setSelected(false);
		ivChatToRecents.setSelected(false);
		ivChatToContacts.setSelected(true);
		ivChatUserStatus.setSelected(false);
		ibNewContact.setVisibility(View.GONE);
		tvManualSync.setVisibility(View.VISIBLE);
		tvManualSync.setText("Sync");
		tvIMTitle.setText("All Contacts");
	}
	private void initializeStatusTab() {
		ivChatToFavourites.setSelected(false);
		ivChatToRecents.setSelected(false);
		ivChatToContacts.setSelected(false);
		ivChatUserStatus.setSelected(true);
		ibNewContact.setVisibility(View.GONE);
		tvManualSync.setVisibility(View.GONE);
		tvIMTitle.setText("User Status");
	}
}
