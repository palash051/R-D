package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ContactListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.classes.ContactList;
import com.vipdashboard.app.interfaces.IAsynchronousTask;

public class MyNetSMSSenderActivity extends MainActionbarBase implements OnClickListener, OnItemClickListener, IAsynchronousTask, TextWatcher {
	EditText etNumberField, etMessageBody;
	Button bSendSMS;
	ListView lvContactList;
	DownloadableAsyncTask asyncTask;
	ArrayList<ContactList> contactListInformarion;
	Bitmap bitmap;
	ContactListAdapter contactListadapter;
	TextView tvNumberClear;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sms_sender);
		Initilization();
	}
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {		
		MyNetApplication.activityResumed();
		LoadInformation();
		super.onResume();
	}	
	
	private void Initilization() {
		etNumberField = (EditText) findViewById(R.id.etExperinceSMSSender);
		etMessageBody = (EditText) findViewById(R.id.etMessageBody);
		bSendSMS = (Button) findViewById(R.id.bSendSMS);
		lvContactList = (ListView) findViewById(R.id.lvDialerList);
		tvNumberClear = (TextView) findViewById(R.id.tvExperinceDialerClear);
		bSendSMS.setOnClickListener(this);
		tvNumberClear.setOnClickListener(this);
		lvContactList.setOnItemClickListener(this);
		etNumberField.addTextChangedListener(this);
	}
	
	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.bSendSMS){
			if(!etMessageBody.getText().toString().equals("")){
				SmsManager smsManager = SmsManager.getDefault();
			    smsManager.sendTextMessage(etNumberField.getText().toString(), 
			    		null, etMessageBody.getText().toString(), null, null);
			    etMessageBody.setText("");
			    Toast.makeText(this, "SMS Sent", Toast.LENGTH_SHORT).show();
			}
		}else if(view.getId() == R.id.tvExperinceDialerClear){
			if(!etNumberField.getText().toString().equals("")){
				Intent intent = new Intent(this, MakeSMSActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("NUMBER", etNumberField.getText().toString());
				startActivity(intent);
			}else{
				etNumberField.setError("Enter Number");
			}			
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		Object object = view.getTag();
		if(object != null){
			etNumberField.setText(((ContactList) object).getNumber());
		}
	}
	
	private void LoadInformation(){
		if(asyncTask != null)
			asyncTask.cancel(true);
		asyncTask = new DownloadableAsyncTask(this);
		asyncTask.execute();
	}
	@Override
	public void showProgressLoader() {
		
	}
	@Override
	public void hideProgressLoader() {
		
	}
	@Override
	public Object doBackgroundPorcess() {
		contactListInformarion = new ArrayList<ContactList>();
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
		return contactListInformarion;
	}
	
	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			contactListadapter = new ContactListAdapter(this, R.layout.contact_list_item, contactListInformarion);
			lvContactList.setAdapter(contactListadapter);
		}
	}
	
	@Override
	public void afterTextChanged(Editable arg0) {
		
	}
	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		
	}
	@Override
	public void onTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {
		if(charSequence.toString().length()>0){
			contactListadapter.ContactListFilter(charSequence.toString());
		}else{
			lvContactList.setAdapter(contactListadapter);
		}
	}

}
