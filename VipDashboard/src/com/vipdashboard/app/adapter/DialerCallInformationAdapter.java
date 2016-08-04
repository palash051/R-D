package com.vipdashboard.app.adapter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.MyNetAllCallDetails;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.entities.PhoneSMSInformation;
import com.vipdashboard.app.utils.CommonTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.CallLog;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DialerCallInformationAdapter extends ArrayAdapter<PhoneCallInformation> {
	
	private Context context;
	private ArrayList<PhoneCallInformation> callInformaitonList;
	private PhoneCallInformation phoneCallInformation;
	private ArrayList<PhoneCallInformation> callInformationItemList;
	public boolean search;

	public DialerCallInformationAdapter(Context _context, int resource,
			ArrayList<PhoneCallInformation> _objects) {
		super(_context, resource, _objects);
		context = _context;
		callInformaitonList = _objects;
		callInformationItemList = new ArrayList<PhoneCallInformation>();
		callInformationItemList.addAll(callInformaitonList);
	}
	@Override
	public int getCount() {
		return callInformaitonList.size();
	}

	public void setList(ArrayList<PhoneCallInformation> _userGroupUnionList) {
		callInformaitonList = _userGroupUnionList;
		notifyDataSetChanged();
	}

	@Override
	public PhoneCallInformation getItem(int position) {
		return callInformaitonList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public ArrayList<PhoneCallInformation> getAdapterList(){
		return callInformationItemList;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		phoneCallInformation = callInformaitonList.get(position);
		//View callDialerItemView = null;
		//TextView tvName = null, tvNumber = null, tvDate = null, tvDuration = null;
		//ImageView callTypeImage = null;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View callDialerItemView = inflater.inflate(R.layout.call_dialer_item,null);
		TextView tvName = (TextView) callDialerItemView.findViewById(R.id.tvPhoneNumberName);
		TextView tvNumber = (TextView) callDialerItemView.findViewById(R.id.tvPhoneNumber);
		TextView tvDate = (TextView) callDialerItemView.findViewById(R.id.tvPhoneCallDate);
		TextView tvDuration = (TextView) callDialerItemView.findViewById(R.id.tvPhoneCallDuration);
		TextView tvdistance = (TextView) callDialerItemView.findViewById(R.id.tvdistance);
		ImageView callTypeImage = (ImageView) callDialerItemView.findViewById(R.id.ivCallType);
		ImageView ivPhoneCallerIamge = (ImageView) callDialerItemView.findViewById(R.id.ivUserGroup);
		
		String dateTime="";
		if(phoneCallInformation.Number != null && !phoneCallInformation.Number.isEmpty()){
			//String name = CommonTask.getContentName(context, phoneCallInformation.Number);
			//phoneCallInformation.Reson=name;
			if(phoneCallInformation.Name != "" || phoneCallInformation.Name != null){
				tvName.setText(phoneCallInformation.Name);
			}else{
				tvName.setText("");
			}
			int photoId=CommonTask.getContentPhotoId(this.context, phoneCallInformation.Number);
			if(photoId>0){
				ivPhoneCallerIamge.setImageBitmap(CommonTask.fetchContactImageThumbnail(context,photoId));
			}
			tvNumber.setText(phoneCallInformation.Number);
		}
		
		if(phoneCallInformation.CallTime != null){
		
			dateTime = (String) DateUtils.getRelativeTimeSpanString(
					phoneCallInformation.CallTime.getTime(), new Date().getTime(), 0);
			tvDate.setText(dateTime);
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
		tvDuration.setText(durationText);
		if (phoneCallInformation.CallType == CallLog.Calls.INCOMING_TYPE)
			callTypeImage.setImageResource(R.drawable.call_arrow_received);
		else if (phoneCallInformation.CallType == CallLog.Calls.OUTGOING_TYPE)
			callTypeImage.setImageResource(R.drawable.call_arrow_dialed);
		else{
			callTypeImage.setImageResource(R.drawable.call_arrow_missed);
		}
		
		/*int distence= CommonTask.distanceCalculationInMeter(MyNetService.currentLocation.getLatitude(), MyNetService.currentLocation.getLongitude(),phoneCallInformation.Latitude , phoneCallInformation.Longitude);
		distence=distence/1000;
		tvdistance.setText(distence<2?"within one km":String.valueOf(distence)+" km");*/
		
		callDialerItemView.setTag(phoneCallInformation);
		
		return callDialerItemView;
	}
	
	public void CallFilter(String charText){
		search = true;
		charText = charText.toLowerCase(Locale.getDefault());
		callInformaitonList.clear();
		if (charText.length() == 0) {
			callInformaitonList.addAll(getAdapterList());
		}else{
			for (PhoneCallInformation phonecallInformation : getAdapterList()) {
				if(phonecallInformation.Number.toLowerCase(Locale.getDefault()).contains(charText)||phonecallInformation.Name.toLowerCase(Locale.getDefault()).contains(charText)){
					callInformaitonList.add(phonecallInformation);
				}
				notifyDataSetChanged();
			}
		}
	}

}
