package com.vipdashboard.app.adapter;

import com.vipdashboard.app.activities.CallLogFragment;
import com.vipdashboard.app.activities.ContactsListFragment;
import com.vipdashboard.app.activities.SMSFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class UtilityAdapter extends FragmentPagerAdapter{

	public UtilityAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if(position == 0){
			return new CallLogFragment();
		}else if(position == 1){
			return new SMSFragment();
		}else{
			return new ContactsListFragment();
		}
	}

	@Override
	public int getCount() {
		return 3;
	}

}
