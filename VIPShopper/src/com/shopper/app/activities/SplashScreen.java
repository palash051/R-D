package com.shopper.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import com.shopper.app.R;
import com.shopper.app.utils.CommonValues;

/**
 * Application Launcher class 
 * This class activity perform a splash screen with a time interval
 * After showing this class redirect to home screen as application default 
 * @author Shafiqul Alam
 * 
 */
public class SplashScreen extends Activity {

	// how long until we go to the next activity
	protected boolean _active = true;
	protected int _splashTime = 5000; // time to display the splash screen in ms

	/** Called when the activity is first created. */
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
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					SplashScreen.this.finish();
					Intent intent = new Intent(
							"com.shopper.app.activities.HOME");
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					CommonValues.getInstance().homeIntent=intent;
					startActivity(intent);
					// stop();

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