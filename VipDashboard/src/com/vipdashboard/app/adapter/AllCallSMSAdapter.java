package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.MyNetAllCallDetails;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.utils.CommonTask;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AllCallSMSAdapter extends ArrayAdapter<PhoneSMSInformation>{
	
	private Context context;
	private ArrayList<PhoneSMSInformation> smsInformationList;
	private ArrayList<PhoneSMSInformation> smsInformationItemList;
	private PhoneSMSInformation phoneSMSInformation;
	public boolean search;

	public AllCallSMSAdapter(Context _context, int textViewResourceId,
			ArrayList<PhoneSMSInformation> objects) {
		super(_context, textViewResourceId, objects);
		context = _context;
		smsInformationList = objects;
		smsInformationItemList = new ArrayList<PhoneSMSInformation>();
		smsInformationItemList.addAll(smsInformationList);
	}

	@Override
	public int getCount() {
		return smsInformationList.size();
	}
	
	public void setList(ArrayList<PhoneSMSInformation> _userGroupUnionList) {
		smsInformationList = _userGroupUnionList;
		notifyDataSetChanged();
	}

	@Override
	public PhoneSMSInformation getItem(int position) {
		return this.smsInformationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public ArrayList<PhoneSMSInformation> getAdapterList(){
		return smsInformationItemList;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		phoneSMSInformation = smsInformationList.get(position);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View userItemView = inflater.inflate(R.layout.all_call_sms,null);
		
		TextView number = (TextView) userItemView.findViewById(R.id.tvSMSPersonName);
		TextView date = (TextView) userItemView.findViewById(R.id.tvSMSDate);
		TextView subSMS = (TextView) userItemView.findViewById(R.id.tvSubSMS);
		
		ImageView ivSMSPersonImage=(ImageView) userItemView.findViewById(R.id.ivSMSPersonImage);
		String dateTime = "";
		if(!phoneSMSInformation.Number.equals("")){
			//String name = CommonTask.getContentName(this.context, phoneSMSInformation.Number);
			//phoneSMSInformation.Name = name;
			if(phoneSMSInformation.Name != ""){
				number.setText(phoneSMSInformation.Name);
			}else{
				number.setText(phoneSMSInformation.Number);
			}
			
			int photoId=CommonTask.getContentPhotoId(this.context, phoneSMSInformation.Number);
			if(photoId>0){
				ivSMSPersonImage.setImageBitmap(CommonTask.fetchContactImageThumbnail(context,photoId));
			}
		}		
		
		if (phoneSMSInformation.SMSBody.length() > 20) {
			String text = phoneSMSInformation.SMSBody.substring(0, 20);
			text = text.replaceAll("\n", " ");
			subSMS.setText(text + "...");
		} else {
			subSMS.setText(phoneSMSInformation.SMSBody);
		}
		if(phoneSMSInformation.SMSTime != null){
			dateTime = (String) DateUtils.getRelativeTimeSpanString(
					phoneSMSInformation.SMSTime.getTime(), new Date().getTime(), 0);
			date.setText(dateTime);
		}
		
		userItemView.setTag(phoneSMSInformation);
		return userItemView;
	}
	
	public void SMSFilter(String charText){
		search = true;
		charText = charText.toLowerCase(Locale.getDefault());
		smsInformationList.clear();
		if (charText.length() == 0) {
			smsInformationList.addAll(getAdapterList());
		}else{
			for (PhoneSMSInformation phoneSMSInformation : getAdapterList()) {
				if(phoneSMSInformation.Number.toLowerCase(Locale.getDefault()).contains(charText)){
					smsInformationList.add(phoneSMSInformation);
				}else if(phoneSMSInformation.Name.toLowerCase(Locale.getDefault()).contains(charText)){
					smsInformationList.add(phoneSMSInformation);
				}else if(phoneSMSInformation.SMSBody.toLowerCase(Locale.getDefault()).contains(charText)){
					smsInformationList.add(phoneSMSInformation);
				}
				
			}
			notifyDataSetChanged();
		}
	}

}
