package com.vipdashboard.app.base;

import java.io.File;
import java.util.Date;

import com.vipdashboard.app.activities.MakeCallActivity;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

public class CallInformationReceiver extends BroadcastReceiver {
	String previousState = "IDLE";
	PhoneCallInformation phoneCallInformation;
	String phoneNumber;
	TelephonyManager telManager;
	File audiofile = null;
	boolean checkFirst = true;
	MediaRecorder Callrecorder;
	String voice_file_path = "";
	boolean recordStarted;
	@Override
	public void onReceive(final Context context, Intent intent) {	
		try {
			phoneCallInformation = new PhoneCallInformation();
			Thread.sleep(1000);
			ContentResolver resolver = context.getContentResolver();
			Cursor cur = resolver.query(CallLog.Calls.CONTENT_URI, null,"_id >" + MyNetService.phoneCallId, null, CallLog.Calls._ID	+ " DESC");
			if (cur != null) {				
				if (cur.moveToFirst()) {
					phoneNumber	=cur.getString(cur.getColumnIndex(CallLog.Calls.NUMBER));					
					phoneCallInformation.CallType = cur.getInt(cur.getColumnIndex(CallLog.Calls.TYPE));
					phoneCallInformation.Number = phoneNumber;
					phoneCallInformation.CallTime = new Date(cur.getLong((cur.getColumnIndex(CallLog.Calls.DATE))));
					phoneCallInformation.DurationInSec = cur.getInt(cur.getColumnIndex(CallLog.Calls.DURATION));
					phoneCallInformation.Latitude = MyNetService.currentLocation.getLatitude();
					phoneCallInformation.Longitude = MyNetService.currentLocation.getLongitude();
					phoneCallInformation.LocationName = MyNetService.currentLocationName; 
					MyNetService.phoneCallId = cur.getInt(cur.getColumnIndex(CallLog.Calls._ID));
					phoneCallInformation.CallLogId = cur.getInt(cur.getColumnIndex(CallLog.Calls._ID));
				}
			}
			cur.close();
			if(phoneCallInformation.CallType>0){
				phoneCallInformation.Reson = "";
				TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
				phoneCallInformation.LAC=String.valueOf(location.getLac() % 0xffff);
				phoneCallInformation.CellID=String.valueOf(location.getCid() % 0xffff);
				phoneCallInformation.SiteLang=0;
				phoneCallInformation.SiteLong=0;
				phoneCallInformation.IsLocal= CommonTask.isInternationalPhoneNumber(context,phoneNumber);
				phoneCallInformation.Latitude = MyNetService.currentLocation.getLatitude();
				phoneCallInformation.Longitude = MyNetService.currentLocation.getLongitude();
				phoneCallInformation.LocationName= MyNetService.currentLocationName;
				if(CommonConstraints.IsRecordVoiceMemo)
					phoneCallInformation.VoiceRecordPath = voice_file_path;
				MyNetDatabase mynetDatabase = new MyNetDatabase(context);
				mynetDatabase.open();
				phoneCallInformation.CallId = (int) mynetDatabase.createPhoneCallInformation(phoneCallInformation);
				mynetDatabase.close();	
				
				
				
				if (phoneCallInformation.CallType != CallLog.Calls.MISSED_TYPE
						&& phoneCallInformation.DurationInSec > 0) {
					String name = null;
			   			name = CommonTask.getContentName(context, phoneNumber);
			    	if(phoneNumber != null)
			    		phoneNumber = phoneNumber.replace(" ", "");
			    	Intent i = new Intent(context, MakeCallActivity.class);
			    	MakeCallActivity.IsCalledAfterCalled=true;
			    	 i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			    		        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra("CONTACT_NAME", name);
					i.putExtra("CONTACT_NUMBER", phoneNumber);
					i.putExtra("CALL_FROM_CALL_LOG", true);
					  
					context.startActivity(i);
				}
			}
		} catch (Exception e) {
			return;
		}
		/*if(CommonConstraints.IsRecordVoiceMemo){
			telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
			telManager.listen(phoneListener,
				PhoneStateListener.LISTEN_CALL_STATE);
		}*/
	}
	
	private final PhoneStateListener phoneListener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			try {
				switch (state) {
				case TelephonyManager.CALL_STATE_RINGING: {
					//
					break;
				}
				case TelephonyManager.CALL_STATE_OFFHOOK: {
					/*if(!recordStarted){
						File sampleDir = Environment.getExternalStorageDirectory();
						try {
							audiofile = File.createTempFile(String.valueOf(System.currentTimeMillis()),".amr", sampleDir);
							voice_file_path = audiofile.getPath();
							if(Callrecorder == null)
								Callrecorder = new MediaRecorder();
							Callrecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
							Callrecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
							Callrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
							Callrecorder.setOutputFile(audiofile.getAbsolutePath());
							Callrecorder.prepare();
							Callrecorder.start();
							recordStarted = true;
						} catch (Exception e) {
							return;
						}
					}*/
					break;
				}
				case TelephonyManager.CALL_STATE_IDLE: {
					/*if (recordStarted) {
						Callrecorder.stop();
						Callrecorder.release();
						recordStarted = false;
						Callrecorder = null;
					}*/
					break;
				}
				default: {
				}
				}
			} catch (Exception ex) {
			}
		}
	};

}


//[gn_version, formatted_number, numberlabel, matched_number, number, type, date, lookup_uri, geocoded_location, countryiso, data_id, numbertype, new, duration, _id, simid, name, voicemail_uri, normalized_number, is_read, photo_id, raw_contact_id, vtcall]
