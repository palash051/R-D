package com.vipdashboard.app.activities;

import java.util.List;
import java.util.Vector;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.PagerAdapterAppManager;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.fragments.CacheFragment;
import com.vipdashboard.app.fragments.MemoryBootsFragment;
import com.vipdashboard.app.fragments.ResidualFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

public class History extends MainActionbarBase {

	private ViewPager vPage;
	// private APKManagerActivity u = null;
	public PagerAdapterAppManager mPagerAdapter;
	// private BroadcastReceiver loadFinish = new LoadFinishReceiver();
	public Boolean isStop = false;
	private Button btnBack;
	public Button btn_one_click_accelerate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		btnBack = (Button) findViewById(R.id.btn_back_main);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		initialisePaging();
		// if (savedInstanceState != null) {
		// mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
		// }
	}

	private void initialisePaging() {
		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, CacheFragment.class.getName()));
		fragments.add(Fragment.instantiate(this, ResidualFragment.class.getName()));
		/*fragments.add(Fragment.instantiate(this, MemoryBootsFragment.class.getName()));*/
		this.mPagerAdapter = new PagerAdapterAppManager(super.getSupportFragmentManager(),
				fragments);
		vPage = (ViewPager) super.findViewById(R.id.vPager);
		vPage.setAdapter(this.mPagerAdapter);
		vPage.setCurrentItem(0);
		vPage.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				// this will be called when the page is changed
				UpdateLayout(position);
				if (position == 0) {
					mPagerAdapter.fragments.get(0).onHiddenChanged(true);
					mPagerAdapter.fragments.get(1).onHiddenChanged(false);
					/*mPagerAdapter.fragments.get(2).onHiddenChanged(false);*/
				} else if(position == 1) {
					mPagerAdapter.fragments.get(0).onHiddenChanged(false);
					mPagerAdapter.fragments.get(1).onHiddenChanged(true);
				/*	mPagerAdapter.fragments.get(2).onHiddenChanged(false);*/
				}
				/*else if(position == 2) {
					mPagerAdapter.fragments.get(0).onHiddenChanged(false);
					mPagerAdapter.fragments.get(1).onHiddenChanged(false);
					mPagerAdapter.fragments.get(2).onHiddenChanged(true);
				}*/
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private void UpdateLayout(int tab) {
		ImageView cursor = (ImageView) findViewById(R.id.cursor);
		TextView uns = (TextView) findViewById(R.id.cache_tab);
		ImageView imgU = (ImageView) findViewById(R.id.image1);
		TextView apk = (TextView) findViewById(R.id.residual_tab);
		ImageView imgA = (ImageView) findViewById(R.id.image2);
		if (tab == 0) {
			cursor.setScaleType(ScaleType.FIT_START);
			imgU.setImageResource(R.drawable.uninstalleron);
			uns.setTextColor(getResources().getColor(R.color.cache_title_select_color));
			imgA.setImageResource(R.drawable.apksoff);
			apk.setTextColor(getResources().getColor(R.color.cache_title_noselect_color));
		} else if(tab ==1){
			cursor.setScaleType(ScaleType.FIT_END);
			imgU.setImageResource(R.drawable.uninstalleroff);
			uns.setTextColor(getResources().getColor(R.color.cache_title_noselect_color));
			imgA.setImageResource(R.drawable.apkson);
			apk.setTextColor(getResources().getColor(R.color.cache_title_select_color));
		}
		/*else if(tab ==2){
			cursor.setScaleType(ScaleType.FIT_END);
			imgU.setImageResource(R.drawable.uninstalleroff);
			uns.setTextColor(getResources().getColor(R.color.cache_title_noselect_color));
			imgA.setImageResource(R.drawable.apkson);
			apk.setTextColor(getResources().getColor(R.color.cache_title_select_color));
		}*/
	}

	public void Tab1Click(View v) {
		vPage.setCurrentItem(0);
	}

	public void Tab2Click(View v) {
		vPage.setCurrentItem(1);
	}
	
	/*public void Tab3Click(View v) {
		vPage.setCurrentItem(2);
	}*/

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_history, menu);
		return true;
	}*/

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}
}