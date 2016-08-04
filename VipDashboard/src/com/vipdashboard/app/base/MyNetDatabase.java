package com.vipdashboard.app.base;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.SetPhoneBasicInfoDataAsynctask;
import com.vipdashboard.app.asynchronoustask.UserGroupSyncAsyncTask;
import com.vipdashboard.app.entities.Collaboration;
import com.vipdashboard.app.entities.Collaborations;
import com.vipdashboard.app.entities.CustomMode;
import com.vipdashboard.app.entities.ContactUser;
import com.vipdashboard.app.entities.FacebokPerson;
import com.vipdashboard.app.entities.FacebookFriends;
import com.vipdashboard.app.entities.FacebookQualificationExperience;
import com.vipdashboard.app.entities.Group;
import com.vipdashboard.app.entities.LocalUser;
import com.vipdashboard.app.entities.PhoneBasicInformation;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneDataInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.entities.PhoneSignalStrenght;
import com.vipdashboard.app.entities.ReportProblemAndBadExperience;
import com.vipdashboard.app.entities.UserActivityInformation;
import com.vipdashboard.app.entities.UserFamilyMember;
import com.vipdashboard.app.entities.UserFamilyMembers;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.entities.UserLocationActivityInformation;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

public class MyNetDatabase {
	private static String DATABASE_NAME = "MYNETDB";
	private static int DATABASE_VERSION = 1;

	private static DbHelper _DbHelper;
	private static Context context;
	public static SQLiteDatabase mynetDatabase;
	private static String SQL_TABLE = "PhoneCallInformation";
	private static String SQL = "";

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		public static DbHelper getInstance(Context context) {

			// Use the application context, which will ensure that you
			// don't accidentally leak an Activity's context.
			// See this article for more information: http://bit.ly/6LRzfx
			if (_DbHelper == null) {
				_DbHelper = new DbHelper(context.getApplicationContext());				
			}
			
			if(mynetDatabase==null || !mynetDatabase.isOpen()){
				mynetDatabase = _DbHelper.getWritableDatabase();
			}
			while ((mynetDatabase.isDbLockedByCurrentThread()
					|| mynetDatabase.isDbLockedByOtherThreads())) {

			}
			return _DbHelper;
		}		
		@Override
		public void onCreate(SQLiteDatabase db) {

			SQL = "CREATE TABLE PhoneBasicInformation("
					+ "PhoneId INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "Model nvarchar(50) NULL, "
					+ "DeciceID nvarchar(50) NULL, "
					+ "DeviceType nvarchar(50) NULL, "
					+ "MACID nvarchar(50) NULL, "
					+ "MobileNo nvarchar(20) NULL, "
					+ "OperatorName nvarchar(20) NULL, "
					+ "OperatorCountryCode nvarchar(6) NULL, "
					+ "OperatorCountry nvarchar(20) NULL, "
					+ "SIMID nvarchar(30) NULL, "
					+ "NetworkType nvarchar(15) NULL,"
					+ "LocationName nvarchar(1000) NULL ) ";

			db.execSQL(SQL);

			SQL = "CREATE TABLE PhoneSignalStrength( "
					+ "SignalId INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "PhoneId int NOT NULL, " + "SignalLevel int NOT NULL, "
					+ "Latitude float NULL, " + "Longitude float NULL, "
					+ "LAC nvarchar(40) NULL, " + "CellID nvarchar(40) NULL, "
					+ "Time datetime NULL," + "IsSync int NOT NULL,"
					+ "SiteLang  float NULL, " + "SiteLong  float NULL, "
					+ "LocationName nvarchar(1000) NULL" + ")";
			db.execSQL(SQL);

			SQL = "CREATE TABLE PhoneCallInformation( "
					+ "CallId INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "PhoneId int NOT NULL, " + "CallType int NOT NULL, "
					+ "Number nvarchar(20) NULL, " + "DurationInSec int NULL, "
					+ "Latitude float NULL, " + "Longitude float NULL, "
					+ "TextCallMemo nvarchar(2000),"
					+ "CallTime datetime NULL," + " Reson nvarchar(20) NULL,"
					+ "IsSync int NOT NULL," + "CellID nvarchar(40) NULL, "
					+ "LAC nvarchar(40) NULL, "
					+ "VoiceRecordPath nvarchar(2000) NULL, "
					+ "SiteLang  float NULL, " + "SiteLong  float NULL, "
					+ "IsLocal  int NOT NULL," + "CallLogId  int NOT NULL,"
					+ "LocationName nvarchar(1000) NULL" + ")";
			db.execSQL(SQL);

			SQL = "CREATE TABLE PhoneSMSInformation( "
					+ "SMSId INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "PhoneId int NOT NULL, " + "SMSType int NOT NULL, "
					+ "Number nvarchar(20) NULL, "
					+ "SMSBody nvarchar(20) NULL, " + "Latitude float NULL, "
					+ "Longitude float NULL, " + "SMSTime datetime NULL,"
					+ "IsSync int NOT NULL," + "SmsLogId int  NULL,"
					+ "LAC nvarchar(40) NULL, " + "CellID nvarchar(40) NULL, "
					+ "SiteLang  float NULL, " + "SiteLong  float NULL, "
					+ "IsLocal  int NOT NULL,"
					+ "LocationName nvarchar(1000) NULL" + ")";
			db.execSQL(SQL);

			SQL = "CREATE TABLE PhoneDataInformation( "
					+ "DataId INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "PhoneId int NOT NULL, " + "DownLoadSpeed int NOT NULL, "
					+ "UpLoadSpeed int NOT NULL, " + "Latitude float NULL, "
					+ "Longitude float NULL, " + "DataTime datetime NULL,"
					+ "IsSync int NOT NULL," + "LAC nvarchar(40) NULL, "
					+ "CellID nvarchar(40) NULL, " + "SiteLang  float NULL, "
					+ "SiteLong  float NULL, "
					+ "TotalDownloadData float  NULL, "
					+ "TotalUploadData  float NULL, "
					+ "LocationName nvarchar(1000) NULL" + ")";
			db.execSQL(SQL);

			SQL = "CREATE TABLE ReportProblemAndBadExperience("
					+ "SLNO INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "Latitude float NULL," + "Longitude float NULL,"
					+ "LocationName nvarchar(500) NULL,"
					+ "RxLevel nvarchar(500) NULL,"
					+ "Problem nvarchar(500) NULL,"
					+ "ProblemTime datetime NULL,"
					+ "ReportTime datetime NULL,"
					+ "Status nvarchar(500) NULL,"
					+ "Comment nvarchar(500) NULL," + "ProblemType int NULL,"
					+ "Failed int NULL,"
					+ "ProblemDetailCategory nvarchar(500) NULL,"
					+ "ProblemDetailSubCategory nvarchar(500) NULL,"
					+ "Remarks nvarchar(500) NULL,"
					+ "Extra1 nvarchar(500) NULL,"
					+ "Extra2 nvarchar(500) NULL,"
					+ "ProblemHeader varchar(50) NULL,"
					+ "LatestFeedBack varchar(500) NULL,"
					+ "TTNumber varchar(100) NULL," + "IsSync int NOT NULL"
					+ ")";
			db.execSQL(SQL);

			// Added New Table Here(FB Info)

			SQL = "CREATE TABLE Facebook_Person("
					+ "FBNo INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "FB_UserID bigint NOT NULL,"
					+ "FB_UserName nvarchar(300) NULL,"
					+ "Name varchar(150) NULL,"
					+ "PrimaryEmail nvarchar(300) NULL,"
					+ "PP_Path nvarchar (3000) NULL,"
					+ "Gender varchar(50) NULL,"
					+ "Relationship_Status nvarchar(50) NULL,"
					+ "DateOfBirth datetime NULL,"
					+ "Religion nvarchar(50) NULL,"
					+ "Professional_Skills nvarchar(3000) NULL,"
					+ "About nvarchar(3000) NULL,"
					+ "Pages nvarchar(3000) NULL,"
					+ "Groups nvarchar(3000) NULL,"
					+ "Apps nvarchar(3000) NULL," + "IsSync int NOT NULL" + ")";
			db.execSQL(SQL);

			SQL = "CREATE TABLE Facebook_Qualification_Experience("
					+ "FBQENo INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "FBNo int NOT NULL,"
					+ "Qualification_Experience nvarchar(3000) NULL,"
					+ "Position nvarchar(3000) NULL,"
					+ "Duration_From datetime NULL,"
					+ "Duration_To datetime NULL,"
					+ "Qualification_Experience_Type nvarchar(300) NULL,"
					+ "isWorkSpace int NULL," + "IsSync int NOT NULL" + ")";
			db.execSQL(SQL);

			SQL = "CREATE TABLE Facebook_Friends("
					+ "FBFRDNo INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "FBFRD_Name varchar(150) NULL,"
					+ "FBFRD_UserID bigint NOT NULL" + ")";
			db.execSQL(SQL);

			SQL = "CREATE TABLE LiveCollaboration("
					+ "MsgID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "MsgFrom int NOT NULL,"
					+ "MsgText varchar(3000) NOT NULL," + "GroupID int NULL,"
					+ "MsgTo int NULL," + "PostedDate datetime NOT NULL,"
					+ "isRead int NOT NULL," + "isAttachment int NOT NULL,"
					+ "Latitude float NULL," + "Longitude float NULL,"
					+ "FilePath varchar(500) NULL,"
					+ "MsgFromName varchar(100) NULL,"
					+ "GroupName varchar(100) NULL" + ")";
			db.execSQL(SQL);

			SQL = "CREATE TABLE LocalUserInformation("
					+ "UserID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "UserName nvarchar(100) NOT NULL,"
					+ "ReffId int NOT NULL," + "IsFriend int NOT NULL,"
					+ "MobileNumber nvarchar(20) NOT NULL,"
					+ "UserOnlinestatus int NOT NULL,"
					+ "isFavourite int NOT NULL" + ")";
			db.execSQL(SQL);

			SQL = "CREATE TABLE LocalGroupInformation("
					+ "GroupID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "GroupName nvarchar(100) NOT NULL,"
					+ "ReffId int NOT NULL" + ")";
			db.execSQL(SQL);

			SQL = "CREATE TABLE LocalUserGroup("
					+ "UserGroupID INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "GroupID int NOT NULL," + "UserId int NOT NULL" + ")";
			db.execSQL(SQL);
			
			SQL = "CREATE TABLE CustomMode("
					+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "ModeName nvarchar(500) NOT NULL,"
					+ "Brightness int NOT NULL,"
					+ "TimeOut int NOT NULL,"
					+ "Data int NOT NULL,"
					+ "Wifi int NOT NULL,"
					+ "Bluetooth int NOT NULL,"
					+ "AutomaticSync int NOT NULL,"
					+ "Silence int NOT NULL,"
					+ "Vibration int NOT NULL"
					+ ")";
			db.execSQL(SQL);

			// End Here

			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			ContentValues contentValues = new ContentValues();
			contentValues.put("Model", "");
			contentValues.put("DeciceID", tm.getDeviceId());
			contentValues.put("DeviceType", "");
			contentValues.put("MACID", "");
			contentValues.put("MobileNo", tm.getLine1Number());
			contentValues.put("OperatorName", tm.getNetworkOperatorName());
			contentValues.put("OperatorCountryCode", tm.getNetworkCountryIso());
			contentValues.put("OperatorCountry", "");
			contentValues.put("SIMID", "");
			contentValues.put("NetworkType", PhoneBasicInformation
					.getNetworkTypeString(tm.getNetworkType()));
			db.insertOrThrow("PhoneBasicInformation", null, contentValues);

			Cursor c = db.rawQuery(
					"select PhoneId FROM PhoneBasicInformation where DeciceID='"
							+ tm.getDeviceId() + "'", null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				int phoneId = c.getInt(0);

				ContentResolver resolver = context.getContentResolver();
				c = resolver.query(CallLog.Calls.CONTENT_URI, null, null, null,
						CallLog.Calls.DEFAULT_SORT_ORDER);
				c.moveToFirst();

				for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
					contentValues = new ContentValues();
					contentValues.put("PhoneId", phoneId);
					contentValues.put("CallType",
							c.getInt(c.getColumnIndex(CallLog.Calls.TYPE)));
					contentValues
							.put("Number", c.getString(c
									.getColumnIndex(CallLog.Calls.NUMBER)));
					contentValues.put("CallTime",
							c.getLong(c.getColumnIndex(CallLog.Calls.DATE)));
					contentValues.put("DurationInSec",
							c.getInt(c.getColumnIndex(CallLog.Calls.DURATION)));
					contentValues.put("Latitude", 0);
					contentValues.put("Longitude", 0);
					contentValues.put("Reson", "");
					contentValues.put("IsSync", 1);
					contentValues.put("LAC", "");
					contentValues.put("CellID", "");
					contentValues.put("SiteLang", 0);
					contentValues.put("SiteLong", 0);
					contentValues.put("IsLocal", CommonTask
							.isInternationalPhoneNumber(context, c.getString(c
									.getColumnIndex(CallLog.Calls.NUMBER))));
					contentValues.put("CallLogId",
							c.getInt(c.getColumnIndex(CallLog.Calls._ID)));
					db.insertOrThrow("PhoneCallInformation", null,
							contentValues);
					c.moveToNext();
				}

				c = resolver.query(Uri.parse("content://sms"), null, null,
						null, null);
				c.moveToFirst();
				// _id, thread_id, address, m_size, person, date, date_sent,
				// protocol, read, status, type, reply_path_present, subject,
				// body, service_center, locked, sim_id, error_code, seen, star

				for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
					contentValues = new ContentValues();
					contentValues.put("PhoneId", phoneId);
					contentValues.put("SMSType",
							c.getInt(c.getColumnIndex("type")));
					contentValues.put("Number",
							c.getString(c.getColumnIndex("address")));
					contentValues.put("SMSBody",
							c.getString(c.getColumnIndex("body")));
					contentValues.put("Latitude", 0);
					contentValues.put("Longitude", 0);
					contentValues.put("SMSTime",
							c.getLong(c.getColumnIndex("date")));
					contentValues.put("IsSync", 1);
					contentValues.put("SmsLogId",
							c.getInt(c.getColumnIndex("_id")));
					contentValues.put("LAC", "");
					contentValues.put("CellID", "");
					contentValues.put("SiteLang", 0);
					contentValues.put("SiteLong", 0);
					contentValues.put(
							"IsLocal",
							CommonTask.isInternationalPhoneNumber(context,
									c.getString(c.getColumnIndex("address"))));
					db.insertOrThrow("PhoneSMSInformation", null, contentValues);
					c.moveToNext();
				}
			}
			c.close();
			(new SetPhoneBasicInfoDataAsynctask(context)).execute();
			new UserGroupSyncAsyncTask(context).execute();
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS PhoneBasicInformation");
			db.execSQL("DROP TABLE IF EXISTS PhoneSignalStrength");
			db.execSQL("DROP TABLE IF EXISTS PhoneCallInformation");
			db.execSQL("DROP TABLE IF EXISTS PhoneSMSInformation");
			db.execSQL("DROP TABLE IF EXISTS PhoneDataInformation");
			onCreate(db);
		}
	}

	public MyNetDatabase(Context _context) {
		context = _context;
		DbHelper.getInstance(context);
	}

	public MyNetDatabase open() {
		/*
		 * DbHelper.getInstance(context); mynetDatabase =
		 * _DbHelper.getWritableDatabase();
		 */
		return this;
	}

	public void close() {
		// _DbHelper.close(); 
	}

	public long createBasicInformation(
			PhoneBasicInformation phoneBasicInformation) {

		Cursor c = mynetDatabase.rawQuery(
				"select PhoneId FROM PhoneBasicInformation where DeciceID='"
						+ phoneBasicInformation.DeciceID + "'", null);
		if (c != null && c.getCount() == 0) {
			ContentValues contentValues = new ContentValues();
			contentValues.put("Model", phoneBasicInformation.Model);
			contentValues.put("DeciceID", phoneBasicInformation.DeciceID);
			contentValues.put("DeviceType", phoneBasicInformation.DeviceType);
			contentValues.put("MACID", phoneBasicInformation.MACID);
			contentValues.put("MobileNo", phoneBasicInformation.MobileNo);
			contentValues.put("OperatorName",
					phoneBasicInformation.OperatorName);
			contentValues.put("OperatorCountryCode",
					phoneBasicInformation.OperatorCountryCode);
			contentValues.put("OperatorCountry",
					phoneBasicInformation.OperatorCountry);
			contentValues.put("SIMID", phoneBasicInformation.SIMID);
			contentValues.put("NetworkType", phoneBasicInformation.NetworkType);
			contentValues.put("LocationName",
					phoneBasicInformation.LocationName);
			return mynetDatabase.insertOrThrow("PhoneBasicInformation", null,
					contentValues);
		}
		return 0;
	}

	public PhoneBasicInformation getEntry() {
		Cursor c = mynetDatabase.rawQuery(
				"select * FROM PhoneBasicInformation", null);
		PhoneBasicInformation phoneBasicInformation = null;
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			phoneBasicInformation = new PhoneBasicInformation();
			phoneBasicInformation.PhoneId = c.getInt(0);
			phoneBasicInformation.Model = c.getString(1);
			phoneBasicInformation.DeciceID = c.getString(2);
			phoneBasicInformation.DeviceType = c.getString(3);
			phoneBasicInformation.MACID = c.getString(4);
			phoneBasicInformation.MobileNo = c.getString(5);
			phoneBasicInformation.OperatorName = c.getString(6);
			phoneBasicInformation.OperatorCountryCode = c.getString(7);
			phoneBasicInformation.OperatorCountry = c.getString(8);
			phoneBasicInformation.SIMID = c.getString(9);
			phoneBasicInformation.NetworkType = c.getString(10);
			phoneBasicInformation.LocationName = c.getString(11);
		}
		return phoneBasicInformation;
	}

	public void deleteSignal() {
		mynetDatabase.rawQuery("delete  FROM PhoneSignalStrength", null);
	}

	public long createSignalStrenght(PhoneSignalStrenght phoneSignalStrenght) {
		Cursor c = mynetDatabase.rawQuery(
				"select PhoneId FROM PhoneBasicInformation", null);
		int phoneId = 0;
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			phoneId = c.getInt(0);
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put("PhoneId", phoneId);
		contentValues.put("SignalLevel",
				phoneSignalStrenght.SignalLevel == 99 ? 0
						: phoneSignalStrenght.SignalLevel);
		contentValues.put("Latitude", phoneSignalStrenght.Latitude);
		contentValues.put("Longitude", phoneSignalStrenght.Longitude);
		contentValues.put("Time", phoneSignalStrenght.Time.getTime());
		contentValues.put("LAC", phoneSignalStrenght.LAC);
		contentValues.put("CellID", phoneSignalStrenght.CellID);
		contentValues.put("IsSync", 0);
		contentValues.put("SiteLang", phoneSignalStrenght.SiteLang);
		contentValues.put("SiteLong", phoneSignalStrenght.SiteLong);
		contentValues.put("LocationName", phoneSignalStrenght.LocationName);
		return mynetDatabase.insertOrThrow("PhoneSignalStrength", null,
				contentValues);

	}

	public long createPhoneCallInformation(
			PhoneCallInformation phoneCallInformation) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("PhoneId", phoneCallInformation.PhoneId);
		contentValues.put("Number", phoneCallInformation.Number);
		contentValues.put("Reson", phoneCallInformation.Reson);
		contentValues.put("CallType", phoneCallInformation.CallType);
		contentValues.put("CallTime", phoneCallInformation.CallTime.getTime());
		contentValues.put("DurationInSec", phoneCallInformation.DurationInSec);
		contentValues.put("TextCallMemo", phoneCallInformation.TextCallMemo);
		contentValues.put("VoiceRecordPath",
				phoneCallInformation.VoiceRecordPath);
		contentValues.put("Latitude", phoneCallInformation.Latitude);
		contentValues.put("Longitude", phoneCallInformation.Longitude);
		contentValues.put("IsSync", 0);
		contentValues.put("LAC", phoneCallInformation.LAC);
		contentValues.put("CellID", phoneCallInformation.CellID);
		contentValues.put("SiteLang", phoneCallInformation.SiteLang);
		contentValues.put("SiteLong", phoneCallInformation.SiteLong);
		contentValues.put("IsLocal", phoneCallInformation.IsLocal);
		contentValues.put("CallLogId", phoneCallInformation.CallLogId);
		contentValues.put("LocationName", phoneCallInformation.LocationName);
		mynetDatabase
				.insertOrThrow("PhoneCallInformation", null, contentValues);
		Cursor c = mynetDatabase
				.rawQuery(
						"SELECT CallId FROM PhoneCallInformation ORDER BY CallId desc LIMIT 1",
						null);

		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return 0;
	}

	public long createPhoneSMSInformation(
			PhoneSMSInformation phoneSMSInformation) {
		/*
		 * Cursor c = mynetDatabase.rawQuery(
		 * "select PhoneId FROM PhoneBasicInformation", null); int phoneId=0; if
		 * (c != null && c.getCount() > 0) { c.moveToFirst(); phoneId =
		 * c.getInt(0); } ContentValues contentValues = new ContentValues();
		 * contentValues.put("PhoneId", phoneId); contentValues.put("Number",
		 * phoneSMSInformation.Number); contentValues.put("SMSBody",
		 * phoneSMSInformation.SMSBody); contentValues.put("SMSType",
		 * phoneSMSInformation.SMSType); contentValues.put("SMSTime",
		 * phoneSMSInformation.SMSTime.getTime()); contentValues.put("Latitude",
		 * phoneSMSInformation.Latitude); contentValues.put("Longitude",
		 * phoneSMSInformation.Longitude); contentValues.put("IsSync",0);
		 * contentValues.put("SmsLogId",phoneSMSInformation.SmsLogId);
		 * contentValues.put("LAC", phoneSMSInformation.LAC);
		 * contentValues.put("CellID", phoneSMSInformation.CellID);
		 * contentValues.put("SiteLang",phoneSMSInformation.SiteLang);
		 * contentValues.put("SiteLong",phoneSMSInformation.SiteLong);
		 * contentValues.put("IsLocal",phoneSMSInformation.IsLocal);
		 * contentValues.put("LocationName", phoneSMSInformation.LocationName);
		 * 
		 * c = mynetDatabase.rawQuery(
		 * "select SMSId FROM PhoneSMSInformation where SmsLogId="
		 * +phoneSMSInformation.SmsLogId, null); int SMSId=0; if (c != null &&
		 * c.getCount() > 0) { c.moveToFirst(); SMSId = c.getInt(0); }
		 * if(SMSId==0){ return
		 * mynetDatabase.insertOrThrow("PhoneSMSInformation",
		 * null,contentValues); }else{
		 * mynetDatabase.update("PhoneSMSInformation", contentValues,
		 * " SMSId="+SMSId,null); } return 0;
		 */
		ContentValues contentValues = new ContentValues();
		contentValues.put("PhoneId", phoneSMSInformation.PhoneId);
		contentValues.put("Number", phoneSMSInformation.Number);
		contentValues.put("SMSBody", phoneSMSInformation.SMSBody);
		contentValues.put("SMSType", phoneSMSInformation.SMSType);
		contentValues.put("SMSTime", phoneSMSInformation.SMSTime.getTime());
		contentValues.put("Latitude", phoneSMSInformation.Latitude);
		contentValues.put("Longitude", phoneSMSInformation.Longitude);

		contentValues.put("IsSync", 0);
		contentValues.put("SmsLogId", phoneSMSInformation.SmsLogId);
		contentValues.put("LAC", phoneSMSInformation.LAC);
		contentValues.put("CellID", phoneSMSInformation.CellID);
		contentValues.put("SiteLang", phoneSMSInformation.SiteLang);
		contentValues.put("SiteLong", phoneSMSInformation.SiteLong);
		contentValues.put("IsLocal", phoneSMSInformation.IsLocal);
		contentValues.put("LocationName", phoneSMSInformation.LocationName);
		return mynetDatabase.insertOrThrow("PhoneSMSInformation", null,
				contentValues);
	}

	public void updatePhoneCallInformation(
			PhoneCallInformation phoneCallInformation) {
		ContentValues contentValues = new ContentValues();
		/*
		 * contentValues.put("Number", phoneCallInformation.Number);
		 * contentValues.put("Reson", phoneCallInformation.Reson);
		 * contentValues.put("CallType", phoneCallInformation.CallType);
		 * contentValues.put("CallTime",
		 * phoneCallInformation.CallTime.getTime());
		 * contentValues.put("DurationInSec",
		 * phoneCallInformation.DurationInSec);
		 */
		contentValues.put("TextCallMemo", phoneCallInformation.TextCallMemo);
		/*
		 * contentValues.put("Latitude", phoneCallInformation.Latitude);
		 * contentValues.put("Longitude", phoneCallInformation.Longitude);
		 */

		// String
		// sql="update PhoneCallInformation SET TextCallMemo= '"+phoneCallInformation.TextCallMemo+"' where PhoneId="+phoneCallInformation.PhoneId+";";
		mynetDatabase.update("PhoneCallInformation", contentValues, " CallId="
				+ phoneCallInformation.CallId, null);
		// mynetDatabase.execSQL(sql, null);
	}

	public void updatePhoneSMSInformation(
			PhoneSMSInformation phoneSMSInformation) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("SMSType", phoneSMSInformation.SMSType);
		contentValues.put("SMSTime", phoneSMSInformation.SMSTime.getTime());
		contentValues.put("Latitude", phoneSMSInformation.Latitude);
		contentValues.put("Longitude", phoneSMSInformation.Longitude);
		contentValues.put("IsSync", 0);
		mynetDatabase.update("PhoneSMSInformation", contentValues, " SmsLogId="
				+ phoneSMSInformation.SmsLogId, null);
	}
	
	public void updateUserList(int reffid){
		ContentValues contentValues = new ContentValues();
		contentValues.put("IsFriend", 0);
		mynetDatabase.update("LocalUserInformation", contentValues, " ReffId="+reffid, null);
	}
	
	

	public void updateDataSyncInfo(ArrayList<PhoneCallInformation> callList,
			ArrayList<PhoneSMSInformation> smsList,
			ArrayList<PhoneSignalStrenght> signalList) {
		ContentValues contentValues = new ContentValues();
		if (signalList != null && signalList.size() > 0) {
			for (PhoneSignalStrenght signal : signalList) {
				contentValues.put("IsSync", 1);
				mynetDatabase.update("PhoneSignalStrength", contentValues,
						" SignalId=" + signal.SignalId, null);
			}
		}
		if (callList != null && callList.size() > 0) {
			for (PhoneCallInformation call : callList) {
				contentValues.put("IsSync", 1);
				mynetDatabase.update("PhoneCallInformation", contentValues,
						" CallId=" + call.CallId, null);
			}
		}
		if (smsList != null && smsList.size() > 0) {
			for (PhoneSMSInformation sms : smsList) {
				contentValues.put("IsSync", 1);
				mynetDatabase.update("PhoneSMSInformation", contentValues,
						" SMSId=" + sms.SMSId, null);
			}
		}
	}

	public long createPhoneDataInformation(
			PhoneDataInformation phoneDataInformation) {

		Cursor c = mynetDatabase.rawQuery(
				"select PhoneId FROM PhoneBasicInformation", null);
		int phoneId = 0;
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			phoneId = c.getInt(0);
		}
		c.close();
		ContentValues contentValues = new ContentValues();
		contentValues.put("PhoneId", phoneId);
		contentValues.put("DownLoadSpeed", phoneDataInformation.DownLoadSpeed);
		contentValues.put("UpLoadSpeed", phoneDataInformation.UpLoadSpeed);
		contentValues.put("DataTime", new Date().getTime());
		contentValues.put("Latitude", phoneDataInformation.Latitude);
		contentValues.put("Longitude", phoneDataInformation.Longitude);
		contentValues.put("IsSync", 1);
		contentValues.put("LAC", phoneDataInformation.LAC);
		contentValues.put("CellID", phoneDataInformation.CellID);
		contentValues.put("SiteLang", 0);
		contentValues.put("SiteLong", 0);
		contentValues.put("TotalDownloadData",
				phoneDataInformation.TotalDownloadData);
		contentValues.put("TotalUploadData",
				phoneDataInformation.TotalUploadData);
		contentValues.put("LocationName", phoneDataInformation.LocationName);
		return mynetDatabase.insertOrThrow("PhoneDataInformation", null,
				contentValues);
	}
	
	public long CreateCustomMode(CustomMode customMode) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("ModeName", customMode.ModeName);
		contentValues.put("Brightness", customMode.Brightness);
		contentValues.put("TimeOut", customMode.TimeOut);
		contentValues.put("Data", customMode.Data);
		contentValues.put("Wifi", customMode.Wifi);
		contentValues.put("Bluetooth", customMode.Bluetooth);
		contentValues.put("AutomaticSync", customMode.AutomaticSync);
		contentValues.put("Silence", customMode.Silence);
		contentValues.put("Vibration", customMode.Vibration);
		return mynetDatabase.insertOrThrow("CustomMode", null, contentValues);
	}
	
	public int getTotalCountOfCustomMode(){
		int count = 0;
		Cursor c = mynetDatabase.rawQuery("select count(_id) from CustomMode", null);
		if(c != null && c.getCount() > 0){
			c.moveToFirst();
			count = c.getInt(0);
		}
		c.close();
		return count;
	}
	
	public ArrayList<CustomMode> getAllCustomModeList(){
		ArrayList<CustomMode> customModes = new ArrayList<CustomMode>();
		Cursor c = mynetDatabase.rawQuery("select * from CustomMode", null);
		CustomMode customMode = null;
		if(c != null && c.getCount()>0){
			c.moveToFirst();
			for(int rowIndex = 0;rowIndex<c.getCount();rowIndex++){
				customMode = new CustomMode();
				customMode._id = c.getInt(0);
				customMode.ModeName = c.getString(1);
				customMode.Brightness = c.getInt(2);
				customMode.TimeOut = c.getInt(3);
				customMode.Data = c.getInt(4);
				customMode.Wifi = c.getInt(5);
				customMode.Bluetooth = c.getInt(6);
				customMode.AutomaticSync = c.getInt(7);
				customMode.Silence = c.getInt(8);
				customMode.Vibration = c.getInt(9);
				customModes.add(customMode);
				c.moveToNext();
			}
		}
		return customModes;
	}
	
	public CustomMode getSpecificCustomMode(int _id){
		CustomMode customMode = new CustomMode();
		Cursor c = mynetDatabase.rawQuery("select ModeName,Brightness,TimeOut,Data,Wifi,Bluetooth,AutomaticSync,Silence,Vibration from CustomMode where _id='"+_id+"'", null);
		if(c != null && c.getCount()>0){
			c.moveToFirst();
			customMode.ModeName = c.getString(0);
			customMode.Brightness = c.getInt(1);
			customMode.TimeOut = c.getInt(2);
			customMode.Data = c.getInt(3);
			customMode.Wifi = c.getInt(4);
			customMode.Bluetooth = c.getInt(5);
			customMode.AutomaticSync = c.getInt(6);
			customMode.Silence = c.getInt(7);
			customMode.Vibration = c.getInt(8);
		}
		c.close();
		return customMode;
	}

	public ArrayList<PhoneSignalStrenght> getSignalStrenghtList() {
		ArrayList<PhoneSignalStrenght> list = new ArrayList<PhoneSignalStrenght>();
		Cursor c = mynetDatabase
				.rawQuery(
						"select * FROM PhoneSignalStrength where SignalLevel<100",
						null);
		PhoneSignalStrenght phoneSignalStrenght = null;
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneSignalStrenght = new PhoneSignalStrenght();
				phoneSignalStrenght.SignalId = c.getInt(0);
				phoneSignalStrenght.PhoneId = c.getInt(1);
				phoneSignalStrenght.SignalLevel = c.getInt(2);
				phoneSignalStrenght.Latitude = c.getDouble(3);
				phoneSignalStrenght.Longitude = c.getDouble(4);
				phoneSignalStrenght.Time = new Date(c.getLong(5));
				list.add(phoneSignalStrenght);
			}
		}
		c.close();
		return list;
	}

	public PhoneSignalStrenght getAvgSignalStrenght(long lastSyncTime) {

		/*
		 * Cursor c = mynetDatabase.rawQuery(
		 * "select * FROM PhoneSignalStrength where SignalLevel<100 and Time>"
		 * +lastSyncTime +" order by Time", null);
		 */
		Cursor c = mynetDatabase
				.rawQuery(
						"select * FROM PhoneSignalStrength where SignalLevel<100",
						null);
		PhoneSignalStrenght phoneSignalStrenght = null;
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			phoneSignalStrenght = new PhoneSignalStrenght();
			phoneSignalStrenght.SignalId = c.getInt(0);
			phoneSignalStrenght.PhoneId = c.getInt(1);
			phoneSignalStrenght.SignalLevel = c.getInt(2);
			phoneSignalStrenght.Latitude = c.getDouble(3);
			phoneSignalStrenght.Longitude = c.getDouble(4);
			phoneSignalStrenght.Time = new Date(System.currentTimeMillis());
			return phoneSignalStrenght;

		}
		return null;
	}

	public ArrayList<PhoneSignalStrenght> getSignalStrenght(long lastSyncTime) {
		ArrayList<PhoneSignalStrenght> list = new ArrayList<PhoneSignalStrenght>();
		Cursor c = mynetDatabase.rawQuery(
				"select * FROM PhoneSignalStrength where SignalLevel<100 and Time>"
						+ lastSyncTime + " order by SignalId", null);

		PhoneSignalStrenght phoneSignalStrenght = null;
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			for (int count = 0; count < c.getCount(); count++) {
				phoneSignalStrenght = new PhoneSignalStrenght();
				phoneSignalStrenght.SignalId = c.getInt(0);
				phoneSignalStrenght.PhoneId = c.getInt(1);
				phoneSignalStrenght.SignalLevel = c.getInt(2);
				phoneSignalStrenght.Latitude = c.getDouble(3);
				phoneSignalStrenght.Longitude = c.getDouble(4);
				phoneSignalStrenght.LAC = c.getString(5);
				phoneSignalStrenght.CellID = c.getString(6);
				phoneSignalStrenght.Time = new Date(c.getLong(7));
				list.add(phoneSignalStrenght);
				c.moveToNext();
			}

		}
		return list;
	}

	public ArrayList<PhoneSignalStrenght> getSignalStrenghtListForSync() {
		ArrayList<PhoneSignalStrenght> list = new ArrayList<PhoneSignalStrenght>();
		Cursor c = mynetDatabase
				.rawQuery(
						"select * FROM PhoneSignalStrength where IsSync=0 and Latitude>0 order by SignalId",
						null);

		PhoneSignalStrenght phoneSignalStrenght = null;
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			for (int count = 0; count < c.getCount(); count++) {
				phoneSignalStrenght = new PhoneSignalStrenght();
				phoneSignalStrenght.SignalId = c.getInt(0);
				phoneSignalStrenght.PhoneId = c.getInt(1);
				phoneSignalStrenght.SignalLevel = c.getInt(2);
				phoneSignalStrenght.Latitude = c.getDouble(3);
				phoneSignalStrenght.Longitude = c.getDouble(4);
				phoneSignalStrenght.LAC = c.getString(5);
				phoneSignalStrenght.CellID = c.getString(6);
				phoneSignalStrenght.Time = new Date(c.getLong(7));
				phoneSignalStrenght.LocationName = c.getString(11);
				list.add(phoneSignalStrenght);
				c.moveToNext();
			}

		}
		return list;
	}

	public int getAgvSignalStrenght() {
		Cursor c = mynetDatabase.rawQuery(
				"select avg(SignalLevel) from PhoneSignalStrength", null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return 0;
	}

	public int getAgvSignalStrenght(long date) {
		Cursor c = mynetDatabase.rawQuery(
				"select avg(SignalLevel) from PhoneSignalStrength where Time > "
						+ date, null);
		try {
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				return c.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public int getAgvSignalStrenght(long dateFrom, long dateTo) {
		Cursor c = mynetDatabase.rawQuery(
				"select avg(SignalLevel) from PhoneSignalStrength where Time > "
						+ dateFrom + " and Time < " + dateTo, null);
		try {
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				return c.getInt(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	public int getAgvSignalStrenght(int type) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		String sql = "select avg(SignalLevel) from PhoneSignalStrength ";

		if (type == 1) {
			// cal.add(Calendar.HOUR, -1);
			sql = sql + " WHERE Time >" + cal.getTimeInMillis();
		} else if (type == 2) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			sql = sql + " WHERE Time >" + cal.getTimeInMillis();
		} else if (type == 3) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			sql = sql + " WHERE Time >" + cal.getTimeInMillis();
			cal.add(Calendar.DAY_OF_MONTH, +1);
			sql = sql + " and Time <" + cal.getTimeInMillis();
		} else if (type == 4) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.add(Calendar.DAY_OF_MONTH, -7);
			sql = sql + " WHERE Time >" + cal.getTimeInMillis();
		}

		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return 0;
	}

	public int getMaxSignalStrenght() {
		String sql = "select SignalLevel from PhoneSignalStrength ORDER BY SignalLevel desc LIMIT 1";
		try {
			Cursor c = mynetDatabase.rawQuery(sql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				return c.getInt(0);
			}
		} catch (Exception e) {
			String ss = e.getMessage();
			ss = ss + "";
		}
		return 0;
	}

	public int getMaxSignalStrength(long date) {
		String sql = "select SignalLevel from PhoneSignalStrength where Time > "
				+ date + " ORDER BY SignalLevel desc LIMIT 1";
		try {
			Cursor c = mynetDatabase.rawQuery(sql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				return c.getInt(0);
			}
		} catch (Exception e) {
			String ss = e.getMessage();
			ss = ss + "";
		}
		return 0;
	}

	public int getMaxSignalStrength(long dateFrom, long dateTo) {
		String sql = "select SignalLevel from PhoneSignalStrength where Time > "
				+ dateFrom
				+ " and Time < "
				+ dateTo
				+ " ORDER BY SignalLevel desc LIMIT 1";
		try {
			Cursor c = mynetDatabase.rawQuery(sql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				return c.getInt(0);
			}
		} catch (Exception e) {
			String ss = e.getMessage();
			ss = ss + "";
		}
		return 0;
	}

	public int getminSignalStrenght() {
		String sql = "select SignalLevel from PhoneSignalStrength ORDER BY SignalLevel asc LIMIT 1";
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return 0;
	}

	public int getMinSignalStrength(long date) {
		String sql = "select SignalLevel from PhoneSignalStrength where Time > "
				+ date + " ORDER BY SignalLevel asc LIMIT 1";
		try {
			Cursor c = mynetDatabase.rawQuery(sql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				return c.getInt(0);
			}
		} catch (Exception e) {
			String ss = e.getMessage();
			ss = ss + "";
		}
		return 0;
	}

	public int getMinSignalStrength(long dateFrom, long dateTo) {
		String sql = "select SignalLevel from PhoneSignalStrength where Time > "
				+ dateFrom
				+ " and Time < "
				+ dateTo
				+ " ORDER BY SignalLevel asc LIMIT 1";
		try {
			Cursor c = mynetDatabase.rawQuery(sql, null);
			if (c != null && c.getCount() > 0) {
				c.moveToFirst();
				return c.getInt(0);
			}
		} catch (Exception e) {
			String ss = e.getMessage();
			ss = ss + "";
		}
		return 0;
	}

	public int getAvgCallDuration() {
		String sql = "select avg(DurationInSec) from PhoneCallInformation";
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return 0;
	}

	public int getAvgCallDuration(long dateFrom, long dateTo) {
		String sql = "select avg( DurationInSec) from PhoneCallInformation where CallTime  >"
				+ dateFrom + " and CallTime  <" + dateTo;
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return 0;
	}

	public int getAvgCallDuration(long date) {
		String sql = "select avg( DurationInSec) from PhoneCallInformation where CallTime  >"
				+ date;
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return 0;
	}

	public int getMaxCallDuration() {
		String sql = "select DurationInSec from PhoneCallInformation ORDER BY DurationInSec desc LIMIT 1";
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return 0;
	}

	public int getMaxCallDuration(long date) {
		String sql = "select  DurationInSec from PhoneCallInformation where CallTime > "
				+ date + " ORDER BY DurationInSec desc LIMIT 1";
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return 0;
	}

	public int getMaxCallDuration(long dateFrom, long DateTo) {
		String sql = "select  DurationInSec from PhoneCallInformation where CallTime > "
				+ dateFrom
				+ " and CallTime < "
				+ DateTo
				+ " ORDER BY DurationInSec desc LIMIT 1";
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return 0;
	}

	public int getMinCallDuration() {
		String sql = "select DurationInSec from PhoneCallInformation where DurationInSec>0  ORDER BY DurationInSec asc LIMIT 1";
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return 0;
	}

	public int getMinCallDuration(long date) {
		String sql = "select  DurationInSec from PhoneCallInformation where CallTime > "
				+ date + " ORDER BY DurationInSec asc LIMIT 1";
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return 0;
	}

	public int getMinCallDuration(long dateFrom, long DateTo) {
		String sql = "select  DurationInSec from PhoneCallInformation where CallTime > "
				+ dateFrom
				+ " and CallTime < "
				+ DateTo
				+ " ORDER BY DurationInSec asc LIMIT 1";
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return 0;
	}

	public PhoneDataInformation getAgvDownLoadUploadSpeed(int type) {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		String sql = "select avg(DownLoadSpeed) downspeed,avg(UpLoadSpeed) upspeed from PhoneDataInformation ";
		if (type == 1) {
			// cal.add(Calendar.HOUR, -1);
			sql = sql + " WHERE DataTime >=" + cal.getTimeInMillis();
		} else if (type == 2) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			sql = sql + " WHERE DataTime >=" + cal.getTimeInMillis();
		} else if (type == 3) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.add(Calendar.DAY_OF_MONTH, -1);
			sql = sql + " WHERE DataTime >=" + cal.getTimeInMillis();
			cal.add(Calendar.DAY_OF_MONTH, 1);
			sql = sql + " and DataTime <" + cal.getTimeInMillis();
		} else if (type == 4) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.add(Calendar.DAY_OF_MONTH, -7);
			sql = sql + " WHERE DataTime >=" + cal.getTimeInMillis();
		}

		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneDataInformation phoneDataInformation = new PhoneDataInformation();
			phoneDataInformation.DownLoadSpeed = c.getInt(0);
			phoneDataInformation.UpLoadSpeed = c.getInt(1);
			return phoneDataInformation;
		}
		return null;
	}

	public ArrayList<PhoneDataInformation> getDownLoadUploadSpeedList() {
		ArrayList<PhoneDataInformation> phoneCallList = new ArrayList<PhoneDataInformation>();
		Cursor c = mynetDatabase.rawQuery("select * from PhoneDataInformation",
				null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneDataInformation phoneDataInformation = null;
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneDataInformation = new PhoneDataInformation();
				phoneDataInformation.DataId = c.getInt(0);
				phoneDataInformation.PhoneId = c.getInt(1);
				phoneDataInformation.DownLoadSpeed = c.getInt(2);
				phoneDataInformation.UpLoadSpeed = c.getInt(3);
				phoneDataInformation.Latitude = c.getDouble(4);
				phoneDataInformation.Longitude = c.getDouble(5);
				phoneDataInformation.DataTime = new Date(c.getLong(6));
				phoneCallList.add(phoneDataInformation);
				c.moveToNext();
			}
		}
		return phoneCallList;
	}

	public ArrayList<PhoneCallInformation> getTotalCallInfo(int type) {
		ArrayList<PhoneCallInformation> phoneCallList = new ArrayList<PhoneCallInformation>();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		String sql = "select CallType, count(Number) CallCount,sum(DurationInSec) duration from PhoneCallInformation";
		if (type == CommonConstraints.INFO_TYPE_LASTHOUR) {
			// cal.add(Calendar.HOUR, -1);
			sql = sql + " WHERE CallTime >=" + cal.getTimeInMillis();
		} else if (type == CommonConstraints.INFO_TYPE_TODAY) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			sql = sql + " WHERE CallTime >=" + cal.getTimeInMillis();
		} else if (type == CommonConstraints.INFO_TYPE_YESTERDAY) {
			cal.set(Calendar.HOUR, -24);
			sql = sql + " WHERE CallTime >=" + cal.getTimeInMillis();
			/*
			 * cal.set(Calendar.HOUR_OF_DAY, 0); cal.add(Calendar.DAY_OF_MONTH,
			 * -1); sql=sql+" WHERE CallTime >="+cal.getTimeInMillis();
			 * cal.add(Calendar.DAY_OF_MONTH, 1);
			 * sql=sql+" and CallTime <"+cal.getTimeInMillis();
			 */
		} else if (type == CommonConstraints.INFO_TYPE_WEEK) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.add(Calendar.DAY_OF_MONTH, -7);
			sql = sql + " WHERE CallTime >=" + cal.getTimeInMillis();
		} else if (type == CommonConstraints.INFO_TYPE_MONTH) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			sql = sql + " WHERE CallTime >=" + cal.getTimeInMillis();
		}

		sql = sql + " group by CallType order by CallType";

		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneCallInformation phoneCallInformation = null;

			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneCallInformation = new PhoneCallInformation();
				phoneCallInformation.CallType = c.getInt(0);
				phoneCallInformation.CallCount = c.getInt(1);
				phoneCallInformation.DurationInSec = c.getInt(0) == 3 ? 0 : c
						.getInt(2);
				phoneCallList.add(phoneCallInformation);
				c.moveToNext();
			}
		}
		return phoneCallList;
	}

	public ArrayList<PhoneCallInformation> getTotalCallInfoByPhoneNumber() {
		ArrayList<PhoneCallInformation> phoneCallList = new ArrayList<PhoneCallInformation>();
		/*
		 * Cursor c = mynetDatabase.rawQuery(
		 * "select Number, MAX(CallTime) MaxCallTime, count(CallId) CallCount,sum( CASE when CallType=3 then 0 else DurationInSec end ) duration from PhoneCallInformation group by Number order by MaxCallTime desc"
		 * , null);
		 */
		Cursor c = mynetDatabase
				.rawQuery(
						"select substr(Number, -11) as subnumber,Number, Max(CallTime) callTime,count(CallId) CallCount,sum(CASE when CallType=3 then 0 else DurationInSec end) duration From PhoneCallInformation group by subnumber order by CallTime desc",
						null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneCallInformation phoneCallInformation = null;

			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneCallInformation = new PhoneCallInformation();
				phoneCallInformation.Number = c.getString(1);
				phoneCallInformation.CallTime = new Date(c.getLong(2));
				phoneCallInformation.CallCount = c.getInt(3);
				phoneCallInformation.DurationInSec = c.getInt(4);
				phoneCallList.add(phoneCallInformation);
				c.moveToNext();
			}
		}
		return phoneCallList;
	}

	public ArrayList<PhoneSMSInformation> getTotalSMSInfo(int type) {
		ArrayList<PhoneSMSInformation> phoneSmsList = new ArrayList<PhoneSMSInformation>();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		String sql = "select SMSType, count(SMSId) CallCount from PhoneSMSInformation";
		if (type == 1) {
			// cal.add(Calendar.HOUR, -1);
			sql = sql + " WHERE SMSTime >=" + cal.getTimeInMillis();
		} else if (type == 2) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			sql = sql + " WHERE SMSTime >=" + cal.getTimeInMillis();
		} else if (type == 3) {
			cal.set(Calendar.HOUR, -24);
			sql = sql + " WHERE SMSTime >=" + cal.getTimeInMillis();
			/*
			 * cal.set(Calendar.HOUR_OF_DAY, 0); cal.add(Calendar.DAY_OF_MONTH,
			 * -1); sql=sql+ " WHERE SMSTime >="+cal.getTimeInMillis();
			 * cal.add(Calendar.DAY_OF_MONTH, 1); sql=sql+
			 * " and SMSTime <"+cal.getTimeInMillis();
			 */
		} else if (type == 4) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.add(Calendar.DAY_OF_MONTH, -7);
			sql = sql + " WHERE SMSTime >=" + cal.getTimeInMillis();
		}

		sql = sql + " group by SMSType order by SMSType";

		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneSMSInformation phoneSMSInformation = null;

			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneSMSInformation = new PhoneSMSInformation();
				phoneSMSInformation.SMSType = c.getInt(0);
				phoneSMSInformation.SMSCount = c.getInt(1);
				phoneSmsList.add(phoneSMSInformation);
				c.moveToNext();
			}
		}
		return phoneSmsList;
	}

	public int getTotalDataCount() {
		String sql = "select count(DataId) from PhoneDataInformation ";
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return 0;
	}

	public ArrayList<PhoneCallInformation> getCallInfoList() {
		return getCallInfoList(0);
	}

	public ArrayList<PhoneCallInformation> getCallInfoList(long lastSyncTime) {
		ArrayList<PhoneCallInformation> phoneCallList = new ArrayList<PhoneCallInformation>();
		String sql = "select * from PhoneCallInformation ";
		if (lastSyncTime > 0)
			sql = sql + " where Latitude>0 and CallTime>" + lastSyncTime;
		sql = sql + " order by CallTime desc";
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneCallInformation phoneCallInformation = null;

			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneCallInformation = new PhoneCallInformation();
				phoneCallInformation.CallId = c.getInt(0);
				phoneCallInformation.PhoneId = c.getInt(1);
				phoneCallInformation.CallType = c.getInt(2);
				phoneCallInformation.Number = c.getString(3);
				phoneCallInformation.DurationInSec = c.getInt(2) == 3 ? 0 : c
						.getInt(4);
				phoneCallInformation.Latitude = c.getDouble(5);
				phoneCallInformation.Longitude = c.getDouble(6);
				phoneCallInformation.TextCallMemo = c.getString(7);
				phoneCallInformation.CallTime = new Date(c.getLong(8));
				phoneCallInformation.Reson = c.getString(10);
				phoneCallInformation.Name = CommonTask.getContentName(context,
						phoneCallInformation.Number);
				phoneCallList.add(phoneCallInformation);
				c.moveToNext();
			}
		}
		return phoneCallList;
	}

	public Cursor getCallInfoListCursor() {
		String sql = "select * from PhoneCallInformation order by CallTime desc";
		Cursor c = mynetDatabase.rawQuery(sql, null);
		return c;
	}

	public ArrayList<PhoneCallInformation> getCallInfoListForSync() {
		ArrayList<PhoneCallInformation> phoneCallList = new ArrayList<PhoneCallInformation>();
		String sql = "select * from PhoneCallInformation where IsSync=0 and Latitude>0 order by CallId";

		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneCallInformation phoneCallInformation = null;

			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneCallInformation = new PhoneCallInformation();
				phoneCallInformation.CallId = c.getInt(0);
				phoneCallInformation.PhoneId = c.getInt(1);
				phoneCallInformation.CallType = c.getInt(2);
				phoneCallInformation.Number = c.getString(3);
				phoneCallInformation.DurationInSec = c.getInt(2) == 3 ? 0 : c
						.getInt(4);
				phoneCallInformation.Latitude = c.getDouble(5);
				phoneCallInformation.Longitude = c.getDouble(6);
				phoneCallInformation.TextCallMemo = c.getString(7);
				phoneCallInformation.CallTime = new Date(c.getLong(8));
				phoneCallInformation.Reson = c.getString(9);
				phoneCallInformation.CellID = c.getString(11);
				phoneCallInformation.LAC = c.getString(12);

				phoneCallInformation.IsLocal = c.getInt(16) > 0;
				phoneCallInformation.LocationName = c.getString(18);
				phoneCallList.add(phoneCallInformation);
				c.moveToNext();
			}
		}
		return phoneCallList;
	}

	public ArrayList<PhoneCallInformation> getTotalCallInforByNumber(int type,
			String number) {
		ArrayList<PhoneCallInformation> phoneCallList = new ArrayList<PhoneCallInformation>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		// /
		number = CommonTask.trimTentativeCountryCode(number);
		// /
		String sql = "select Number,CallType,CallTime,count(Number) CallCount,sum(CASE when CallType=3 then 0 else DurationInSec end) duration from PhoneCallInformation";
		if (type == CommonConstraints.INFO_TYPE_ALL) {
			sql = sql + " WHERE Number like '%" + number + "%'";
		} else if (type == CommonConstraints.INFO_TYPE_TODAY) {
			cal.set(Calendar.HOUR, 0);
			// sql=sql+" WHERE CallTime >="+cal.getTimeInMillis()+" AND Number='"+number+"'";
			sql = sql + " WHERE CallTime >=" + cal.getTimeInMillis()
					+ " AND Number like '%" + number + "%'";
		} else if (type == CommonConstraints.INFO_TYPE_WEEK) {
			cal.set(Calendar.HOUR, 0);
			cal.add(Calendar.DAY_OF_MONTH, -7);
			// sql=sql+" WHERE CallTime >="+cal.getTimeInMillis()+" AND Number='"+number+"'";
			sql = sql + " WHERE CallTime >=" + cal.getTimeInMillis()
					+ " AND Number like '%" + number + "%'";
		} else if (type == CommonConstraints.INFO_TYPE_MONTH) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			// sql=sql+" WHERE CallTime >="+cal.getTimeInMillis()+" AND Number='"+number+"'";
			sql = sql + " WHERE CallTime >=" + cal.getTimeInMillis()
					+ " AND Number like '%" + number + "%'";
		}
		sql = sql + " group by CallType order by CallType";
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneCallInformation phoneCallInformation = null;

			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneCallInformation = new PhoneCallInformation();
				phoneCallInformation.Number = c.getString(0);
				phoneCallInformation.CallType = c.getInt(1);
				phoneCallInformation.CallTime = new Date(c.getLong(2));
				phoneCallInformation.CallCount = c.getInt(3);
				phoneCallInformation.DurationInSec = c.getInt(4);
				phoneCallList.add(phoneCallInformation);
				c.moveToNext();
			}
		}
		return phoneCallList;
	}

	public ArrayList<PhoneSMSInformation> getSMSInfoListForSync() {
		ArrayList<PhoneSMSInformation> phoneSmsList = new ArrayList<PhoneSMSInformation>();
		String sql = "select * from PhoneSMSInformation where IsSync=0 and Latitude>0 order by SMSId";

		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneSMSInformation phoneSmsInformation = null;

			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneSmsInformation = new PhoneSMSInformation();
				phoneSmsInformation.SMSId = c.getInt(0);
				phoneSmsInformation.PhoneId = c.getInt(1);
				phoneSmsInformation.SMSType = c.getInt(2);
				phoneSmsInformation.Number = c.getString(3);
				phoneSmsInformation.SMSBody = c.getString(4);
				phoneSmsInformation.Latitude = c.getDouble(5);
				phoneSmsInformation.Longitude = c.getDouble(6);
				phoneSmsInformation.SMSTime = new Date(c.getLong(7));
				phoneSmsInformation.LAC = c.getString(10);
				phoneSmsInformation.CellID = c.getString(11);
				phoneSmsInformation.IsLocal = c.getInt(14) > 0;
				phoneSmsInformation.LocationName = c.getString(15);
				phoneSmsList.add(phoneSmsInformation);
				c.moveToNext();
			}
		}
		return phoneSmsList;
	}

	public ArrayList<PhoneSMSInformation> getSMSInfoList(long lastSyncTime) {
		ArrayList<PhoneSMSInformation> phoneSmsList = new ArrayList<PhoneSMSInformation>();
		String sql = "select * from PhoneSMSInformation ";
		if (lastSyncTime > 0)
			sql = sql + " where Latitude>0 and SMSTime>" + lastSyncTime;
		sql = sql + " order by SMSTime desc";
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneSMSInformation phoneSmsInformation = null;

			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneSmsInformation = new PhoneSMSInformation();
				phoneSmsInformation.SMSId = c.getInt(0);
				phoneSmsInformation.PhoneId = c.getInt(1);
				phoneSmsInformation.SMSType = c.getInt(2);
				phoneSmsInformation.Number = c.getString(3);
				phoneSmsInformation.SMSBody = c.getString(4);
				phoneSmsInformation.Latitude = c.getDouble(5);
				phoneSmsInformation.Longitude = c.getDouble(6);
				phoneSmsInformation.SMSTime = new Date(c.getLong(7));
				phoneSmsInformation.Name = CommonTask.getContentName(context,
						phoneSmsInformation.Number);
				phoneSmsList.add(phoneSmsInformation);
				c.moveToNext();
			}
		}
		return phoneSmsList;
	}

	public ArrayList<PhoneSMSInformation> getSMSInfoListByPageIndex(
			int pageIndex, ArrayList<PhoneSMSInformation> sms,
			boolean isAllNeeded) {
		ArrayList<PhoneSMSInformation> phoneSmsList = sms;
		String sql = "select SMSId,PhoneId,SMSType,Number,SMSBody,SMSTime from PhoneSMSInformation";
		// sql=sql+" where SMSId Between " +(pageIndex*40+1) + " and "
		// +(pageIndex+1)*40;
		sql = sql + " order by SMSTime desc  limit 40 OFFSET "
				+ ((pageIndex - 1) * 40);
		/*
		 * if(!isAllNeeded)
		 * sql+="limit "+((pageIndex*20)+1)+" OFFSET "+((pageIndex-1)*20);
		 */// Should change here
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneSMSInformation phoneSmsInformation = null;
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneSmsInformation = new PhoneSMSInformation();
				phoneSmsInformation.SMSId = CommonTask.TryParseInt(c
						.getString(0));
				phoneSmsInformation.PhoneId = CommonTask.TryParseInt(c
						.getString(1));
				phoneSmsInformation.SMSType = CommonTask.TryParseInt(c
						.getString(2));
				phoneSmsInformation.Number = c.getString(3);
				phoneSmsInformation.SMSBody = c.getString(4);
				// phoneSmsInformation.Latitude=CommonTask.TryParseDouble(c.getString(5));
				// phoneSmsInformation.Longitude=CommonTask.TryParseDouble(c.getString(6));
				phoneSmsInformation.SMSTime = new Date(
						CommonTask.TryParseLong(c.getString(5)));
				phoneSmsInformation.Name = phoneSmsInformation.Number != null ? CommonTask
						.getContentName(context, phoneSmsInformation.Number)
						: "";
				phoneSmsList.add(phoneSmsInformation);
				c.moveToNext();
			}
		}
		return phoneSmsList;
	}

	public ArrayList<PhoneCallInformation> getCallInfoListByPageIndex(
			int pageIndex, ArrayList<PhoneCallInformation> call) {
		ArrayList<PhoneCallInformation> phoneCallList = call;
		// String
		// sql="select CallId,PhoneId,CallType,Number,DurationInSec,CallTime  from PhoneCallInformation order by CallTime  desc limit "+((pageIndex*20)+1)+" OFFSET "+((pageIndex-1)*20);
		// // Should change here
		String sql = "select CallId,PhoneId,CallType,Number,DurationInSec,CallTime  from PhoneCallInformation";
		// sql=sql+" where CallId Between " +(pageIndex*40+1) + " and "
		// +(pageIndex+1)*40;
		sql = sql + " order by CallTime desc limit 40 OFFSET "
				+ ((pageIndex - 1) * 40);

		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneCallInformation phoneCallInformation = null;

			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneCallInformation = new PhoneCallInformation();
				phoneCallInformation.CallId = CommonTask.TryParseInt(c
						.getString(0));
				phoneCallInformation.PhoneId = CommonTask.TryParseInt(c
						.getString(1));
				phoneCallInformation.CallType = CommonTask.TryParseInt(c
						.getString(2));
				phoneCallInformation.Number = c.getString(3);
				phoneCallInformation.DurationInSec = CommonTask.TryParseInt(c
						.getString(2)) == 3 ? 0 : CommonTask.TryParseInt(c
						.getString(4));
				// phoneCallInformation.Latitude=CommonTask.TryParseDouble(c.getString(5));
				// phoneCallInformation.Longitude=CommonTask.TryParseDouble(c.getString(6));
				// phoneCallInformation.TextCallMemo = c.getString(7);
				phoneCallInformation.CallTime = new Date(
						CommonTask.TryParseLong(c.getString(5)));
				// phoneCallInformation.Reson=c.getString(10);
				phoneCallInformation.Name = phoneCallInformation.Number != null ? CommonTask
						.getContentName(context, phoneCallInformation.Number)
						: "";
				phoneCallList.add(phoneCallInformation);
				c.moveToNext();
			}
		}
		return phoneCallList;
	}

	public ArrayList<PhoneSMSInformation> getSMSInfoList() {
		return getSMSInfoList(0);
	}

	public PhoneDataInformation getAvgDataInfoByTime(long lastSyncTime) {

		String sql = "SELECT * FROM PhoneDataInformation "
				+ " where DataTime > " + lastSyncTime
				+ " order by DataTime desc";

		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneDataInformation phoneDataInformation = new PhoneDataInformation();
			phoneDataInformation.DataId = c.getInt(0);
			phoneDataInformation.PhoneId = c.getInt(1);
			phoneDataInformation.DownLoadSpeed = c.getInt(2);
			phoneDataInformation.UpLoadSpeed = c.getInt(3);
			phoneDataInformation.Latitude = c.getDouble(4);
			phoneDataInformation.Longitude = c.getDouble(5);
			phoneDataInformation.DataTime = new Date(c.getLong(6));
			return phoneDataInformation;
		}
		return null;
	}

	public ArrayList<PhoneDataInformation> getDataInfoList() {
		ArrayList<PhoneDataInformation> phoneDataList = new ArrayList<PhoneDataInformation>();
		Cursor c = mynetDatabase.rawQuery("select * from PhoneDataInformation",
				null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneDataInformation phoneDataInformation = null;

			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneDataInformation = new PhoneDataInformation();
				phoneDataInformation.DataId = c.getInt(0);
				phoneDataInformation.PhoneId = c.getInt(1);
				phoneDataInformation.DownLoadSpeed = c.getInt(2);
				phoneDataInformation.UpLoadSpeed = c.getInt(3);
				phoneDataInformation.Latitude = c.getDouble(4);
				phoneDataInformation.Longitude = c.getDouble(5);
				phoneDataInformation.DataTime = new Date(c.getLong(6));
				phoneDataList.add(phoneDataInformation);
				c.moveToNext();
			}
		}
		return phoneDataList;
	}

	public ArrayList<Object> getUsersHistry(long date) {

		ArrayList<Object> userHistryList = new ArrayList<Object>();
		ArrayList<PhoneCallInformation> phoneCallList = new ArrayList<PhoneCallInformation>();
		ArrayList<PhoneSMSInformation> phoneSmsList = new ArrayList<PhoneSMSInformation>();

		String Callquery = "SELECT * FROM PhoneCallInformation WHERE CallTime>='"
				+ String.valueOf(date) + "' order by CallTime desc";
		Cursor callHistry = mynetDatabase.rawQuery(Callquery, null);
		if (callHistry != null && callHistry.getCount() > 0) {
			callHistry.moveToFirst();
			PhoneCallInformation phoneCallInformation = null;

			for (int rowIndex = 0; rowIndex < callHistry.getCount(); rowIndex++) {
				phoneCallInformation = new PhoneCallInformation();
				phoneCallInformation.CallId = callHistry.getInt(0);
				phoneCallInformation.PhoneId = callHistry.getInt(1);
				phoneCallInformation.CallType = callHistry.getInt(2);
				phoneCallInformation.Number = callHistry.getString(3);
				phoneCallInformation.DurationInSec = callHistry.getInt(2) == 3 ? 0
						: callHistry.getInt(4);
				phoneCallInformation.Latitude = callHistry.getDouble(5);
				phoneCallInformation.Longitude = callHistry.getDouble(6);
				phoneCallInformation.TextCallMemo = callHistry.getString(7);
				phoneCallInformation.CallTime = new Date(callHistry.getLong(8));
				phoneCallInformation.Reson = callHistry.getString(9);

				phoneCallList.add(phoneCallInformation);
				callHistry.moveToNext();
			}
		}
		userHistryList.add(phoneCallList);

		String SMSquery = "SELECT * FROM PhoneSMSInformation WHERE SMSTime>='"
				+ String.valueOf(date) + "' order by SMSTime desc";
		Cursor c = mynetDatabase.rawQuery(SMSquery, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneSMSInformation phoneSmsInformation = null;

			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneSmsInformation = new PhoneSMSInformation();
				phoneSmsInformation.SMSId = c.getInt(0);
				phoneSmsInformation.PhoneId = c.getInt(1);
				phoneSmsInformation.SMSType = c.getInt(2);
				phoneSmsInformation.Number = c.getString(3);
				phoneSmsInformation.SMSBody = c.getString(4);
				phoneSmsInformation.Latitude = c.getDouble(5);
				phoneSmsInformation.Longitude = c.getDouble(6);
				phoneSmsInformation.SMSTime = new Date(c.getLong(7));
				phoneSmsList.add(phoneSmsInformation);
				c.moveToNext();
			}
		}
		userHistryList.add(phoneSmsList);
		return userHistryList;
	}

	public ArrayList<Object> getUsersHistry(long dateFrom, long dateTo) {

		ArrayList<Object> userHistryList = new ArrayList<Object>();
		ArrayList<PhoneCallInformation> phoneCallList = new ArrayList<PhoneCallInformation>();
		ArrayList<PhoneSMSInformation> phoneSmsList = new ArrayList<PhoneSMSInformation>();

		String Callquery = "SELECT * FROM PhoneCallInformation WHERE CallTime>="
				+ dateFrom
				+ " and CallTime<"
				+ dateTo
				+ " order by CallTime desc";
		Cursor callHistry = mynetDatabase.rawQuery(Callquery, null);
		if (callHistry != null && callHistry.getCount() > 0) {
			callHistry.moveToFirst();
			PhoneCallInformation phoneCallInformation = null;

			for (int rowIndex = 0; rowIndex < callHistry.getCount(); rowIndex++) {
				phoneCallInformation = new PhoneCallInformation();
				phoneCallInformation.CallId = callHistry.getInt(0);
				phoneCallInformation.PhoneId = callHistry.getInt(1);
				phoneCallInformation.CallType = callHistry.getInt(2);
				phoneCallInformation.Number = callHistry.getString(3);
				phoneCallInformation.DurationInSec = callHistry.getInt(2) == 3 ? 0
						: callHistry.getInt(4);
				phoneCallInformation.Latitude = callHistry.getDouble(5);
				phoneCallInformation.Longitude = callHistry.getDouble(6);
				phoneCallInformation.TextCallMemo = callHistry.getString(7);
				phoneCallInformation.CallTime = new Date(callHistry.getLong(8));
				phoneCallInformation.Reson = callHistry.getString(9);

				phoneCallList.add(phoneCallInformation);
				callHistry.moveToNext();
			}
		}
		userHistryList.add(phoneCallList);

		String SMSquery = "SELECT * FROM PhoneSMSInformation WHERE SMSTime>="
				+ dateFrom + " and SMSTime<" + dateTo
				+ " order by SMSTime desc";
		Cursor c = mynetDatabase.rawQuery(SMSquery, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneSMSInformation phoneSmsInformation = null;

			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneSmsInformation = new PhoneSMSInformation();
				phoneSmsInformation.SMSId = c.getInt(0);
				phoneSmsInformation.PhoneId = c.getInt(1);
				phoneSmsInformation.SMSType = c.getInt(2);
				phoneSmsInformation.Number = c.getString(3);
				phoneSmsInformation.SMSBody = c.getString(4);
				phoneSmsInformation.Latitude = c.getDouble(5);
				phoneSmsInformation.Longitude = c.getDouble(6);
				phoneSmsInformation.SMSTime = new Date(c.getLong(7));
				phoneSmsList.add(phoneSmsInformation);
				c.moveToNext();
			}
		}
		userHistryList.add(phoneSmsList);
		return userHistryList;
	}

	public ArrayList<PhoneCallInformation> getCallInformation(String number) {
		ArrayList<PhoneCallInformation> phoneCallList = new ArrayList<PhoneCallInformation>();
		String query = "SELECT * FROM PhoneCallInformation WHERE Number='"
				+ number + "'order by  CallTime desc";
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneCallInformation phoneCallInformation = null;

			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneCallInformation = new PhoneCallInformation();
				phoneCallInformation.CallId = c.getInt(0);
				phoneCallInformation.PhoneId = c.getInt(1);
				phoneCallInformation.CallType = c.getInt(2);
				phoneCallInformation.Number = c.getString(3);
				phoneCallInformation.DurationInSec = c.getInt(2) == 3 ? 0 : c
						.getInt(4);
				phoneCallInformation.Latitude = c.getDouble(5);
				phoneCallInformation.Longitude = c.getDouble(6);
				phoneCallInformation.TextCallMemo = c.getString(7);
				phoneCallInformation.CallTime = new Date(c.getLong(8));
				phoneCallInformation.Reson = c.getString(9);

				phoneCallList.add(phoneCallInformation);
				c.moveToNext();
			}
		}
		return phoneCallList;
	}

	public ArrayList<PhoneSMSInformation> getSMSInformation(String number) {
		ArrayList<PhoneSMSInformation> smsList = new ArrayList<PhoneSMSInformation>();
		String query = "SELECT * FROM PhoneSMSInformation WHERE Number='"
				+ number + "'order by SMSTime asc";
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneSMSInformation phoneSMSInformation = null;
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneSMSInformation = new PhoneSMSInformation();
				phoneSMSInformation.SMSId = c.getInt(0);
				phoneSMSInformation.PhoneId = c.getInt(1);
				phoneSMSInformation.SMSType = c.getInt(2);
				phoneSMSInformation.Number = c.getString(3);
				phoneSMSInformation.SMSBody = c.getString(4);
				phoneSMSInformation.Latitude = c.getDouble(5);
				phoneSMSInformation.Longitude = c.getDouble(6);
				phoneSMSInformation.SMSTime = new Date(c.getLong(7));
				smsList.add(phoneSMSInformation);
				c.moveToNext();
			}
		}
		return smsList;
	}

	public int CreateReportProblemAndBadExperience(
			ReportProblemAndBadExperience rpbe) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("Latitude", rpbe.Latitude);
		contentValues.put("Longitude", rpbe.Longitude);
		contentValues.put("LocationName", rpbe.LocationName);
		contentValues.put("RxLevel", rpbe.RxLevel);
		contentValues.put("Problem", rpbe.Problem);
		contentValues.put("ProblemTime",
				CommonTask.convertDatetoLong(rpbe.ProblemTime));
		contentValues.put("ReportTime",
				CommonTask.convertDatetoLong(rpbe.ReportTime));
		contentValues.put("Status", rpbe.Status);
		contentValues.put("Comment", rpbe.Comment);
		contentValues.put("ProblemType", rpbe.ProblemType);
		contentValues.put("Failed", rpbe.Failed);
		contentValues.put("ProblemDetailCategory", rpbe.ProblemDetailCategory);
		contentValues.put("ProblemDetailSubCategory",
				rpbe.ProblemDetailSubCategory);
		contentValues.put("Remarks", rpbe.Remarks);
		contentValues.put("Extra1", rpbe.Extra1);
		contentValues.put("Extra2", rpbe.Extra2);
		contentValues.put("ProblemHeader", rpbe.problemHeader);
		contentValues.put("LatestFeedBack", rpbe.LatestFeedBack);
		contentValues.put("TTNumber", rpbe.TTNumber);
		contentValues.put("IsSync", 0);
		return (int) mynetDatabase.insertOrThrow(
				"ReportProblemAndBadExperience", null, contentValues);
	}

	public ArrayList<ReportProblemAndBadExperience> getReportProblemAndBadExperience() {
		ArrayList<ReportProblemAndBadExperience> rpbeList = new ArrayList<ReportProblemAndBadExperience>();
		String query = "SELECT * FROM ReportProblemAndBadExperience order by  ProblemTime desc";
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			ReportProblemAndBadExperience rpbe = null;
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				rpbe = new ReportProblemAndBadExperience();
				rpbe.SLNO = c.getInt(0);
				rpbe.Latitude = c.getDouble(1);
				rpbe.Longitude = c.getDouble(2);
				rpbe.LocationName = c.getString(3);
				rpbe.RxLevel = c.getString(4);
				rpbe.Problem = c.getString(5);
				rpbe.ProblemDate = new Date(c.getLong(6));
				rpbe.ReportDate = new Date(c.getLong(7));
				rpbe.ProblemTime = String.valueOf(c.getLong(6));
				rpbe.ReportTime = String.valueOf(c.getLong(7));
				rpbe.Status = c.getString(8);
				rpbe.Comment = c.getString(9);
				rpbe.ProblemType = c.getInt(10) == 1 ? true : false;
				rpbe.Failed = c.getInt(11);
				rpbe.ProblemDetailCategory = c.getString(12);
				rpbe.ProblemDetailSubCategory = c.getString(13);
				rpbe.Remarks = c.getString(14);
				rpbe.Extra1 = c.getString(15);
				rpbe.Extra2 = c.getString(16);
				rpbe.problemHeader = c.getString(17);
				rpbeList.add(rpbe);
				c.moveToNext();
			}
		}
		return rpbeList;
	}

	public ArrayList<ReportProblemAndBadExperience> getCountOfDropAndBlockCalls(
			int type) {
		ArrayList<ReportProblemAndBadExperience> reportProblemAndBadExperienceList = new ArrayList<ReportProblemAndBadExperience>();
		Calendar cal = Calendar.getInstance();
		String sql = "select Problem, count(Problem) Failed from ReportProblemAndBadExperience";
		if (type == CommonConstraints.INFO_TYPE_LASTHOUR) {
			cal.add(Calendar.HOUR, -1);
			sql = sql + " WHERE ProblemTime >=" + cal.getTimeInMillis();
		} else if (type == CommonConstraints.INFO_TYPE_TODAY) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			sql = sql + " WHERE ProblemTime >=" + cal.getTimeInMillis();
		} else if (type == CommonConstraints.INFO_TYPE_YESTERDAY) {
			cal.set(Calendar.HOUR, -24);
			/*
			 * cal.set(Calendar.HOUR_OF_DAY, 0); cal.add(Calendar.DAY_OF_MONTH,
			 * -1);
			 */
			sql = sql + " WHERE ProblemTime >=" + cal.getTimeInMillis();
			/*
			 * cal.add(Calendar.DAY_OF_MONTH, 1); sql=sql+
			 * " and ProblemTime <"+cal.getTimeInMillis() + ")";
			 */
		} else if (type == CommonConstraints.INFO_TYPE_WEEK) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.add(Calendar.DAY_OF_MONTH, -7);
			sql = sql + " WHERE ProblemTime >=" + cal.getTimeInMillis();
		}

		sql = sql
				+ " and (Problem = 'Dropped Call' or Problem = 'Blocked Call')";

		sql = sql + " group by Problem";

		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			ReportProblemAndBadExperience reportProblemAndBadExperience = null;

			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				reportProblemAndBadExperience = new ReportProblemAndBadExperience();
				reportProblemAndBadExperience.Problem = c.getString(0);
				reportProblemAndBadExperience.Failed = c.getInt(1);
				reportProblemAndBadExperienceList
						.add(reportProblemAndBadExperience);
				c.moveToNext();
			}
		}
		return reportProblemAndBadExperienceList;
	}

	public ArrayList<ReportProblemAndBadExperience> getReportProblemAndBadExperienceForSync() {
		ArrayList<ReportProblemAndBadExperience> rpbeList = new ArrayList<ReportProblemAndBadExperience>();
		String query = "SELECT * FROM ReportProblemAndBadExperience where IsSync=0";
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			ReportProblemAndBadExperience rpbe = null;
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				rpbe = new ReportProblemAndBadExperience();
				rpbe.SLNO = c.getInt(0);
				rpbe.Latitude = c.getDouble(1);
				rpbe.Longitude = c.getDouble(2);
				rpbe.LocationName = c.getString(3);
				rpbe.RxLevel = c.getString(4);
				rpbe.Problem = c.getString(5);
				rpbe.ProblemDate = new Date(c.getLong(6));
				rpbe.ReportDate = new Date(c.getLong(7));
				rpbe.ProblemTime = String.valueOf(c.getLong(6));
				rpbe.ReportTime = String.valueOf(c.getLong(7));
				rpbe.Status = c.getString(8);
				rpbe.Comment = c.getString(9);
				rpbe.ProblemType = c.getInt(10) == 1 ? true : false;
				rpbe.Failed = c.getInt(11);
				rpbe.ProblemDetailCategory = c.getString(12);
				rpbe.ProblemDetailSubCategory = c.getString(13);
				rpbe.Remarks = c.getString(14);
				rpbe.Extra1 = c.getString(15);
				rpbe.Extra2 = c.getString(16);
				rpbe.problemHeader = c.getString(17);
				rpbeList.add(rpbe);
				c.moveToNext();
			}
		}
		return rpbeList;
	}

	public FacebokPerson getFacebokPerson() {
		FacebokPerson facebokPerson = new FacebokPerson();
		String query = "SELECT * FROM Facebook_Person";
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			facebokPerson.FBNo = c.getInt(0);
			facebokPerson.FB_UserID = c.getString(1);
			facebokPerson.FB_UserName = c.getString(2);
			facebokPerson.Name = c.getString(3);
			facebokPerson.PrimaryEmail = c.getString(4);
			facebokPerson.PP_Path = c.getString(5);
			facebokPerson.Gender = c.getString(6);
			facebokPerson.Relationship_Status = c.getString(7);
			facebokPerson.DateOfBirth = c.getString(8);
			facebokPerson.Religion = c.getString(9);
			facebokPerson.Professional_Skills = c.getString(10);
			facebokPerson.About = c.getString(11);
			facebokPerson.Pages = c.getString(12);
			facebokPerson.Groups = c.getString(13);
			facebokPerson.Apps = c.getString(14);
			facebokPerson.isSync = c.getInt(15);
		}

		return facebokPerson;
	}

	public ArrayList<FacebookQualificationExperience> getFacebook_Qualification_Experience() {
		ArrayList<FacebookQualificationExperience> facebookQualificationExperienceList = new ArrayList<FacebookQualificationExperience>();
		String query = "SELECT * FROM Facebook_Qualification_Experience";
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			FacebookQualificationExperience facebookQualificationExperience = null;
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				facebookQualificationExperience = new FacebookQualificationExperience();
				facebookQualificationExperience.FBQENo = c.getInt(0);
				facebookQualificationExperience.FBNo = c.getInt(1);
				facebookQualificationExperience.QualificationExperience = c
						.getString(2);
				facebookQualificationExperience.Position = c.getString(3);
				facebookQualificationExperience.Duration_From = c.getString(4);
				facebookQualificationExperience.Duration_To = c.getString(5);
				facebookQualificationExperience.QualificationExperienceType = c
						.getString(6);
				facebookQualificationExperience.isWorkSpace = c.getInt(7) == 1 ? true
						: false;
				facebookQualificationExperience.isSync = c.getInt(8);
				facebookQualificationExperienceList
						.add(facebookQualificationExperience);
				c.moveToNext();
			}
		}
		return facebookQualificationExperienceList;
	}

	public int CreateFacebookPerson(FacebokPerson facebookPerson) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("FB_UserID", facebookPerson.FB_UserID);
		contentValues.put("FB_UserName", facebookPerson.FB_UserName);
		contentValues.put("Name", facebookPerson.Name);
		contentValues.put("PrimaryEmail", facebookPerson.PrimaryEmail);
		contentValues.put("PP_Path", facebookPerson.PP_Path);
		contentValues.put("Gender", facebookPerson.Gender);
		contentValues.put("Relationship_Status",
				facebookPerson.Relationship_Status);
		contentValues.put("DateOfBirth", facebookPerson.DateOfBirth);
		contentValues.put("Religion", facebookPerson.Religion);
		contentValues.put("Professional_Skills",
				facebookPerson.Professional_Skills);
		contentValues.put("About", facebookPerson.About);
		contentValues.put("Pages", facebookPerson.Pages);
		contentValues.put("Groups", facebookPerson.Groups);
		contentValues.put("Apps", facebookPerson.Apps);
		contentValues.put("IsSync", 0);
		return (int) mynetDatabase.insertOrThrow("Facebook_Person", null,
				contentValues);
	}

	public int CreateFacebook_Qualification_Experience(
			FacebookQualificationExperience facebookQualificationExperience) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("FBNo", facebookQualificationExperience.FBNo);
		contentValues.put("Qualification_Experience",
				facebookQualificationExperience.QualificationExperience);
		contentValues.put("Position", facebookQualificationExperience.Position);
		contentValues.put("Duration_From",
				facebookQualificationExperience.Duration_From);
		contentValues.put("Duration_To",
				facebookQualificationExperience.Duration_To);
		contentValues.put("Qualification_Experience_Type",
				facebookQualificationExperience.QualificationExperienceType);
		contentValues.put("isWorkSpace", 0);
		contentValues.put("IsSync", 0);
		return (int) mynetDatabase.insertOrThrow(
				"Facebook_Qualification_Experience", null, contentValues);
	}

	public int CreateFacebookFriends(FacebookFriends facebookFriends) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("FBFRD_Name", facebookFriends.FriendName);
		contentValues.put("FBFRD_UserID", facebookFriends.FriendsID);
		return (int) mynetDatabase.insertOrThrow("Facebook_Friends", null,contentValues);
	}

	public ArrayList<FacebookFriends> getFacebookFriends() {
		ArrayList<FacebookFriends> facebookFriendsList = new ArrayList<FacebookFriends>();
		String query = "SELECT * FROM Facebook_Friends";
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			FacebookFriends facebookFriends = null;
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				facebookFriends = new FacebookFriends();
				facebookFriends.FriendName = c.getString(1);
				facebookFriends.FriendsID = c.getString(2);
				facebookFriendsList.add(facebookFriends);
				c.moveToNext();
			}
		}
		return facebookFriendsList;
	}

	public ArrayList<UserActivityInformation> getUserActivityInformation(
			String othersPhoneNumber) {
		ArrayList<UserActivityInformation> userActivityInformationList = new ArrayList<UserActivityInformation>();

		othersPhoneNumber = CommonTask
				.trimTentativeCountryCode(othersPhoneNumber);

		String query = "Select ActivityTypeID,ActivityDetails,ActivityTime,Number from (SELECT 1 as ActivityTypeID,Number as ActivityDetails,CallTime as ActivityTime,Number FROM PhoneCallInformation union all SELECT 2 as ActivityTypeID,SMSBody as ActivityDetails,SMSTime as ActivityTime,Number FROM PhoneSMSInformation) as a "
				+ "where a.number like '%"
				+ othersPhoneNumber
				+ "%' order by ActivityTime desc limit 3";
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			UserActivityInformation userActivityInformation = null;
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				userActivityInformation = new UserActivityInformation();
				userActivityInformation.ActivityTypeID = c.getInt(0);
				userActivityInformation.ActivityDetails = c.getString(1);
				userActivityInformation.ActivityTime = c.getString(2);
				userActivityInformation.Number = c.getString(3);
				userActivityInformationList.add(userActivityInformation);
				c.moveToNext();
			}
		}
		return userActivityInformationList;
	}

	public ArrayList<UserLocationActivityInformation> getTotalCallAndSMSInforByNumber(
			int type, String number) {
		ArrayList<UserLocationActivityInformation> userLocationActivityInformationList = new ArrayList<UserLocationActivityInformation>();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		// /
		number = CommonTask.trimTentativeCountryCode(number);
		// /
		String sql = "Select ActivityTypeID,Latitude,Longitude,ActivityTime,Number,ActivityType from (SELECT 1 as ActivityTypeID,Latitude,Longitude,CallTime as ActivityTime,Number,CallType as ActivityType FROM PhoneCallInformation union all SELECT 2 as ActivityTypeID,Latitude,Longitude,SMSTime as ActivityTime,Number,SMSType AS ActivityType FROM PhoneSMSInformation) as a ";
		if (type == CommonConstraints.INFO_TYPE_ALL) {
			sql = sql + " WHERE a.Number like '%" + number + "%'";
		} else if (type == CommonConstraints.INFO_TYPE_TODAY) {
			cal.set(Calendar.HOUR, 0);
			// sql=sql+" WHERE CallTime >="+cal.getTimeInMillis()+" AND Number='"+number+"'";
			sql = sql + " WHERE a.ActivityTime >=" + cal.getTimeInMillis()
					+ " AND a.Number like '%" + number + "%'";
		} else if (type == CommonConstraints.INFO_TYPE_WEEK) {
			cal.set(Calendar.HOUR, 0);
			cal.add(Calendar.DAY_OF_MONTH, -7);
			// sql=sql+" WHERE CallTime >="+cal.getTimeInMillis()+" AND Number='"+number+"'";
			sql = sql + " WHERE a.ActivityTime >=" + cal.getTimeInMillis()
					+ " AND a.Number like '%" + number + "%'";
		} else if (type == CommonConstraints.INFO_TYPE_MONTH) {
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			// sql=sql+" WHERE CallTime >="+cal.getTimeInMillis()+" AND Number='"+number+"'";
			sql = sql + " WHERE a.ActivityTime >=" + cal.getTimeInMillis()
					+ " AND a.Number like '%" + number + "%'";
		}
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			UserLocationActivityInformation userLocationActivityInformation = null;

			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				userLocationActivityInformation = new UserLocationActivityInformation();
				userLocationActivityInformation.ActivityTypeID = c.getInt(0);
				userLocationActivityInformation.Latitude = c.getDouble(1);
				userLocationActivityInformation.Longitude = c.getDouble(2);
				userLocationActivityInformation.ActivityTime = c.getString(3);
				userLocationActivityInformation.Number = c.getString(4);
				userLocationActivityInformation.ActivityType = c.getString(5);
				userLocationActivityInformationList
						.add(userLocationActivityInformation);
				c.moveToNext();
			}
		}
		return userLocationActivityInformationList;
	}

	public void updateReportProblemAndBadExperience() {
		ContentValues contentValues = new ContentValues();
		contentValues.put("IsSync", 1);
		mynetDatabase.update("ReportProblemAndBadExperience", contentValues,
				null, null);
	}

	public void updateFacebookPerson(int issync) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("IsSync", issync);
		mynetDatabase.update("Facebook_Person", contentValues, null, null);
	}

	public void deleteReportProblemAndBadExperience() {
		mynetDatabase.execSQL("delete from ReportProblemAndBadExperience");
	}

	public void deleteLocalUserGroup(int groupID, String commaSeperateUserID) {
		mynetDatabase.execSQL("delete from LocalUserGroup where groupID="
				+ groupID + " and UserID in(" + commaSeperateUserID + ")");
	}

	public ArrayList<PhoneCallInformation> getAllCallInformationByCallMemo() {
		ArrayList<PhoneCallInformation> callList = new ArrayList<PhoneCallInformation>();
		String query = "SELECT * FROM PhoneCallInformation where TextCallMemo !='' order by CallId desc";
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			PhoneCallInformation phoneCallInformation = null;
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneCallInformation = new PhoneCallInformation();
				phoneCallInformation.CallId = c.getInt(0);
				phoneCallInformation.PhoneId = c.getInt(1);
				phoneCallInformation.CallType = c.getInt(2);
				phoneCallInformation.Number = c.getString(3);
				phoneCallInformation.DurationInSec = c.getInt(2) == 3 ? 0 : c
						.getInt(4);
				phoneCallInformation.Latitude = c.getDouble(5);
				phoneCallInformation.Longitude = c.getDouble(6);
				phoneCallInformation.TextCallMemo = c.getString(7);
				phoneCallInformation.CallTime = new Date(c.getLong(8));
				phoneCallInformation.VoiceRecordPath = c.getString(13);
				phoneCallInformation.Reson = c.getString(10);
				callList.add(phoneCallInformation);
				c.moveToNext();
			}
		}
		return callList;
	}

	public Cursor getAllCaLLMemo() {
		String query = "SELECT CallId as _id, Number,TextCallMemo,VoiceRecordPath,CallTime,LocationName FROM PhoneCallInformation where TextCallMemo !='' order by CallTime desc";
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null)
			c.moveToFirst();
		return c;
	}

	public long createCollaboration(Collaboration collaboration) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("MsgFrom", collaboration.MsgFrom);
		contentValues.put("MsgText", collaboration.MsgText);
		contentValues.put("Latitude", collaboration.Latitude);
		contentValues.put("Longitude", collaboration.Longitude);
		contentValues.put("GroupID", collaboration.GroupID);
		contentValues.put("MsgTo", collaboration.MsgTo);
		contentValues.put("PostedDate",
				CommonTask.convertDatetoLong(collaboration.PostedDate));
		contentValues.put("isRead", 0);
		contentValues.put("isAttachment", 0);
		contentValues.put("FilePath", collaboration.FilePath);
		contentValues.put("MsgFromName", collaboration.MsgFromName);
		contentValues.put("GroupName", collaboration.GroupName);
		return mynetDatabase.insertOrThrow("LiveCollaboration", null,
				contentValues);
	}

	public Collaborations GetLiveCollaborationsByMsgTo(int id, String type,
			int limit) {
		String query = "SELECT * FROM LiveCollaboration ";
		if (type.equals("U"))
			query = query + "where GroupID=0 and (MsgFrom =" + id + " or MsgTo=" + id+")";
		else
			query = query + "where GroupID =" + id;
		/*
		 * if(longLastMessageTime>0)
		 * query=query+" and PostedDate >"+longLastMessageTime;
		 */

		//query = query + " order by PostedDate asc LIMIT " + limit;
		query = query + " order by PostedDate asc ";
		Cursor c = mynetDatabase.rawQuery(query, null);

		List<Collaboration> collaborationList = null;
		if (c != null && c.getCount() > 0) {
			collaborationList = new ArrayList<Collaboration>();
			c.moveToFirst();
			Collaboration collaboration = null;
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				collaboration = new Collaboration();
				collaboration.MsgFrom = c.getInt(1);
				collaboration.MsgText = c.getString(2);
				collaboration.GroupID = c.getInt(3);
				collaboration.MsgTo = c.getInt(4);
				collaboration.PostedDate = String.valueOf(c.getLong(5));
				collaboration.Latitude = c.getDouble(8);
				collaboration.Longitude = c.getDouble(9);
				collaboration.FilePath = c.getString(10);
				collaboration.MsgFromName = c.getString(11);
				collaboration.GroupName = c.getString(12);
				collaborationList.add(collaboration);
				c.moveToNext();
			}
		}
		Collaborations collaborations = new Collaborations();
		collaborations.collaborationList = collaborationList;
		return collaborations;
	}
	
	
	public void GetIMbyId(int id, String type) {
		String query = "SELECT * FROM LiveCollaboration ";
		if (type.equals("U"))
			query = query + "where GroupID=0 and (MsgFrom =" + id + " or MsgTo=" + id+")";
		else
			query = query + "where GroupID =" + id;		
		query = query + " order by PostedDate asc ";
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {
			Intent i =null;
			c.moveToFirst();
			Collaboration collaboration = null;
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				collaboration = new Collaboration();
				collaboration.MsgFrom = c.getInt(1);
				collaboration.MsgText = c.getString(2);
				collaboration.GroupID = c.getInt(3);
				collaboration.MsgTo = c.getInt(4);
				collaboration.PostedDate = String.valueOf(c.getLong(5));
				collaboration.Latitude = c.getDouble(8);
				collaboration.Longitude = c.getDouble(9);
				collaboration.FilePath = c.getString(10);
				collaboration.MsgFromName = c.getString(11);
				collaboration.GroupName = c.getString(12);
				c.moveToNext();
				i = new Intent(CommonValues.getInstance().XmppReceiveMessageIntent);
				
				i.putExtra("msg", collaboration);			
						
				context.sendBroadcast(i);
			}
		}
	}

	public UserGroupUnions GetLiveCollaborationsHistory() {
		UserGroupUnions userGroupUnions = new UserGroupUnions();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.add(Calendar.DAY_OF_MONTH, -7);
		// String query =
		// "SELECT distinct MsgFrom,GroupID,MsgTo,MsgFromName FROM LiveCollaboration PostedDate>"+cal.getTimeInMillis()
		// +" and (MsgFrom!="+CommonValues.getInstance().LoginUser.UserNumber+" or MsgTo!="+CommonValues.getInstance().LoginUser.UserNumber+")";
		String query = "select c.MsgFrom,c.MsgTo,c.GroupID,c.MsgText,c.PostedDate,c.MsgFromName,c.GroupName from LiveCollaboration c,"
				+ "(select Max(msgid) as MsgId from LiveCollaboration  where MsgFrom="
				+ CommonValues.getInstance().LoginUser.UserNumber+" and MsgTo>0"
				+ " group by MsgTo"
				  +" union " +
				  "select Max(msgid) as MsgId from LiveCollaboration  where MsgTo="
				  +CommonValues.getInstance().LoginUser.UserNumber+
				  " group by MsgFrom"
				+ " union "
				+ "select Max(msgid) as MsgId from LiveCollaboration where GroupID>0 group by GroupID) t "
				+ "where c.MsgID=t.MsgId and c.PostedDate>"
				+ cal.getTimeInMillis() + " order by c.PostedDate desc";
		Cursor c = mynetDatabase.rawQuery(query, null);
		List<UserGroupUnion> collaborationList = null;
		if (c != null && c.getCount() > 0) {
			collaborationList = new ArrayList<UserGroupUnion>();
			c.moveToFirst();
			UserGroupUnion collaboration = null;
			boolean isAdded=false;
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				collaboration = new UserGroupUnion();
				if (c.getInt(2) > 0) {
					collaboration.ID = c.getInt(2);
					collaboration.Type = "G";
					collaboration.Name = c.getString(6);
				} else {
					collaboration.ID = c.getInt(0) != CommonValues
							.getInstance().LoginUser.UserNumber ? c.getInt(0)
							: c.getInt(1);
					collaboration.Type = "U";
					collaboration.Name = c.getString(5);
					//collaboration.userOnlinestatus = CommonTask.TryParseInt(String.valueOf(c.getInt(7)));
				}

				collaboration.LastMessage = c.getString(3);
				collaboration.PostedDate = String.valueOf(c.getLong(4));
				isAdded=false;
				for (UserGroupUnion userGroupUnion : collaborationList) {
					if((userGroupUnion.Type.equals("U") && userGroupUnion.ID==collaboration.ID)||(userGroupUnion.Type.equals("G") && userGroupUnion.ID==collaboration.ID)){
						isAdded=true;
						break;
					}
				}
				if(!isAdded)
					collaborationList.add(collaboration);
				c.moveToNext();
			}
		}
		userGroupUnions.userGroupUnionList = collaborationList;

		return userGroupUnions;
	}

	public PhoneCallInformation getLatestPhoneCallInfo(String number) {
		number = CommonTask.trimTentativeCountryCode(number);
		String query = "SELECT * FROM PhoneCallInformation where number like'%"
				+ number + "%' order by CallTime desc limit 1";
		Cursor c = mynetDatabase.rawQuery(query, null);
		PhoneCallInformation phoneCallInformation = null;
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneCallInformation = new PhoneCallInformation();
				phoneCallInformation.CallId = c.getInt(0);
				phoneCallInformation.PhoneId = c.getInt(1);
				phoneCallInformation.CallType = c.getInt(2);
				phoneCallInformation.Number = c.getString(3);
				phoneCallInformation.DurationInSec = c.getInt(2) == 3 ? 0 : c
						.getInt(4);
				phoneCallInformation.Latitude = c.getDouble(5);
				phoneCallInformation.Longitude = c.getDouble(6);
				phoneCallInformation.TextCallMemo = c.getString(7);
				phoneCallInformation.CallTime = new Date(c.getLong(8));
				phoneCallInformation.VoiceRecordPath = c.getString(13);
				phoneCallInformation.Reson = c.getString(10);
				c.moveToNext();
			}
		}
		return phoneCallInformation;
	}

	public PhoneSMSInformation getLatestPhoneSMSInfo(String number) {
		number = CommonTask.trimTentativeCountryCode(number);
		String sql = "select * from PhoneSMSInformation where number like'%"
				+ number + "%' order by SMSTime  desc limit 1";
		PhoneSMSInformation phoneSmsInformation = null;
		Cursor c = mynetDatabase.rawQuery(sql, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				phoneSmsInformation = new PhoneSMSInformation();
				phoneSmsInformation.SMSId = c.getInt(0);
				phoneSmsInformation.PhoneId = c.getInt(1);
				phoneSmsInformation.SMSType = c.getInt(2);
				phoneSmsInformation.Number = c.getString(3);
				phoneSmsInformation.SMSBody = c.getString(4);
				phoneSmsInformation.Latitude = c.getDouble(5);
				phoneSmsInformation.Longitude = c.getDouble(6);
				phoneSmsInformation.SMSTime = new Date(c.getLong(7));
				phoneSmsInformation.LAC = c.getString(10);
				phoneSmsInformation.CellID = c.getString(11);
				phoneSmsInformation.IsLocal = c.getInt(14) > 0;
				phoneSmsInformation.LocationName = c.getString(15);
				c.moveToNext();
			}
		}
		return phoneSmsInformation;
	}

	public void deleteFacebook_Qualification_Experience() {
		mynetDatabase.execSQL("delete FROM Facebook_Qualification_Experience");
	}

	public long createUser(LocalUser localUserGroup) {
		String query = "";
		ContentValues contentValues = new ContentValues();
		contentValues.put("UserName", localUserGroup.UserName);
		contentValues.put("ReffId", localUserGroup.ReffId);
		contentValues.put("IsFriend", localUserGroup.IsFriend);
		contentValues.put("MobileNumber", localUserGroup.MobileNumber);
		contentValues.put("UserOnlinestatus", localUserGroup.UserOnlinestatus);
		contentValues.put("isFavourite", localUserGroup.isFavourite);
		query = "SELECT UserID FROM LocalUserInformation where ReffId="
				+ localUserGroup.ReffId;
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			mynetDatabase.update("LocalUserInformation", contentValues,
					" UserID=" + c.getInt(0), null);

		} else {
			return mynetDatabase.insertOrThrow("LocalUserInformation", null,
					contentValues);
		}
		return 0;
	}

	public long updateFavourite(int reffId, boolean isFeb) {
		String query = "";
		ContentValues contentValues = new ContentValues();
		contentValues.put("isFavourite", isFeb ? 1 : 0);
		query = "SELECT UserID FROM LocalUserInformation where ReffId="
				+ reffId;
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			mynetDatabase.update("LocalUserInformation", contentValues,
					" UserID=" + c.getInt(0), null);

		} else {
			return mynetDatabase.insertOrThrow("LocalUserInformation", null,
					contentValues);
		}
		return 0;
	}

	public int getUserId(int reffId) {
		String query = "";
		query = "SELECT UserID FROM LocalUserInformation where ReffId="
				+ reffId;
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getInt(0);
		}
		return 0;
	}

	public long createGroup(String name, int reffId) {
		String query = "";
		ContentValues contentValues = new ContentValues();
		contentValues.put("GroupName", name);
		contentValues.put("ReffId", reffId);
		query = "SELECT GroupID FROM LocalGroupInformation where ReffId="
				+ reffId;
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			mynetDatabase.update("LocalGroupInformation", contentValues,
					" GroupID=" + c.getInt(0), null);

		} else {
			return mynetDatabase.insertOrThrow("LocalGroupInformation", null,
					contentValues);
		}
		return 0;
	}

	public long createUserGroup(int groupID, int userId) {
		String query = "";
		ContentValues contentValues = new ContentValues();
		contentValues.put("GroupID", groupID);
		contentValues.put("UserId", userId);
		query = "SELECT GroupID FROM LocalUserGroup where GroupID=" + groupID
				+ " and UserId=" + userId;
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {

		} else {
			return mynetDatabase.insertOrThrow("LocalUserGroup", null,
					contentValues);
		}
		return 0;
	}

	public String getGroupName(int groupID) {
		String query = "";
		query = "SELECT GroupName FROM LocalGroupInformation where ReffId="
				+ groupID;
		Cursor c = mynetDatabase.rawQuery(query, null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
			return c.getString(0);
		}
		return "";
	}

	public UserGroupUnions GetUserGroupList() {
		UserGroupUnions userGroupUnions = new UserGroupUnions();
		String query = "SELECT GroupName,ReffId FROM LocalGroupInformation order by GroupName";
		Cursor c = mynetDatabase.rawQuery(query, null);
		List<UserGroupUnion> collaborationList = null;
		UserGroupUnion userGroupUnion = null;
		if (c != null && c.getCount() > 0) {
			collaborationList = new ArrayList<UserGroupUnion>();
			c.moveToFirst();
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				userGroupUnion = new UserGroupUnion();
				userGroupUnion.ID = c.getInt(1);
				userGroupUnion.Type = "G";
				userGroupUnion.Name = c.getString(0);
				collaborationList.add(userGroupUnion);
				c.moveToNext();
			}
		}

		query = "SELECT MobileNumber,ReffId,isFavourite,UserOnlinestatus FROM LocalUserInformation where IsFriend=1 order by MobileNumber";
		c = mynetDatabase.rawQuery(query, null);

		if (c != null && c.getCount() > 0) {
			if (collaborationList == null)
				collaborationList = new ArrayList<UserGroupUnion>();
			c.moveToFirst();
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				userGroupUnion = new UserGroupUnion();
				userGroupUnion.ID = c.getInt(1);
				userGroupUnion.Type = "U";
				userGroupUnion.Name = c.getString(0);
				userGroupUnion.isFavourite = c.getInt(2) == 1 ? true : false;
				userGroupUnion.userOnlinestatus = c.getInt(3);
				collaborationList.add(userGroupUnion);
				c.moveToNext();
			}
		}

		userGroupUnions.userGroupUnionList = collaborationList;
		return userGroupUnions;
	}
	
	public List<Group> GetGroupList() {
		
		String query = "SELECT GroupName,ReffId FROM LocalGroupInformation order by GroupName";
		Cursor c = mynetDatabase.rawQuery(query, null);
		List<Group> list = null;
		Group group = null;
		if (c != null && c.getCount() > 0) {
			list = new ArrayList<Group>();
			c.moveToFirst();
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				group = new Group();
				group.GroupID = c.getInt(1);
				group.Name = c.getString(0);
				list.add(group);
				c.moveToNext();
			}
		}	
		return list;
	}

	public UserGroupUnions GetUserLists() {
		UserGroupUnions userGroupUnions = new UserGroupUnions();
		String query = "SELECT MobileNumber,ReffId,isFavourite,UserOnlinestatus FROM LocalUserInformation where IsFriend=1 order by MobileNumber";
		Cursor c = mynetDatabase.rawQuery(query, null);
		List<UserGroupUnion> collaborationList = null;
		UserGroupUnion userGroupUnion = null;
		if (c != null && c.getCount() > 0) {
			if (collaborationList == null)
				collaborationList = new ArrayList<UserGroupUnion>();
			c.moveToFirst();
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				userGroupUnion = new UserGroupUnion();
				userGroupUnion.ID = c.getInt(1);
				userGroupUnion.Type = "U";
				userGroupUnion.Name = c.getString(0);
				userGroupUnion.isFavourite = c.getInt(2) == 1 ? true : false;
				userGroupUnion.userOnlinestatus = c.getInt(3);
				collaborationList.add(userGroupUnion);
				c.moveToNext();
			}
		}
		userGroupUnions.userGroupUnionList = collaborationList;
		return userGroupUnions;
	}

	public UserFamilyMembers GetFebUserList() {
		UserFamilyMembers userGroupUnions = new UserFamilyMembers();
		String query = "SELECT MobileNumber,ReffId,UserOnlinestatus FROM LocalUserInformation where IsFriend=1 and isFavourite=1 order by MobileNumber";
		Cursor c = mynetDatabase.rawQuery(query, null);
		List<UserFamilyMember> collaborationList = null;
		UserFamilyMember userGroupUnion = null;
		if (c != null && c.getCount() > 0) {
			collaborationList = new ArrayList<UserFamilyMember>();
			c.moveToFirst();
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				userGroupUnion = new UserFamilyMember();
				userGroupUnion.UserNumber = c.getInt(1);
				userGroupUnion.Name = c.getString(0);
				userGroupUnion.OnlineStatus = c.getInt(2);
				collaborationList.add(userGroupUnion);
				c.moveToNext();
			}
		}

		userGroupUnions.userFamilyMemberList = collaborationList;
		return userGroupUnions;
	}

	public UserGroupUnions GetGroupMember(int groupId) {
		UserGroupUnions userGroupUnions = new UserGroupUnions();
		String query = "SELECT MobileNumber,ReffId,UserOnlinestatus FROM LocalUserInformation u,(SELECT UserId FROM LocalUserGroup where GroupID="
				+ groupId
				+ ") t where u.ReffId=t.UserId order by u.MobileNumber";
		Cursor c = mynetDatabase.rawQuery(query, null);
		List<UserGroupUnion> collaborationList = null;
		UserGroupUnion userGroupUnion = null;
		if (c != null && c.getCount() > 0) {
			collaborationList = new ArrayList<UserGroupUnion>();
			c.moveToFirst();
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				userGroupUnion = new UserGroupUnion();
				userGroupUnion.ID = c.getInt(1);
				userGroupUnion.Type = "U";
				userGroupUnion.Name = c.getString(0);
				userGroupUnion.userOnlinestatus = c.getInt(1);
				collaborationList.add(userGroupUnion);
				c.moveToNext();
			}
		}
		userGroupUnions.userGroupUnionList = collaborationList;
		return userGroupUnions;
	}
	
	public HashMap<Integer, ContactUser> GetUserHashMap() {
		String query = "SELECT MobileNumber,ReffId,UserOnlinestatus FROM LocalUserInformation  order by MobileNumber";
		Cursor c = mynetDatabase.rawQuery(query, null);
		
		HashMap<Integer, ContactUser> contactList=null;
		ContactUser contactUser=null;
		if (c != null && c.getCount() > 0) {
			contactList = new HashMap<Integer, ContactUser>();
			c.moveToFirst();
			ContentResolver cr = context.getContentResolver();
			Cursor cursor =null;
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {				
				contactUser = new ContactUser();
				contactUser.ID = c.getInt(1);
				contactUser.PhoneNumber = c.getString(0);
				contactUser.Name = "Unknown("+c.getString(0)+")";
				cursor = cr.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, " replace(data1,' ', '') like '%" + contactUser.PhoneNumber.substring(3) +"%'", null, null);
				
				if(cursor.getCount() > 0){
					cursor.moveToFirst();
					contactUser.Name= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					contactUser.Image=CommonTask.fetchContactImageThumbnail(context,cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID)));					
				}
				cursor.close();
				contactList.put(contactUser.ID,contactUser);
				c.moveToNext();
			}
			contactUser = new ContactUser();
			contactUser.ID =CommonValues.getInstance().LoginUser.UserNumber;
			contactUser.PhoneNumber = CommonValues.getInstance().LoginUser.Mobile;
			contactUser.Name = "Unknown("+CommonValues.getInstance().LoginUser.Mobile+")";
			cursor = cr.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, " replace(data1,' ', '') like '%" + CommonValues.getInstance().LoginUser.Mobile.substring(3) +"%'", null, null);
			
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				contactUser.Name= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				contactUser.Image=CommonTask.fetchContactImageThumbnail(context,cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID)));					
			}
			cursor.close();
			contactList.put(contactUser.ID,contactUser);
		}
		c.close();		
		return contactList;
	}

	public UserGroupUnions GetUserList() {
		UserGroupUnions userGroupUnions = new UserGroupUnions();
		String query = "SELECT MobileNumber,ReffId,UserOnlinestatus FROM LocalUserInformation u where u.IsFriend=1 order by u.MobileNumber";
		Cursor c = mynetDatabase.rawQuery(query, null);
		List<UserGroupUnion> collaborationList = null;
		UserGroupUnion userGroupUnion = null;
		if (c != null && c.getCount() > 0) {
			collaborationList = new ArrayList<UserGroupUnion>();
			c.moveToFirst();
			for (int rowIndex = 0; rowIndex < c.getCount(); rowIndex++) {
				userGroupUnion = new UserGroupUnion();
				userGroupUnion.ID = c.getInt(1);
				userGroupUnion.Type = "U";
				userGroupUnion.Name = c.getString(0);
				userGroupUnion.userOnlinestatus = c.getInt(1);
				collaborationList.add(userGroupUnion);
				c.moveToNext();
			}
		}
		userGroupUnions.userGroupUnionList = collaborationList;
		return userGroupUnions;
	}
}
