package com.vipdashboard.app.activities;

import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.VIPDBatteryDoctorCPUManagementActivity.CpuStat.CpuInfo;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.Menu;

public  class VIPDBatteryDoctorInfoActivity extends MainActionbarBase implements OnClickListener
 {
	double ibatteryCapacity ;
	TextView tvHealthStatusResults,
	tvBatteryDoctorMaximumPowerResults,
	tvBatteryDoctorcurrentPowerResult,
	tvBatteryDoctorTemperatureResults,
	tvBatteryDoctorVoltageResults,
	tvHealthStatusResultsTest,
	tvBatteryDoctorTyperesults;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battery_doctor_info);
		Initialization() ;
		registerBatteryLevelReceiver();
		
	}
	
	
	private void Initialization() 
	{
		tvHealthStatusResults= (TextView) findViewById(R.id.tvHealthStatusResults);
		tvBatteryDoctorMaximumPowerResults = (TextView)findViewById(R.id.tvBatteryDoctorMaximumPowerResults);
		tvBatteryDoctorcurrentPowerResult = (TextView)findViewById(R.id.tvBatteryDoctorcurrentPowerResult);
		tvBatteryDoctorTemperatureResults = (TextView)findViewById(R.id.tvBatteryDoctorTemperatureResults);
		tvBatteryDoctorVoltageResults = (TextView)findViewById(R.id.tvBatteryDoctorVoltageResults);
		
		tvBatteryDoctorTyperesults = (TextView)findViewById(R.id.tvBatteryDoctorTyperesults);
		tvHealthStatusResultsTest = (TextView)findViewById(R.id.tvHealthStatusResultsTest);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view.getId() == R.id.bBack){
			onBackPressed();
			{
				Intent intent = new Intent(this, VIPD_BatteryDoctor.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
		}
	}


	}
	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		super.onResume();
	}
		
    private BroadcastReceiver battery_receiver = new BroadcastReceiver()
    {
            @Override
            public void onReceive(Context context, Intent intent)
            {
            	Bundle bundle = intent.getExtras(); 
                    boolean isPresent = bundle.getBoolean("present", false);
                    String technology = intent.getStringExtra("technology");
                    int plugged = intent.getIntExtra("plugged", -1);
                    int scale = intent.getIntExtra("scale", -1);
                    int health = intent.getIntExtra("health", 0);
                    int status = intent.getIntExtra("status", 0);
                    int rawlevel = intent.getIntExtra("level", -1);
                    int tmempurture = bundle.getInt("temperature", 0);
                    double voltage = bundle.getInt("voltage", 0);
   
                    if(voltage>0)
                    {
                    	//voltage= Math.round(voltage/1000);
                    
                    
                    	voltage=Math.round((voltage/1000)*10.0)/10.0;
                    	tvBatteryDoctorVoltageResults.setText(""+voltage+ "V");
                        
                    }
                  
                    getBatteryCapacity();
                    if(tmempurture>10)
                    {
                    	//String value = String.valueOf(tmempurture);
                        String value = String.valueOf(new DecimalFormat("##.##")
    					.format(tmempurture));
                        String itmempurture = String.valueOf((double) tmempurture / 10);
                   String  firtmempurture = String.valueOf((double)((double) (tmempurture / 10) * 1.8)+32);
                   tvBatteryDoctorTemperatureResults.setText(itmempurture+"°C"+"/"+firtmempurture+"°F");
                   
                    	/*String[] es = itmempurture.split("\\.");
    					if (es.length < 2)
    						tvBatteryDoctorTemperatureResults.setText(es[0] + " C");
                    	
    					else
    						tvBatteryDoctorTemperatureResults.setText(es[0] + "C " + es[1] + "F");*/
                    	
                    }
      
                   
        int level = 0;        
        double CurrentPower = 0;          
        if(isPresent)
        {
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                
                if(level>=0)
                {
                	CurrentPower = (ibatteryCapacity/100)*level;
                	
                }
                String batteryinfo = "Battery Charging Level: " + level + "%\n";
                batteryinfo += ("Technology: " + technology + "\n");
                batteryinfo += ("Plugged: " + getPlugTypeString(plugged) + "\n");
                batteryinfo += ("Health: " + getHealthString(health) + "\n");
                tvHealthStatusResults.setText( getHealthString(health));
                
                tvBatteryDoctorcurrentPowerResult.setText(""+CurrentPower+ "mAh");
               // tvBatteryDoctorTemperatureResults.setText(""+tmempurture);
                
               // tvBatteryDoctorVoltageResults.setText(""+voltage+ "V");
                tvBatteryDoctorTyperesults.setText(""+technology);
                //batteryinfo += ("Status: " + getStatusString(status) + "\n");
                setBatteryLevelText(batteryinfo + "\n\n" + bundle.toString());
        }
        else
        {
            setBatteryLevelText("Battery not present!!!");
        }
            }
    };  
    public void getBatteryCapacity() {
        Object mPowerProfile_ = null;
     //   double batteryCapacity = 0;
        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        } 

        try {
            double batteryCapacity = (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");
/*            Toast.makeText(MainActivity.this, batteryCapacity + " mah",
                    Toast.LENGTH_LONG).show();*/
            tvBatteryDoctorMaximumPowerResults.setText(""+batteryCapacity+ "mAh");
            ibatteryCapacity =batteryCapacity;
            
        } catch (Exception e) {
            e.printStackTrace();
        } 

    }
    
    private String getPlugTypeString(int plugged){
            String plugType = "Unknown";
            
            switch(plugged)
            {
                    case BatteryManager.BATTERY_PLUGGED_AC: plugType = "AC";        break;
                    case BatteryManager.BATTERY_PLUGGED_USB: plugType = "USB";      break;
            }                  
            return plugType;
    }           
    private String getHealthString(int health)
    {
            String healthString = "Unknown";
            
            switch(health)
            {
                    case BatteryManager.BATTERY_HEALTH_DEAD: healthString = "Dead"; break;
                    case BatteryManager.BATTERY_HEALTH_GOOD: healthString = "Good"; break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE: healthString = "Over Voltage"; break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT: healthString = "Over Heat"; break;
                    case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE: healthString = "Failure"; break;
            }
            
            return healthString;
    } 
   
    
    private String getStatusString(int status)
    {
            String statusString = "Unknown";
            
            switch(status)
            {
                    case BatteryManager.BATTERY_STATUS_CHARGING: statusString = "Charging"; break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING: statusString = "Discharging"; break;
                    case BatteryManager.BATTERY_STATUS_FULL: statusString = "Full"; break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING: statusString = "Not Charging"; break;
            }
            
            return statusString;
    }           
    private void setBatteryLevelText(String text){
    	//tvBatteryDoctorTyperesults.setText(text);
    }
  
    private void registerBatteryLevelReceiver(){
            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            
    registerReceiver(battery_receiver, filter);
    }
	
	}
 



