package com.vipdashboard.app.manager;

import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.TrafficStats;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Base64;
import android.util.Log;

import com.vipdashboard.app.activities.VIPD_ServiceTestActivity;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;

import com.vipdashboard.app.entities.PhoneBasicInformation;
import com.vipdashboard.app.entities.PhoneBasicInformationRoot;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneDataInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.entities.PhoneSignalStrenght;
import com.vipdashboard.app.entities.ReportProblemAndBadExperience;
import com.vipdashboard.app.entities.ServiceTest;
import com.vipdashboard.app.entities.ServiceTestsRoot;
import com.vipdashboard.app.entities.WebDataRequest;
import com.vipdashboard.app.entities.WebDataRequestEntityHolder;
import com.vipdashboard.app.interfaces.IPhoneInformationService;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;
import com.vipdashboard.app.utils.TrafficRecord;
import com.vipdashboard.app.utils.TrafficSnapshot;

public class PhoneInformationManager implements IPhoneInformationService{
	public PhoneInformationManager() {

	}

	@Override
	public Object SetPhoneBasicInfo(Context context, int phoneId,
			ArrayList<PhoneCallInformation> callList,
			ArrayList<PhoneSMSInformation> smsList,
			PhoneDataInformation dataInfo,
			ArrayList<PhoneSignalStrenght> signalInfo) {
		try {
			String strCallList = "", strSmsList = "", strData = "", strSignal = "";
			String strDataConnectionInfo="";
			updatePhoneId(context);
			if( MyNetService.phoneId==0)
				return null;
			if (callList != null && callList.size() > 0) {
				for (PhoneCallInformation phCInfo : callList) {
					strCallList = strCallList + MyNetService.phoneId + "~"
							+ phCInfo.CallType + "~" + phCInfo.Number + "~"
							+ phCInfo.DurationInSec + "~" + phCInfo.Latitude
							+ "~" + phCInfo.Longitude + "~"
							+ phCInfo.CallTime.getTime() + "~" + phCInfo.Reson+ "~"
							+ phCInfo.LAC  + "~" + phCInfo.CellID+ "~"
							+ phCInfo.SiteLang   + "~" + phCInfo.SiteLong+ "~"+phCInfo.IsLocal
							+"~"+phCInfo.LocationName
							+ "|";
				}
			}
			if (strCallList.length() > 0)
				strCallList = strCallList
						.substring(0, strCallList.length() - 1);

			if (smsList != null && smsList.size() > 0)
				for (PhoneSMSInformation phSmsInfo : smsList) {
					strSmsList = strSmsList + MyNetService.phoneId + "~" + phSmsInfo.SMSType
							+ "~" + phSmsInfo.Number + "~" + phSmsInfo.SMSBody
							+ "~" + phSmsInfo.Latitude + "~"
							+ phSmsInfo.Longitude + "~"
							+ phSmsInfo.SMSTime.getTime() + "~" +  phSmsInfo.LAC  + "~" + phSmsInfo.CellID+ "~"
									+ phSmsInfo.SiteLang   + "~" + phSmsInfo.SiteLong+ "~"+phSmsInfo.IsLocal+ "~"+phSmsInfo.LocationName+"|";
				}
			if (strSmsList.length() > 0)
				strSmsList = strSmsList.substring(0, strSmsList.length() - 1);

			if (dataInfo != null) {
				strData = MyNetService.phoneId + "~" + dataInfo.DownLoadSpeed + "~"
						+ dataInfo.UpLoadSpeed + "~" + dataInfo.Latitude + "~"
						+ dataInfo.Longitude + "~"
						+ dataInfo.DataTime.getTime()+"~"+ dataInfo.LocationName;
			}
			if (signalInfo != null && signalInfo.size() > 0) {
				for (PhoneSignalStrenght phSigInfo : signalInfo) {
					strSignal = strSignal + MyNetService.phoneId + "~"
							+ phSigInfo.SignalLevel + "~" + phSigInfo.Latitude
							+ "~" + phSigInfo.Longitude + "~"
							+ phSigInfo.Time.getTime() + "~" + phSigInfo.LAC
							+ "~" + phSigInfo.CellID + "~"+ phSigInfo.SiteLang   + "~" + phSigInfo.SiteLong+ "~"+phSigInfo.LocationName+ "|";
				}
				if (strSignal.length() > 0)
					strSignal = strSignal.substring(0, strSignal.length() - 1);
			}
			//Coded here start
			
			if (CommonValues.totalDataConnectionInConnectedMode>0) {
				
				TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				
				GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
				
				strDataConnectionInfo = MyNetService.phoneId + "~"
									+ MyNetService.currentLocation.getLatitude()+"~"
									+ MyNetService.currentLocation.getLongitude()+ "~"
									+ String.valueOf(location.getLac() % 0xffff) + "~"
									+ String.valueOf(location.getCid() % 0xffff) + "~"
									+"" + "~"
									+"" + "~"
									+ CommonValues.totalDataConnectionInConnectedMode+ "~"
									+ System.currentTimeMillis()+ "~"+MyNetService.currentLocationName+ "|";
				CommonValues.totalDataConnectionInConnectedMode=0;
			}
			
			//Coded here end
			
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userNumber",
					CommonValues.getInstance().LoginUser.UserNumber);
			jsonObject.put("intPhoneId", MyNetService.phoneId);
			jsonObject.put("strCalls", strCallList);
			jsonObject.put("strSmss", strSmsList);
			jsonObject.put("strDatas", strData);
			jsonObject.put("strSignals", strSignal);
			jsonObject.put("strDataConnectionInfos", strDataConnectionInfo); //Added this one line

			PhoneBasicInformationRoot userRoot = (PhoneBasicInformationRoot) JSONfunctions
					.retrieveDataFromJsonPost(
							CommonURL.getInstance().setPhoneDataPost,
							jsonObject, PhoneBasicInformationRoot.class);
			if (userRoot != null)
				return userRoot.phoneBasicInformation;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private void updatePhoneId(Context context) {
		if(MyNetService.phoneId==0){
			if(CommonValues.getInstance().LoginUser==null){
				int usernumber=Integer.parseInt(CommonTask.getPreferences(context, CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY));
				IUserManager userManager = new UserManager();
				CommonValues.getInstance().LoginUser=userManager.GetUserByID(usernumber);
			}
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			PhoneBasicInformationRoot userRoot = (PhoneBasicInformationRoot) JSONfunctions
					.retrieveDataFromStream(String.format(
							CommonURL.getInstance().GetPhoneBasicInfoByUserNumber,
							CommonValues.getInstance().LoginUser.UserNumber, tm.getDeviceId(),tm.getSimSerialNumber()), PhoneBasicInformationRoot.class);

			if (userRoot != null){
				MyNetService.phoneId = userRoot.phoneBasicInformation.PhoneId;					
			}
		}
	}

	@Override
	public PhoneBasicInformation SetPhoneBasicInfo(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String Model = getDeviceName();
			String DeviceID = tm.getDeviceId();
			String DeviceType = android.os.Build.DEVICE;
			String MACID = "";
			String MobileNo = CommonValues.getInstance().LoginUser.Mobile;
			String OperatorName = tm.getNetworkOperatorName();
			String OperatorCountryCode = tm.getNetworkCountryIso();
			String OperatorCountry = "";
			String SIMID = tm.getSimSerialNumber();
			String NetworkType = PhoneBasicInformation.getNetworkTypeString(tm
					.getNetworkType());
			String TAC = "";
			String Manufacturer = Build.MANUFACTURER;
			String CauseCodeDescription = "";
			int CauseCode = 0;
			String RAT = "";
			String RANVendor = "";
			String Controller = "";
			String AccessArea = "";
			String IMEI = tm.getDeviceId();
			String IMSI = tm.getSimSerialNumber();

			String networkOperator = tm.getNetworkOperator();
			String MCC = "";
			String MNC = "";
			if (networkOperator != null) {
				
				//Raju Dutta Changed here, because of wrong input 31-Mar-2014
				
				MCC = String.valueOf(Integer.parseInt(networkOperator.substring(0, 3)));
				MNC = String.valueOf(Integer.parseInt(networkOperator.substring(3)));
				
				
				/*MCC = networkOperator.substring(0, 3);
				MNC = networkOperator.substring(3);*/
			}
			GsmCellLocation location = (GsmCellLocation) tm.getCellLocation();
			String CID = String.valueOf(location.getCid() % 0xffff);
			String LAC = String.valueOf(location.getLac() % 0xffff);
			
			 LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
			 double Latitude = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
			 double Longitude =locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();

			/*double Latitude = MyNetService.currentLocation.getLatitude();
			double Longitude = MyNetService.currentLocation.getLongitude();*/

			String strPhoneBasic = Model + "~" + DeviceID + "~" + DeviceType
					+ "~" + MACID + "~" + MobileNo + "~" + OperatorName + "~"
					+ OperatorCountryCode + "~" + OperatorCountry + "~" + SIMID
					+ "~" + NetworkType + "~" + TAC + "~" + Manufacturer + "~"
					+ CauseCodeDescription + "~" + CauseCode + "~" + RAT + "~"
					+ RANVendor + "~" + Controller + "~" + AccessArea + "~"
					+ IMEI + "~" + IMSI + "~" + MCC + "~" + MNC + "~" + CID
					+ "~" + LAC + "~" + Latitude + "~" + Longitude + "~"
					+ CommonValues.getInstance().LoginUser.UserNumber+ "~" + MyNetService.currentLocationName;
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("datas", strPhoneBasic);

			PhoneBasicInformationRoot userRoot = (PhoneBasicInformationRoot) JSONfunctions
					.retrieveDataFromJsonPost(
							CommonURL.getInstance().SetPhoneBasicInfoPOST,
							jsonObject, PhoneBasicInformationRoot.class);
			if (userRoot != null)
				return userRoot.phoneBasicInformation;

		} catch (Exception e) {
			String s = e.getMessage();
			s = s + s;
		}

		return null;
	}

	@Override
	public PhoneDataInformation SetDataSpeedInfo(Context context,boolean isTest) {
		try {
			updatePhoneId(context);
			if( MyNetService.phoneId==0)
				return null;
			double BYTE_TO_KILOBIT = 0.0078125;
			long fileSizeInByte=1012000;
			long startTime = System.currentTimeMillis();
			Bitmap dawnloadData=CommonTask.getBitmapImage(CommonURL.getInstance().getDownloadTestLink);
			double timeDiffDown = System.currentTimeMillis() - startTime;
			/*BufferedInputStream bis = new BufferedInputStream(is, 8 * 512);
			Bitmap dawnloadData = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();*/
			
			long bytespersecond   =(long) ((fileSizeInByte / timeDiffDown) * 1000);

			int currentDownloadSpeedInKbPS = (int) (bytespersecond*BYTE_TO_KILOBIT);

			 startTime = System.currentTimeMillis();			
			//Found Error here
			ByteBuffer buffer = ByteBuffer
					.allocate(dawnloadData.getByteCount()); // Create a new
															// buffer
			dawnloadData.copyPixelsToBuffer(buffer); // Move the byte data to
														// the buffer

			byte[] array = buffer.array();

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("byteValue",array != null ? Base64.encodeToString(array, Base64.NO_WRAP) : "");
			JSONfunctions.retrieveDataFromJsonPost(CommonURL.getInstance().UploadSpeedTest, jsonObject,null);
			long timeDiff = System.currentTimeMillis() - startTime;
			
			bytespersecond   =(long) ((fileSizeInByte / timeDiff) * 1000);
			
			int currentUploadSpeedInKbPS = (int) (bytespersecond*BYTE_TO_KILOBIT);
			String CID="",LAC="";
			if ((currentDownloadSpeedInKbPS > 0 || currentUploadSpeedInKbPS > 0)
					&& ( MyNetService.currentLocation != null)) {
				PhoneDataInformation phoneDataInformation = new PhoneDataInformation();
				phoneDataInformation.DownLoadSpeed = currentDownloadSpeedInKbPS;
				phoneDataInformation.UpLoadSpeed = currentUploadSpeedInKbPS;
				phoneDataInformation.DataTime = new Date();
				phoneDataInformation.Latitude = MyNetService.currentLocation
						.getLatitude();
				phoneDataInformation.Longitude = MyNetService.currentLocation
						.getLongitude();
				Long TotalDownloadInKB= (TrafficStats.getTotalRxBytes()-CommonValues.getInstance().TotalDownloadData)/1024;
				Long TotalUploadInKB= (TrafficStats.getTotalTxBytes()-CommonValues.getInstance().TotalUploadData)/1024;
				phoneDataInformation.TotalDownloadData=TotalDownloadInKB;
				phoneDataInformation.TotalUploadData=TotalUploadInKB;
				phoneDataInformation.LocationName=MyNetService.currentLocationName;//Will Change here
				phoneDataInformation.CallCount=(int) timeDiffDown;
				if(!isTest){
					MyNetDatabase mynetDatabase = new MyNetDatabase(context);
					mynetDatabase.open();
					mynetDatabase.createPhoneDataInformation(phoneDataInformation);
					mynetDatabase.close();
					TelephonyManager tm = (TelephonyManager) context
							.getSystemService(Context.TELEPHONY_SERVICE);
					GsmCellLocation location = (GsmCellLocation) tm
							.getCellLocation();
					 CID = String.valueOf(location.getCid() % 0xffff);
					 LAC = String.valueOf(location.getLac() % 0xffff);
					 
					JSONfunctions.retrieveDataFromStream(String.format(
							CommonURL.getInstance().SetPhoneDataInfo,
							MyNetService.phoneId, currentDownloadSpeedInKbPS,
							currentUploadSpeedInKbPS,
							MyNetService.currentLocation.getLatitude(),
							MyNetService.currentLocation.getLongitude(),
							new Date().getTime(), LAC, CID, TotalDownloadInKB, TotalUploadInKB,0, 0,URLEncoder.encode(String.valueOf(MyNetService.currentLocationName), CommonConstraints.EncodingCode)),
							PhoneDataInformation.class);
					
					CommonValues.getInstance().TotalDownloadData=TrafficStats.getTotalRxBytes();
					CommonValues.getInstance().TotalUploadData=TrafficStats.getTotalTxBytes();
					
					ServiceTestsRoot root=getServiceTest();
					long startData,dataDiff;
					String url="";
					if(root!=null && root.ServiceTestList.size()>0){
						VIPD_ServiceTestActivity.strSendData="";
						for(int i=0;i<6;i++){
							startTime=System.currentTimeMillis();
							startData=TrafficStats.getTotalRxBytes();
							url=root.ServiceTestList.get(i).ServiceTestLink;
							CommonTask.getBitmapImage(url);
							timeDiff=System.currentTimeMillis()-startTime;
							dataDiff=TrafficStats.getTotalRxBytes()-startData;
							bytespersecond   =(long) ((dataDiff / timeDiffDown) * 1000);
							currentDownloadSpeedInKbPS = (int) (bytespersecond*BYTE_TO_KILOBIT);						
							VIPD_ServiceTestActivity.strSendData=VIPD_ServiceTestActivity.strSendData+url+"~"+timeDiff+"~"+currentDownloadSpeedInKbPS+"~0~"+
									MyNetService.currentLocation.getLatitude()+"~"+MyNetService.currentLocation.getLongitude()+"~"+new Date().getTime()+
								"~"+LAC+"~"+CID+MyNetService.currentLocationName+"|";
						}
						if(!VIPD_ServiceTestActivity.strSendData.isEmpty()){
							setBrowserDataInfoPost(context,VIPD_ServiceTestActivity.strSendData);
						}
						
					}
				}
				System.gc();
				return phoneDataInformation;
			}
			
			//processPhoneAppsData(context, CID, LAC);

			
		} catch (Exception e) {
			return null;
		}
		return null;

	}
	@Override
	public boolean processPhoneAppsData(Context context) {
		try{
			updatePhoneId(context);
			if( MyNetService.phoneId==0)
				return false;
			JSONObject jsonObject;
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			GsmCellLocation location = (GsmCellLocation) tm
					.getCellLocation();
			String CID = String.valueOf(location.getCid() % 0xffff);
			String LAC = String.valueOf(location.getLac() % 0xffff);
			TrafficSnapshot latestTrafficSnapshot=new TrafficSnapshot(context);
			if(latestTrafficSnapshot!=null && CommonValues.getInstance().LastTrafficSnapshot!=null){
				ArrayList<TrafficRecord> logList=new ArrayList<TrafficRecord>();
				HashSet<Integer> intersection=new HashSet<Integer>(latestTrafficSnapshot.apps.keySet());
				for (Integer uid : intersection) {
					TrafficRecord latest_rec=latestTrafficSnapshot.apps.get(uid);
					TrafficRecord previous_rec=
								(CommonValues.getInstance().LastTrafficSnapshot==null ? null : CommonValues.getInstance().LastTrafficSnapshot.apps.get(uid));
					latestTrafficSnapshot.calculateDelta(latest_rec, previous_rec, logList);
				}
				Collections.sort(logList, new TrafficRecordComparator());
				int count=0;
				String appsData=""; 
				for (TrafficRecord trafficRecord : logList) {
					if(count>4)
						break;
					appsData=appsData+trafficRecord.appLebel+"~"+trafficRecord.deltaRx+"~"+
							trafficRecord.deltaTx+"~"+MyNetService.currentLocation.getLatitude()+"~"+
							MyNetService.currentLocation.getLongitude()+"~"+
							new Date().getTime()+"~"+LAC+"~"+CID+"~0~0~"+
							(trafficRecord.appIcon!=null? Base64.encodeToString(trafficRecord.appIcon,Base64.NO_WRAP):"")+"~"+MyNetService.currentLocationName+
							"|";
					count++;
				}
				if(!appsData.isEmpty()){					
					appsData = appsData.substring(0, appsData.length() - 1);
					jsonObject=new JSONObject();
					jsonObject.put("intPhoneId", MyNetService.phoneId);
					jsonObject.put("strDatas", appsData);					
					JSONfunctions.retrieveDataFromJsonPost(CommonURL.getInstance().setPhoneAppsDataPost,jsonObject, PhoneDataInformation.class);
				}
				CommonValues.getInstance().LastTrafficSnapshot=latestTrafficSnapshot;
				
				
				/*MyNetDatabase db=new MyNetDatabase(context);
				db.open();
				ArrayList<ReportProblemAndBadExperience> list=db.getReportProblemAndBadExperienceForSync();
				db.updateReportProblemAndBadExperience();
				db.close();
				if(list.size()>0){
					IStatisticsReportManager statisticsReportManager = new StatisticsReportManager();
					for (ReportProblemAndBadExperience rpbe : list) {
						statisticsReportManager.SetReportProblemAndBadExperience(CommonValues.getInstance().LoginUser.Mobile,String.valueOf(rpbe.Latitude), String.valueOf(rpbe.Longitude), rpbe.RxLevel, Build.MANUFACTURER, Build.BOARD, rpbe.Problem, rpbe.ProblemTime, rpbe.Status, rpbe.Comment, 
								rpbe.ProblemType?"TRUE":"FALSE", "", "", rpbe.LocationName, String.valueOf(Integer.parseInt(tm.getNetworkOperator().substring(0, 3))), String.valueOf(Integer.parseInt(tm.getNetworkOperator().substring(3))), LAC, CID, tm.getSimSerialNumber(), "0", tm.getDeviceId(), tm.getSimSerialNumber(), rpbe.ProblemDetailCategory, rpbe.ProblemDetailSubCategory,rpbe.Remarks,rpbe.problemHeader);	
					}
					
				}*/
				System.gc();
				return true;
			}
		}catch (Exception e) {
			Log.d("", e.getMessage());
		}
		return false;
	}
	
	@Override
	public boolean setBrowserDataInfoPost(Context context,String  strData) {
		try{
			updatePhoneId(context);
			if( MyNetService.phoneId==0)
				return false;
			JSONObject jsonObject;
			
				if(!strData.isEmpty()){					
					strData = strData.substring(0, strData.length() - 1);
					jsonObject=new JSONObject();
					jsonObject.put("intPhoneId", MyNetService.phoneId);
					jsonObject.put("strDatas", strData);					
					JSONfunctions.retrieveDataFromJsonPost(CommonURL.getInstance().setBrowserDataInfoPost,jsonObject, PhoneDataInformation.class);
				}
				return true;
			
		}catch (Exception e) {
			Log.d("", e.getMessage());
		}
		return false;
	}

	public String getDeviceName() {
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer)) {
			return capitalize(model);
		} else {
			return capitalize(manufacturer) + " " + model;
		}
	}

	private String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	@Override
	public PhoneBasicInformation GetPhoneBasicInfo(String IMEI, String IMSI) {
		PhoneBasicInformation user = null;
		PhoneBasicInformationRoot userRoot = (PhoneBasicInformationRoot) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().GetPhoneBasicInfoByUserNumber,
						CommonValues.getInstance().LoginUser.UserNumber, IMEI,
						IMSI), PhoneBasicInformationRoot.class);

		if (userRoot != null)
			user = userRoot.phoneBasicInformation;
		return user;
	}

	@Override
	public WebDataRequest GetwebDataRequest(String phoneNo) {
		WebDataRequestEntityHolder webDataRequestHolder = (WebDataRequestEntityHolder) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().GetwebDataRequest, phoneNo),
						WebDataRequest.class);
		return webDataRequestHolder.webDataRequest;
	}
	@Override
	public ServiceTestsRoot getServiceTest() {
		return (ServiceTestsRoot) JSONfunctions
				.retrieveDataFromStream(CommonURL.getInstance().getServiceTest,ServiceTestsRoot.class);	
	}
	@Override
	public WebDataRequest SetWebDataRequest(WebDataRequest webDataRequest) {
		WebDataRequestEntityHolder webDataRequestHolder = (WebDataRequestEntityHolder) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().SetWebDataRequest,
						webDataRequest.PhoneNo, webDataRequest.IsUpdate,
						webDataRequest.RequestAt, webDataRequest.RefreshAt,
						webDataRequest.RequestBy), WebDataRequest.class);
		return webDataRequestHolder.webDataRequest;
	}
	
	private class TrafficRecordComparator implements Comparator<TrafficRecord>
	{
		@Override
	    public int compare(TrafficRecord left, TrafficRecord right) {
	    	
	        return Long.valueOf(right.deltaRx).compareTo(Long.valueOf( left.deltaRx));
	    }
	}

}
