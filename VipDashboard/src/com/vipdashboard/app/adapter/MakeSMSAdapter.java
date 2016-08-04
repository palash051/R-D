package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.Date;

import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.MakeSMSActivity;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.utils.CommonTask;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MakeSMSAdapter extends ArrayAdapter<PhoneSMSInformation> {
	
	private Context context;
	private ArrayList<PhoneSMSInformation> smsInformationList;
	private PhoneSMSInformation phoneSMSInformation;

	public MakeSMSAdapter(Context _context, int textViewResourceId,
			ArrayList<PhoneSMSInformation> _objects) {
		super(_context, textViewResourceId, _objects);
		context = _context;
		smsInformationList = _objects;
	}
	
	public int getCount() {
		return smsInformationList.size();
	}
	
	public PhoneSMSInformation getLastItem(){
		return smsInformationList.get(smsInformationList.size()-1);
	}
	
	public void addItem(PhoneSMSInformation item){
		smsInformationList.add(item);
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {

		phoneSMSInformation = smsInformationList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View userItemView = inflater.inflate(R.layout.message_layout, null);
		try {
			TextView tvMessageTitle = (TextView) userItemView
					.findViewById(R.id.tvMessageTitle);
			TextView tvMessageText = (TextView) userItemView
					.findViewById(R.id.tvMessageText);
			TextView tvMessageTime = (TextView) userItemView
					.findViewById(R.id.tvMessageTime);
			LinearLayout rlMessage = (LinearLayout) userItemView
					.findViewById(R.id.rlMessageBody);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);

			if (phoneSMSInformation.SMSType == 2) {
				rlMessage.setBackgroundResource(R.drawable.left_message_bg);
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
				tvMessageTitle.setText("Me");
			} else if(phoneSMSInformation.SMSType == 1) {
				rlMessage.setBackgroundResource(R.drawable.right_message_bg);
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
				String name = CommonTask.getContentName(context, phoneSMSInformation.Number);
				if(name != ""){
					tvMessageTitle.setText(name);
				}else{
					tvMessageTitle.setText(phoneSMSInformation.Number);
				}
				
			}
			rlMessage.setLayoutParams(params);
			tvMessageText.setText(phoneSMSInformation.SMSBody);
			//tvMessageTime.setText(phoneSMSInformation.SMSTime.equals("Sending")?"Sending": CommonTask.convertDateToString(phoneSMSInformation.SMSTime));
			String dateTime = (String) DateUtils.getRelativeTimeSpanString(
					phoneSMSInformation.SMSTime.getTime(), new Date().getTime(), 0);			
			tvMessageTime.setText(dateTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userItemView;
	}

}
