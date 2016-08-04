package com.vipdashboard.app.adapter;

import com.vipdashboard.app.activities.GetStartedFiveActivity;
import com.vipdashboard.app.activities.GetStartedFourActivity;
import com.vipdashboard.app.activities.GetStartedOneActivity;
import com.vipdashboard.app.activities.GetStartedThreeActivity;
import com.vipdashboard.app.activities.GetStartedTwoActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class GetStartedAdapter extends FragmentPagerAdapter{
	
	boolean IsNeedtoSkipLogin=false;

	public GetStartedAdapter(FragmentManager fm,boolean ISNEEDTOSKIPLOGIN) {
		super(fm);
		IsNeedtoSkipLogin=ISNEEDTOSKIPLOGIN;
	}

	@Override
	public Fragment getItem(int position) {
		if(position == 0){
			GetStartedOneActivity.IsNeedtoSkipLogin=IsNeedtoSkipLogin;
			return new GetStartedOneActivity();
		}else if(position == 1){
			GetStartedTwoActivity.IsNeedtoSkipLogin=IsNeedtoSkipLogin;
			return new GetStartedTwoActivity();
		}else if(position == 2){
			GetStartedThreeActivity.IsNeedtoSkipLogin=IsNeedtoSkipLogin;
			return new GetStartedThreeActivity();
		}else {
			GetStartedFourActivity.IsNeedtoSkipLogin=IsNeedtoSkipLogin;
			return new GetStartedFourActivity();
		}
	}
	
//	else{
//		GetStartedFiveActivity.IsNeedtoSkipLogin=IsNeedtoSkipLogin;
//		return new GetStartedFiveActivity();
//	}

	@Override
	public int getCount() {
		return 4;
	}

}
