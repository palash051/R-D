package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.AllCallSMSAdapter;
import com.vipdashboard.app.adapter.ContactListAdapter;
import com.vipdashboard.app.adapter.DialerCallInformationAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.classes.ContactList;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;

public class MyNetDialerActivity extends MainActionbarBase implements
		OnClickListener, OnLongClickListener, OnItemClickListener {
	TextView tvExperinceDialer1, tvExperinceDialer2, tvExperinceDialer3,
			tvExperinceDialer4, tvExperinceDialer5, tvExperinceDialer6,
			tvExperinceDialer7, tvExperinceDialer8, tvExperinceDialer9,
			tvExperinceDialer0, tvExperinceDialerStar, tvExperinceDialerHesh,
			tvExperinceDialerClear, tvExperinceDialerSMS,
			tvExperinceDialerContact, tvExperinceDialerPhone,tvExperinceDialerCall,tvExperinceDialerBack;

	EditText etExperinceDialer;
	ListView callList;
	DialerCallInformationAdapter adapter;
	boolean bSMS, bContact, bRecent;
	Bitmap bitmap;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_dialer);
		initialization();
		/*if(savedInstanceState != null && (savedInstanceState.containsKey("DIAL_SMS")) || 
				savedInstanceState.containsKey("DIAL_CONTACT") || 
				savedInstanceState.containsKey("DIAL_RECENT")){
			if(savedInstanceState.containsKey("DIAL_SMS")){
				bSMS = savedInstanceState.getBoolean("DIAL_SMS");
				AddSMSListInListView();
			}else if(savedInstanceState.containsKey("DIAL_CONTACT")){
				bContact = savedInstanceState.getBoolean("DIAL_CONTACT");
				AddContactListView();
			}else if(savedInstanceState.containsKey("DIAL_RECENT")){
				bRecent = savedInstanceState.getBoolean("DIAL_RECENT");
				AddCallListInListView();
			}
		}*/
		AddContactListView();
		
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
	}

	private void initialization() {
		tvExperinceDialer1 = (TextView) findViewById(R.id.tvExperinceDialer1);
		tvExperinceDialer2 = (TextView) findViewById(R.id.tvExperinceDialer2);
		tvExperinceDialer3 = (TextView) findViewById(R.id.tvExperinceDialer3);
		tvExperinceDialer4 = (TextView) findViewById(R.id.tvExperinceDialer4);
		tvExperinceDialer5 = (TextView) findViewById(R.id.tvExperinceDialer5);
		tvExperinceDialer6 = (TextView) findViewById(R.id.tvExperinceDialer6);
		tvExperinceDialer7 = (TextView) findViewById(R.id.tvExperinceDialer7);
		tvExperinceDialer8 = (TextView) findViewById(R.id.tvExperinceDialer8);
		tvExperinceDialer9 = (TextView) findViewById(R.id.tvExperinceDialer9);
		tvExperinceDialer0 = (TextView) findViewById(R.id.tvExperinceDialer0);
		tvExperinceDialerStar = (TextView) findViewById(R.id.tvExperinceDialerStar);
		tvExperinceDialerHesh = (TextView) findViewById(R.id.tvExperinceDialerHesh);

		tvExperinceDialerClear = (TextView) findViewById(R.id.tvExperinceDialerClear);
		tvExperinceDialerSMS = (TextView) findViewById(R.id.tvExperinceDialerSMS);
		tvExperinceDialerContact = (TextView) findViewById(R.id.tvExperinceDialerContact);
		tvExperinceDialerPhone = (TextView) findViewById(R.id.tvExperinceDialerPhone);
		
		tvExperinceDialerCall= (TextView) findViewById(R.id.tvExperinceDialerCall);
		tvExperinceDialerBack= (TextView) findViewById(R.id.tvExperinceDialerBack);
		
		etExperinceDialer= (EditText) findViewById(R.id.etExperinceDialer);
		etExperinceDialer.setInputType(InputType.TYPE_NULL);
		
		tvExperinceDialer1.setOnClickListener(this);
		tvExperinceDialer2.setOnClickListener(this);
		tvExperinceDialer3.setOnClickListener(this);
		tvExperinceDialer4.setOnClickListener(this);
		tvExperinceDialer5.setOnClickListener(this);
		tvExperinceDialer6.setOnClickListener(this);
		tvExperinceDialer7.setOnClickListener(this);
		tvExperinceDialer8.setOnClickListener(this);
		tvExperinceDialer9.setOnClickListener(this);
		tvExperinceDialer0.setOnClickListener(this);
		tvExperinceDialerStar.setOnClickListener(this);
		tvExperinceDialerHesh.setOnClickListener(this);
		tvExperinceDialer0.setOnLongClickListener(this);
		

		tvExperinceDialerClear.setOnClickListener(this);
		tvExperinceDialerSMS.setOnClickListener(this);
		tvExperinceDialerContact.setOnClickListener(this);
		tvExperinceDialerPhone.setOnClickListener(this);
		
		tvExperinceDialerCall.setOnClickListener(this);
		tvExperinceDialerBack.setOnClickListener(this);
		
		callList = (ListView) findViewById(R.id.lvDialerList);
		
		callList.setOnItemClickListener(this);

	}
	
	/*private void AddCallListInListView(){
		MyNetDatabase database = new MyNetDatabase(this);
		database.open();
		ArrayList<PhoneCallInformation> callInformation = database.getCallInfoList();
		database.close();
		adapter = new DialerCallInformationAdapter(this, R.layout.call_dialer_item, callInformation);
		callList.setAdapter(adapter);
	}*/
	
	/*private void AddSMSListInListView(){
		MyNetDatabase database = new MyNetDatabase(this);
		database.open();
		ArrayList<PhoneSMSInformation> SMSInformation = database.getSMSInfoList();
		database.close();
		AllCallSMSAdapter adpater = new AllCallSMSAdapter(this, R.layout.all_call_sms, SMSInformation);
		callList.setAdapter(adpater);
	}*/
	
	private void AddContactListView(){
		ArrayList<ContactList> contactListInformarion = new ArrayList<ContactList>();
		ContentResolver cr = getContentResolver();
		Cursor cursor = cr.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
				String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				byte[] thumbill = cursor.getBlob(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO));
				if(thumbill != null){
					bitmap = BitmapFactory.decodeByteArray(thumbill, 0, thumbill.length);
				}else{
					bitmap = null;
				}
				contactListInformarion.add(new ContactList(number, name,bitmap));
			}while(cursor.moveToNext());
		}
		cursor.close();
		
		ContactListAdapter contactListadapter = new ContactListAdapter(this, R.layout.contact_list_item, contactListInformarion);
		callList.setAdapter(contactListadapter);
	}

	@Override
	public void onClick(View v) {
		String dialText= String.valueOf(etExperinceDialer.getText());
		if (v.getId() == R.id.tvExperinceDialer1) {
			etExperinceDialer.setText(dialText+"1");
		} else if (v.getId() == R.id.tvExperinceDialer2) {
			etExperinceDialer.setText(dialText+"2");
		} else if (v.getId() == R.id.tvExperinceDialer3) {
			etExperinceDialer.setText(dialText+"3");
		} else if (v.getId() == R.id.tvExperinceDialer4) {
			etExperinceDialer.setText(dialText+"4");
		} else if (v.getId() == R.id.tvExperinceDialer5) {
			etExperinceDialer.setText(dialText+"5");
		} else if (v.getId() == R.id.tvExperinceDialer6) {
			etExperinceDialer.setText(dialText+"6");
		} else if (v.getId() == R.id.tvExperinceDialer7) {
			etExperinceDialer.setText(dialText+"7");
		} else if (v.getId() == R.id.tvExperinceDialer8) {
			etExperinceDialer.setText(dialText+"8");
		} else if (v.getId() == R.id.tvExperinceDialer9) {
			etExperinceDialer.setText(dialText+"9");
		} else if (v.getId() == R.id.tvExperinceDialer0) {
			etExperinceDialer.setText(dialText+"0");
		} else if (v.getId() == R.id.tvExperinceDialerHesh) {
			etExperinceDialer.setText(dialText+"#");
		} else if (v.getId() == R.id.tvExperinceDialerStar) {
			etExperinceDialer.setText(dialText+"*");
		}else if (v.getId() == R.id.tvExperinceDialerClear) {
			etExperinceDialer.setText("");
		} else if (v.getId() == R.id.tvExperinceDialerSMS) {

		} else if (v.getId() == R.id.tvExperinceDialerContact) {

		} else if (v.getId() == R.id.tvExperinceDialerPhone) {

		}else if (v.getId() == R.id.tvExperinceDialerCall) {
			Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+dialText));			
			startActivity(intent);
		}else if (v.getId() == R.id.tvExperinceDialerBack) {
			if(dialText.length()>0)
				etExperinceDialer.setText( dialText.substring(0,dialText.length()-1));
		}
		

	}

	@Override
	public boolean onLongClick(View v) {
		if (v.getId() == R.id.tvExperinceDialer0) {
			etExperinceDialer.setText(String.valueOf(etExperinceDialer.getText())+"+");
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		Object object = view.getTag();
		/*if(object instanceof PhoneCallInformation){
			etExperinceDialer.setText(((PhoneCallInformation) object).Number);
		}else if(object instanceof PhoneSMSInformation){
			etExperinceDialer.setText(((PhoneSMSInformation) object).Number);
		}else */
		if(object instanceof ContactList){
			etExperinceDialer.setText(((ContactList) object).getNumber());
		}
	}
}
