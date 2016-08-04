package com.vipdashboard.app.adapter;

import com.vipdashboard.app.fragments.ChatStatusFragment;
import com.vipdashboard.app.fragments.ChatToContactFragment;
import com.vipdashboard.app.fragments.ChatToFavouriesFragment;
import com.vipdashboard.app.fragments.ChatToRecentsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CareIMAdapter extends FragmentPagerAdapter{

	public CareIMAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		
		if(position == 0){
			return new ChatToFavouriesFragment();
		}else if(position == 1){
			return new ChatToRecentsFragment();
		}else if(position == 2){
			return new ChatToContactFragment();
		}else{
			return new ChatStatusFragment();
		}		
	}

	@Override
	public int getCount() {
		return 4;
	}
}
