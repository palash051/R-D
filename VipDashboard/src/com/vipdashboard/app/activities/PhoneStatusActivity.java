package com.vipdashboard.app.activities;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.entities.PhoneBasicInformation;
import com.vipdashboard.app.utils.CommonTask;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class PhoneStatusActivity extends MainActionbarBase implements LocationListener{
	/*
	 * Signal level
	 * */
	private static final int EXCELLENT_LEVEL = 75;
    private static final int GOOD_LEVEL = 50;
    private static final int MODERATE_LEVEL = 25;
    private static final int WEAK_LEVEL = 0;

    /*
     * 
     * */
    private static final int INFO_SERVICE_STATE_INDEX = 0;
    private static final int INFO_CELL_LOCATION_INDEX = 1;
    private static final int INFO_CALL_STATE_INDEX = 2;
    private static final int INFO_CONNECTION_STATE_INDEX = 3;
    private static final int INFO_SIGNAL_LEVEL_INDEX = 4;
    private static final int INFO_SIGNAL_LEVEL_INFO_INDEX = 5;
    private static final int INFO_DATA_DIRECTION_INDEX = 6;
    private static final int INFO_DEVICE_INFO_INDEX = 7;
    
    private static final int[] info_ids= {
        R.id.serviceState_info,
        R.id.cellLocation_info,
        R.id.callState_info,
        R.id.connectionState_info,
        R.id.signalLevel,
        R.id.signalLevelInfo,
        R.id.dataDirection,
        R.id.device_info
    };

    

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phonestatus);
        
        
        startSignalLevelListener();
        displayTelephonyInfo();
        

    }

    @Override
    protected void onStop() {
    	super.onStop();
    	//Stop listening to the telephony events 
    	StopListener();
    	
    }

    @Override
    protected void onPause() {
    	super.onPause();
    	//Stop listening to the telephony events
    	if (!CommonTask.isOnline(this)) {
    		if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
    	StopListener();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	//subscribes to the telephony related events
    	startSignalLevelListener();
    }
    
    /*
     * Sets the textview contents
     * */
    private void setTextViewText(int id,String text) {
        ((TextView)findViewById(id)).setText(text);
    }	

    
    /*
     * 
     * */
    private void startSignalLevelListener() {
	
    	
    	TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        
		int events = PhoneStateListener.LISTEN_SIGNAL_STRENGTH | 
                                 PhoneStateListener.LISTEN_DATA_ACTIVITY | 
                                 PhoneStateListener.LISTEN_CELL_LOCATION |
                                 PhoneStateListener.LISTEN_CALL_STATE |
                                 PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR |
                                 PhoneStateListener.LISTEN_DATA_CONNECTION_STATE |
                                 PhoneStateListener.LISTEN_SERVICE_STATE;
        
        tm.listen(phoneListener, events);
        
	}
    
    
    /*
     * De-register the telephony events 
     *  
     * */
    private void StopListener() {
    	TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
    	tm.listen(phoneListener, PhoneStateListener.LISTEN_NONE);
    }
    
    
    /*
     * Display the telephony related information
     * */
    private void displayTelephonyInfo() {
	
    	
    	//access to the telephony services
    	TelephonyManager tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
    	
    	//Get the IMEI code
        String deviceid = tm.getDeviceId();
        //Get  the phone number string for line 1, for example, the MSISDN for a GSM phone
        String phonenumber = tm.getLine1Number();
        //Get  the software version number for the device, for example, the IMEI/SV for GSM phones
        String softwareversion = tm.getDeviceSoftwareVersion();
        //Get  the alphabetic name of current registered operator. 
        String operatorname = tm.getNetworkOperatorName();
        //Get  the ISO country code equivalent for the SIM provider's country code.
        String simcountrycode = tm.getSimCountryIso();
        //Get  the Service Provider Name (SPN). 
        String simoperator = tm.getSimOperatorName();
        //Get  the serial number of the SIM, if applicable. Return null if it is unavailable. 
        String simserialno = tm.getSimSerialNumber();
        //Get  the unique subscriber ID, for example, the IMSI for a GSM phone
        String subscriberid = tm.getSubscriberId();
        //Get the type indicating the radio technology (network type) currently in use on the device for data transmission.
        //EDGE,GPRS,UMTS  etc
        String networktype = PhoneBasicInformation.getNetworkTypeString(tm.getNetworkType());
        //indicating the device phone type. This indicates the type of radio used to transmit voice calls
        //GSM,CDMA etc
        String phonetype = getPhoneTypeString(tm.getPhoneType());
        List<NeighboringCellInfo> nn=tm.getNeighboringCellInfo();
        
        
        String deviceinfo = "";
        
        deviceinfo += ("Device ID: " + deviceid + "\n");
        deviceinfo += ("Phone Number: " + phonenumber + "\n");
        deviceinfo += ("Software Version: " + softwareversion + "\n");
        deviceinfo += ("Operator Name: " + operatorname + "\n");
        deviceinfo += ("SIM Country Code: " + simcountrycode + "\n");
        deviceinfo += ("SIM Operator: " + simoperator + "\n");
        deviceinfo += ("SIM Serial No.: " + simserialno + "\n");
        deviceinfo += ("Subscriber ID: " + subscriberid + "\n");
        deviceinfo += ("Network Type: " + networktype + "\n");
        deviceinfo += ("Phone Type: " + phonetype + "\n");
        if(nn.size()>0)
        	deviceinfo += ("Neighboringcell: " + nn.get(0).toString() + "\n");
        
        
        setTextViewText(info_ids[INFO_DEVICE_INFO_INDEX],deviceinfo);
        
	}
    
    private String getLocationAddress(){
    	LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		android.location.Location loc= locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		String addressText="";
		if(loc!=null){
			
							
			Geocoder geocoder =
	                new Geocoder(this, Locale.getDefault());
			
			try {
				List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
				if (addresses != null && addresses.size() > 0) {			                
	                Address address = addresses.get(0);			                
	                for (int  lineIndex=0 ;lineIndex<address.getMaxAddressLineIndex();lineIndex++) {
	                	addressText=addressText+address.getAddressLine(lineIndex)+", ";
					}
	                addressText=addressText+address.getLocality()+", "+address.getCountryName();			               
	            } 
				
				
			}catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		return addressText;
    }
    
    
    private void setDataDirection(int id, int direction){
        int resid = getDataDirectionRes(direction);
        //((TextView)findViewById(id)).setCompoundDrawables(null, null,getResources().getDrawable(resid), null);
        ((ImageView)findViewById(id)).setImageResource(resid);
    }

	private int getDataDirectionRes(int direction){
	        int resid = R.drawable.nodata;
	        
	        switch(direction)
	        {
	                case TelephonyManager.DATA_ACTIVITY_IN:    		resid = R.drawable.indata;break;
	                case TelephonyManager.DATA_ACTIVITY_OUT:        resid = R.drawable.outdata; break;
	                case TelephonyManager.DATA_ACTIVITY_INOUT:      resid = R.drawable.bidata; break;
	                case TelephonyManager.DATA_ACTIVITY_NONE:       resid = R.drawable.nodata; break;
	                default: 
	                	resid = R.drawable.nodata; break;
	        }
	        
	        
	        
	        return resid;
	}

    private void setSignalLevel(int id,int infoid,int level){
    	
        int progress = (int) ((((float)level)/31.0) * 100);
        
        String signalLevelString = getSignalLevelString(progress);
        
        //set the status 
        ((ProgressBar)findViewById(id)).setProgress(progress);
        
        //set the status string
        ((TextView)findViewById(infoid)).setText(signalLevelString);
        
        Log.i("signalLevel ","" + progress);
    }

    
    private String getSignalLevelString(int level) {
    	
        String signalLevelString = "Weak";
        
        if(level > EXCELLENT_LEVEL)             signalLevelString = "Excellent";
        else if(level > GOOD_LEVEL)             signalLevelString = "Good";
        else if(level > MODERATE_LEVEL) 		signalLevelString = "Moderate";
        else if(level > WEAK_LEVEL)             signalLevelString = "Weak";
        
        return signalLevelString;
    }

	
    

	private String getPhoneTypeString(int type){
	        String typeString = "Unknown";
	        
	        switch(type)
	        {
	                case TelephonyManager.PHONE_TYPE_GSM:   typeString = "GSM"; break;
	                case TelephonyManager.PHONE_TYPE_NONE:  typeString = "UNKNOWN"; break;
	                default: 
	                	typeString = "UNKNOWN"; break;
	        }
	        
	        return typeString;
	}


	/*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phonestatus, menu);
        return true;
    }*/
	
	
	private final PhoneStateListener phoneListener = new  PhoneStateListener(){
		
		/*
		 * call fwding
		 * */
		public void onCallForwardingIndicatorChanged(boolean cfi){
			
			super.onCallForwardingIndicatorChanged(cfi);
		}
		
		/*
		 * Call State Changed 
		 * */
		public void onCallStateChanged(int state, String incomingNumber) {
			
			String phoneState = "UNKNOWN";
			
			switch(state){
			
			case TelephonyManager.CALL_STATE_IDLE : 	
				phoneState = "IDLE";
					break;
			case TelephonyManager.CALL_STATE_RINGING :  
				phoneState = "Ringing (" + incomingNumber + ") "; 
					break;
			case TelephonyManager.CALL_STATE_OFFHOOK : 
				phoneState = "Offhook";
					break;
														
			}
			
			setTextViewText(info_ids[INFO_CALL_STATE_INDEX], phoneState);
			super.onCallStateChanged(state, incomingNumber);
			
		}
		
		
		/*
		 * Cell location changed event handler
		 * */
		public void onCellLocationChanged(CellLocation location) {
			
			String strLocation = location.toString()+"\n"+getLocationAddress();			
			
			setTextViewText(info_ids[INFO_CELL_LOCATION_INDEX], strLocation);
			
			super.onCellLocationChanged(location);
		}
		
		
		/*
		 * Cellphone data connection status 
		 * */
		public void onDataConnectionStateChanged(int state) {
		
			String phoneState = "UNKNOWN";
			
			switch(state){
			
			case TelephonyManager.DATA_CONNECTED : 	
				phoneState = "Connected";
					break;
			case TelephonyManager.DATA_CONNECTING :  
				phoneState = "Connecting.."; 
					break;
			case TelephonyManager.DATA_DISCONNECTED : 
				phoneState = "Disconnected";
					break;
			case TelephonyManager.DATA_SUSPENDED : 
				phoneState = "Suspended";
					break;				
			}
			
			setTextViewText(info_ids[INFO_CONNECTION_STATE_INDEX], phoneState);
			
			super.onDataConnectionStateChanged(state);
		}
		
		
		/*
		 * Data activity handler
		 * */
		public void onDataActivity(int direction) {
			
			String strDirection = "NONE";
			
			switch(direction){
			
			case TelephonyManager.DATA_ACTIVITY_IN :
				strDirection = "IN";
				break;
			case TelephonyManager.DATA_ACTIVITY_INOUT:
				strDirection = "IN-OUT";
				break;
			case TelephonyManager.DATA_ACTIVITY_DORMANT:
				strDirection = "Dormant";
				break;
			case TelephonyManager.DATA_ACTIVITY_NONE:
				strDirection="NONE";
				break;
			case TelephonyManager.DATA_ACTIVITY_OUT:
				strDirection="OUT";
				break;
			
			}
			
			setDataDirection(info_ids[INFO_DATA_DIRECTION_INDEX],direction);
			
			super.onDataActivity(direction);
		}
		
		
		/*
		 * Cellphone Service status 
		 * */
		public void onServiceStateChanged(ServiceState serviceState) {
			
			String strServiceState = "NONE";
			
			
			switch(serviceState.getState()){
			
			case ServiceState.STATE_EMERGENCY_ONLY:
				strServiceState = "Emergency";
				break;
				
			case ServiceState.STATE_IN_SERVICE:
				strServiceState = "In Service";
				break;
			case ServiceState.STATE_OUT_OF_SERVICE:
				strServiceState = "Out of Service";
				break;
			case ServiceState.STATE_POWER_OFF:
				strServiceState = "Power off";
				break;
			}
			
			setTextViewText(info_ids[INFO_SERVICE_STATE_INDEX], strServiceState);
			
			super.onServiceStateChanged(serviceState);
			
		}
		
		/*
		 * 
		 * */
		public void onSignalStrengthChanged(int asu) {
			
			setSignalLevel(info_ids[INFO_SIGNAL_LEVEL_INDEX], info_ids[INFO_SIGNAL_LEVEL_INFO_INDEX],asu);
			
			super.onSignalStrengthChanged(asu);
		}
		
	};

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
