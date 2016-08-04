package com.shopper.app.activities;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.shopper.app.R;
import com.shopper.app.asynctasks.AsyncTaskInterface;
import com.shopper.app.base.MainActionbarBase;
import com.shopper.app.entities.ArticleForSearch;
import com.shopper.app.entities.ArticleInq;
import com.shopper.app.entities.ArticleInquieryForSearchManager;
import com.shopper.app.entities.AsyncAddBasketFromSearch;
import com.shopper.app.entities.AsyncArticleInquieryLoading;
import com.shopper.app.entities.AsyncGroupInquieryLoading;
import com.shopper.app.entities.AsyncSearchLoading;
//import com.shopper.app.entities.AsyncSearchTask;
import com.shopper.app.entities.AsyncUpdateBasketFromArticleDetailTask;
import com.shopper.app.entities.Department;
import com.shopper.app.entities.DiscountGroup;
import com.shopper.app.entities.FamilyInq;
import com.shopper.app.entities.Group;
import com.shopper.app.entities.GroupManager;
import com.shopper.app.entities.OrderLine;
import com.shopper.app.entities.PriceInquiery;
import com.shopper.app.entities.SearchArticleAdapter;
import com.shopper.app.entities.SearchFamilyAdapter;
import com.shopper.app.entities.SearchGroupAdapter;
import com.shopper.app.utils.CommonBasketValues;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;

/**
 * This class call from selecting the menu item "Find Vare" and after creating
 * this class onCreate is call once automatically This class is use for
 * controlling all search related action In this class manage the screen flow of
 * the search. From the top Family screen are shown after that group, article
 * and finally article details but if you search by any keyword on the Search
 * box the flow is article and finally article details All server related task
 * like load Family, Load group, Load article are Asynchronously done From the
 * article details screen you can make the order Here create sub article/related
 * article details screen dynamically by selecting the sub/related item image
 * from the article detail screen After pressing back button with backState = -1
 * always gone to home screen otherwise came back to the previous screen
 * 
 * @author jib
 * 
 */

public class Search extends MainActionbarBase implements OnItemClickListener,
		OnClickListener, OnFocusChangeListener, TextWatcher, OnGestureListener {

	public enum StateSearch {
		INITIAL_STATE, // backState -1
		BACK_FROM_GROUP, // backState 0
		BACK_TO_PREVIOUS, // backState 1
		BACK_FROM_ARTICLE_DETAIL, // backState 2
		CANCEL_SEARCH, // backState 3

	};

	public static StateSearch backState = StateSearch.INITIAL_STATE;
	private int current_family_position = -1;
	public int selectedItemQty;
	public static boolean isBackToArticle = false;
	public String articleId;
	ArrayList<Department> familyInquieryList;
	List<Group> groupInquieryList;
	List<ArticleForSearch> articleInquieryList;
//	private List<ArticleForSearch> articleInquierySearchList;
	private SearchFamilyAdapter listAdaptor;
	private SearchGroupAdapter groupListAdaptor;
	private SearchArticleAdapter articleListAdaptor;
	private static Department familyentity;
	private static Group grpEntity;
	private ArticleForSearch atricleEntity;
	public static OrderLine orderLine;
	private FamilyInq familyitems;
	private GroupManager grpinquiery;
	private ArticleInquieryForSearchManager arcinquiery;
//	private ArticleInqueriesSearchManager articleinqueries;
	private RelativeLayout rlayfamilyheaderdefault, rlayarticle, rlfamilylist;
	public DiscountGroup discountGroup;
	public LinearLayout lldiscount;
	private ViewGroup listOverlay;
	public static ViewFlipper flipper, flipper1;

	private EditText saerchitem, etsearcharticle;

//	private Button articleback_btn;

	private TextView groupheader, articleheader;
	private static ListView familylistview;

	private ListView grouplistview, articlelistview;
	private ArticleInq articleInqObj;

	public static EditText etSearchItemQuantity;

	private AsyncAddBasketFromSearch asyncAddBasket;
	InputMethodManager imm;

	// Details view controls related
	final int noOfDefaultFlipperControls = 6;// depends on default controls
	final int noOfOtherControlsFromPosition = 3;// depends on rest of controls
												// after details views
	final int positionNo = 3;// starting from 0
	//

	private int whohasfocus = 0;
	public ProgressBar progressBar, searchResultProgressBar,
			llDiscountProgressBar;
	public boolean isArticleDetailsReady = true, 
				isSearchDetailsReady = true;

//	private AsyncSearchTask asyncSearchTask = null;
	private AsyncSearchLoading asyncSearchLoading = null;
	private AsyncGroupInquieryLoading asyncGroupInquieryLoading = null;
	private AsyncArticleInquieryLoading asyncArticleInquieryLoading = null;
//	private AsyncArticleDetailsLoading asyncArticleDetailsLoading = null;
	private AsyncUpdateBasketFromArticleDetailTask asyncUpdateBasketFromArticleDetailTask = null;
//	private SearchItemDiscountFamilyImageTask searchItemDiscountFamilyImageTask = null;
	private GestureDetector gestureScanner;

	public Hashtable<Object, Object> selectedSubitemCollectionSearch = null;
	
	/**
	 * Automatically call once when class created initialize all control
	 * variable and load family using LoadFamilyContent Asynchronously use
	 * thread for performing search
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		LoadFamilyContent();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		progressBar = (ProgressBar) findViewById(R.id.searchProgressBar);
		searchResultProgressBar = (ProgressBar) findViewById(R.id.searchResultProgressBar);
		llDiscountProgressBar = (ProgressBar) findViewById(R.id.llDiscountProgressBar);
		progressBar.setVisibility(View.INVISIBLE);
		searchResultProgressBar.setVisibility(View.INVISIBLE);
		llDiscountProgressBar.setVisibility(View.INVISIBLE);
		
		isBackToArticle = true;
		flipper.setInAnimation(CommonTask.inFromRightAnimation());
		flipper.setOutAnimation(CommonTask.outToLeftAnimation());
		gestureScanner = new GestureDetector(Search.this, this);
	}

	/**
	 * Stop search Thread after setting changed
	 */
	public static void resetSearchAfterSettingChanged() {
		if (flipper != null && flipper.getDisplayedChild() > 0) {
			flipper.setDisplayedChild(0);
			backState = StateSearch.INITIAL_STATE;
		}
		if (familylistview != null)
			familylistview.setAdapter(null);
		CommonValues.getInstance().IsAnyNewBasketItemAdded = false;
	}

	/**
	 * After pressing back button with backState = -1 always gone to home screen
	 * otherwise came back to the previous screen
	 */
	@Override
	public void onBackPressed() {

		flipper.setInAnimation(CommonTask.inFromLeftAnimation());
		flipper.setOutAnimation(CommonTask.outToRightAnimation());
		switch (backState) {
		case INITIAL_STATE:
			currentMenuIndex = getLastIndex();
			manageActivity();
			break;
		case BACK_FROM_GROUP:// use for back from group
			flipper.showPrevious();
			backState = StateSearch.INITIAL_STATE;
			break;
		case BACK_TO_PREVIOUS:
			flipper.showPrevious();
			backState = StateSearch.BACK_FROM_GROUP;
			break;
		case BACK_FROM_ARTICLE_DETAIL: // use for back from article detail
			backFromArticleDetail();
			break;
		case CANCEL_SEARCH: // use for cancel search
			cancelBack();
			break;
		default:
			// CommonTask.CloseApplication(this);
			break;
		}

	}

	// Edited by SAM for VM-26 Basket item no disappear.
	@Override
	protected void onResume() {
		Home.showBasketMenu();
		if (asyncSearchLoading != null) {
			asyncSearchLoading.cancel(true);
		}
		asyncSearchLoading = new AsyncSearchLoading(this);
		asyncSearchLoading.execute();
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		updateBasket();

	}

	// use for load family
	private void LoadFamilyContent() {

		initializefamily();

	}

	// use for load familyitems in familyInq object
	public void loadListView() {
		familyitems = new FamilyInq();

	}

	/**
	 * load searched article using SearchFamily class
	 * 
	 * @param src
	 */
	public void viewListView(Search src) {

		if (familyitems.familyInquieryResponse != null
				&& familyitems.familyInquieryResponse.familyInquiery != null
				&& familyitems.familyInquieryResponse.familyInquiery.departments != null)
			familyInquieryList = (ArrayList<Department>) familyitems.familyInquieryResponse.familyInquiery.departments.departmentList;// CommonBasketValues.getInstance().FamilyList;
		if (familyInquieryList == null) {
			familyInquieryList = new ArrayList<Department>();
		}
		listAdaptor = new SearchFamilyAdapter(src, R.layout.search_family,
				familyInquieryList);
		familylistview.setAdapter(listAdaptor);
		familylistview.setOnItemClickListener(this);
//		isSearchScreenLoaded = true;
	}

	public boolean isFamilyNotLoaded() {
		return familylistview.getAdapter() == null;
	}

	// initialize family related controls
	private void initializefamily() {
		flipper = (ViewFlipper) findViewById(R.id.vfsearch);

		rlfamilylist = (RelativeLayout) findViewById(R.id.rlfamilylist);

		familylistview = (ListView) findViewById(R.id.lvfamily);

		listOverlay = (ViewGroup) findViewById(R.id.lvarticlesearchOverLay);
		listOverlay.setVisibility(View.GONE);

//		groupback_btn = (Button) findViewById(R.id.bFamilyBack);
		groupheader = (TextView) findViewById(R.id.tvsearchgroupHeadingText);
		grouplistview = (ListView) findViewById(R.id.lvgroup);

//		articleback_btn = (Button) findViewById(R.id.bGroupBack);
		articleheader = (TextView) findViewById(R.id.tvsearcharticleHeadingText);
		articlelistview = (ListView) findViewById(R.id.lvarticle);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long rowid) {

		flipper.setInAnimation(CommonTask.inFromRightAnimation());
		flipper.setOutAnimation(CommonTask.outToLeftAnimation());
		switch (parent.getId()) {
		case R.id.lvfamily: // Clicking family list showing group list
		{
			listAdaptor.setSelection(position);
			familylistview.setSelection(position);
			familylistview.setSelectionFromTop(position, view.getTop());
			current_family_position = position;
			flipper.showNext();
			backState = StateSearch.BACK_FROM_GROUP;
			loadGroupContent(current_family_position);
			break;
		}
		case R.id.lvgroup: // Clicking Group list showing article list
		{
			backState = StateSearch.BACK_TO_PREVIOUS;
			groupListAdaptor.setSelection(position);
			grouplistview.setSelection(position);
			grouplistview.setSelectionFromTop(position, view.getTop());
			current_family_position = position;
			flipper.showNext();
			loadArticleContent(current_family_position);
			break;
		}
		case R.id.lvarticle:// Clicking article list showing Article details
		{
			backState = StateSearch.BACK_FROM_ARTICLE_DETAIL;
			isBackToArticle = true;
			articleListAdaptor.setSelection(position);
			articlelistview.setSelection(position);
			articlelistview.setSelectionFromTop(position, view.getTop());
//			current_article_position = position;
			for (int i = positionNo; i < flipper.getChildCount()
					- noOfOtherControlsFromPosition; i++) {
				flipper.removeViewAt(i);
			}
//			InitializeArticleDetailsContent("new");

//			articledetailscallfrom = 1;// If called from Article Detail

//			LoadArticleDetailsContent(current_article_position);
			
			
//			String gText = grpEntity.GroupText != null ? grpEntity.GroupText
//					.trim() : "";
			atricleEntity = (ArticleForSearch) articlelistview
					.getItemAtPosition(position);
			articleId = atricleEntity.id;
			new DisplayItemDetails(this, flipper, articleId);
			
			break;
		}
//		case R.id.lvarticlesearch: {
//			backState = StateSearch.BACK_FROM_ARTICLE_DETAIL;
//			isBackToArticle = true;
//			current_article_position = position;
//			flipper.setDisplayedChild(2);
//			for (int i = positionNo; i < flipper.getChildCount()
//					- noOfOtherControlsFromPosition; i++) {
//				flipper.removeViewAt(i);
//			}
//			InitializeArticleDetailsContent("new");
//
//			articledetailscallfrom = 2;
//			LoadArticleDetailsContent(current_article_position);
//			break;
//		}
		default:
			break;
		}

	}

	private void loadGroupContent(int position) {

		familyentity = new Department();
		familyentity = (Department) familylistview.getItemAtPosition(position);
		groupheader
				.setText(familyentity.DeptText.trim().length() > 20 ? familyentity.DeptText
						.substring(0, 18) + "..."
						: familyentity.DeptText);
		grouplistview.setAdapter(null);
		groupInquiryLoad();

	}

	/**
	 * call async class for loading group inquiry list...Tanvir
	 */
	public void groupInquiryLoad() {
		if (asyncGroupInquieryLoading != null) {
			asyncGroupInquieryLoading.cancel(true);
		}
		asyncGroupInquieryLoading = new AsyncGroupInquieryLoading(this);
		asyncGroupInquieryLoading.execute();
	}

	/**
	 * Fill Group Inquiry object, called from {@link AsyncGroupInquieryLoading}
	 * 
	 */

	public void loadGroupListView() {

		grpinquiery = new GroupManager(Integer.parseInt(familyentity.DeptNo));

	}

	/**
	 * Prepare Adaptor groupInquieryList and also click Listener, called from {@link AsyncGroupInquieryLoading}
	 * 
	 * @param src
	 */
	public void viewGroupListView(Search src) {
		if (grpinquiery != null && grpinquiery.groupInquieryRoot != null
				&& grpinquiery.groupInquieryRoot.groupInquiery != null
				&& grpinquiery.groupInquieryRoot.groupInquiery.groups != null)
			groupInquieryList = grpinquiery.groupInquieryRoot.groupInquiery.groups.groupList;// CommonBasketValues.getInstance().GroupList;
		if (groupInquieryList == null) {
			groupInquieryList = new ArrayList<Group>();
		}
//		groupback_btn.setOnClickListener(this);
		groupListAdaptor = new SearchGroupAdapter(Search.this,
				R.layout.search_group, (ArrayList<Group>) groupInquieryList);
		grouplistview.setAdapter(groupListAdaptor);
		grouplistview.setOnItemClickListener(this);
	}

	/**
	 * @param position
	 */
	private void loadArticleContent(int position) {
		grpEntity = new Group();
		grpEntity = (Group) grouplistview.getItemAtPosition(position);
//		articleback_btn
//				.setText(familyentity.DeptText.trim().length() > 8 ? familyentity.DeptText
//						.substring(0, 8) + "..."
//						: familyentity.DeptText);
		articleheader
				.setText(grpEntity.GroupText.trim().length() > 20 ? grpEntity.GroupText
						.substring(0, 18) + "..."
						: grpEntity.GroupText);
		articlelistview.setAdapter(null);
		loadArticleInquiry();
	}

	/**
	 * call async class for loading article inquiry list..Tanvir
	 */

	public void loadArticleInquiry() {
		if (asyncArticleInquieryLoading != null) {
			asyncArticleInquieryLoading.cancel(true);
		}
		asyncArticleInquieryLoading = new AsyncArticleInquieryLoading(this);
		asyncArticleInquieryLoading.execute();
	}

	/**
	 * Fill Article Inquiry object, called from {@link AsyncArticleInquieryLoading#onPostExecute}
	 * 
	 */
	public void loadArticleListView() {

		arcinquiery = new ArticleInquieryForSearchManager(grpEntity.GroupNo,
				Integer.parseInt(familyentity.DeptNo));

	}

	/**
	 * Prepare Adaptor articleList and also click Listener
	 * 
	 * @param src
	 */
	public void viewArticleListView(Search src) {
		if (arcinquiery.articleInquiryRoot != null
				&& arcinquiery.articleInquiryRoot.articleInquiry != null
				&& arcinquiery.articleInquiryRoot.articleInquiry.articles != null)
			articleInquieryList = arcinquiery.articleInquiryRoot.articleInquiry.articles.articleList; // CommonBasketValues.getInstance().ArticleList;
		if (articleInquieryList == null) {
			articleInquieryList = new ArrayList<ArticleForSearch>();
		}
//		articleback_btn.setOnClickListener(this);
		articleListAdaptor = new SearchArticleAdapter(Search.this,
				R.layout.search_article,
				(ArrayList<ArticleForSearch>) articleInquieryList);
		articlelistview.setAdapter(articleListAdaptor);
		articlelistview.setOnItemClickListener(this);
	}

//	// load & show data for the Article Detail screen by a Article
//	private void LoadArticleDetailsContent(int position) {
//
//		RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		rel_btn.height = 46;
//		rel_btn.leftMargin = 5;
//		rel_btn.addRule(RelativeLayout.CENTER_VERTICAL, 1);
//
//		if (articledetailscallfrom == 1) // That means call from normal
//											// operation Family -> Group ->
//											// Article
//		{
//			String gText = grpEntity.GroupText != null ? grpEntity.GroupText
//					.trim() : "";
//			atricleEntity = (ArticleForSearch) articlelistview
//					.getItemAtPosition(position);
//			articleId = atricleEntity.id;
//			bSearchBack.setText(gText.length() > 20 ? " "
//					+ gText.substring(0, 20) + "..." : "   " + gText);
//			rel_btn.width = gText.length() > 7 ? gText.length() > 20 ? 28 * 10
//					: gText.length() * 10 + 50 : 120;
//		} else {
////			atricleEntity = (ArticleForSearch) articlesearchlistview
////					.getItemAtPosition(position);
////			articleId = atricleEntity.id;
////			bSearchBack.setText("   Find Vare");
////			rel_btn.width = 120;
////			imm.hideSoftInputFromWindow(bSearchBack.getWindowToken(), 0);
//		}
//
//		bSearchBack.setLayoutParams(rel_btn);
//		rlSearchtDetail.setVisibility(View.INVISIBLE);
//		flipper1.setVisibility(View.INVISIBLE);
//		lldiscount.setVisibility(View.INVISIBLE);
//		discountHeaderText.setVisibility(View.INVISIBLE);
//		discountFooterText.setVisibility(View.INVISIBLE);
//		loadArticleDetails();
//
//	}

	/**
	 * call async class for loading article Details...Tanvir
	 */
//	public void loadArticleDetails() {
//		if (asyncArticleDetailsLoading != null) {
//			asyncArticleDetailsLoading.cancel(true);
//		}
//		asyncArticleDetailsLoading = new AsyncArticleDetailsLoading(detailsAsyncTaskListener,articleId,this);
//		asyncArticleDetailsLoading.execute();
//	}

	// load & show data for the Article Detail screen
//	public void LoadArticleDetailsContent() {
//
//		InitializeArticleDetailsContent("new");
//
//		isBackToArticle = true;
//		RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
//				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		rel_btn.height = 46;
//		rel_btn.leftMargin = 5;
//		rel_btn.width = 100;
//		rel_btn.addRule(RelativeLayout.CENTER_VERTICAL, 1);
//		bSearchBack.setText("Back");
//		imm.hideSoftInputFromWindow(bSearchBack.getWindowToken(), 0);
//
//		bSearchBack.setLayoutParams(rel_btn);
//		rlSearchtDetail.setVisibility(View.INVISIBLE);
//		flipper1.setVisibility(View.INVISIBLE);
//		lldiscount.setVisibility(View.INVISIBLE);
//		discountHeaderText.setVisibility(View.INVISIBLE);
//		discountFooterText.setVisibility(View.INVISIBLE);
//		loadArticleDetails();
//
//	}

	/**
	 * load the Article Inquiry details by articleId
	 */
	public void loadArticleDetailsListView() {
		articleInqObj = new ArticleInq(this, articleId);
	}

	public void viewArticleDetailsListView(Search src) {
		if (articleInqObj != null && articleInqObj.EAN != ""
				&& ArticleInq.IsProductFound) {
//			AssignValues(articleInqObj);
		} else {
			flipper.showPrevious();
		}
	}

	// Initialize Article Details screen controls
//	private void InitializeArticleDetailsContent(String type) {
//
//		flipper.setInAnimation(CommonTask.inFromRightAnimation());
//		flipper.setOutAnimation(CommonTask.outToLeftAnimation());
//		RelativeLayout searchItemDetails;
//		View searchDetails = null;
//
//		if (type.equals("new")) {
//			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			searchDetails = (View) inflater.inflate(
//					R.layout.search_item_detail, null);
//			searchItemDetails = (RelativeLayout) searchDetails
//					.findViewById(R.id.searchItemDetails);
//			searchItemDetails.setOnTouchListener(new OnTouchListener() {
//				public boolean onTouch(final View view, final MotionEvent event) {
//					gestureScanner.onTouchEvent(event);
//					return true;
//				}
//			});
//			flipper.addView(searchDetails, flipper.getChildCount()
//					- noOfOtherControlsFromPosition);
//			flipper.showNext();
//		} else {
//			searchItemDetails = (RelativeLayout) flipper.getChildAt(flipper
//					.getDisplayedChild() > positionNo - 1 ? flipper
//					.getDisplayedChild() : positionNo);
//		}
//
//		rlSearchtDetail = (RelativeLayout) searchItemDetails
//				.findViewById(R.id.rlSearchDetail);
//		if (searchItemDiscountFamilyImageTask != null) {
//			searchItemDiscountFamilyImageTask.cancel(true);
//			stopDiscountProgressBar();
//			isArticleDetailsReady = true;
//		}
//		flipper1 = (ViewFlipper) searchItemDetails
//				.findViewById(R.id.vfSearchItemFilpper);
//		bSearchBack = (Button) searchItemDetails.findViewById(R.id.bSearchBack);
//		tvSearchItemImage = (TextView) searchItemDetails
//				.findViewById(R.id.ivSearchItemImage);
//		tvSearchItemDiscountImage = (TextView) searchItemDetails
//				.findViewById(R.id.tvSearchItemDiscountImage);
//		tvSearchItemText1 = (TextView) searchItemDetails
//				.findViewById(R.id.tvSearchItemText1);
//		tvSearchItemText2 = (TextView) searchItemDetails
//				.findViewById(R.id.tvSearchItemText2);
//		tvSearchItemText3 = (TextView) searchItemDetails
//				.findViewById(R.id.tvSearchItemText3);
//		tvSearchItemPrice = (TextView) searchItemDetails
//				.findViewById(R.id.tvSearchItemPrice);
//		tvSearchItemPriceFraction = (TextView) searchItemDetails
//				.findViewById(R.id.tvSearchItemPriceFraction);
//		bSearchItemAddBusket = (Button) searchItemDetails
//				.findViewById(R.id.bSearchItemAddBusket);
//		bSearchItemSubtract = (Button) searchItemDetails
//				.findViewById(R.id.bSearchItemSubtract);
//		bSearchItemAdd = (Button) searchItemDetails
//				.findViewById(R.id.bSearchItemAdd);
//		etSearchItemQuantity = (EditText) searchItemDetails
//				.findViewById(R.id.etSearchItemQuantity);
//		bSearchItemAddBusket.setOnClickListener(this);
//		bSearchBack.setOnClickListener(this);
//		bSearchItemAdd.setOnClickListener(this);
//		bSearchItemSubtract.setOnClickListener(this);
//
//		lldiscount = (LinearLayout) searchItemDetails
//				.findViewById(R.id.llDiscountFromSearch);
//		discountHeaderText = (TextView) searchItemDetails
//				.findViewById(R.id.tvSearchDiscountHeaderText);
//		discountFooterText = (TextView) searchItemDetails
//				.findViewById(R.id.tvSearchDiscountFooterText);
//
//		etSearchItemQuantity.addTextChangedListener(new TextWatcher() {
//
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//
//			}
//
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//
//			}
//
//			public void afterTextChanged(Editable s) {
//				if (orderLine != null) {
//					Home.showBasketCounter(CommonBasketValues.getInstance().Basket
//							.getTotalItemCount()
//							- orderLine.quantity
//							+ Integer
//									.valueOf("0"
//											+ etSearchItemQuantity.getText()
//													.toString()));
//					updateBasket();
//					if(Integer.valueOf("0"+ etSearchItemQuantity.getText().toString())==0){
//						flipper1.setDisplayedChild(0);
//						bSearchItemAddBusket.setVisibility(View.VISIBLE);
//						imm.hideSoftInputFromWindow(
//								etSearchItemQuantity.getWindowToken(), 0);
//					}
//					
//				}
//			}
//		});
//
//		if (type.equals("new")) {
//
//			flipper1.setDisplayedChild(0);
//			bSearchItemAddBusket.setVisibility(View.VISIBLE);
//
//		}
//
//	}

	/**
	 * Assign all related values of article to the article details screen Here
	 * image for the article are loaded by Asynchronously using AsyncImageLoader
	 * Also load sub/related articles Asynchronously using
	 * SearchItemDiscountFamilyImageTask
	 * 
	 * @param articleInq
	 */

//	private void AssignValues(ArticleInq articleInq) {
//		try {
//			PriceInquiery priceInquiry = articleInq.getPriceInquiery();
//			etSearchItemQuantity.setTag(priceInquiry);
//			if (ArticleInq.IsProductFound) {
//				tvSearchItemText1.setText(CommonTask.toCamelCase(
//						priceInquiry.text, " "));
//				if (priceInquiry.text2 != null) {
//					tvSearchItemText2.setText(CommonTask.toCamelCase(
//							priceInquiry.text2, " "));
//					tvSearchItemText2.setVisibility(View.VISIBLE);
//				} else {
//					tvSearchItemText2.setText("");
//					tvSearchItemText2.setVisibility(View.INVISIBLE);
//				}
//
//				if (priceInquiry.contents > 0 && priceInquiry.priceper > 0) {
//					tvSearchItemText3
//							.setText(CommonTask
//									.getContentString(priceInquiry.contents)
//									+ priceInquiry.contentsdesc
//									+ " ("
//									+ priceInquiry.priceperdesc
//									+ "-pris"
//									+ " "
//									+ CommonTask
//											.getString(priceInquiry.priceper)
//									+ ")");
//					tvSearchItemText3.setVisibility(View.VISIBLE);
//				} else {
//					tvSearchItemText3.setText("");
//					tvSearchItemText3.setVisibility(View.INVISIBLE);
//				}
//
//				//TODO: why are we not directly splitting with .(dots)
//				String[] vals = String.valueOf(priceInquiry.price)
//						.replace('.', ':').split(":");
//				tvSearchItemPrice.setText(vals[0]);
//				tvSearchItemPriceFraction
//						.setText(vals[1].length() > 1 ? vals[1] : vals[1] + "0");
//
//				if (priceInquiry.getDiscount().quantity > 0) {
//					String dVal = String
//							.valueOf(priceInquiry.getDiscount().quantity)
//							+ " "
//							+ priceInquiry.getDiscount().text
//							+ " "
//							+ Math.round(priceInquiry.getDiscount().amount);
//					tvSearchItemDiscountImage.setText(dVal);
//					tvSearchItemDiscountImage.setVisibility(View.VISIBLE);
//				} else {
//					tvSearchItemDiscountImage.setVisibility(View.INVISIBLE);
//				}
//
//				tvSearchItemImage.setTag(articleInq.EAN);
//				AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
//				asyncImageLoader.execute(tvSearchItemImage);
//
//				discountGroup = priceInquiry.getDiscountGroup();
//
//				if (discountGroup != null && discountGroup.id != "") {
//					discountHeaderText.setVisibility(View.VISIBLE);
//					discountFooterText.setVisibility(View.VISIBLE);
//					lldiscount.setVisibility(View.VISIBLE);
//					discountFooterText.setText(discountGroup.text);
//					searchItemDiscountFamilyImageTask = new SearchItemDiscountFamilyImageTask(
//							this);
//					searchItemDiscountFamilyImageTask.execute();
//				} else {
//					discountHeaderText.setVisibility(View.INVISIBLE);
//					discountFooterText.setVisibility(View.INVISIBLE);
//					lldiscount.setVisibility(View.INVISIBLE);
//					discountFooterText.setText("");
//				}
//
//			} else {
//				tvSearchItemText1.setText(getString(R.string.productError));
//				tvSearchItemText2.setText("");
//				tvSearchItemPrice.setText("");
//				tvSearchItemPriceFraction.setText("");
//				tvSearchItemText3.setText("");
//				tvSearchItemImage.setBackgroundDrawable(CommonTask.getDrawableImage(String.format(
//				CommonURL.getInstance().ProductImageURL,articleId), articleId));
//			}
//		} catch (Exception e) {
//			tvSearchItemText1.setText(getString(R.string.productError));
//			tvSearchItemText2.setText("");
//			tvSearchItemPrice.setText("");
//			tvSearchItemPriceFraction.setText("");
//			tvSearchItemText3.setText("");
//			tvSearchItemImage.setBackgroundDrawable(CommonTask.getDrawableImage(String.format(
//			CommonURL.getInstance().ProductImageURL,articleId), articleId));
//		}
//		rlSearchtDetail.setVisibility(View.VISIBLE);
//		Animation animation = AnimationUtils.loadAnimation(this,
//				R.anim.right_to_left);
//		flipper1.startAnimation(animation);
//		flipper1.setVisibility(View.VISIBLE);
//	}

	public void onClick(View v) {
		try {
			flipper.setInAnimation(CommonTask.inFromLeftAnimation());
			flipper.setOutAnimation(CommonTask.outToRightAnimation());
			switch (v.getId()) {
//			case R.id.bFamilyBack: {
//				flipper.showPrevious();
//				backState = StateSearch.INITIAL_STATE;
//				break;
//			}
//			case R.id.bGroupBack: {
//				flipper.showPrevious();
//				backState = StateSearch.BACK_FROM_GROUP;
//				break;
//			}
			case R.id.bSearchItemAddBusket: {
				orderLine = CommonTask
						.getOrderLineFromBasketByEan(((PriceInquiery) etSearchItemQuantity
								.getTag()).EAN);
				if (asyncAddBasket != null) {
					asyncAddBasket.cancel(true);
				}
//				asyncAddBasket = new AsyncAddBasketFromSearch(asyncAddBasketListener);
//				asyncAddBasket.execute();
				break;
			}
			case R.id.bSearchItemAdd: {
				selectedItemQty = Integer.valueOf("0"
						+ etSearchItemQuantity.getText().toString());
				selectedItemQty++;
				etSearchItemQuantity.setText(String.valueOf(selectedItemQty));
				etSearchItemQuantity.setSelection(etSearchItemQuantity
						.getText().length());
				imm.hideSoftInputFromWindow(
						etSearchItemQuantity.getWindowToken(), 0);
				Home.showBasketCounter(CommonBasketValues.getInstance().Basket
						.getTotalItemCount()
						- orderLine.quantity
						+ selectedItemQty);
				break;
			}
			case R.id.bSearchItemSubtract: {
				selectedItemQty = Integer.valueOf("0"
						+ etSearchItemQuantity.getText().toString());
				if (selectedItemQty > 0)
					selectedItemQty--;
				etSearchItemQuantity.setText(String.valueOf(selectedItemQty));
				etSearchItemQuantity.setSelection(etSearchItemQuantity
						.getText().length());
				imm.hideSoftInputFromWindow(
						etSearchItemQuantity.getWindowToken(), 0);
				Home.showBasketCounter(CommonBasketValues.getInstance().Basket
						.getTotalItemCount()
						- orderLine.quantity
						+ selectedItemQty);
				
				break;
			}
			case R.id.bSearchBack: {
				backFromArticleDetail();
				break;
			}
			case R.id.bsearchcancel: {
				cancelBack();
				break;
			}
			default:
				break;
			}
		} catch (Exception oEx) {
		}
	}

	/**
	 * Add article to the order line if the order line(selected article) is old
	 * one or a continuous order then just add the quantity Or if the order line
	 * (selected article) are new the add it with the order as a new orderline
	 * with quantity 1
	 */
//	public void startAddingTask() {
//		if (orderLine != null && orderLine.Id != "") {
//			selectedItemQty = orderLine.quantity + 1;
//			CommonTask.addBasketFromDetail(this, orderLine, selectedItemQty);
//		} else {
//			selectedItemQty = 1;
//
//			CommonTask.addBasketObject(this,
//					(PriceInquiery) etSearchItemQuantity.getTag());
//		}
//	}

	/**
	 * Update basket title & quantity Show update basket screen for the selected
	 * item
	 */
//	public void finishAddingTask() {
//		updateOrderline();
//		if(orderLine!=null){
//			selectedItemQty = orderLine.quantity;
//			etSearchItemQuantity.setText(String.valueOf(selectedItemQty));
//			etSearchItemQuantity.setSelection(etSearchItemQuantity.getText()
//					.length());
//			Home.showBasketMenu();
//			flipper1.showNext();
//		}
//	}

	private void cancelBack() {
		rlayfamilyheaderdefault.setVisibility(View.VISIBLE);
		rlayarticle.setVisibility(View.GONE);

		rlfamilylist.setVisibility(View.VISIBLE);

		imm.hideSoftInputFromWindow(saerchitem.getWindowToken(), 0);
		familylistview.setEnabled(true);

		listOverlay.setVisibility(View.GONE);
		backState = StateSearch.INITIAL_STATE;
	}

	/**
	 * Call where user need to back from Article Detail screen if order related
	 * issue done like increase/decrease order quantity then update basket also
	 */

	private void backFromArticleDetail() {
//		if (isSearchDetailsReady) {
//			isBackToArticle = false;
//			flipper.setInAnimation(CommonTask.inFromLeftAnimation());
//			flipper.setOutAnimation(CommonTask.outToRightAnimation());
//			if (articledetailscallfrom == 1) {
//				flipper.showPrevious();
//				if (flipper.getChildCount() == noOfDefaultFlipperControls + 1) {
//					backState = StateSearch.BACK_TO_PREVIOUS;
//				}
//			} else {
//
//				if (flipper.getChildCount() > noOfDefaultFlipperControls + 1)
//					flipper.showPrevious();
//				else {
//					backState = StateSearch.CANCEL_SEARCH;
//					flipper.setDisplayedChild(0);
//				}
//			}
//			if (flipper.getChildCount() > noOfDefaultFlipperControls) {
//				updateBasket();
////				InitializeArticleDetailsContent("old");
//				flipper.removeViewAt(flipper.getDisplayedChild() > 1 ? flipper
//						.getDisplayedChild() + 1 : positionNo);
//			}
//		}
		
		if(flipper.getDisplayedChild()>(noOfOtherControlsFromPosition-1)){
			flipper.setDisplayedChild(flipper.getChildCount()-2);
			flipper.removeViewAt(flipper.getChildCount()-1);
		}
//		else if(flipper.getDisplayedChild() == noOfDefaultFlipperControls + 1){
//			backState = StateSearch.BACK_TO_PREVIOUS;
//		}
		
		if(flipper.getDisplayedChild() == (noOfOtherControlsFromPosition-1)){
			backState = StateSearch.BACK_TO_PREVIOUS;
		}
	}

	/**
	 * If user update the order quantity then call this function and update
	 * basket Asynchronously using runBacktoBasketAsync()
	 */
	public void updateBasket() {
		try {
			if (isSearchDetailsReady && flipper1 != null
					&& flipper1.getDisplayedChild() > 0) {
				updateOrderline();
				if (orderLine != null && orderLine.Id != "") {
					selectedItemQty = Integer.valueOf("0"
							+ etSearchItemQuantity.getText().toString());
					if (orderLine.quantity != selectedItemQty) {
						runBacktoBasketAsync();
					}
					imm.hideSoftInputFromWindow(
							etSearchItemQuantity.getWindowToken(), 0);

				}
			}
		} catch (Exception oEx) {
		}
	}

	public void updateOrderline() {
		if( null != etSearchItemQuantity && null != etSearchItemQuantity.getTag()){
			orderLine = CommonTask
					.getOrderLineFromBasketByEan(((PriceInquiery) etSearchItemQuantity
							.getTag()).EAN);
		}
	}

	private void runBacktoBasketAsync() {
		updateBasketWithArticleDetail();
	}

	/**
	 * call async class for Updating Basket From Article Detail..Tanvir
	 */
	public void updateBasketWithArticleDetail() {
		if (asyncUpdateBasketFromArticleDetailTask != null) {
			asyncUpdateBasketFromArticleDetailTask.cancel(true);
		}
		asyncUpdateBasketFromArticleDetailTask = new AsyncUpdateBasketFromArticleDetailTask(
				this);
		asyncUpdateBasketFromArticleDetailTask.execute();
	}

	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.etsearcharticle: {
			whohasfocus = 2;
		}
		default:
			break;
		}
	}

	protected void onCancel() {
		super.onPause();
	}


	

	
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	public void afterTextChanged(Editable s) {

		switch (whohasfocus) {
		case 2: {
		}
		default:
			break;
		}
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	public void startProgress() {
		progressBar.setVisibility(View.VISIBLE);
	}

	public void stopProgress() {

		progressBar.setVisibility(View.INVISIBLE);
	}

	public void startProgressForSearch() {
		searchResultProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopProgressForSearch() {

		searchResultProgressBar.setVisibility(View.INVISIBLE);
	}

	public void startDiscountProgressBar() {

		llDiscountProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopDiscountProgressBar() {

		llDiscountProgressBar.setVisibility(View.INVISIBLE);
	}

	public boolean onDown(MotionEvent arg0) {

		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// Check movement along the Y-axis. If it exceeds SWIPE_MAX_OFF_PATH,
		// then dismiss the swipe.
		if (Math.abs(e1.getY() - e2.getY()) > CommonTask.SWIPE_MAX_OFF_PATH())
			return false;

		// Swipe from right to left.
		// The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE) and
		// a certain velocity (SWIPE_THRESHOLD_VELOCITY).
		if (e1.getX() - e2.getX() > CommonTask.SWIPE_MIN_DISTANCE()
				&& Math.abs(velocityX) > CommonTask.SWIPE_THRESHOLD_VELOCITY()) {
			// do stuff

			return true;
		}

		try {
			if (flipper.getDisplayedChild() > 0) {

				// Swipe from left to right.
				// The swipe needs to exceed a certain distance
				// (SWIPE_MIN_DISTANCE) and a certain velocity
				// (SWIPE_THRESHOLD_VELOCITY).
				boolean isDetectable = true;
				if (lldiscount != null) {
					int[] pos = new int[2];
					lldiscount.getLocationInWindow(pos);
					int ypos = pos != null ? pos[1] - lldiscount.getHeight()
							/ 2 : 0;
					isDetectable = e1.getY() > 40 ? lldiscount.isShown() ? e1
							.getY() > ypos
							&& e1.getY() < ypos + lldiscount.getHeight() ? false
							: true
							: true
							: false;
				}
				if (isDetectable
						&& e2.getX() - e1.getX() > CommonTask
								.SWIPE_MIN_DISTANCE()
						&& Math.abs(velocityX) > CommonTask
								.SWIPE_THRESHOLD_VELOCITY()) {
					flipper.setInAnimation(CommonTask.inFromLeftAnimation());
					flipper.setOutAnimation(CommonTask.outToRightAnimation());
					switch (backState) {
					case BACK_FROM_GROUP:// use for back from group
						flipper.showPrevious();
						backState = StateSearch.INITIAL_STATE;
						break;
					case BACK_TO_PREVIOUS:
						flipper.showPrevious();
						backState = StateSearch.BACK_FROM_GROUP;
						break;
					case BACK_FROM_ARTICLE_DETAIL: // use for back from article
													// detail
						backFromArticleDetail();
						break;
					case CANCEL_SEARCH: // use for cancel search
						cancelBack();
						break;
					default:
						break;
					}
					return true;
				}
			}
		} catch (Exception oEx) {

		}

		return false;
	}

	public void onLongPress(MotionEvent e) {

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (distanceY > 0 && flipper != null && etsearcharticle != null) {
			imm.hideSoftInputFromWindow(etsearcharticle.getWindowToken(), 0);
		}

		return false;
	}

	public void onShowPress(MotionEvent e) {

	}

	public boolean onSingleTapUp(MotionEvent e) {

		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		return gestureScanner.onTouchEvent(me);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (gestureScanner != null) {
			if (gestureScanner.onTouchEvent(ev))
				return true;
		}
		return super.dispatchTouchEvent(ev);
	}

	public int getFlipperDisplayIndex() {
		return flipper.getDisplayedChild();
	}

	// update basket if same menu select again.
	public void updateBasketAfterTabReselect() {
		try {
			if (flipper != null && flipper.getDisplayedChild() > 0) {
				if (etSearchItemQuantity != null
						&& etSearchItemQuantity.getTag() != null) {
					orderLine = CommonTask
							.getOrderLineFromBasketByEan(((PriceInquiery) etSearchItemQuantity
									.getTag()).EAN);
				} else {
					orderLine = null;
				}

				flipper.setDisplayedChild(0);
				if (flipper1 != null && flipper1.getDisplayedChild() == 1
						&& orderLine != null) {
					OrderLine objOrderLine = orderLine;
					selectedItemQty = Integer.valueOf("0"
							+ etSearchItemQuantity.getText().toString());
					if (objOrderLine.quantity != selectedItemQty) {
						Search.orderLine = objOrderLine;
						runBacktoBasketAsync();
					}
					imm.hideSoftInputFromWindow(
							etSearchItemQuantity.getWindowToken(), 0);
				}
//				InitializeArticleDetailsContent("old");
				for (int i = 4; i < flipper.getChildCount()
						- noOfOtherControlsFromPosition; i++) {
					flipper.removeViewAt(i);
				}
				backState = StateSearch.INITIAL_STATE;

			}
		} catch (Exception e) {
		}
	}
	
//	AsyncTaskAddBasketFromSearchInterface asyncAddBasketListener = new AsyncTaskAddBasketFromSearchInterface() {
//		
//		@Override
//		public void onTaskPreExecute() {
//			startProgressForSearch();
//		}
//		
//		@Override
//		public void onTaskPostExecute(Object result) {
//			stopProgressForSearch();
//			if (CommonValues.getInstance().ErrorCode==CommonConstraints.NO_EXCEPTION) {
//				finishAddingTask();
//			} else {
//				CommonTask.ShowMessage(Search.this,
//						CommonTask.getCustomExceptionMessage(Search.this, CommonValues.getInstance().ErrorCode));
//			}
//		}
//		
//		@Override
//		public void startAddTask() {
//			startAddingTask();
//		}
//
//		@Override
//		public void onDoInBackground() {
//		}
//	};
	
	//this is for loading  details
	AsyncTaskInterface detailsAsyncTaskListener = new AsyncTaskInterface() {
		
		@Override
		public void onTaskPreExecute() {
			isSearchDetailsReady = false;
			startProgress();
		}
		
		@Override
		public void onTaskPostExecute(Object object) {
			stopProgress();	
			if(CommonValues.getInstance().IsArticleDetailsRecordFound)
			{			
				if(CommonValues.getInstance().IsServerConnectionError)
				{
					CommonTask.ShowMessage(Search.this, Search.this.getString(R.string.serverswitchError));		
				}
				else
				{
					if(object instanceof ArticleInq)
					articleInqObj = (ArticleInq)object;
					//view the Article Inquiry details from articleinqobject
					viewArticleDetailsListView(Search.this);
					
				}
			}
			isSearchDetailsReady = true;	
		}
		
		@Override
		public void onDoInBackground() {
			
		}
	};
}
