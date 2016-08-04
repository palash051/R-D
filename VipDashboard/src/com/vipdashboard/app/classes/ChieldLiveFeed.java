package com.vipdashboard.app.classes;

import java.util.ArrayList;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.ExperinceLiveActivity;
import com.vipdashboard.app.activities.ExperinceLiveFeedActivity;
import com.vipdashboard.app.adapter.ChieldLiveFeedListAdapter;
import com.vipdashboard.app.adapter.LiveFeedListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.LiveFeed;
import com.vipdashboard.app.entities.LiveFeeds;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.ILiveFeedManager;
import com.vipdashboard.app.manager.LiveFeedManager;
import com.vipdashboard.app.utils.CommonValues;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChieldLiveFeed implements OnClickListener, IAsynchronousTask {
	
	private Context context;
	ListView list;
	EditText tvCommentsText;
	Button bSendComments;
	//ProgressDialog progress;
	private int DOWNLOAD_ALL = 0;
	private int DOWNLOAD_COMMENTS = 1;
	int selectedBackgroundProcess = DOWNLOAD_ALL;
	DownloadableAsyncTask asyncTask;
	int feedId;
	ChieldLiveFeedListAdapter chieldLiveFeedListAdapter;
	LiveFeed liveFeed;
	private final static int FEED_TYPE_EXPERINCE = 1;
	private final static int FEED_TYPE_CALLMEMO = 2;
	private final static int FEED_TYPE_CHECKIN = 3;
	private final static int FEED_TYPE_OFFER = 4;
	
	String filename="";
	int commentCount;
	InputMethodManager imm ;
	
	
	public ChieldLiveFeed(Context _context,int _commentCount){
		this.context = _context;
		commentCount=_commentCount;
		
		imm = (InputMethodManager)this.context.getSystemService(this.context.INPUT_METHOD_SERVICE);
	}
	
	public void getAllChieldLiveFeed(int ID){
		final Dialog dialog = new Dialog(context);		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.livefeed_comments);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE); 		
		feedId = ID;
		Initilization(dialog);		
		dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                    KeyEvent event) {			                    
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    
                	dialog.dismiss();
                }
                return true;
            }
        });
		dialog.show();
		LoadInformation();
	}

	private void Initilization(Dialog dialog) {
		//list = (ListView) dialog.findViewById(R.id.lvFeedChieldList);
		final PullToRefreshListView mPullRefreshListView= (PullToRefreshListView) dialog.findViewById(R.id.pull_refresh_FeedChieldlist);
		tvCommentsText = (EditText) dialog.findViewById(R.id.etCommentsText);
		bSendComments = (Button) dialog.findViewById(R.id.bSendComments);
		bSendComments.setOnClickListener(this);
		
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(context.getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				// Do work to refresh the list here.
				LoadInformation();
				mPullRefreshListView.onRefreshComplete();
			}
		});
		
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(context, "End of List!", Toast.LENGTH_SHORT).show();
			}
		});
		
		list=mPullRefreshListView.getRefreshableView();
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.bSendComments){
			if (tvCommentsText.getText().toString().equals(""))
				return;
			selectedBackgroundProcess = DOWNLOAD_COMMENTS;
			imm.hideSoftInputFromWindow(tvCommentsText.getWindowToken(), 0);
			liveFeed = new LiveFeed();
			liveFeed.FeedText = tvCommentsText.getText().toString();
			liveFeed.FeedType = FEED_TYPE_EXPERINCE;
			liveFeed.UserNumber = CommonValues.getInstance().LoginUser.UserNumber;
			liveFeed.Latitude = MyNetService.currentLocation.getLatitude();
			liveFeed.Longitude = MyNetService.currentLocation.getLongitude();
			tvCommentsText.setText("");
			if (asyncTask != null) {
				asyncTask.cancel(true);
			}
			asyncTask = new DownloadableAsyncTask(this);
			asyncTask.execute();
		}
	}
	
	public void LoadInformation(){
		selectedBackgroundProcess = DOWNLOAD_ALL;
		if(asyncTask != null)
			asyncTask.cancel(true);
		asyncTask = new DownloadableAsyncTask(this);
		asyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		/*progress= ProgressDialog.show(context, "","Processing...", true);
		progress.setIcon(null);	
		progress.setCancelable(false);*/
	}

	@Override
	public void hideProgressLoader() {
		//progress.dismiss();	
	}

	@Override
	public Object doBackgroundPorcess() {
		ILiveFeedManager liveFeedManager = new LiveFeedManager();
		
			
		if(selectedBackgroundProcess == DOWNLOAD_ALL){
			return liveFeedManager.GetAllChieldLiveFeed(feedId);
		}else if(selectedBackgroundProcess == DOWNLOAD_COMMENTS){
			return liveFeedManager.SetLiveFeed(liveFeed, feedId, filename, null);
		}
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			LiveFeeds liveFeeds = (LiveFeeds) data;
			if (liveFeeds.liveFeedList != null
					&& liveFeeds.liveFeedList.size() > 0) {
				chieldLiveFeedListAdapter = new ChieldLiveFeedListAdapter(this.context, 
						R.layout.chield_feed_item_layout, new ArrayList<LiveFeed>(liveFeeds.liveFeedList));
				list.setAdapter(chieldLiveFeedListAdapter);
				commentCount=liveFeeds.liveFeedList.size();
			}
			if (selectedBackgroundProcess == DOWNLOAD_COMMENTS) {
				((ExperinceLiveFeedActivity)context).updateCommentCounter(feedId,commentCount);
				selectedBackgroundProcess = DOWNLOAD_ALL;
				filename="";
			}
		}
	}
}
