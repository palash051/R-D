package com.vipdashboard.app.activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import com.actionbarsherlock.app.SherlockPreferenceActivity;


import com.vipdashboard.app.R;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;

public class SettingsActivity extends SherlockPreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction()
		.replace(android.R.id.content, new SettingPreferenceFragment())
		.commit();
	}

	public static class SettingPreferenceFragment extends PreferenceFragment
			implements OnSharedPreferenceChangeListener {
		
		public EditTextPreference baseurl_preference;

		@Override
		public void onCreate(Bundle savedInstanceState) {

			super.onCreate(savedInstanceState);
			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences);
			// new code block for preference change listener
			initializePreferenceControls();
			getPreferenceManager().getSharedPreferences()
					.registerOnSharedPreferenceChangeListener(this);
			//changePreferenceDependency();

		}

		public void initializePreferenceControls() {
			baseurl_preference = (EditTextPreference) getPreferenceManager()
					.findPreference("pref_server_url");			
		}

		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			// Let's do something when a preference value changes
			if (key.equals("pref_server_url")) {
				CommonURL.getInstance().assignValues(CommonTask.getPreferences(this.getActivity(),key));
			}
		}


		/*public void changePreferenceDependency(String key) {
			String serverUrl = CommonTask.getBaseUrl(this.getActivity(),key);
			if (!serverUrl.equals("")) {
				setEnablePreferences(true);
			} else { 
				setEnablePreferences(false);
			}
		}	*/	

		/*@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			return null;//inflater.inflate(R.layout.preference_list, container, false);
		}*/

		@Override
		public void onResume() {
			super.onResume();
			getPreferenceScreen().getSharedPreferences()
					.registerOnSharedPreferenceChangeListener(this);

		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			getPreferenceManager().getSharedPreferences()
					.unregisterOnSharedPreferenceChangeListener(this);
		}
	}

}
