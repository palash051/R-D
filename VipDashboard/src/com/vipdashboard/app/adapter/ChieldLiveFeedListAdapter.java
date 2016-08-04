package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.Date;
import com.androidquery.AQuery;
import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.ContactUser;
import com.vipdashboard.app.entities.LiveFeed;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.content.Context;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChieldLiveFeedListAdapter extends ArrayAdapter<LiveFeed> {
	Context context;
	LiveFeed liveFeed;
	ArrayList<LiveFeed> liveFeedList;
	
	public ChieldLiveFeedListAdapter(Context _context, int textViewResourceId,
			ArrayList<LiveFeed> _objects) {
		super(_context, textViewResourceId, _objects);
		context = _context;
		liveFeedList = _objects;
		/*if(CommonValues.getInstance().ConatactUserList==null || CommonValues.getInstance().ConatactUserList.size()==0){
			MyNetDatabase db=new MyNetDatabase(context);
			CommonValues.getInstance().ConatactUserList=db.GetUserHashMap();
		}*/
	}
	
	private class ViewHolder {
		public TextView tvChieldFeedPersonName;
		public ImageView ivChieldFeedPersonImage;
		public TextView tvChieldFeedText;
		public TextView tvChieldFeedTime;
		public TextView tvChieldFeedLike;
	}
	
	public int getCount() {
		return liveFeedList.size();
	}
	
	public LiveFeed getLastItem(){
		return liveFeedList.get(liveFeedList.size()-1);
	}
	public void addItem(LiveFeed item){
		liveFeedList.add(item);
	
	}
	public void addItemOnTop(LiveFeed item){
		liveFeedList.add(0,item);
	
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		View feedItemView=convertView;
		final ViewHolder holder;
		final AQuery aq = new AQuery(feedItemView);
		try {
			liveFeed = liveFeedList.get(position);
			if (convertView == null) {			
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				feedItemView = inflater.inflate(R.layout.chield_feed_item_layout, null);
				holder = new ViewHolder();
				holder.tvChieldFeedPersonName=(TextView)feedItemView.findViewById(R.id.tvChieldFeedPersonName);
				holder.ivChieldFeedPersonImage=(ImageView)feedItemView.findViewById(R.id.ivChieldFeedPersonImage);
				holder.tvChieldFeedText=(TextView)feedItemView.findViewById(R.id.tvChieldFeedText);
				holder.tvChieldFeedTime=(TextView)feedItemView.findViewById(R.id.tvChieldFeedTime);
				holder.tvChieldFeedLike = (TextView) feedItemView.findViewById(R.id.tvChieldFeedLike);
				feedItemView.setTag(holder);
			}else{
				holder = (ViewHolder) feedItemView.getTag();
			}
			
			if(liveFeed.UserNumber==CommonValues.getInstance().LoginUser.UserNumber && CommonValues.getInstance().LoginUser.Facebook_Person != null){
				holder.tvChieldFeedPersonName.setText(CommonValues.getInstance().LoginUser.Facebook_Person.Name);
			 	aq.id(holder.ivChieldFeedPersonImage).image(CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path, CommonValues.getInstance().defaultImageOptions);
			}else{
				ContactUser contact=CommonValues.getInstance().ConatactUserList.get(liveFeed.UserNumber);
				if(contact!=null){
					holder.tvChieldFeedPersonName.setText(contact.Name);
					if(contact.Image!=null)
						holder.ivChieldFeedPersonImage.setImageBitmap(contact.Image);
					else 
						holder.ivChieldFeedPersonImage.setImageResource(R.drawable.user_icon);
				}else{
					holder.tvChieldFeedPersonName.setText("Unknown");
					holder.ivChieldFeedPersonImage.setImageResource(R.drawable.user_icon);
				}
				
				/*ContentResolver cr = context.getContentResolver();
				Cursor cursor = cr.query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, " replace(data1,' ', '') like '%" + liveFeed.FeedUser.Mobile.substring(3) +"%'", null, null);
				
				if(cursor.getCount() > 0){
					cursor.moveToFirst();
					holder.tvChieldFeedPersonName.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
					Bitmap bit=CommonTask.fetchContactImageThumbnail(context,cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID)));
					if(bit!=null)
						holder.ivChieldFeedPersonImage.setImageBitmap(bit);
					else{
						holder.ivChieldFeedPersonImage.setImageResource(R.drawable.user_icon);
					}
				}else{
					holder.tvChieldFeedPersonName.setText(liveFeed.FeedUser.Mobile);
					holder.ivChieldFeedPersonImage.setImageResource(R.drawable.user_icon);
					
				}
				cursor.close();*/
			}
			
			holder.tvChieldFeedTime.setText(DateUtils.getRelativeTimeSpanString(CommonTask.convertJsonDateToLong(liveFeed.DateTime), new Date().getTime(), 0));
			holder.tvChieldFeedText.setText(liveFeed.FeedText);
			
			if(URLUtil.isValidUrl(liveFeed.FeedText)){
				final SpannableString s = 
			               new SpannableString(liveFeed.FeedText);
			  Linkify.addLinks(s, Linkify.WEB_URLS);
			  holder.tvChieldFeedText.setText(s);
			  holder.tvChieldFeedText.setMovementMethod(LinkMovementMethod.getInstance());
		}else{
			holder.tvChieldFeedText.setText(liveFeed.FeedText);
		}
			/*imageLoader.DisplayImage("http://funlava.com/wp-content/uploads/2013/04/Android_Army_by_fetuscakemix.jpg", holder.ivLiveFeedImage);
			holder.tvLiveFeedComments.setTag(liveFeed.FeedID);
			holder.tvLiveFeedComments.setOnClickListener(new View.OnClickListener() {				
				@Override
				public void onClick(View v) {
					ChieldLiveFeed chieldLiveFeed = new ChieldLiveFeed(context);
					int id = (Integer) holder.tvLiveFeedComments.getTag();
					chieldLiveFeed.getAllChieldLiveFeed(id);
				}
			});*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return feedItemView;
	}

}
