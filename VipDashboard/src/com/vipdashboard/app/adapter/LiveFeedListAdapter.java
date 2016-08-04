package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.apache.commons.io.FilenameUtils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.vipdashboard.app.R;

import com.vipdashboard.app.activities.ContactActivity;
import com.vipdashboard.app.activities.PlayVoiceMemoActivity;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.classes.ChieldLiveFeed;
import com.vipdashboard.app.customcontrols.FeedImageView;
import com.vipdashboard.app.entities.ContactUser;
import com.vipdashboard.app.entities.LiveFeed;
import com.vipdashboard.app.entities.LiveFeedLikeLists;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.ILiveFeedManager;
import com.vipdashboard.app.manager.LiveFeedManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;

public class LiveFeedListAdapter extends ArrayAdapter<LiveFeed> implements
		IAsynchronousTask {
	Context context;
	public LiveFeed liveFeed;
	ArrayList<LiveFeed> liveFeedList;
	ArrayList<LiveFeed> liveFeedListFilter;
	DownloadableAsyncTask downloadableAsyncTask;		
	ImageSize targetSize = new ImageSize(300, 200);
	int selectedFeedId = 0;
	int selectedPosition=-1;
	ImageLoader imageLoader = MyNetApplication.getInstance().getImageLoader();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	boolean isLike=true;
	//HashMap<Integer, ContactUser>conatactList;
	public LiveFeedListAdapter(Context _context, int resource,
			ArrayList<LiveFeed> _liveFeedList) {
		super(_context, resource, _liveFeedList);
		context = _context;
		liveFeedList = _liveFeedList;
		liveFeedListFilter = new ArrayList<LiveFeed>();
		liveFeedListFilter.addAll(liveFeedList);		
		/*if(CommonValues.getInstance().ConatactUserList==null || CommonValues.getInstance().ConatactUserList.size()==0){
			MyNetDatabase db=new MyNetDatabase(context);
			CommonValues.getInstance().ConatactUserList=db.GetUserHashMap();
		}*/
	}

	private class ViewHolder {
		public TextView tvLiveFeedUser;
		public ImageView ivLiveFeedUserImage;
		public com.vipdashboard.app.customcontrols.FeedImageView ivLiveFeedImage;
		public TextView tvLiveFeedTime;
		public TextView tvLiveFeedText;
		public TextView tvLiveFeedComments;
		public TextView tvLiveFeedLike;
		public TextView tvLiveFeedLikeCount;
		public TextView tvLiveFeedCommentCount;
		public TextView tvLiveFeedTextSub;
		public ImageView ivPlayVoiceRecord;
		public ImageView ivLiveFeedLikeProfileImage1;
		public ImageView ivLiveFeedLikeProfileImage2;
		public ImageView ivLiveFeedLikeProfileImage3;
		public ImageView ivLiveFeedLikeProfileImage4;
		public TextView tvLiveFeedLikeMore;
		public TextView  tvLiveFeedDistance;		
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
		for (int index=0 ;index< liveFeedList.size();index++) {
			if(liveFeedList.get(index).FeedID==feedId)
				return index;
		}
		return -1;
	}
	
	public LiveFeed getItemByFeedId(int feedId) {
		for (LiveFeed liveFeed : liveFeedList) {
			if(liveFeed.FeedID==feedId)
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
				feedItemView = inflater
						.inflate(R.layout.feed_item_layout, null);				
				holder = new ViewHolder();
				holder.tvLiveFeedUser = (TextView) feedItemView
						.findViewById(R.id.tvLiveFeedUser);
				holder.tvLiveFeedTime = (TextView) feedItemView
						.findViewById(R.id.tvLiveFeedTime);
				holder.tvLiveFeedText = (TextView) feedItemView
						.findViewById(R.id.tvLiveFeedText);
				holder.ivLiveFeedImage = (FeedImageView) feedItemView
						.findViewById(R.id.ivLiveFeedImage);
				holder.tvLiveFeedComments = (TextView) feedItemView
						.findViewById(R.id.tvLiveFeedComments);
				holder.tvLiveFeedLike = (TextView) feedItemView
						.findViewById(R.id.tvLiveFeedLike);
				holder.tvLiveFeedLikeCount = (TextView) feedItemView
						.findViewById(R.id.tvLiveFeedLikeCount);
				holder.tvLiveFeedTextSub = (TextView) feedItemView						
						.findViewById(R.id.tvLiveFeedTextSub);
				holder.ivPlayVoiceRecord = (ImageView)feedItemView
						.findViewById(R.id.ivPlayVoiceRecord);
				holder.tvLiveFeedCommentCount= (TextView) feedItemView						
						.findViewById(R.id.tvLiveFeedCommentCount);
				
				holder.tvLiveFeedLikeMore= (TextView) feedItemView.findViewById(R.id.tvLiveFeedLikeMore);
				holder.ivLiveFeedLikeProfileImage1 =(ImageView) feedItemView.findViewById(R.id.ivLiveFeedLikeProfileImage1);
				holder.ivLiveFeedLikeProfileImage2 =(ImageView) feedItemView.findViewById(R.id.ivLiveFeedLikeProfileImage2);
				holder.ivLiveFeedLikeProfileImage3 =(ImageView) feedItemView.findViewById(R.id.ivLiveFeedLikeProfileImage3);
				holder.ivLiveFeedLikeProfileImage4 =(ImageView) feedItemView.findViewById(R.id.ivLiveFeedLikeProfileImage4);
				holder.ivLiveFeedUserImage=(ImageView) feedItemView.findViewById(R.id.ivLiveFeedUserImage);
				holder.tvLiveFeedDistance=(TextView) feedItemView.findViewById(R.id.tvLiveFeedDistance);
				feedItemView.setTag(holder);
				
				holder.tvLiveFeedComments
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								ChieldLiveFeed chieldLiveFeed = new ChieldLiveFeed(context, Integer.valueOf(String.valueOf( holder.tvLiveFeedCommentCount.getTag())));
								selectedFeedId = (Integer) v.getTag();
								chieldLiveFeed.getAllChieldLiveFeed(selectedFeedId);

							}
						});
				
				holder.ivLiveFeedUserImage.setOnClickListener(new View.OnClickListener() {					
					@Override
					public void onClick(View v) {
						ContactUser user=CommonValues.getInstance().ConatactUserList.get(Integer.parseInt(String.valueOf(v.getTag())));
						UserGroupUnion ugu=new UserGroupUnion();
						ugu.ID=user.ID;
						ugu.Name=user.PhoneNumber;	
						ugu.Type="U";
						ContactActivity.selectUserGroupUnion=ugu;
						Intent intent = new Intent(context, ContactActivity.class);											
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						context.startActivity(intent);
					}
				});

				holder.tvLiveFeedLike
						.setOnClickListener(new View.OnClickListener() {
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
										LiveFeedListAdapter.this);
								downloadableAsyncTask.execute();						
							}
							
						});
				holder.ivLiveFeedImage.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						final Dialog nagDialog = new Dialog(context,android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
			            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); 
			            nagDialog.setCancelable(false);
			            nagDialog.setContentView(R.layout.preview_image);			            
			            ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);			            
			            CommonValues.getInstance().imageLoader.displayImage((String) ((ImageView)v).getTag(), ivPreview, CommonValues.getInstance().imageOptions, animateFirstListener);
			            ivPreview.setScaleType(ScaleType.FIT_XY);

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

			} else {
				holder = (ViewHolder) feedItemView.getTag();				
			}
			
			holder.tvLiveFeedComments.setTag(liveFeed.FeedID);
			holder.tvLiveFeedLike.setTag(liveFeed.FeedID+"~LIKE");
			holder.tvLiveFeedLike.setEnabled(true);
			ContactUser user=CommonValues.getInstance().ConatactUserList.get(liveFeed.UserNumber);
			if(liveFeed.UserNumber!=CommonValues.getInstance().LoginUser.UserNumber){				
				if(user!=null){
					holder.tvLiveFeedUser.setText(user.Name);
					if(user.Image!=null)
						holder.ivLiveFeedUserImage.setImageBitmap(user.Image);	
					else
						holder.ivLiveFeedUserImage.setImageResource(R.drawable.user_icon);
				}
				
			}else{
				
				 if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
					 	holder.tvLiveFeedUser.setText(CommonValues.getInstance().LoginUser.Facebook_Person.Name);
					 	CommonValues.getInstance().imageLoader.displayImage(CommonValues.getInstance().LoginUser.Facebook_Person.PP_Path, holder.ivLiveFeedUserImage, CommonValues.getInstance().imageOptions, animateFirstListener);
				  }					 
				  else if(user!=null)
				  {
					  	holder.tvLiveFeedUser.setText(user.Name);
						if(user.Image!=null)
							holder.ivLiveFeedUserImage.setImageBitmap(user.Image);	
						else
							holder.ivLiveFeedUserImage.setImageResource(R.drawable.user_icon);		
					  
					  
				  }else{
					  holder.tvLiveFeedUser.setText(CommonValues.getInstance().LoginUser.Mobile);
					  holder.ivLiveFeedUserImage.setImageResource(R.drawable.user_icon);
				  }
			}
			
			
			
			holder.ivLiveFeedUserImage.setTag(liveFeed.UserNumber);
			holder.tvLiveFeedTime.setText((String) DateUtils.getRelativeTimeSpanString(CommonTask.convertJsonDateToLong(liveFeed.DateTime), new Date().getTime(), 0));
			int distence= CommonTask.distanceCalculationInMeter(MyNetService.currentLocation.getLatitude(), MyNetService.currentLocation.getLongitude(),liveFeed.Latitude , liveFeed.Longitude);
			distence=distence/1000;
			holder.tvLiveFeedDistance.setText(distence<2?"within one km":String.valueOf(distence)+" km");
			
			holder.tvLiveFeedTextSub.setVisibility(View.GONE);
			holder.ivPlayVoiceRecord.setVisibility(View.GONE);
			holder.ivLiveFeedImage.setVisibility(View.GONE);

			if (URLUtil.isValidUrl(liveFeed.FeedText)) {
				final SpannableString s = new SpannableString(liveFeed.FeedText);
				Linkify.addLinks(s, Linkify.WEB_URLS);
				holder.tvLiveFeedText.setText(s);
				holder.tvLiveFeedText.setMovementMethod(LinkMovementMethod
						.getInstance());		
				
			} else {
				holder.tvLiveFeedText.setText(liveFeed.FeedText);
			}
			if (liveFeed.LiveFeedAttachments != null
					&& liveFeed.LiveFeedAttachments.size() > 0) {
				if (liveFeed.LiveFeedAttachments.get(0).FilePath.length() > 0) {
					String filePath = CommonURL.getInstance().getCareImageServer
							+ liveFeed.LiveFeedAttachments.get(0).FilePath;
					String extension = FilenameUtils.getExtension(filePath);
					if (extension.equals("jpg") || extension.equals("png")
							|| extension.equals("gif")
							|| extension.equals("jpeg")) {						
						
						/*ImageSize targetSize = new ImageSize(300, 200); // result Bitmap will be fit to this size
						
						CommonValues.getInstance().imageLoader.loadImage(filePath, targetSize, CommonValues.getInstance().imageOptions, new SimpleImageLoadingListener() {
						    @Override
						    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						    	holder.ivLiveFeedImage.setVisibility(View.VISIBLE);
						    	holder.ivLiveFeedImage.setImageBitmap(loadedImage);
						    }
						});*/
						
						holder.ivLiveFeedImage.setImageUrl(filePath, imageLoader);
						holder.ivLiveFeedImage.setVisibility(View.VISIBLE);
						holder.ivLiveFeedImage
								.setResponseObserver(new FeedImageView.ResponseObserver() {
									@Override
									public void onError() {
									}

									@Override
									public void onSuccess() {
									}
								});
						
						//holder.ivLiveFeedImage.setImageBitmap(CommonValues.getInstance().imageLoader.loadImageSync(filePath, targetSize, CommonValues.getInstance().imageOptions));
						holder.ivLiveFeedImage.setTag(filePath);						
					} else {
						holder.tvLiveFeedTextSub.setVisibility(View.VISIBLE);
						final SpannableString s = new SpannableString(filePath);
						Linkify.addLinks(s, Linkify.WEB_URLS);
						holder.tvLiveFeedTextSub.setText(s);
						holder.tvLiveFeedTextSub.setMovementMethod(LinkMovementMethod.getInstance());
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
						
						if(i==0){
							if(likeUser!=null && likeUser.Image!=null)
								holder.ivLiveFeedLikeProfileImage1.setImageBitmap(likeUser.Image);
							else
								holder.ivLiveFeedLikeProfileImage1.setImageResource(R.drawable.user_icon);
						}else if(i==1 ){
							if(likeUser!=null && likeUser.Image!=null)
								holder.ivLiveFeedLikeProfileImage2.setImageBitmap(likeUser.Image);
							else
								holder.ivLiveFeedLikeProfileImage1.setImageResource(R.drawable.user_icon);
						}else if(i==2){
							if(likeUser!=null && likeUser.Image!=null)
								holder.ivLiveFeedLikeProfileImage3.setImageBitmap(likeUser.Image);
							else
								holder.ivLiveFeedLikeProfileImage1.setImageResource(R.drawable.user_icon);
						}else if(i==3){									
							if(likeUser!=null && likeUser.Image!=null)
								holder.ivLiveFeedLikeProfileImage4.setImageBitmap(likeUser.Image);
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
			holder.tvLiveFeedTextSub.setVisibility(View.GONE);
			holder.ivPlayVoiceRecord.setVisibility(View.GONE);
			if(liveFeed.LiveFeedWebLinks!=null&& liveFeed.LiveFeedWebLinks.size()>0){
				String url=liveFeed.LiveFeedWebLinks.get(0).Link;
				if(url.contains(".mp3")){
					holder.tvLiveFeedTextSub.setVisibility(View.VISIBLE);
					holder.ivPlayVoiceRecord.setVisibility(View.VISIBLE);
					holder.tvLiveFeedTextSub.setText("One voice have to play");
					holder.ivPlayVoiceRecord.setTag(url);
					holder.ivPlayVoiceRecord.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							/*VoiceCallPlayer voiceCallPlayer = new VoiceCallPlayer(context);
							voiceCallPlayer.PlayVoiceCallRecorder(view.getTag());*/
							String voicePath = (String) view.getTag();
							Intent intent = new Intent(context, PlayVoiceMemoActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
							intent.putExtra("VOICE_PATH", voicePath);
							context.startActivity(intent);
						}
					});
				}
			}
			
			holder.tvLiveFeedCommentCount.setText("");
			holder.tvLiveFeedCommentCount.setTag(liveFeed.CommentCount);
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
	
	public void LiveFeedFilter(String charText){
		charText = charText.toLowerCase(Locale.getDefault());
		liveFeedList.clear();
		if (charText.length() == 0) {
			liveFeedList.addAll(liveFeedListFilter);
		}else{
			for (LiveFeed liveFeed : liveFeedListFilter) {
				if(liveFeed.FeedText.toLowerCase(Locale.getDefault()).contains(charText)){
					liveFeedList.add(liveFeed);
				}else if(liveFeed.FeedUser.FullName.toLowerCase(Locale.getDefault()).contains(charText)){
					liveFeedList.add(liveFeed);
				}
			}
			notifyDataSetChanged();
		}
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
