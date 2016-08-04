package com.vipdashboard.app.base;

import com.vipdashboard.app.activities.BatteryAlertNotificationActivity;
import com.vipdashboard.app.activities.VIPDBatteryDoctorSmartSavingActivity;
import com.vipdashboard.app.utils.CommonConstraints;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;

public class BatteryInfomrationReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// context.unregisterReceiver(this);
		int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		int level = -1;
		if (currentLevel >= 0 && scale > 0) {
			level = (currentLevel * 100) / scale;
		}
		
		if (!"10%".equals(level + "%")&& CommonConstraints.IS_DISABLE_WIFI == true) {
			CommonConstraints.IS_DISABLE_WIFI = false;
		}
		
		if (CommonConstraints.IS_SCREEN_OFF_SAVING==true && CommonConstraints.IS_ALREADY_CALLED_SCREEN_OFF_SAVING==false)
		{
			CommonConstraints.IS_ALREADY_CALLED_SCREEN_OFF_SAVING=true;
		}
		
		if(CommonConstraints.IS_DISABLE_WIFI==true && CommonConstraints.IS_ALREADY_CALLED_DISABLE_WIFI == false)
		{
			CommonConstraints.IS_DISABLE_WIFI = true;
			
			//WifiManager wifiManager= (WifiManager) context.getSystemService(
			//		Context.WIFI_SERVICE);;
			//wifiManager.setWifiEnabled(false);
		}

		if (CommonConstraints.IS_BD_LOW_POWER_NOTIFIATION) {
			if (!CommonConstraints.BD_LOW_POWER_NOTIFIATION_PERCENTAGE
					.equals(level + "%")
					&& CommonConstraints.IS_ALREADY_CALLED_POPUP == true) {
				CommonConstraints.IS_ALREADY_CALLED_POPUP = false;
			}
			
			
				

			else if (CommonConstraints.BD_LOW_POWER_NOTIFIATION_PERCENTAGE
					.equals(level + "%")
					&& CommonConstraints.IS_ALREADY_CALLED_POPUP == false) {
				try {
					CommonConstraints.IS_ALREADY_CALLED_POPUP = true;
					Intent i = new Intent(context,
							BatteryAlertNotificationActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(i);
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
			
		
		}
		// batteryPercent.setText("Battery Level Remaining: " + level + "%");
	}
}

// [gn_version, formatted_number, numberlabel, matched_number, number, type,
// date, lookup_uri, geocoded_location, countryiso, data_id, numbertype, new,
// duration, _id, simid, name, voicemail_uri, normalized_number, is_read,
// photo_id, raw_contact_id, vtcall]
