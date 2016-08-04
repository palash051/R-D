package com.vipdashboard.app.activities;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ChatListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.Collaboration;
import com.vipdashboard.app.entities.Group;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.ILiveCollaborationManager;
import com.vipdashboard.app.manager.LiveCollaborationManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;

public class CareIMMessegingActivity extends MainActionbarBase implements  IAsynchronousTask,OnClickListener, OnKeyListener, TextWatcher{
	
	EditText etMessageText;
	Button bSendMessage;
	ListView lvChatList;
	TextView tvChatMainTitle, tvGroupMemberName, tvVoiceRecorder,tvFileUpload;
	private static UserGroupUnion selectedUserGroupUnion;
	
	ChatListAdapter chatListAdapter;
	
	TextView loadEarlier;
	
	int LoadEarlierPress,count;
	
	boolean isCallFromFileSend=false;
	
	ImageView ivChatUserImage;
	private MessageReceiver messageReceiver = new MessageReceiver();
	DownloadableAsyncTask downloadableAsyncTask;
	Collaboration selectedCollaboration=null;
	//FileTransferManager fileTransferManager ;
	
	MultiUserChat consumerMuc;
	
	String msgTo="",messageText;
	
	long previousMsgTime=0,currentMsgTime=0;;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_layout);  
		
		loadEarlier= (TextView) findViewById(R.id.tvShowMeMore);
		lvChatList=(ListView) findViewById(R.id.lvChatList);
		
		etMessageText = (EditText) findViewById(R.id.etMessageText);
		bSendMessage = (Button) findViewById(R.id.bSendMessage);
		tvChatMainTitle=(TextView) findViewById(R.id.tvChatMainTitle);
		tvGroupMemberName = (TextView) findViewById(R.id.tvGroupMemberName);
		tvVoiceRecorder = (TextView) findViewById(R.id.tvVoiceRecorder);
		ivChatUserImage= (ImageView) findViewById(R.id.ivChatUserImage);
		tvFileUpload= (TextView) findViewById(R.id.tvFileUpload);
		
		bSendMessage.setOnClickListener(this);
		
		tvChatMainTitle.setOnClickListener(this);
		etMessageText.setOnKeyListener(this);
		etMessageText.addTextChangedListener(this);
		CommonTask.makeLinkedTextview(this, loadEarlier, loadEarlier.getText().toString());
		loadEarlier.setOnClickListener(this);
		tvFileUpload.setOnClickListener(this);
		count = 0;
		LoadEarlierPress = (++count)*CommonConstraints.USER_MESSAGE_COUNT;
	}	
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		if(!isCallFromFileSend){
			selectedUserGroupUnion=null;
		}
		super.onPause();
	}

	@Override
	protected void onStart() {			
		super.onStart();		
	}

	@Override
	protected void onResume() {
		
		//Bundle bundle=this.getIntent().getExtras();
		super.onResume();
		if(!isCallFromFileSend){
			/*if(bundle==null)
				return;*/
			selectedUserGroupUnion=new UserGroupUnion();
			selectedUserGroupUnion.ID=this.getIntent().getIntExtra("UserID", 0);//bundle.getInt("UserID");
			selectedUserGroupUnion.Type=this.getIntent().getStringExtra("UserType");		
			if(selectedUserGroupUnion.Type.equals("G")&&CommonValues.getInstance().XmppConnectedGroup!=null){
				Group g=CommonValues.getInstance().XmppConnectedGroup.get(selectedUserGroupUnion.ID);
				if(g!=null)
					selectedUserGroupUnion.Name=g.Name;	
				else
					return;
			}else if(selectedUserGroupUnion.Type.equals("U")&& CommonValues.getInstance().ConatactUserList!=null){				
				selectedUserGroupUnion.Name=CommonValues.getInstance().ConatactUserList.get(selectedUserGroupUnion.ID).PhoneNumber;
			}
			
			if (!CommonTask.isOnline(this)) {
				return;	
			}		
			if(selectedUserGroupUnion==null)
			{
				String prefUserPass=CommonTask.getPreferences(CareIMMessegingActivity.this, CommonConstraints.LOGIN_USERPASS_SHAREDPREF_KEY);
				Intent intent = new Intent(CareIMMessegingActivity.this,
						HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra("LoginUserNumber", Integer.parseInt(prefUserPass));
				startActivity(intent);
			}
			else
			{
				MyNetApplication.activityResumed();
				loadEarlier.setVisibility(View.GONE);
				tvChatMainTitle.setText(selectedUserGroupUnion.Name);
				manageGroupUserOrUserImage();
			}
			msgTo=selectedUserGroupUnion.Name+"@"+CommonURL.getInstance().CareIMService;
			if(selectedUserGroupUnion.Type.equals("G")){
				msgTo=selectedUserGroupUnion.Name+CommonURL.getInstance().CareIMConference;
				DiscussionHistory history = new DiscussionHistory();
				history.setMaxStanzas(50);
				if(CommonValues.getInstance().XmppConnectedGroup!=null){				
					consumerMuc=CommonValues.getInstance().XmppConnectedGroup.get(selectedUserGroupUnion.ID).MultiUserChatId;
					/*try {
						consumerMuc.join(CommonValues.getInstance().LoginUser.UserID, CommonValues.getInstance().XmppUserPassword, history, SmackConfiguration.getPacketReplyTimeout());
					} catch (XMPPException e) {
						e.printStackTrace();
					}*/
				}			
			}
			if(CommonValues.getInstance().ConatactUserList==null){
				MyNetDatabase db=new MyNetDatabase(this);
				CommonValues.getInstance().ConatactUserList=db.GetUserHashMap();
			}
			IntentFilter i = new IntentFilter();
			i.addAction(CommonValues.getInstance().XmppReceiveMessageIntent);		
			registerReceiver(messageReceiver, i);
			chatListAdapter=new ChatListAdapter(CareIMMessegingActivity.this, R.layout.message_layout, new ArrayList<Collaboration>());
			lvChatList.setAdapter(chatListAdapter);
			MyNetDatabase db=new MyNetDatabase(this);
			db.GetIMbyId(selectedUserGroupUnion.ID,selectedUserGroupUnion.Type);
			
			/*FileTransferManager fileTransferManager = new FileTransferManager(CommonValues.getInstance().XmppConnection);
			FileTransferNegotiator.getInstanceFor(CommonValues.getInstance().XmppConnection);
	        FileTransferNegotiator.setServiceEnabled(CommonValues.getInstance().XmppConnection,true);
	        final ProgressDialog progressDialog= new ProgressDialog(CareIMMessegingActivity.this,ProgressDialog.THEME_HOLO_LIGHT);
    		progressDialog.setCancelable(false);
    		progressDialog.setMessage("File Transferring..");
			fileTransferManager.addFileTransferListener(new FileTransferListener() {
			   public void fileTransferRequest(final FileTransferRequest request) {
			      new Thread(){
			         @Override
			         public void run() {				        	 
			            IncomingFileTransfer transfer = request.accept();				           
			            File folder = new File(Environment.getExternalStorageDirectory() + "/MumtazCare");
			            if(!folder.exists())
			            	folder.mkdir();
			            File file=new File(folder.getAbsoluteFile()+"/"+ request.getFileName());
			            if(file.exists()){
			            	file.delete();
			            }
			            Uri myFileUri = Uri.fromFile(file);
			            try{
			            	runOnUiThread(new Runnable() {
			            	    public void run() {
					        		progressDialog.show();
			            	    }
			            	});
			            	
			                transfer.recieveFile(file);
			                while(!transfer.isDone()) {
			                   try{
			                      Thread.sleep(1000L);
			                   }catch (Exception e) {
			                   }
			                   if(transfer.getStatus().equals(Status.error)) {
			                   }
			                   if(transfer.getException() != null) {
			                      transfer.getException().printStackTrace();
			                   }
			                }
			                runOnUiThread(new Runnable() {
			            	    public void run() {
					        		progressDialog.dismiss();
			            	    }
			            	});
			                if(transfer.getStatus().equals(Status.refused) || transfer.getStatus().equals(Status.error)
			       				 || transfer.getStatus().equals(Status.cancelled)){
			       			} else {
			       				Intent myIntent = new Intent(android.content.Intent.ACTION_VIEW);
			       				String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(myFileUri.toString().toLowerCase());
			       				String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
			       				myIntent.setDataAndType(myFileUri,mimetype);
			       				myIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			       				startActivity(myIntent);
			       			}
			             }catch (Exception e) {
			            	 e.printStackTrace();
			            }
			         };
			       }.start();
			    }
			 });	*/
			
		}
		isCallFromFileSend=false;
	}
	
	private void manageGroupUserOrUserImage() {
		if(selectedUserGroupUnion.Type.equals("U")){
			ContentResolver cr = getContentResolver();
			Cursor cursor = cr.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, " replace(data1,' ', '') like '%" + selectedUserGroupUnion.Name.substring(3) +"%'", null, null);
			
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				tvChatMainTitle.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
				Bitmap bit=CommonTask.fetchContactImageThumbnail(this,cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID)));
				if(bit!=null)
					ivChatUserImage.setImageBitmap(bit);
				else{
					ivChatUserImage.setImageResource(R.drawable.user_icon);
				}
			}else{
				tvChatMainTitle.setText(selectedUserGroupUnion.Name);
				ivChatUserImage.setImageResource(R.drawable.user_icon);
			}
			cursor.close();
		}else{
			tvChatMainTitle.setText(selectedUserGroupUnion.Name);
			ivChatUserImage.setImageResource(R.drawable.user_group);
		}
	}
	@Override
	public void onClick(View view) {
		if (!CommonTask.isOnline(this)) {
			return;	
		}
		if(view.getId() == R.id.tvShowMeMore){
			if (!CommonTask.isOnline(this)) {
				return;	
			}			
		}
		else if(view.getId() == R.id.bSendMessage){					
			messageText=etMessageText.getText().toString();
			etMessageText.setText("");
			sendMessage(messageText);			
			messageText="";			
		}else if(view.getId() == R.id.tvChatMainTitle){
			if (selectedUserGroupUnion.Type.equals("G")) {
				GroupInformationActivity.selectUserGroupUnion = selectedUserGroupUnion;
				Intent intent = new Intent(this, GroupInformationActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else{
				ContactActivity.selectUserGroupUnion = selectedUserGroupUnion;
				Intent intent = new Intent(this, ContactActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			
		}else if(view.getId() == R.id.tvFileUpload){	
			isCallFromFileSend=true;
			Intent intent = new Intent();
			//intent.setType("application/*");
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, "Select File"),2);
		}
	}	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == RESULT_OK) {
				messageText= "'FILE:START'";
				etMessageText.setText("");
				sendMessage(messageText);			
				messageText="";
				Uri currImageURI = data.getData();
				File file = new File(getRealPathFromURI(currImageURI));
				
				Bitmap b = CommonTask.decodeImage(file);
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				if (b.getByteCount() > (1024 * 1024)) {
					b.compress(Bitmap.CompressFormat.JPEG, 20, stream);
				}
				if (b.getByteCount() > (1024 * 512)) {
					b.compress(Bitmap.CompressFormat.JPEG, 40, stream);
				}
				if (b.getByteCount() > (1024 * 256)) {
					b.compress(Bitmap.CompressFormat.JPEG, 60, stream);
				} else {
					b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				}
				messageText="FILESTRING:"+ Base64.encodeToString(stream.toByteArray(),Base64.NO_WRAP) ;
				etMessageText.setText("");
				sendMessage(messageText);			
				messageText="";
				/*ProgressDialog progressDialog= new ProgressDialog(CareIMMessegingActivity.this,ProgressDialog.THEME_HOLO_LIGHT);
	        	progressDialog.setCancelable(false);
	        	progressDialog.setMessage("File Transferring..");
	        	progressDialog.show();
	        	FileTransferManager fileTransferManager = new FileTransferManager(CommonValues.getInstance().XmppConnection);
				FileTransferNegotiator.getInstanceFor(CommonValues.getInstance().XmppConnection);
		        FileTransferNegotiator.setServiceEnabled(CommonValues.getInstance().XmppConnection,true);
				OutgoingFileTransfer transfer = fileTransferManager.createOutgoingFileTransfer(selectedUserGroupUnion.Name+"@"+CommonURL.getInstance().CareIMService+"/Smack");
				transfer.sendFile(file, "test_file");
				while(!transfer.isDone()) {
				   if(transfer.getStatus().equals(Status.error)) {
				   } else if (transfer.getStatus().equals(Status.cancelled)
				                    || transfer.getStatus().equals(Status.refused)) {
				   }				  
				  Thread.sleep(1000L);		  
				}
				if(transfer.getStatus().equals(Status.refused) || transfer.getStatus().equals(Status.error)
				 || transfer.getStatus().equals(Status.cancelled)){
				} else {	
					
				}
				progressDialog.dismiss();*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public String getRealPathFromURI(Uri contentUri) {

		Cursor cursor = getContentResolver()
				.query(contentUri,
						new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
						null, null, null);

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(v.getId()==R.id.etMessageText && keyCode == KeyEvent.KEYCODE_ENTER && 
                event.getAction() == KeyEvent.ACTION_DOWN){	
			if (!CommonTask.isOnline(this) || etMessageText.getText().toString().equals("")) {
				return false;	
			}
			messageText=etMessageText.getText().toString();
			etMessageText.setText("");
			sendMessage(messageText);			
			messageText="";
		}
		return false;
	}
	
	private void sendMessage(String msgText) {
		currentMsgTime=System.currentTimeMillis();
		if(currentMsgTime- previousMsgTime<300){
			return;
		}
		previousMsgTime=currentMsgTime;
		bSendMessage.setEnabled(false);
		selectedCollaboration=new Collaboration();
		selectedCollaboration = new Collaboration();
		selectedCollaboration.UserType=selectedUserGroupUnion.Type;		
		selectedCollaboration.GroupID = selectedUserGroupUnion.Type.equals("G")? selectedUserGroupUnion.ID:0;		
		selectedCollaboration.MsgTo=selectedUserGroupUnion.Type.equals("U")? selectedUserGroupUnion.ID:0;
		selectedCollaboration.MsgFrom = CommonValues.getInstance().LoginUser.UserNumber;
		selectedCollaboration.MsgText = msgText;
		selectedCollaboration.PostedDate=String.valueOf(currentMsgTime);
		selectedCollaboration.Latitude = MyNetService.currentLocation.getLatitude();
		selectedCollaboration.Longitude = MyNetService.currentLocation.getLongitude();	
		
		if(selectedUserGroupUnion.Type.equals("G")){			
			try {
				Message msg =consumerMuc.createMessage();
				msg.setBody(selectedCollaboration.MsgText);
				msg.setProperty("UserType", selectedCollaboration.UserType);
				msg.setProperty("GroupId", selectedCollaboration.GroupID);
				msg.setProperty("UserIdFrom", selectedCollaboration.MsgFrom);
				msg.setProperty("UserIdTo", selectedCollaboration.MsgTo);
				msg.setProperty("PostedDate", selectedCollaboration.PostedDate);
				msg.setProperty("Latitude", selectedCollaboration.Latitude);
				msg.setProperty("Longitude", selectedCollaboration.Longitude);
				
				consumerMuc.sendMessage(msg);				
			} catch (XMPPException e) {				
				e.printStackTrace();
			} 			
		}else{	
			Message msg = new Message(msgTo, Message.Type.chat);
			msg.setBody(selectedCollaboration.MsgText);
			msg.setProperty("UserType", selectedCollaboration.UserType);
			msg.setProperty("GroupId", selectedCollaboration.GroupID);
			msg.setProperty("UserIdFrom", selectedCollaboration.MsgFrom);
			msg.setProperty("UserIdTo", selectedCollaboration.MsgTo);
			msg.setProperty("PostedDate", selectedCollaboration.PostedDate);
			msg.setProperty("Latitude", selectedCollaboration.Latitude);
			msg.setProperty("Longitude", selectedCollaboration.Longitude);
			if (CommonValues.getInstance().XmppConnection != null) {					
				CommonValues.getInstance().XmppConnection.sendPacket(msg);				
			}
			if(!selectedCollaboration.MsgText.equals("'FILE:START'")&&!selectedCollaboration.MsgText.contains("FILESTRING:"))
			{
				Intent i = new Intent(CommonValues.getInstance().XmppReceiveMessageIntent);
				
				i.putExtra("msg", selectedCollaboration);			
						
				sendBroadcast(i);
				MyNetDatabase db=new MyNetDatabase(this);				
				db.createCollaboration(selectedCollaboration);
			}
		}
		
		bSendMessage.setEnabled(true);
		
		
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();		
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
		if(charSequence.length() > 0){
			tvVoiceRecorder.setVisibility(View.GONE);
			bSendMessage.setVisibility(View.VISIBLE);
		}else{
			tvVoiceRecorder.setVisibility(View.VISIBLE);
			bSendMessage.setVisibility(View.GONE);
		}
	}
	
	public class  MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) 
		{		
			Collaboration collaboration = (Collaboration) intent.getSerializableExtra("msg");
			if(chatListAdapter==null){
				chatListAdapter=new ChatListAdapter(CareIMMessegingActivity.this, R.layout.message_layout, new ArrayList<Collaboration>());
				lvChatList.setAdapter(chatListAdapter);
			}
			if(collaboration.MsgText.contains("FILEPATH:")){
				chatListAdapter.getLastItem().MsgText=collaboration.MsgText;
			}else{
				chatListAdapter.addItem(collaboration);
			}
			chatListAdapter.notifyDataSetChanged();
			if(chatListAdapter.getCount()>0){
				lvChatList.setSelection(chatListAdapter.getCount()-1);
			}
		}
		
	}
	
	public static UserGroupUnion getCurrentUser(){
		return selectedUserGroupUnion;
	}

	@Override
	public void showProgressLoader() {
		
		
	}

	@Override
	public void hideProgressLoader() {
		
		
	}

	@Override
	public Object doBackgroundPorcess() {
		ILiveCollaborationManager liveCollaborationManager = new LiveCollaborationManager();
		return liveCollaborationManager.SendLiveCollaboration(selectedCollaboration);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		/*if(data!=null){
			Collaboration collaboration=(Collaboration)data;			
			MyNetDatabase db=new MyNetDatabase(this);
			if(selectedUserGroupUnion.Type.equals("G")){
				collaboration.MsgFromName=  CommonValues.getInstance().LoginUser.Mobile;
				collaboration.GroupName= selectedUserGroupUnion.Type.equals("G")?selectedUserGroupUnion.Name: "";
			}else{
				collaboration.MsgFromName=selectedUserGroupUnion.Name;
				collaboration.GroupName="";
			}
			db.createCollaboration(collaboration);
		}*/
		selectedCollaboration=null;
	};
	
}
