package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;

import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.CollaborationUserLocationActivity;
import com.vipdashboard.app.entities.Collaboration;
import com.vipdashboard.app.entities.ContactUser;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class ChatListAdapter extends ArrayAdapter<Collaboration> implements OnClickListener{

	Context context;
	Collaboration collaboration;
	ArrayList<Collaboration> collaborationList;
	RelativeLayout.LayoutParams params,paramsPin;
	public ChatListAdapter(Context _context, int resource,
			ArrayList<Collaboration> _collaborationList) {
		super(_context, resource, _collaborationList);
		context = _context;
		collaborationList = _collaborationList;		
		paramsPin = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);		
		paramsPin.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);	
	}
	public int getCount() {
		return collaborationList.size();
	}
	
	public Collaboration getLastItem(){
		return collaborationList.get(collaborationList.size()-1);
	}
	public void addItem(Collaboration item){
		collaborationList.add(item);
	}
	private class ViewHolder {
		public TextView tvMessageTitle;
		public TextView tvMessageText;
		public TextView tvMessageTime;
		public ImageView userCurrentLocation;
		public LinearLayout rlMessage;
		public  RelativeLayout rlIMImage;
		public ImageView ivIMImage;
		public ProgressBar pbIMImage;
	}
	public View getView(final int position, View convertView, ViewGroup parent) {
		View chatItemView = convertView;
		ViewHolder holder = new ViewHolder();
		collaboration = collaborationList.get(position);
		
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			chatItemView = inflater.inflate(R.layout.message_layout, null);
			holder.tvMessageTitle = (TextView) chatItemView.findViewById(R.id.tvMessageTitle);
			holder.tvMessageText = (TextView) chatItemView.findViewById(R.id.tvMessageText);
			holder.tvMessageTime = (TextView) chatItemView.findViewById(R.id.tvMessageTime);
			holder.userCurrentLocation = (ImageView) chatItemView.findViewById(R.id.ivUserCurrentLocation);
			holder.userCurrentLocation.setOnClickListener(this);
			holder.rlMessage = (LinearLayout) chatItemView.findViewById(R.id.rlMessageBody);
			holder.rlIMImage = (RelativeLayout) chatItemView.findViewById(R.id.rlIMImage);
			holder.ivIMImage = (ImageView) chatItemView.findViewById(R.id.ivIMImage);
			holder.pbIMImage = (ProgressBar) chatItemView.findViewById(R.id.pbIMImage);
			holder.ivIMImage.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					final Dialog nagDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
		            nagDialog.setCancelable(false);
		            nagDialog.setContentView(R.layout.preview_image);			            
		            ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);
		            ivPreview.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(((ImageView)v).getTag())));

		            ivPreview.setOnClickListener(new View.OnClickListener() {
		                @Override
		                public void onClick(View arg0) {

		                    nagDialog.dismiss();
		                }
		            });
		            nagDialog.setOnKeyListener(new Dialog.OnKeyListener() {

		                @Override
		                public boolean onKey(DialogInterface arg0, int keyCode,
		                        KeyEvent event) {			                    
		                    if (keyCode == KeyEvent.KEYCODE_BACK) {
		                        
		                        nagDialog.dismiss();
		                    }
		                    return true;
		                }
		            });
		            nagDialog.show();
					
				}
			});
			chatItemView.setTag(holder);
		}else{
			holder = (ViewHolder) chatItemView.getTag();
		}	
		holder.rlMessage.setVisibility(View.VISIBLE);
		holder.rlIMImage.setVisibility(View.GONE);
		
		if(collaboration.MsgText.equals("'FILE:START'")){
			holder.rlMessage.setVisibility(View.GONE);
			holder.rlIMImage.setVisibility(View.VISIBLE);
			holder.pbIMImage.setVisibility(View.VISIBLE);
			holder.ivIMImage.setVisibility(View.GONE);
			return chatItemView;
		}
		if(collaboration.MsgText.contains("FILEPATH:")){	
			String filePath=collaboration.MsgText.replace("FILEPATH:", "");
			holder.rlMessage.setVisibility(View.GONE);
			holder.rlIMImage.setVisibility(View.VISIBLE);
			holder.pbIMImage.setVisibility(View.GONE);
			holder.ivIMImage.setVisibility(View.VISIBLE);
			holder.ivIMImage.setImageBitmap(BitmapFactory.decodeFile(filePath));
			holder.ivIMImage.setTag(filePath);
			return chatItemView;
		}
		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		holder.tvMessageTitle.setVisibility(View.VISIBLE);
		if (collaboration.MsgFrom == CommonValues.getInstance().LoginUser.UserNumber) {
			holder.rlMessage.setBackgroundResource(R.drawable.left_message_bg);
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			paramsPin.addRule(RelativeLayout.RIGHT_OF, R.id.rlMessageBody);	
			paramsPin.setMargins(10, 0, 0, 0);
			//tvMessageTitle.setText("Me");
			holder.tvMessageTitle.setText("");
			holder.tvMessageTitle.setVisibility(View.GONE);
		} else {
			holder.rlMessage.setBackgroundResource(R.drawable.right_message_bg);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
			paramsPin.addRule(RelativeLayout.LEFT_OF, R.id.rlMessageBody);				
			if(CommonValues.getInstance().ConatactUserList!=null){
				ContactUser user=CommonValues.getInstance().ConatactUserList.get(collaboration.MsgFrom);
				if(user!=null){
					holder.tvMessageTitle.setText(user.Name);
				}else{
					holder.tvMessageTitle.setText("Unknown("+ collaboration.MsgFromName+")");
				}
			}			
			paramsPin.setMargins(0, 0, 10, 0);
		}
		holder.userCurrentLocation.setVisibility(View.GONE);
		if(collaboration.Latitude > 0.0 && collaboration.Longitude > 0.0){
			//userCurrentLocation.setVisibility(View.VISIBLE);
			holder.userCurrentLocation.setLayoutParams(paramsPin);
		}
		
		holder.rlMessage.setLayoutParams(params);
		holder.tvMessageText.setText(collaboration.MsgText);
		holder.tvMessageTime.setText(collaboration.PostedDate.equals("Sending")?"Sending": DateUtils.getRelativeTimeSpanString(CommonTask.convertJsonDateToLong(collaboration.PostedDate), new Date().getTime(), 0));
		holder.userCurrentLocation.setTag(collaboration);
		return chatItemView;
	}
	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.ivUserCurrentLocation){
			Collaboration userCurrentLocation = (Collaboration) view.getTag();
			if(userCurrentLocation != null){
				Intent intent = new Intent(context, CollaborationUserLocationActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra("LATITUDE", userCurrentLocation.Latitude);
				intent.putExtra("LONGITUDE", userCurrentLocation.Longitude);
				context.startActivity(intent);
			}
		}
	}

}
