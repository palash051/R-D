package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.vipdashboard.app.R;

import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.classes.ChieldLiveFeed;
import com.vipdashboard.app.customcontrols.CustomNetworkImageView;
import com.vipdashboard.app.customcontrols.FeedImageView;
import com.vipdashboard.app.entities.ContactUser;
import com.vipdashboard.app.entities.LiveFeed;
import com.vipdashboard.app.entities.LiveFeedLikeLists;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.ILiveFeedManager;
import com.vipdashboard.app.manager.LiveFeedManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;

public class LiveFeedAdapter extends ArrayAdapter<LiveFeed> implements
IAsynchronousTask{
	Context context;
	public LiveFeed liveFeed;
	ArrayList<LiveFeed> liveFeedList;
	ImageLoader imageLoader = MyNetApplication.getInstance().getImageLoader();
	boolean isLike=true;
	int selectedFeedId = 0;	
	DownloadableAsyncTask downloadableAsyncTask;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	public LiveFeedAdapter(Context _context, int resource,ArrayList<LiveFeed> _liveFeedList) {
		super(_context, resource, _liveFeedList);
		context = _context;
		liveFeedList = _liveFeedList;

	}

	private class ViewHolder {
		public ImageView profilePic;
		public TextView name;
		public TextView tvLiveFeedTime;
		public TextView tvLiveFeedDistance;
		public TextView txtStatusMsg;
		public TextView txtUrl;
		public FeedImageView feedImage1;
		public TextView tvLiveFeedLike;
		public TextView tvLiveFeedLikeCount;
		public TextView tvLiveFeedLikeMore;
		public TextView tvLiveFeedComments;
		public TextView tvLiveFeedCommentCount;
		public ImageView ivLiveFeedLikeProfileImage1;
		public ImageView ivLiveFeedLikeProfileImage2;
		public ImageView ivLiveFeedLikeProfileImage3;
		public ImageView ivLiveFeedLikeProfileImage4;
	}

	public int getCount() {
		return liveFeedList.size();
	}

	public LiveFeed getLastItem() {
		return liveFeedList.get(liveFeedList.size() - 1);
	}

	public LiveFeed getFirstItem() {
		return liveFeedList.get(0);
	}

	public ArrayList<LiveFeed> getAllItems() {
		return liveFeedList;
	}

	public int getItemIndexByFeedId(int feedId) {
		for (int index = 0; index < liveFeedList.size(); index++) {
			if (liveFeedList.get(index).FeedID == feedId)
				return index;
		}
		return -1;
	}

	public LiveFeed getItemByFeedId(int feedId) {
		for (LiveFeed liveFeed : liveFeedList) {
			if (liveFeed.FeedID == feedId)
				return liveFeed;
		}
		return null;
	}

	public void addItem(LiveFeed item) {
		liveFeedList.add(item);

	}

	public void addItemOnTop(LiveFeed item) {
		liveFeedList.add(0, item);

	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View feedItemView = convertView;

		final ViewHolder holder;

		try {
			liveFeed = liveFeedList.get(position);
			if (convertView == null) {

				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				feedItemView = inflater.inflate(R.layout.live_feed_item_layout,null);
				holder = new ViewHolder();
				holder.profilePic=(ImageView) feedItemView.findViewById(R.id.profilePic);
				holder.name=(TextView) feedItemView.findViewById(R.id.name);
				holder.tvLiveFeedTime=(TextView) feedItemView.findViewById(R.id.tvLiveFeedTime);
				holder.tvLiveFeedDistance=(TextView) feedItemView.findViewById(R.id.tvLiveFeedDistance);
				holder.txtStatusMsg=(TextView) feedItemView.findViewById(R.id.txtStatusMsg);
				holder.txtUrl=(TextView) feedItemView.findViewById(R.id.txtUrl);
				holder.feedImage1=(FeedImageView) feedItemView.findViewById(R.id.feedImage1);
				holder.tvLiveFeedLike=(TextView) feedItemView.findViewById(R.id.tvLiveFeedLike);
				holder.tvLiveFeedLikeCount=(TextView) feedItemView.findViewById(R.id.tvLiveFeedLikeCount);
				holder.tvLiveFeedLikeMore=(TextView) feedItemView.findViewById(R.id.tvLiveFeedLikeMore);
				holder.tvLiveFeedComments=(TextView) feedItemView.findViewById(R.id.tvLiveFeedComments);
				holder.tvLiveFeedCommentCount=(TextView) feedItemView.findViewById(R.id.tvLiveFeedCommentCount);
				holder.ivLiveFeedLikeProfileImage1=(ImageView) feedItemView.findViewById(R.id.ivLiveFeedLikeProfileImage1);
				holder.ivLiveFeedLikeProfileImage2=(ImageView) feedItemView.findViewById(R.id.ivLiveFeedLikeProfileImage2);
				holder.ivLiveFeedLikeProfileImage3=(ImageView) feedItemView.findViewById(R.id.ivLiveFeedLikeProfileImage3);
				holder.ivLiveFeedLikeProfileImage4=(ImageView) feedItemView.findViewById(R.id.ivLiveFeedLikeProfileImage4);
				feedItemView.setTag(holder);
				
				holder.tvLiveFeedComments
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						ChieldLiveFeed chieldLiveFeed = new ChieldLiveFeed(
								context, Integer.valueOf(String.valueOf( holder.tvLiveFeedCommentCount.getTag())));
						selectedFeedId = (Integer) v.getTag();
						chieldLiveFeed
								.getAllChieldLiveFeed(selectedFeedId);

					}
				});
				
				holder.profilePic.setOnClickListener(new View.OnClickListener() {					
					@Override
					public void onClick(View v) {
						/*ContactUser user=CommonValues.getInstance().ConatactUserList.get(Integer.parseInt(String.valueOf(v.getTag())));
						UserGroupUnion ugu=new UserGroupUnion();
						ugu.ID=user.ID;
						ugu.Name=user.PhoneNumber;	
						ugu.Type="U";
						ContactActivity.selectUserGroupUnion=ugu;
						Intent intent = new Intent(context, ContactActivity.class);											
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						context.startActivity(intent);*/
					}
				});
				
				holder.tvLiveFeedLike.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {	
						String[] tag=((String) v.getTag()).split("~");
						selectedFeedId =  Integer.valueOf(tag[0]);
						v.setEnabled(false);
						if(tag[1].equalsIgnoreCase("LIKE")){
							isLike=true;
						}else{
							isLike=false;
						}
						if (downloadableAsyncTask != null) {
							downloadableAsyncTask.cancel(true);
						}
						downloadableAsyncTask = new DownloadableAsyncTask(
								LiveFeedAdapter.this);
						downloadableAsyncTask.execute();						
					}
					
				});
				
				
				holder.feedImage1.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						final Dialog nagDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
			            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			            nagDialog.setCancelable(false);
			            nagDialog.setContentView(R.layout.preview_image);			            
			            CustomNetworkImageView ivPreview = (CustomNetworkImageView)nagDialog.findViewById(R.id.iv_preview_image);
			            ivPreview.setImageUrl((String) ((ImageView)v).getTag(), imageLoader);
			            //ivPreview.setScaleType(ScaleType.FIT_XY);

			            ivPreview.setOnClickListener(new View.OnClickListener() {
			                @Override
			                public void onClick(View arg0) {

			                    nagDialog.dismiss();
			                }
			            });
			            ImageView ivPreviewClose = (ImageView)nagDialog.findViewById(R.id.iv_preview_image_close);
			            ivPreviewClose.setOnClickListener(new View.OnClickListener() {
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
				
				
			}else {
				holder = (ViewHolder) feedItemView.getTag();				
			}
			
			
			holder.tvLiveFeedTime.setText((String) DateUtils.getRelativeTimeSpanString(CommonTask.convertJsonDateToLong(liveFeed.DateTime), new Date().getTime(), 0));
			int distence= CommonTask.distanceCalculationInMeter(MyNetService.currentLocation.getLatitude(), MyNetService.currentLocation.getLongitude(),liveFeed.Latitude , liveFeed.Longitude);
			distence=distence/1000;
			holder.tvLiveFeedDistance.setText(distence<2?"within one km":String.valueOf(distence)+" km");
			if (URLUtil.isValidUrl(liveFeed.FeedText)) {
				holder.txtUrl.setVisibility(View.VISIBLE);
				holder.txtUrl.setText(Html.fromHtml("<a href=\"" + liveFeed.FeedText + "\">"+ liveFeed.FeedText + "</a> "));
				holder.txtUrl.setMovementMethod(LinkMovementMethod.getInstance());
			} else {
				holder.txtStatusMsg.setVisibility(View.VISIBLE);
				holder.txtStatusMsg.setText(liveFeed.FeedText);
			}
			
			/*holder.tvLiveFeedLike.setText("");
			holder.tvLiveFeedLikeCount.setText("");
			holder.tvLiveFeedLikeMore.setText("");
			holder.tvLiveFeedComments.setText("");
			holder.tvLiveFeedCommentCount.setText("");*/
			
			holder.tvLiveFeedComments.setTag(liveFeed.FeedID);
			holder.tvLiveFeedLike.setTag(liveFeed.FeedID+"~LIKE");
			holder.profilePic.setTag(liveFeed.UserNumber);
			holder.tvLiveFeedCommentCount.setTag(liveFeed.CommentCount);
			holder.tvLiveFeedLike.setEnabled(true);			
			holder.feedImage1.setVisibility(View.GONE);
			if (liveFeed.LiveFeedAttachments != null
					&& liveFeed.LiveFeedAttachments.size() > 0) {
				if (liveFeed.LiveFeedAttachments.get(0).FilePath.length() > 0) {
					String filePath = CommonURL.getInstance().getCareImageServer
							+ liveFeed.LiveFeedAttachments.get(0).FilePath;
					String extension = FilenameUtils.getExtension(filePath);
					if (extension.equals("jpg") || extension.equals("png")
							|| extension.equals("gif")
							|| extension.equals("jpeg")) {						
						holder.feedImage1.setImageUrl(filePath, imageLoader);
						holder.feedImage1.setVisibility(View.VISIBLE);
						holder.feedImage1.setResponseObserver(new FeedImageView.ResponseObserver() {
									@Override
									public void onError() {
									}

									@Override
									public void onSuccess() {
									}
								});
						
						//holder.ivLiveFeedImage.setImageBitmap(CommonValues.getInstance().imageLoader.loadImageSync(filePath, targetSize, CommonValues.getInstance().imageOptions));
						holder.feedImage1.setTag(filePath);						
					} else {
						/*holder.tvLiveFeedTextSub.setVisibility(View.VISIBLE);
						final SpannableString s = new SpannableString(filePath);
						Linkify.addLinks(s, Linkify.WEB_URLS);
						holder.tvLiveFeedTextSub.setText(s);
						holder.tvLiveFeedTextSub.setMovementMethod(LinkMovementMethod.getInstance());*/
					}
				}
			}
			
			holder.tvLiveFeedLikeCount.setText("");
			holder.tvLiveFeedLikeCount.setVisibility(View.GONE);
			holder.tvLiveFeedLike.setBackgroundResource(R.drawable.feed_like);
			holder.ivLiveFeedLikeProfileImage1.setVisibility(View.GONE);
			holder.ivLiveFeedLikeProfileImage2.setVisibility(View.GONE);
			holder.ivLiveFeedLikeProfileImage3.setVisibility(View.GONE);
			holder.ivLiveFeedLikeProfileImage4.setVisibility(View.GONE);
			
			holder.tvLiveFeedLikeMore.setVisibility(View.GONE);
			if (liveFeed.LiveFeedLikeLists != null
					&& liveFeed.LiveFeedLikeLists.size() > 0) {
				ContactUser likeUser;
				holder.ivLiveFeedLikeProfileImage1.setImageResource(R.drawable.user_icon);
				holder.ivLiveFeedLikeProfileImage2.setImageResource(R.drawable.user_icon);
				holder.ivLiveFeedLikeProfileImage3.setImageResource(R.drawable.user_icon);
				holder.ivLiveFeedLikeProfileImage4.setImageResource(R.drawable.user_icon);
				for (int i=0;i<liveFeed.LiveFeedLikeLists.size();i++) {
					likeUser= CommonValues.getInstance().ConatactUserList.get(liveFeed.LiveFeedLikeLists.get(i).UserNumber);
					if(i==0){
						holder.ivLiveFeedLikeProfileImage1.setVisibility(View.VISIBLE);
					}else if(i==1){
						holder.ivLiveFeedLikeProfileImage2.setVisibility(View.VISIBLE);
					}else if(i==2){
						holder.ivLiveFeedLikeProfileImage3.setVisibility(View.VISIBLE);
					}else if(i==3){
						holder.ivLiveFeedLikeProfileImage4.setVisibility(View.VISIBLE);
					}
					if (liveFeed.LiveFeedLikeLists.get(i).UserNumber == CommonValues.getInstance().LoginUser.UserNumber) {
						holder.tvLiveFeedLike.setBackgroundResource(R.drawable.feed_unlike);	
						holder.tvLiveFeedLike.setTag(liveFeed.FeedID+"~UNLIKE");
						if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
							if(i==0){
								CommonValues.getInstance().imageLoader.displayImage(CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path, holder.ivLiveFeedLikeProfileImage1, CommonValues.getInstance().imageOptions, animateFirstListener);
							}else if(i==1){
								CommonValues.getInstance().imageLoader.displayImage(CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path, holder.ivLiveFeedLikeProfileImage2, CommonValues.getInstance().imageOptions, animateFirstListener);
							}else if(i==2){
								CommonValues.getInstance().imageLoader.displayImage(CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path, holder.ivLiveFeedLikeProfileImage3, CommonValues.getInstance().imageOptions, animateFirstListener);
							}else if(i==3){
								CommonValues.getInstance().imageLoader.displayImage(CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path, holder.ivLiveFeedLikeProfileImage4, CommonValues.getInstance().imageOptions, animateFirstListener);
							}
						} else if(likeUser!=null)
						  {
							if(i==0){
								holder.ivLiveFeedLikeProfileImage1.setImageBitmap(likeUser.Image);
							}else if(i==1){
								holder.ivLiveFeedLikeProfileImage2.setImageBitmap(likeUser.Image);
							}else if(i==2){
								holder.ivLiveFeedLikeProfileImage3.setImageBitmap(likeUser.Image);
							}else if(i==3){
								holder.ivLiveFeedLikeProfileImage4.setImageBitmap(likeUser.Image);
							}
						  }
					}else{
						int photoId = CommonTask.getContentPhotoId(context, likeUser.PhoneNumber);						
						if(i==0){
							if(photoId>0)
								holder.ivLiveFeedLikeProfileImage1.setImageBitmap(CommonTask.fetchContactImageThumbnail(context, photoId));
							else
								holder.ivLiveFeedLikeProfileImage1.setImageResource(R.drawable.user_icon);
						}else if(i==1 ){
							if(photoId>0)
								holder.ivLiveFeedLikeProfileImage2.setImageBitmap(CommonTask.fetchContactImageThumbnail(context, photoId));
							else
								holder.ivLiveFeedLikeProfileImage1.setImageResource(R.drawable.user_icon);
						}else if(i==2){
							if(photoId>0)
								holder.ivLiveFeedLikeProfileImage3.setImageBitmap(CommonTask.fetchContactImageThumbnail(context, photoId));
							else
								holder.ivLiveFeedLikeProfileImage1.setImageResource(R.drawable.user_icon);
						}else if(i==3){									
							if(photoId>0)
								holder.ivLiveFeedLikeProfileImage4.setImageBitmap(CommonTask.fetchContactImageThumbnail(context, photoId));
							else
								holder.ivLiveFeedLikeProfileImage1.setImageResource(R.drawable.user_icon);
						}
					}
				}
				
				if(liveFeed.LiveFeedLikeLists.size()>4){
					holder.tvLiveFeedLikeCount.setText(String.valueOf(liveFeed.LiveFeedLikeLists.size()-4));
					holder.tvLiveFeedLikeCount.setVisibility(View.VISIBLE);
					holder.tvLiveFeedLikeMore.setVisibility(View.VISIBLE);
				}
			}
			
			ContactUser user=null;
			if(CommonValues.getInstance().ConatactUserList!=null && CommonValues.getInstance().ConatactUserList.size()>0){
				user=CommonValues.getInstance().ConatactUserList.get(liveFeed.UserNumber);				
			}
			
			/*if(liveFeed.UserNumber==CommonValues.getInstance().LoginUser.UserNumber){
				if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
					holder.name.setText(CommonValues.getInstance().LoginUser.Facebook_Person.Name);						
					holder.profilePic.setImageUrl(CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path, imageLoader);
				}					 
				else if(user!=null)
				{
					holder.name.setText(user.Name);
					if(user.Image!=null)
						holder.profilePic.setLocalImageBitmap(user.Image);	
					else
						holder.profilePic.setImageResource(R.drawable.user_icon);	
				}else{
					holder.name.setText(CommonValues.getInstance().LoginUser.Mobile);
					holder.profilePic.setDefaultImageResId(R.drawable.user_icon);
				}
			}else{
				if(user!=null){
					holder.name.setText(user.Name);
					if(user.Image!=null)
						holder.profilePic.setLocalImageBitmap(user.Image);	
					else
						holder.profilePic.setDefaultImageResId(R.drawable.user_icon);
				}else{					
					holder.profilePic.setDefaultImageResId(R.drawable.user_icon);
				}
			}
			holder.profilePic.setDefaultImageResId(R.drawable.user_icon);
			holder.profilePic.setErrorImageResId(R.drawable.user_icon);*/
			
			
			if(liveFeed.UserNumber!=CommonValues.getInstance().LoginUser.UserNumber){				
				if(user!=null){
					holder.name.setText(user.Name);
					int photoId = CommonTask.getContentPhotoId(context, user.PhoneNumber);
					if (photoId > 0) {						
						holder.profilePic.setImageBitmap(CommonTask.fetchContactImageThumbnail(context, photoId));
					} else {
						holder.profilePic.setImageResource(R.drawable.user_icon);
					}					
					/*if(user.Image!=null)
						holder.profilePic.setImageBitmap(user.Image);	
					else
						holder.profilePic.setImageResource(R.drawable.user_icon);*/
				}else{
					holder.name.setText("Unknown");
					holder.profilePic.setImageResource(R.drawable.user_icon);
				}
			}else{

				
				 if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
					 	holder.name.setText(CommonValues.getInstance().LoginUser.Facebook_Person.Name);
					 	CommonValues.getInstance().imageLoader.displayImage(CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path, holder.profilePic, CommonValues.getInstance().imageOptions, animateFirstListener);
				  }					 
				  else if(user!=null)
				  {
					  	holder.name.setText(user.Name);
						if(user.Image!=null)
							holder.profilePic.setImageBitmap(user.Image);	
						else
							holder.profilePic.setImageResource(R.drawable.user_icon);		
				  }else{
					  holder.name.setText(CommonValues.getInstance().LoginUser.Mobile);
					  holder.profilePic.setImageResource(R.drawable.user_icon);
				  }
			}
			
			holder.tvLiveFeedCommentCount.setText("");			
			holder.tvLiveFeedCommentCount.setVisibility(View.GONE);
			if(liveFeed.CommentCount>0){
				holder.tvLiveFeedCommentCount.setText(String.valueOf(liveFeed.CommentCount));
				holder.tvLiveFeedCommentCount.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return feedItemView;
	}
	
	
	@Override
	public void showProgressLoader() {
		/*progress= ProgressDialog.show(context, "","Processing...", true);
		progress.setIcon(null);	*/
	}

	@Override
	public void hideProgressLoader() {
		//progress.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		ILiveFeedManager liveFeedManager = new LiveFeedManager();
		if(isLike)
			return liveFeedManager.SetLikeList(selectedFeedId);
		else
			return liveFeedManager.SetUnLikeList(selectedFeedId);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			
			LiveFeedLikeLists liveFeedLikeLists =(LiveFeedLikeLists) data;
			if(liveFeedLikeLists.liveFeedLikeList!=null && liveFeedLikeLists.liveFeedLikeList.size()>0){
				for (LiveFeed liveFeed : liveFeedList) {
					if(liveFeed.FeedID==liveFeedLikeLists.liveFeedLikeList.get(0).FeedID){
						liveFeed.LiveFeedLikeLists= liveFeedLikeLists.liveFeedLikeList;
						notifyDataSetChanged();
						break;
					}	
				}	
			}else{
				for (LiveFeed liveFeed : liveFeedList) {
					if(liveFeed.FeedID==selectedFeedId){
						liveFeed.LiveFeedLikeLists.clear();
						notifyDataSetChanged();
						break;
					}	
				}
			}
			
			
		}
	}
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
