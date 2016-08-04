package com.vipdashboard.app.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AlarmPagerAdapter extends FragmentPagerAdapter{
	private Context _context;

	public AlarmPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		_context = context;

	}
	@Override
	public Fragment getItem(int position) {
		Fragment f = new Fragment();
		/*if (position == 0) {
			f = AlarmMainFragment.newInstance(_context);
		}else if (position == 1) {
			f = CriticalAlarmsFragment.newInstance(_context);
		}else if (position == 2) {
			f = TTRequestDetailsFragment.newInstance(_context);
		}*/
		return f;
	}

	@Override
	public int getCount() {
		return 1;
	}
}
