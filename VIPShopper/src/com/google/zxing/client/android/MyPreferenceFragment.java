/**
 * 
 */
package com.google.zxing.client.android;

import com.shopper.app.R;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * @author tac
 *
 */
public class MyPreferenceFragment extends PreferenceFragment {
	
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            getPreferenceScreen();
        }
    
}
