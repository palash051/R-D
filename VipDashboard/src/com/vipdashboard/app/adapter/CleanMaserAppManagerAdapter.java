package com.vipdashboard.app.adapter;

import com.vipdashboard.app.fragments.AppManagerFragmnet;
import com.vipdashboard.app.fragments.PickManagerFragment;
//import com.vipdashboard.app.fragments.VIPD_Apk_Files_Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class CleanMaserAppManagerAdapter extends FragmentPagerAdapter{

	public CleanMaserAppManagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if(position == 0){
		return new AppManagerFragmnet();
		}else if(position == 1){
				return new PickManagerFragment();
		}
//	else if(position == 2){
//		return new VIPD_Apk_Files_Fragment();
//}
		return null;
		
	}
	
	  
//	    public AppManagerFragmnet getItem(int index) {
//	 
//	        switch (index) {
//	        case 0:
//	            // Top Rated fragment activity
//	            return new AppManagerFragmnet();
//	        case 1:
//	            // Games fragment activity
//	            return new PickManagerFragment();
////	        case 2:
////	            // Movies fragment activity
////	            return new MoviesFragment();
//	        }
//	 
//	        return null;
//	    }

	@Override
	public int getCount() {
		return 2;
	}

}
