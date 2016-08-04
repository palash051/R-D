package com.vipdashboard.app.base;

import java.util.Date;

import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.utils.CommonTask;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

public class SMSObserver extends ContentObserver{	
	
	private Context mContext;
	String phoneNumber;
	int previousId=0;

	public SMSObserver(Context context, Handler handler) {
	    super(handler);
	    mContext = context;
	}	
	
	@Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange); 
        try{
	       /* if(MyNetService.phoneBasicInformation==null || MyNetService.currentLocation==null)
	        	return;*/
	        Uri uriSMSURI = Uri.parse("content://sms");
	        ContentResolver resolver = mContext.getContentResolver();
	        
	       // Cursor cur =resolver.query(uriSMSURI, null, " type!=3", null, " date DESC");
	        Cursor cur =resolver.query(uriSMSURI, null, " type!=3 and _id>"+MyNetService.lastSmsId, null, " _id DESC");
	        if(cur !=null && cur.moveToFirst()){  
	        	phoneNumber=cur.getString(cur.getColumnIndex("address"));
		        PhoneSMSInformation phoneSMSInformation=new PhoneSMSInformation();
		        phoneSMSInformation.PhoneId=MyNetService.phoneBasicInformation.PhoneId;
		        phoneSMSInformation.SMSType=cur.getInt(cur.getColumnIndex("type"));
		        phoneSMSInformation.Number=cur.getString(cur.getColumnIndex("address"));
		        phoneSMSInformation.SMSBody=cur.getString(cur.getColumnIndex("body"));	        
		        phoneSMSInformation.Latitude=MyNetService.currentLocation.getLatitude();
		        phoneSMSInformation.Longitude=MyNetService.currentLocation.getLongitude();
		        phoneSMSInformation.SMSTime=new Date(cur.getLong(cur.getColumnIndex("date")));	
		        phoneSMSInformation.SmsLogId=cur.getInt(cur.getColumnIndex("_id"));
		        MyNetService.lastSmsId=cur.getInt(cur.getColumnIndex("_id"));
		        cur.close();
		        TelephonyManager tm = (TelephonyManager) mContext
						.getSystemService(Context.TELEPHONY_SERVICE);
				GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
				
		        phoneSMSInformation.LAC=String.valueOf(location.getLac() % 0xffff);
		        phoneSMSInformation.CellID=String.valueOf(location.getCid() % 0xffff);
		        phoneSMSInformation.SiteLang=0;
		        phoneSMSInformation.SiteLong=0;
		        phoneSMSInformation.IsLocal= CommonTask.isInternationalPhoneNumber(mContext, phoneNumber);
		        phoneSMSInformation.LocationName=MyNetService.currentLocationName;
		        MyNetDatabase mynetDatabase = new MyNetDatabase(mContext);
				mynetDatabase.open();
				mynetDatabase.createPhoneSMSInformation(phoneSMSInformation);			
				mynetDatabase.close();	
				MyNetService.lastSmsId=cur.getInt(cur.getColumnIndex("_id"));
	        }
        }catch (Exception e) {
			
		}
    }
}

/*
0:_id
1:thread_id
2:address
3:m_size
4:person
5:date
6:date_sent
7:protocol
8:read
9:status
10:type
11:reply_path_present
12:subject
13:body
14:service_center
15:locked
16:sim_id
17:error_code
18:seen
19:star

MESSAGE_TYPE_ALL    = 0;
MESSAGE_TYPE_INBOX  = 1;
MESSAGE_TYPE_SENT   = 2;
MESSAGE_TYPE_DRAFT  = 3;
MESSAGE_TYPE_OUTBOX = 4;
MESSAGE_TYPE_FAILED = 5; 
MESSAGE_TYPE_QUEUED = 6;


*/
