package com.vipdashboard.app.adapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.CallMemoActivity;
import com.vipdashboard.app.entities.PhoneCallInformation;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AllSMSAdapter extends ArrayAdapter<PhoneCallInformation> {
	
	Context context;
	ArrayList<PhoneCallInformation> callInformationList;
	PhoneCallInformation phoneCallInformation;

	public AllSMSAdapter(Context _context, int textViewResourceId,
			ArrayList<PhoneCallInformation> _objects) {
		super(_context, textViewResourceId, _objects);
		context = _context;
		callInformationList = _objects;	
	}
	
	@Override
	public int getCount() {
		return callInformationList.size();
	}

	public void setList(ArrayList<PhoneCallInformation> _userGroupUnionList) {
		callInformationList = _userGroupUnionList;
		notifyDataSetChanged();
	}

	@Override
	public PhoneCallInformation getItem(int position) {
		return callInformationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {

		phoneCallInformation = callInformationList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View userItemView = inflater.inflate(R.layout.all_sms_item_layout,
				null);

		TextView tvPhoneCallerName = (TextView) userItemView
				.findViewById(R.id.tvPhoneName);
		TextView tvDate = (TextView) userItemView
				.findViewById(R.id.tvDate);
		TextView tvSubMessage = (TextView) userItemView.findViewById(R.id.tvSubMessage);
		TextView tvCallTime = (TextView)userItemView.findViewById(R.id.tvCallDate);
		TextView callmemo = (TextView) userItemView.findViewById(R.id.tvCallDemoMemo);
		TextView dialCall = (TextView) userItemView.findViewById(R.id.tvCallDemoDialer);
		TextView voiceMemo = (TextView) userItemView.findViewById(R.id.tvCallDemoVoice);
		
		/*dialCall.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				Intent calltomanager = new Intent(Intent.ACTION_CALL);
				calltomanager.setData(Uri.parse("tel:" + phoneCallInformation.Number));
				context.startActivity(calltomanager);
			}
		});
		callmemo.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				CallMemoEdit callMemoEdit = new CallMemoEdit(context);
				callMemoEdit.showCallMemo(view.getTag());
			}
		});
		voiceMemo.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View view) {
				VoiceCallPlayer voiceCallPlayer = new VoiceCallPlayer(context);
				voiceCallPlayer.PlayVoiceCallRecorder(view.getTag());
			}
		});*/
		try {
			Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneCallInformation.Number));
			Cursor cursor = context.getContentResolver().query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
			if(cursor != null && cursor.getCount() > 0){
				cursor.moveToFirst();
				String name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
				tvPhoneCallerName.setText(name);
				cursor.close();
			}else{
				tvPhoneCallerName.setText(phoneCallInformation.Number);
			}
			
			int min = 0, sec= 0, hour = 0;
			String durationText = "";
			NumberFormat formatter = new DecimalFormat("00");
			sec = phoneCallInformation.DurationInSec % 60;
			min = phoneCallInformation.DurationInSec / 60;
			if (min > 59) {
				hour = min / 60;
				min = min % 60;
			}
			durationText = formatter.format(hour) + ":"
					+ formatter.format(min) + ":" + formatter.format(sec);
			tvDate.setText(durationText);
			
			if (phoneCallInformation.CallType == CallLog.Calls.INCOMING_TYPE)
				tvSubMessage.setText("Incoming");
			else if (phoneCallInformation.CallType == CallLog.Calls.OUTGOING_TYPE)
				tvSubMessage.setText("Outgoing");
			else{
				tvSubMessage.setText("Missed");
			}
			
			String dateTime = (String) DateUtils.getRelativeTimeSpanString(
					phoneCallInformation.CallTime.getTime(), new Date().getTime(), 0);
			tvCallTime.setText(dateTime);
			
			if (phoneCallInformation.CallType == CallLog.Calls.INCOMING_TYPE)
				dialCall.setBackground(context.getResources().getDrawable(R.drawable.call_arrow_received));
			else if (phoneCallInformation.CallType == CallLog.Calls.OUTGOING_TYPE)
				dialCall.setBackground(context.getResources().getDrawable(R.drawable.call_arrow_dialed));
				//callTypeImage.setImageResource(R.drawable.call_arrow_dialed);
			else{
				dialCall.setBackground(context.getResources().getDrawable(R.drawable.call_arrow_missed));
				//callTypeImage.setImageResource(R.drawable.call_arrow_missed);
			}
			
			/*if(phoneCallInformation.TextCallMemo == null)
				callmemo.setVisibility(View.GONE);
			else{
				callmemo.setVisibility(View.VISIBLE);
				callmemo.setTag(phoneCallInformation);
			}*/
			/*if(phoneCallInformation.VoiceRecordPath == null)
				voiceMemo.setVisibility(View.GONE);
			else{
				voiceMemo.setVisibility(View.VISIBLE);
				voiceMemo.setTag(phoneCallInformation);
			}*/
			
			userItemView.setTag(phoneCallInformation);
			
			
		} catch (Exception ex) {

		}
		return userItemView;
	}

}
