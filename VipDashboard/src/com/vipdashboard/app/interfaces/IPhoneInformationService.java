package com.vipdashboard.app.interfaces;

import java.util.ArrayList;

import android.content.Context;

import com.vipdashboard.app.entities.PhoneBasicInformation;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneDataInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.entities.PhoneSignalStrenght;
import com.vipdashboard.app.entities.ServiceTestsRoot;
import com.vipdashboard.app.entities.WebDataRequest;

public interface IPhoneInformationService {

	PhoneBasicInformation GetPhoneBasicInfo(String IMEI, String IMSI);	
	PhoneBasicInformation SetPhoneBasicInfo(Context context);
	PhoneDataInformation SetDataSpeedInfo(Context context,boolean isTest);
	Object SetPhoneBasicInfo(Context context,int phoneId,
			ArrayList<PhoneCallInformation> callList,
			ArrayList<PhoneSMSInformation> smsList,
			PhoneDataInformation dataInfo,
			ArrayList<PhoneSignalStrenght> signalInfo);
	WebDataRequest GetwebDataRequest(String phoneNo);
	WebDataRequest SetWebDataRequest(WebDataRequest webDataRequest);
	boolean processPhoneAppsData(Context context);
	boolean setBrowserDataInfoPost(Context context, String strData);
	ServiceTestsRoot getServiceTest();

}
