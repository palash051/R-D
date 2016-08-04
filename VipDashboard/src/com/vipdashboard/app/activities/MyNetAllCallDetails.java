package com.vipdashboard.app.activities;

import java.util.ArrayList;
import com.vipdashboard.app.classes.EndlessScrollListener;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.AllCallSMSAdapter;
import com.vipdashboard.app.adapter.ContactListAdapter;
import com.vipdashboard.app.adapter.DialerCallInformationAdapter;
import com.vipdashboard.app.adapter.LiveFeedCallMemoAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.classes.CallMemoItemDetails;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;

public class MyNetAllCallDetails extends MainActionbarBase implements  OnClickListener, OnItemClickListener, OnQueryTextListener,OnContactSelectedListener{
	ListView callLogList,smsLogList,call_memo_list;
	boolean bsms, brecentCall, bContactList, bCallMemo,search_sms, search_contact,search_recentcall;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	
	LiveFeedCallMemoAdapter callMemoAdapter;
	AllCallSMSAdapter smsAdapter;
	DialerCallInformationAdapter callAdapter;
	ContactListAdapter contactListadapter;
	Bitmap bitmap;
	RelativeLayout rlCallLog, rlMessage, rlCallMemo, rlContactList;
	AutoCompleteTextView search_text;
	SearchView svAllCall;
	CallMemoItemDetails callMemoItemDetails;
	int pageIndexCalllog=0,pageIndexSMS=0,pageIndexCallMemo=0,pageIndexContact=0;
	String LastSelectedAction="";    
    EndlessScrollListener callendlessScrollListener, smslendlessScrollListener,contactendlessScrollListener;
    TextView tvUpdate,tvProcess;
    RelativeLayout rlProgressbar;
    ProgressBar pb;
    ContentResolver cr;
    Cursor cursor;
    String[] projection;
    ArrayList<PhoneCallInformation> callInformation;
    ArrayList<PhoneSMSInformation> smsInformation;
    public int existingTotalContactNo=0;
	public int recentTotalContactNo=-1;
    boolean isCallFromSearchItem, isCallFromCallLog, isCallFromSMSInbox;
    public static ArrayList<PhoneCallInformation> callLogSearch;
    public static ArrayList<PhoneSMSInformation> smsLogSearch;
    FrameLayout frameLayout;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_call_detatils);
		init();
		search_text = (AutoCompleteTextView) svAllCall.findViewById(svAllCall.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
		search_text.setTextColor(Color.WHITE);
		search_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);

		//LoadInformation();
	}
	private void init() {
		rlCallLog = (RelativeLayout) findViewById(R.id.rlCallLog);
		rlMessage = (RelativeLayout) findViewById(R.id.rlMessage);
		rlCallMemo = (RelativeLayout) findViewById(R.id.rlCallMemo);
		rlContactList = (RelativeLayout) findViewById(R.id.rlContactList);
		callLogList = (ListView) findViewById(R.id.call_details_list);
		smsLogList = (ListView) findViewById(R.id.sms_details_list);
		//contactList = (ListView) findViewById(R.id.contact_details_list);
		call_memo_list =(ListView) findViewById(R.id.call_memo_list);
		svAllCall = (SearchView) findViewById(R.id.svAllCall);
		tvUpdate = (TextView)findViewById(R.id.tvUpdate);
		tvProcess = (TextView)findViewById(R.id.tvProcess);
		frameLayout= (FrameLayout)findViewById(R.id.fragment_container);
		frameLayout.setVisibility(View.GONE);
	    //rlProgressbar = (RelativeLayout)findViewById(R.id.rlProgressbar);
	   // pb = (ProgressBar)findViewById(R.id.pb);
		
		callLogList.setOnItemClickListener(this);
		smsLogList.setOnItemClickListener(this);
		//contactList.setOnItemClickListener(this);
		call_memo_list.setOnItemClickListener(this);
		svAllCall.setOnQueryTextListener(this);
		rlCallLog.setOnClickListener(this);
		rlMessage.setOnClickListener(this);
		rlCallMemo.setOnClickListener(this);
		rlContactList.setOnClickListener(this);
		
		projection = new String[] {
				ContactsContract.CommonDataKinds.Phone._ID,
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID
                };
		
		callendlessScrollListener=new EndlessScrollListener() {
	        @Override
	        public void onLoadMore(int page, int totalItemsCount) {
	        	pageIndexCalllog++;
        		tvUpdate.setVisibility(View.VISIBLE);
	        	//LoadInformation();
        		BindCallLog();
	        }
	    };
    	
	    smslendlessScrollListener=new EndlessScrollListener() {
	        @Override
	        public void onLoadMore(int page, int totalItemsCount) {	        	
	        	pageIndexSMS++;
        		tvUpdate.setVisibility(View.VISIBLE);
        		//LoadInformation();
        		BindSMSInbox();
	        }
	    };
	    /*contactendlessScrollListener = new EndlessScrollListener() {
	        @Override
	        public void onLoadMore(int page, int totalItemsCount) {	        	
	        	pageIndexContact++;
        		tvUpdate.setVisibility(View.VISIBLE);
        		//LoadInformation();
        		BindContactList();
	        }
	    };*/
	    
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
	}

	@Override
	protected void onResume() {		
		MyNetApplication.activityResumed();
		super.onResume();
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
		//pageIndexCalllog=pageIndexSMS=pageIndexCallMemo=pageIndexContact=0;
		
		pageIndexCalllog=pageIndexSMS=pageIndexCallMemo=pageIndexContact=1;
		callInformation = new ArrayList<PhoneCallInformation>();
		smsInformation = new ArrayList<PhoneSMSInformation>();
		registerLitsener();
		arrengeRecentCall();
	}
	
	private void registerLitsener() {
		callLogList.setOnScrollListener(callendlessScrollListener);
		smsLogList.setOnScrollListener(smslendlessScrollListener);
	}
	
	@Override
	public void onClick(View view) {
		
		String searchToken="";
		
		svAllCall.setQuery(searchToken, true);
		
		if(view.getId() == R.id.rlCallLog){
			arrengeRecentCall();
		}
		else if(view.getId() == R.id.rlMessage){
			arrengeSMS();
		}
		else if(view.getId() == R.id.rlCallMemo){
			arrengeCallMemo();
		}
		else if(view.getId() == R.id.rlContactList){
			arrengeContactlist();
		}
	}
	
	private void arrengeCallMemo(){
		// get boolean for individual list
		svAllCall.setVisibility(View.VISIBLE);
		bCallMemo = true;
		bsms = search_sms = brecentCall = bContactList = search_contact = search_recentcall = false;
		//set selection color
		rlCallLog.setBackgroundColor(Color.parseColor("#000099"));
		rlMessage.setBackgroundColor(Color.parseColor("#00B050"));
		rlCallMemo.setBackgroundColor(Color.parseColor("#FFC000"));
		rlContactList.setBackgroundColor(Color.parseColor("#C00000"));
		//visiable or invisiable list
		smsLogList.setVisibility(View.GONE);
		callLogList.setVisibility(View.GONE);
		frameLayout.setVisibility(FrameLayout.GONE);
		call_memo_list.setVisibility(View.VISIBLE);
		//call async process
		//LoadInformation();
	}
	
	private void arrengeSMS() {
		//init page value
		//pageIndexSMS = 0;
		//get boolean for individual process
		svAllCall.setVisibility(View.VISIBLE);
		bsms = search_sms = true;
		bCallMemo = brecentCall = bContactList = search_contact = search_recentcall = false;
		//set selection color
		rlCallLog.setBackgroundColor(Color.parseColor("#000099"));
		rlMessage.setBackgroundColor(Color.parseColor("#FFC000"));
		rlCallMemo.setBackgroundColor(Color.parseColor("#9900FF"));
		rlContactList.setBackgroundColor(Color.parseColor("#C00000"));
		//visiable and invisiable list
		smsLogList.setVisibility(View.VISIBLE);
		callLogList.setVisibility(View.GONE);
		frameLayout.setVisibility(FrameLayout.GONE);
		call_memo_list.setVisibility(View.GONE);
		//call asynac process
		//LoadInformation();
		BindSMSInbox();
	}
	
	private void arrengeRecentCall() {
		//init page value
		//pageIndexCalllog = 0;
		//get boolean for individual process
		svAllCall.setVisibility(View.VISIBLE);
		brecentCall = search_recentcall = true;
		bCallMemo = bsms = bContactList = search_sms = search_contact = false;
		//set selection color
		rlCallLog.setBackgroundColor(Color.parseColor("#FFC000"));
		rlMessage.setBackgroundColor(Color.parseColor("#00B050"));
		rlCallMemo.setBackgroundColor(Color.parseColor("#9900FF"));
		rlContactList.setBackgroundColor(Color.parseColor("#C00000"));
		//visiable and invisiable list
		callLogList.setVisibility(View.VISIBLE);
		smsLogList.setVisibility(View.GONE);
		frameLayout.setVisibility(FrameLayout.GONE);
		call_memo_list.setVisibility(View.GONE);
		//call async process for calllog
		//LoadInformation();
		BindCallLog();
	}
	private void arrengeContactlist() {
		//get boolean for individual process
		svAllCall.setVisibility(View.GONE);
		bContactList = search_contact = true;
		bCallMemo = brecentCall = bsms = search_sms = search_recentcall = false;
		//set selection color
		rlCallLog.setBackgroundColor(Color.parseColor("#000099"));
		rlMessage.setBackgroundColor(Color.parseColor("#00B050"));
		rlCallMemo.setBackgroundColor(Color.parseColor("#9900FF"));
		rlContactList.setBackgroundColor(Color.parseColor("#FFC000"));
		//visiable and invisiable list
		smsLogList.setVisibility(View.GONE);
		callLogList.setVisibility(View.GONE);
		frameLayout.setVisibility(FrameLayout.VISIBLE);
		call_memo_list.setVisibility(View.GONE);
		//call async process for contactlist
		//LoadInformation();
		//bind data into view
		BindContactList();
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		Object object = view.getTag();
		if(bCallMemo){
			PhoneCallInformation callInformation = (PhoneCallInformation) object;
			if(callMemoItemDetails == null)
				callMemoItemDetails = new CallMemoItemDetails(this);
			callMemoItemDetails.showCallMemoItemDetails(callInformation);
		}else{
			Intent intent = new Intent(this, MakeCallActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			MakeCallActivity.object = object;
			startActivity(intent);
		}		
	}
	
	@Override
	public boolean onQueryTextChange(String value) {
		if(search_recentcall){
			callAdapter.CallFilter(value);
		}else if(search_sms){
			smsAdapter.SMSFilter(value);
		}
		return false;
	}
	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}
	
	private void BindCallLog(){
		MyNetDatabase db = new MyNetDatabase(this);
		db.open();
		callInformation = db.getCallInfoListByPageIndex(pageIndexCalllog,callInformation);
		db.close();
		
		tvUpdate.setVisibility(View.GONE);
		if(callAdapter==null){
			callAdapter = new DialerCallInformationAdapter(this,R.layout.call_dialer_item, callInformation);
			callLogList.setAdapter(callAdapter);
		    }else{
		    	if(callInformation.size()<=40)
		    	{
		    		callAdapter = new DialerCallInformationAdapter(this,R.layout.call_dialer_item, callInformation);
					callLogList.setAdapter(callAdapter);
		    	}
		    	callAdapter.notifyDataSetChanged();
		    }
		 tvUpdate.setVisibility(View.GONE);
		
	}
	
	private void BindSMSInbox() {
		MyNetDatabase db = new MyNetDatabase(this);
		db.open();
		smsInformation = db.getSMSInfoListByPageIndex(pageIndexSMS,smsInformation,false);
		db.close();
		
		
		/*if(!isCallFromSMSInbox){
			smsLogList.setSelection(0);
			isCallFromSMSInbox = true;
		}else{
			smsLogList.setSelection(smsAdapter.getCount()-20);
		}*/
		 if(smsAdapter==null){
			 smsAdapter = new AllCallSMSAdapter(this, R.layout.all_call_sms,smsInformation);
				smsLogList.setAdapter(smsAdapter);
		    }else{
		    	if(smsInformation.size()<=40)
		    	{
		    		smsAdapter = new AllCallSMSAdapter(this, R.layout.all_call_sms,smsInformation);
					smsLogList.setAdapter(smsAdapter);
		    	}
		    	smsAdapter.notifyDataSetChanged();
		    }
		 tvUpdate.setVisibility(View.GONE);
	}
	
	private void BindContactList(){	
		if(!isCallFromSearchItem){
			//isCallFromSearchItem = true;
			FragmentManager fragmentManager = this.getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			ContactsListFragment fragment = new ContactsListFragment();
			fragmentTransaction.add(R.id.fragment_container, fragment);
			fragmentTransaction.commit();
		}
	}
	@Override
	public void onContactNameSelected(long contactId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onContactNumberSelected(String contactNumber, String contactName) {
		// TODO Auto-generated method stub
		
	}
}
