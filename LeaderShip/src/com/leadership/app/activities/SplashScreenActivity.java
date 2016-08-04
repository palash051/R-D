package com.leadership.app.activities;

import com.leadership.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashScreenActivity extends Activity{
	protected boolean _active = true;
	protected int _splashTime = 24000;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		// thread for displaying the SplashScreen
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while (_active && (waited < _splashTime)) {
						sleep(10);
						if (_active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
					
				} finally {	
					
					Intent intent = new Intent(SplashScreenActivity.this,LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
					SplashScreenActivity.this.finish();					
				}
			}
		};
		splashTread.start();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}
		return true;
	}
}
