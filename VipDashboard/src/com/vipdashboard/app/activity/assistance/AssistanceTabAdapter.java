package com.vipdashboard.app.activity.assistance;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AssistanceTabAdapter extends FragmentPagerAdapter{

	public AssistanceTabAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		switch (index) {
        case 0:
        	return new AssistanceNetworkDoctor();
        case 1:
            // Games fragment activity
            return new AssistanceAskaFriend();
        case 2:
            // Movies fragment activity
            return new AssistanceContactUs();
        case 3:
        	return new AssistanceLiveSupport();
        }
		
 
        return null;
	}

	@Override
	public int getCount() {
		return 4;
	}

}
