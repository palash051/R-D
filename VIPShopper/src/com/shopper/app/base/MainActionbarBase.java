package com.shopper.app.base;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnSuggestionListener;
import com.google.zxing.client.android.CaptureActivity;
import com.shopper.app.R;
import com.shopper.app.activities.Basket;
import com.shopper.app.activities.Catalogue;
import com.shopper.app.activities.Home;
import com.shopper.app.activities.More;
import com.shopper.app.activities.Search;
import com.shopper.app.activities.Search.StateSearch;
import com.shopper.app.activities.SearchDetails;
import com.shopper.app.adapters.CustomCursorAdapter;
import com.shopper.app.entities.ArticleForSearch;
import com.shopper.app.entities.ArticleInqueriesSearchManager;
import com.shopper.app.entities.AsyncSearchTaskMain;
import com.shopper.app.entities.FamilyInquieryRoot;
import com.shopper.app.utils.CommonBasketValues;
import com.shopper.app.utils.CommonConstraints;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonURL;
import com.shopper.app.utils.CommonValues;
import com.shopper.app.utils.JSONfunctions;

/**
 * @author Tanvir Ahmed Chowdhury 
 * used for loading sherlock action bar,set
 * Application Theme and also describing menu functions,starting
 * activities,validating server configuration if no valid IP and shop
 * number is set in settigs screen related error message will dispalyed
 * and prompt user for providing valid server address.Also starts
 * various activities when user click on realted menu items.Also go home
 * method of this class is working for return back to homescreen from
 * any stage of application when necessary.
 */

public class MainActionbarBase extends SherlockFragmentActivity implements SearchView.OnQueryTextListener,OnClickListener,Observer {
	public static final int INITAIL_STATE = -1;
	public static final int SCANNING_ACTIVITY = 0,BASKET_ACTIVITY= 1,SEARCH_ACTIVITY=2,CATALOGUE_ACTIVITY=3,LOGIN_ACTIVITY=4,SEARCH_DETAILS_ACTIVITY=5;
	
	public static final int THEME = R.style.AppTheme;
	public static com.actionbarsherlock.app.ActionBar mSupportActionBar;
	public static Intent capture, basket, search, catalogue, more,searchDetails;
	private static boolean selectBasket = false, selectScan = false;
	public static TextView basketTotal, basketTotal1, basketTotal2,
			basketTotal3, basketTotal4, basketTotal5, basketCount,
			basketCount1, basketCount2, basketCount3, basketCount4,
			basketCount5,basketTotal6,basketCount6;
	public static int currentMenuIndex = INITAIL_STATE;
	
	public static Stack<String> stackIndex;

	public static CaptureActivity scanner;
	private InputMethodManager imm;
	
	public Menu actionBarMenu;
	public ProgressBar abs__search_progress_bar;
	
	AsyncSearchTaskMain asyncSearchWidgetTask = null;
    List<ArticleForSearch> articleInquierySearchWidgetList = null;
    AutoCompleteTextView searchAutoCompleteTextView;

    private Handler updateBasketCountHandler;
    

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(THEME);
		super.onCreate(savedInstanceState);
		
		if (stackIndex == null)
			stackIndex = new Stack<String>();
		mSupportActionBar = getSupportActionBar();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);	
//		mSupportActionBar.setBackgroundDrawable(getResources().getDrawable(
//				R.drawable.tool_bar));
		mSupportActionBar.setTitle("");
//		mSupportActionBar.setIcon(R.drawable.vikinglogo);
		//mSupportActionBar.setIcon(R.drawable.arrow_search);
		updateBasketCountHandler = new Handler();
		CommonBasketValues.getInstance().addObserver(this);

	}
	public SearchView mSearchView=null;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		com.actionbarsherlock.view.MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.actionbarmenu, menu);
		
		/////seach widget////////////
		MenuItem searchItem = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);
        
        mSearchView.setQueryHint(getResources().getString(R.string.search));
        mSearchView.setHideSuggestionOnKeyBoardHide(false);
        abs__search_progress_bar=(ProgressBar) mSearchView.findViewById(R.id.abs__search_progress_bar);
        searchAutoCompleteTextView = (AutoCompleteTextView)mSearchView.findViewById(R.id.abs__search_src_text);
        searchAutoCompleteTextView.setTextColor(getResources().getColor(R.color.textColor));
        searchAutoCompleteTextView.setThreshold(1);
        
        
        
		/////seach widget//////////////
		
		CommonValues.getInstance().menuList = menu;
		MenuItem searchMenuItem = menu.findItem(R.id.menu_basket_scan);
		RelativeLayout rlCapture = (RelativeLayout) searchMenuItem
				.getActionView().findViewById(R.id.rlActionMenuHome);

		ImageView iamgeScan = (ImageView) rlCapture
				.findViewById(R.id.ivMenuHome);
		if (selectScan)
			iamgeScan.setBackgroundResource(R.drawable.barcode_selected);
		else
			iamgeScan.setBackgroundResource(R.drawable.barcode_normal);

		// Scanner tab
		rlCapture.setOnClickListener(new RelativeLayout.OnClickListener() {

			public void onClick(View v) {
				if (validateServerConfig()) {
					currentMenuIndex = SCANNING_ACTIVITY;
					manageActivity();
				}
			}
		});
		RelativeLayout rlBasket = (RelativeLayout) searchMenuItem
				.getActionView().findViewById(R.id.rlActionMenuBasket);
		ImageView iamgeBasket = (ImageView) rlBasket
				.findViewById(R.id.ivMenuBasket);
		if (selectBasket)
			iamgeBasket.setBackgroundResource(R.drawable.basket_selected);
		else
			iamgeBasket.setBackgroundResource(R.drawable.basket_normal);

		if (currentMenuIndex == INITAIL_STATE) {
			basketTotal = (TextView) rlBasket
					.findViewById(R.id.tvMenuBasketTitle);
			basketCount = (TextView) rlBasket
					.findViewById(R.id.tvMenuBasketCount);
		} else if (currentMenuIndex == SCANNING_ACTIVITY) {
			basketTotal1 = (TextView) rlBasket
					.findViewById(R.id.tvMenuBasketTitle);
			basketCount1 = (TextView) rlBasket
					.findViewById(R.id.tvMenuBasketCount);
		} else if (currentMenuIndex == BASKET_ACTIVITY) {
			basketTotal2 = (TextView) rlBasket
					.findViewById(R.id.tvMenuBasketTitle);
			basketCount2 = (TextView) rlBasket
					.findViewById(R.id.tvMenuBasketCount);
		} else if (currentMenuIndex == SEARCH_ACTIVITY) {
			basketTotal3 = (TextView) rlBasket
					.findViewById(R.id.tvMenuBasketTitle);
			basketCount3 = (TextView) rlBasket
					.findViewById(R.id.tvMenuBasketCount);
		} else if (currentMenuIndex == CATALOGUE_ACTIVITY) {
			basketTotal4 = (TextView) rlBasket
					.findViewById(R.id.tvMenuBasketTitle);
			basketCount4 = (TextView) rlBasket
					.findViewById(R.id.tvMenuBasketCount);
		} else if (currentMenuIndex == LOGIN_ACTIVITY) {
			basketTotal5 = (TextView) rlBasket
					.findViewById(R.id.tvMenuBasketTitle);
			basketCount5 = (TextView) rlBasket
					.findViewById(R.id.tvMenuBasketCount);
		}
		else if (currentMenuIndex == SEARCH_DETAILS_ACTIVITY) {
			basketTotal6 = (TextView) rlBasket
					.findViewById(R.id.tvMenuBasketTitle);
			basketCount6 = (TextView) rlBasket
					.findViewById(R.id.tvMenuBasketCount);
		}
		// basket tab
		Home.showBasketMenu();
		rlBasket.setOnClickListener(new RelativeLayout.OnClickListener() {

			public void onClick(View v) {
				if (validateServerConfig()) {
					currentMenuIndex = BASKET_ACTIVITY;
					/*
					 * if (Basket.flipper != null &&
					 * Basket.flipper.getDisplayedChild() > 0) {
					 * Basket.flipper.setDisplayedChild(0); }
					 */
					manageActivity();
				}
			}
		});
		
		this.actionBarMenu = menu;
		return true;
	}

	public static int getLastIndex() {
		if (stackIndex.size() > 1) {
			stackIndex.pop();
			return Integer.parseInt(stackIndex.peek());
		} else if (stackIndex.size() > 0) {
			stackIndex.pop();
		}
		return INITAIL_STATE;
	}

	/***
	 * The user has selected an Menu Item. Go to that Activity
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			switch (currentMenuIndex) {
			case SCANNING_ACTIVITY:
				stackIndex.removeAllElements();
				currentMenuIndex = getLastIndex();
				manageActivity();
				break;
			case BASKET_ACTIVITY: // go to home page

				if (this instanceof Basket) {
					if (Basket.backState == Basket.INITIAL_STATE) {
						stackIndex.removeAllElements();
					}
					Basket bsk = (Basket) this;
					bsk.onBackPressed();
				}
				break;
			case SEARCH_ACTIVITY:

				if (this instanceof Search) {
					if (Search.backState == StateSearch.INITIAL_STATE) {
						stackIndex.removeAllElements();
					}
					Search src = (Search) this;
					src.onBackPressed();
				}
				break;
			case CATALOGUE_ACTIVITY:
				if (this instanceof Catalogue) {
					if (!Catalogue.isDetailsVisible) {
						stackIndex.removeAllElements();
					}
					Catalogue catlg = (Catalogue) this;
					catlg.onBackPressed();
				}
			case LOGIN_ACTIVITY:
				if (this instanceof More) {
					if (More.backStateMore == INITAIL_STATE) {
						stackIndex.removeAllElements();
					}
					More mre = (More) this;
					mre.onBackPressed();
				}
				break;
			case SEARCH_DETAILS_ACTIVITY://Search widget related
				if (this instanceof SearchDetails) {
					/*if (SearchDetails.backState == INITAIL_STATE) {
						stackIndex.removeAllElements();
					}*/
					SearchDetails sd = (SearchDetails) this;
					sd.onBackPressed();
				}
				break;
			default:
				if(Home.flipper!=null){
					Home.flipper.setInAnimation(CommonTask.inFromLeftAnimation());
					Home.flipper.setOutAnimation(CommonTask.outToRightAnimation());
					Home.backState = INITAIL_STATE;
					mSupportActionBar.setDisplayHomeAsUpEnabled(false);
					//this below line was added on this scenario, when going into settings and coming back 
					// the top home button was constantly looping aroud. Only setDisplayHomeAsUpEnabled did not work
					// refer to this link http://developer.android.com/reference/android/app/ActionBar.html#setHomeButtonEnabled%28boolean%29
					mSupportActionBar.setHomeButtonEnabled(false);
					Home.flipper.setDisplayedChild(0);
				}
				break;
			}

			selectScan = false;
			selectBasket = false;
			return true;
		}// All vare submenu
		else if (item.getItemId() == R.id.menu_Search) {
			if (validateServerConfig()) {
				currentMenuIndex = SEARCH_ACTIVITY;
				manageActivity();
			}
		}
		// catalogue submenu
		else if (item.getItemId() == R.id.menu_Catalogue) {
			if (validateServerConfig()) {
				currentMenuIndex = CATALOGUE_ACTIVITY;
				manageActivity();
			}
		}
		// bruger submenu
		else if (item.getItemId() == R.id.menu_More) {
			if (validateServerConfig()) {
				currentMenuIndex = LOGIN_ACTIVITY;
				if (More.flipper != null
						&& More.flipper.getDisplayedChild() > 0)
					More.flipper.setDisplayedChild(0);
				manageActivity();
			}
		}
		return true;
	}

	// defining the state and start of various activities
	/**
	 * Selecting an item from the menu
	 */
	public void manageActivity() {
		switch (currentMenuIndex) {
		case SCANNING_ACTIVITY:// We choose the Scanning Activity
			if (capture == null) { // If scanning has not been done before,
									// create scanner
				// Create new Scanner
				capture = new Intent(MainActionbarBase.this,
						CaptureActivity.class);
				capture.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			}
			selectScan = true;
			selectBasket = false;
			if (!stackIndex.contains(String.valueOf(0)))
				stackIndex.push(String.valueOf(0));
			startActivity(capture);
			break;
		case BASKET_ACTIVITY:// basket activity
			if (basket == null) {
				basket = new Intent(MainActionbarBase.this, Basket.class);
				basket.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				if (more == null) {
					more = new Intent(MainActionbarBase.this, More.class);
					more.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				}
			}
			selectScan = false;
			selectBasket = true;
			if (!stackIndex.contains(String.valueOf(1)))
				stackIndex.push(String.valueOf(1));
			Basket.backState = Basket.INITIAL_STATE;
			startActivity(basket);
			break;
		case SEARCH_ACTIVITY:// search activity
			if (search == null) {
				search = new Intent(MainActionbarBase.this, Search.class);
				search.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			}
			selectScan = false;
			selectBasket = false;
			// Search.backState = StateSearch.INITIAL_STATE;
			if (!stackIndex.contains(String.valueOf(2)))
				stackIndex.push(String.valueOf(2));
			startActivity(search);
			break;
		case CATALOGUE_ACTIVITY:// catalogue activity
			if (catalogue == null) {
				catalogue = new Intent(MainActionbarBase.this, Catalogue.class);
				catalogue.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			}
			selectScan = false;
			selectBasket = false;
			if (!stackIndex.contains(String.valueOf(3)))
				stackIndex.push(String.valueOf(3));
			// Catalogue.isDetailsVisible = false;
			if (!CommonValues.getInstance().IsServerConnectionError) {
				startActivity(catalogue);
			} else {
				CommonTask.ShowMessage(this,
						getString(R.string.serverswitchError));
			}

			break;
		case LOGIN_ACTIVITY:// bruger means user log in activity
			if (more == null) {
				more = new Intent(MainActionbarBase.this, More.class);
				more.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			}
			selectScan = false;
			selectBasket = false;
			if (!stackIndex.contains(String.valueOf(4)))
				stackIndex.push(String.valueOf(4));
			More.backStateMore = INITAIL_STATE;
			if (!CommonValues.getInstance().IsServerConnectionError) {
				startActivity(more);
			} else {
				CommonTask.ShowMessage(this,
						getString(R.string.serverswitchError));
			}
			break;
		case SEARCH_DETAILS_ACTIVITY://Search widget activity
			if (searchDetails == null) {
				searchDetails = new Intent(MainActionbarBase.this, SearchDetails.class);
				searchDetails.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			}
			selectScan = false;
			selectBasket = false;
			if(articleIdForIntent!=null){
				searchDetails.putExtra("id", articleIdForIntent );
			}
			if (!stackIndex.contains(String.valueOf(5)))
				stackIndex.push(String.valueOf(5));
//			SearchDetails.backState = INITAIL_STATE;
			if (!CommonValues.getInstance().IsServerConnectionError) {
				startActivity(searchDetails);
			} else {
				CommonTask.ShowMessage(this,
						getString(R.string.serverswitchError));
			}
			break;
		

		default:
			goHome(MainActionbarBase.this);
			break;
		}
	}

	// method used for return back to homescreen from any activity
	public void goHome(Context activity) {
		if (!(activity instanceof Home)) {
			try {
				if (Home.flipper != null
						&& Home.flipper.getDisplayedChild() == 2) {
					Home.flipper.setInAnimation(CommonTask
							.inFromRightAnimation());
					Home.flipper.setOutAnimation(CommonTask
							.outToLeftAnimation());
					Home.flipper.setDisplayedChild(0);
				}
				Intent i = CommonValues.getInstance().homeIntent;

				// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
				// Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);

			} catch (Exception e) {
			}
		}
	}

	// this method checks whether any value has set in setting screen or not.
	// If no value is set it will show alert Server not found..TAC
	public boolean validateServerConfig() {
		if (CommonTask.isNetworkAvailable(this)) {
			String serverUrl = "";
			String shopNumber = "";

			SharedPreferences sharedPreferences = getSharedPreferences(
					"settings", MODE_PRIVATE);

			if (sharedPreferences.contains(CommonConstraints.PREF_URL_KEY)
					&& sharedPreferences
							.contains(CommonConstraints.PREF_SHOPNUMBER_KEY)) {
				serverUrl = sharedPreferences.getString(
						CommonConstraints.PREF_URL_KEY, "");
				shopNumber = sharedPreferences.getString(
						CommonConstraints.PREF_SHOPNUMBER_KEY, "");

			}

			if (serverUrl != "" && shopNumber != "") {
				return true;
			} else {
				/*CommonTask
						.ShowMessage(this, getString(R.string.servernotFound));*/
				CommonTask.showServerSettingConfirmation(this, getString(R.string.servernotFound), showServerSettingsEvent());

			}
		} else {
			CommonTask.ShowMessage(this,
					this.getString(R.string.networkError));
		}
		return false;

	}

	// This Method Checks Whether The application is connected to Viking server
	// or not.
	public boolean isValidServer(String URL, String shopNumber) {
		if (JSONfunctions.retrieveDataFromStream(String.format(
				URL + CommonURL.getInstance().NewServerUrlValidation,
				shopNumber), FamilyInquieryRoot.class) != null) {
			return true;
		}
		return false;
	}
	
	/////////Search WIdget Related Code Block ..Tanvir//////////////
	
	String searchTextValue = "";
	private void setupSearchView(MenuItem searchItem) {
	
 
		
        mSearchView.setOnQueryTextListener(this);
    }
 
    public boolean onQueryTextChange(String newText) {
    	onSearchTextChanged(newText);
        return false;
    }
 
    public boolean onQueryTextSubmit(String query) {
    	onSearchTextChanged(query);
        return true;
    }
    
    void onSearchTextChanged(String newText){
    	if(validateServerConfig()){
	    	searchAutoCompleteTextView.showDropDown();
	    	//imm.hideSoftInputFromWindow(searchAutoCompleteTextView.getWindowToken(), 0);
	    	searchTextValue = newText;
	    	abs__search_progress_bar.setVisibility(View.VISIBLE);
	    	startSearchWidgetTask();
    	}
    }
 
    public boolean onClose() {
        return false;
    }
 
    protected boolean isAlwaysExpanded() {
        return false;
    }
    
    /**
	 * call async class for searching...Tanvir
	 */
	public void startSearchWidgetTask() {
		
		if (asyncSearchWidgetTask != null) {
			asyncSearchWidgetTask.cancel(true);
		}
		asyncSearchWidgetTask = new AsyncSearchTaskMain(this);
		asyncSearchWidgetTask.execute();
	}
	String articleIdForIntent = null;
	/**
	 * Fill list with searchable list
	 * 
	 * @param src
	 * @param isDoSearch
	 */
	public void applySearchResultInWidget(MainActionbarBase src, boolean status) {
		
		abs__search_progress_bar.setVisibility(View.GONE);
		if(status == false)
			return;
		String[] columnNames = {"_id","text","subtitle"};
	    MatrixCursor cursor = new MatrixCursor(columnNames);
	    String[] temp = new String[3];
	    
	    if(articleInquierySearchWidgetList!=null){
		    for (ArticleForSearch article : articleInquierySearchWidgetList) {
		    	temp[0] = article.id;
	            temp[1] = article.text;
	            temp[2] = article.text2;
	            cursor.addRow(temp);
		    }
		}
             
	    CustomCursorAdapter adapter = new CustomCursorAdapter(this, cursor);
		mSearchView.setSuggestionsAdapter(adapter);
		mSearchView.getSuggestionsAdapter().notifyDataSetChanged();
		
		 
		mSearchView.setOnSuggestionListener(new OnSuggestionListener() {
			
			@Override
			public boolean onSuggestionSelect(int position) {
				return false;
			}
			
			@Override
			public boolean onSuggestionClick(int position) {

				Cursor itemCursor = (Cursor)mSearchView.getSuggestionsAdapter().getItem(position);
				currentMenuIndex = SEARCH_DETAILS_ACTIVITY;
				articleIdForIntent = itemCursor.getString(itemCursor.getColumnIndex("_id"));
				manageActivity();
				return true;
			}
		});
		}
	
	/**
	 * Run Search Asynchronously uning ArticleInqueriesSearch
	 * 
	 * @return
	 */
	public boolean doSearchInWidget() {
		if (!searchTextValue.equals("")) {
			
			ArticleInqueriesSearchManager articleinqueries = new ArticleInqueriesSearchManager(
					searchTextValue);
			if (articleinqueries.searchArticleInquiryRoot != null
					&& articleinqueries.searchArticleInquiryRoot.searchArticleInquiry != null
					&& articleinqueries.searchArticleInquiryRoot.searchArticleInquiry.searchArticles != null)
				articleInquierySearchWidgetList = articleinqueries.searchArticleInquiryRoot.searchArticleInquiry.searchArticles.articleList;
			
			return true;
		} else {
			//articleInquierySearchWidgetList = new ArrayList<ArticleForSearch>();
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void update(Observable observable, Object data) {
		Log.d("observernotified",this.getClass().getName());
		updateBasketCountHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Home.showBasketMenu();
			}
		}, 50);
	}
	
	public DialogInterface.OnClickListener showServerSettingsEvent() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					/*Intent i = CommonValues.getInstance().homeIntent;
					i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					i.putExtra("showsettings", true);
					startActivity(i);*/
					Home.showServerSettingsScreen();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					dialog.cancel();
					break;
				}
			}
		};
		return dialogClickListener;

	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		CommonBasketValues.getInstance().deleteObserver(this);
	}
	
}

	

