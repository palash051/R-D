package com.vipdashboard.app.adapter;

import com.vipdashboard.app.activities.BatterydoctorTopheaderFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class BatterydoctorAdapter extends FragmentPagerAdapter{

	public BatterydoctorAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
			return new BatterydoctorTopheaderFragment();
	}

	@Override
	public int getCount() {
		return 1;
	}

}
