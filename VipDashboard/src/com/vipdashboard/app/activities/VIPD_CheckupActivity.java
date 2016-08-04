package com.vipdashboard.app.activities;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.triggertrap.seekarc.SeekArc;
import com.triggertrap.seekarc.SeekArc.ProgressAnimationListener;
import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class VIPD_CheckupActivity extends MainActionbarBase implements OnClickListener {
	
	ProgressBar pbBackProcess;
	LinearLayout llOptimize, llCancleOptimize;
	ImageView ivError, ivActive,ivSafeBrowingError,
	ivSafeBrowingActive,
	ivRealTimeProtectionError,
	ivRealTimeProtectionActive;
	TextView ivCashTextView,ivMemoryTextView,ivWifiTextView,ivRealTimeProtectionTextView,ivSafeBrowingTextView;
	long totalCacheSize = 0, totalAppsRunning = 0;
	private static final long CACHE_APP = Long.MAX_VALUE;
	
	public int totalRunningApps = 0,runningApps=0;
	long useram;
	boolean IsRealTimeProtectionEnable=false,IsSafeBrowsingEnable=false;
	SeekArc seekArcStroages, seekArcRam;
	RelativeLayout rlPointsCheck;
	
	WifiConfiguration wc=new WifiConfiguration();
	WifiManager wifi;
	
	long totalLeaving,minimumleaving=0;
	/*ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);*/
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vipd_checkup);
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
			}
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		Initialization();
	}
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		super.onResume();
		if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				if (!isFinishing()) 
				{
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
			}
		Calculation();
		WiFiSecurity();
		isRooted();
		
		getTotalRAMUse();
		
		totalRunningApps=runningApps=0;
	}
	public void Initialization(){
		pbBackProcess = (ProgressBar) findViewById(R.id.pbBackProcess);
		llOptimize = (LinearLayout) findViewById(R.id.llOptimize);
		llCancleOptimize = (LinearLayout) findViewById(R.id.llCancleOptimize);
		ivError = (ImageView) findViewById(R.id.ivError);
		ivActive = (ImageView) findViewById(R.id.ivActive);
		ivCashTextView = (TextView) findViewById(R.id.ivCashTextView);
		ivMemoryTextView = (TextView) findViewById(R.id.ivMemoryTextView);
		ivWifiTextView=(TextView) findViewById(R.id.ivWifiTextView);
		rlPointsCheck=(RelativeLayout) findViewById(R.id.rlPointsCheck);
		ivSafeBrowingError = (ImageView) findViewById(R.id.ivSafeBrowingError);
		ivSafeBrowingActive = (ImageView) findViewById(R.id.ivSafeBrowingActive);
		ivRealTimeProtectionError = (ImageView) findViewById(R.id.ivRealTimeProtectionError);
		ivRealTimeProtectionActive = (ImageView) findViewById(R.id.ivRealTimeProtectionActive);
		ivSafeBrowingTextView=(TextView) findViewById(R.id.ivSafeBrowingTextView);
		ivRealTimeProtectionTextView=(TextView) findViewById(R.id.ivRealTimeProtectionTextView);
		
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		rlPointsCheck.setOnClickListener(this);
		llOptimize.setOnClickListener(this);
		llCancleOptimize.setOnClickListener(this);
	}
	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.llOptimize||view.getId() == R.id.rlPointsCheck){
		/*	
			if(CommonConstraints.DEVICE_OPTIMIZED == totalLeaving )
			{
				totalLeaving = 0;
				Toast.makeText(getApplicationContext(), "Your device is already optimized", Toast.LENGTH_SHORT).show();
				ivMemoryTextView.setText(ivMemoryTextView.getText()+ " leaving " + String.valueOf(totalLeaving) + "% of your memory");
			}
			else
			{
				
			}*/
			OptimizationProcess();
		}else if(view.getId() == R.id.llCancleOptimize){
			
		}
	}
	
	private void getAllRunningApps() {
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				
				CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(
						info.processName, PackageManager.GET_META_DATA));

				ApplicationInfo ai = pm.getApplicationInfo(info.processName,
						totalRunningApps);

				if (!String.valueOf(info.processName).equals(
						CommonValues.APPLICATION_PACKAGE_NAME)) {
					if ((ai.flags & ApplicationInfo.FLAG_SYSTEM) != 1) {
						runningApps++;
					}
					
					if(String.valueOf(info.processName).equals("com.nqmobile.antivirus20")||String.valueOf(info.processName).equals("com.cleanmaster.mguard:service")
							||String.valueOf(info.processName).equals("com.cleanmaster.mguard"))
					{
						IsSafeBrowsingEnable=true;
						IsRealTimeProtectionEnable=true;
					}
				}

				totalRunningApps++;
			} catch (Exception e) {
				// Name Not FOund Exception
			}
		}
		//ivClearNow.setText("Clean Now(" + runningApps + ")");
		ivMemoryTextView.setText(runningApps + " apps running");

	}
	
	private void Calculation(){
		PackageManager packageManager = getApplicationContext().getPackageManager();

        List<PackageInfo> packs = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
        totalCacheSize = 0;
        totalAppsRunning = 0;
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            Method getPackageSizeInfo;
            try {
                getPackageSizeInfo = packageManager.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                getPackageSizeInfo.invoke(packageManager, p.packageName, new IPackageStatsObserver.Stub() {

					@Override
					public void onGetStatsCompleted(PackageStats pStats,
							boolean succeeded) throws RemoteException {
						totalCacheSize = (totalCacheSize + pStats.cacheSize);
					}
                   
                });
            } catch (Exception e) {
                e.printStackTrace();    
            }
        }
        String value = String.valueOf((totalCacheSize/1024));
        ivCashTextView.setText("Found " + value + "MB of cache");
		final   ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		final List<RunningTaskInfo> recentTasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
		totalAppsRunning = recentTasks.size(); 
		
		long totalMemoryUsed = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
		//old code
		//totalLeaving = (Runtime.getRuntime().freeMemory()*100)/Runtime.getRuntime().totalMemory();
		totalLeaving  = (totalMemoryUsed/1000000);
		//totalLeaving=(minimumleaving/100)/10;
		//getTotalRAMUse();
		 useram = getTotalRAMUse(); 
		getAllRunningApps();
/*		if(CommonConstraints.DEVICE_OPTIMIZED == totalLeaving )
		{
			totalLeaving = 0;
			Toast.makeText(getApplicationContext(), "Your device is already optimized", Toast.LENGTH_SHORT).show();
			ivMemoryTextView.setText(ivMemoryTextView.getText()+ " leaving " + String.valueOf(totalLeaving) + "% of your memory");
		}
		else
		{*/
		
		ivMemoryTextView.setText(ivMemoryTextView.getText()+ " leaving " + String.valueOf((useram-30)<0?"0":(useram-35)) + "% of your memory");
		//CommonConstraints.DEVICE_OPTIMIZED = totalLeaving;*/
		//}
		
		
		
		llOptimize.setVisibility(View.VISIBLE);
		llCancleOptimize.setVisibility(View.GONE);
		
		SafeBrowsingEnableorDisable();
		RealTimeProtectionEnableorDisable();
		
	
	}
	
	private void KillAllRunningApps() {
		try {
			List<ApplicationInfo> packages;
			PackageManager pm;
			pm = getPackageManager();
			// get a list of installed apps.
			packages = pm.getInstalledApplications(0);

			ActivityManager mActivityManager = (ActivityManager) this
					.getSystemService(Context.ACTIVITY_SERVICE);

			for (ApplicationInfo packageInfo : packages) {
			/*	if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1)
					continue;*/
				if (packageInfo.packageName
						.equals(CommonValues.APPLICATION_PACKAGE_NAME))
					continue;	
				mActivityManager
						.killBackgroundProcesses(packageInfo.packageName);
			}
		} catch (Exception e) {
			// Name Not FOund Exception
		}
	}
	
	private void OptimizationProcess(){
		KillAllRunningApps();
		//Toast.makeText(getApplicationContext(), "Optimized successfully.", Toast.LENGTH_LONG).show();
		/*if(CommonConstraints.DEVICE_OPTIMIZED == totalLeaving )
		{
			totalLeaving = 0;
			Toast.makeText(getApplicationContext(), "Your device is already optimized", Toast.LENGTH_SHORT).show();
			ivMemoryTextView.setText(ivMemoryTextView.getText()+ " leaving " + String.valueOf(totalLeaving) + "% of your memory");
		}
		else
		{
		
		ivMemoryTextView.setText(ivMemoryTextView.getText()+ " leaving " + String.valueOf(totalLeaving) + "% of your memory");
		CommonConstraints.DEVICE_OPTIMIZED = totalLeaving;
		} */
		Intent intent = new Intent(this, OptimizationResultActivity.class);
		OptimizationResultActivity.calledFromInfo="checkup";
		OptimizationResultActivity.optimizationMessage=(useram-getTotalRAMUse())+"% Optimized.";
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
		
		
		//onBackPressed();
	}
	
	private void SafeBrowsingEnableorDisable() {
		if(IsSafeBrowsingEnable)
		{
			ivSafeBrowingActive.setVisibility(View.VISIBLE);
			ivSafeBrowingError.setVisibility(View.GONE);
			ivSafeBrowingTextView.setText("Safe Browsing Enabled");
		}
		else
		{
			ivSafeBrowingActive.setVisibility(View.GONE);
			ivSafeBrowingError.setVisibility(View.VISIBLE);
			ivSafeBrowingTextView.setText("Safe Browsing Disabled");
			
		}
	}
	
private void RealTimeProtectionEnableorDisable() {
	if(IsRealTimeProtectionEnable)
	{
		ivRealTimeProtectionActive.setVisibility(View.VISIBLE);
		ivRealTimeProtectionError.setVisibility(View.GONE);
		ivRealTimeProtectionTextView.setText("Real Time Protection Enabled");
	}
	else
	{
		ivRealTimeProtectionActive.setVisibility(View.GONE);
		ivRealTimeProtectionError.setVisibility(View.VISIBLE);
		ivRealTimeProtectionTextView.setText("Real Time Protection Disabled");
	}
	}
	
	
	
	private void WiFiSecurity() {
		// TODO Auto-generated method stub
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		//wifi.setWifiEnabled(true);
		wifi.startScan();
		wc.SSID = "\"myssid\"";
		wc.preSharedKey = "\"mypwd\"";
		wc.status = WifiConfiguration.Status.ENABLED;
		wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
		wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
		wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
		wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
		wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
		wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
		wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
		wc.allowedProtocols.set(WifiConfiguration.GroupCipher.WEP104);
		wc.allowedProtocols.set(WifiConfiguration.GroupCipher.WEP40);
		int netId = wifi.addNetwork(wc);
		wifi.enableNetwork(netId, true);
		if (((mWifi.isConnected())
			||(wifi.getConnectionInfo().getSSID() == "myssid"))||(!isRooted())) {
			ivWifiTextView.setText("WiFi Protection Enabled");
			ivActive.setVisibility(View.VISIBLE);
			ivError.setVisibility(View.GONE);
			//Toast.makeText(getApplicationContext(), "Wifi is Enabled", Toast.LENGTH_SHORT).show();
			//Toast.makeText(getApplicationContext(), ""+wifi.getConnectionInfo().getNetworkId(), Toast.LENGTH_LONG).show();
			//Toast.makeText(getApplicationContext(), ""+wifi.getConnectionInfo().getSSID(), Toast.LENGTH_LONG).show();
			//Toast.makeText(getApplicationContext(), ""+wc.SSID, Toast.LENGTH_SHORT).show();
			//Toast.makeText(getApplicationContext(), ""+wifi.getConnectionInfo().getBSSID(), Toast.LENGTH_LONG).show();
			//Toast.makeText(getApplicationContext(), ""+wifi.getConnectionInfo().getIpAddress(), Toast.LENGTH_LONG).show();
			//Toast.makeText(getApplicationContext(), ""+wifi.getDhcpInfo(), Toast.LENGTH_LONG).show();
			
		}
		else{
			ivWifiTextView.setText("WiFi Protection Disabled");
			ivActive.setVisibility(View.GONE);
			ivError.setVisibility(View.VISIBLE);
			
	}
	}
	private long getTotalRAMUse() {
		ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);
		double freeRAM = (double) memoryInfo.availMem;
		double totalRAM = (double) memoryInfo.totalMem;
		double useRAM = totalRAM - freeRAM;

		long usePercentage = (int) ((100 * useRAM) / totalRAM);

		return usePercentage;
	}

	private boolean isRooted()  {

	    // get from build info
	    String buildTags = android.os.Build.TAGS;
	    if (buildTags != null && buildTags.contains("test-keys")) {
	      return true;
	    }

	    // check if /system/app/Superuser.apk is present
	    try {
	      File file = new File("/system/app/Superuser.apk");
	      if (file.exists()) {
	        return true;
	      }
	    } catch (Exception e1) {
	      // ignore
	    }

	    // try executing commands
	    return canExecuteCommand("/system/xbin/which su")
	        || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
	  }
	  private  boolean canExecuteCommand(String command) {
		    boolean executedSuccesfully;
		    try {
		      Runtime.getRuntime().exec(command);
		      executedSuccesfully = true;
		    } catch (Exception e) {
		      executedSuccesfully = false;
		    }

		    return executedSuccesfully;
		  }
}
