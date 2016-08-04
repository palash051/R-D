package com.shopper.app.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.shopper.app.R;
import com.shopper.app.base.CatalogueBase;
import com.shopper.app.entities.CataloguePageList;
import com.shopper.app.entities.CataloguePageList.CatalogueButton;
import com.shopper.app.entities.CataloguePageList.CataloguePage;
import com.shopper.app.entities.HorizontalView;
import com.shopper.app.utils.CommonTask;

/**
 * Automatically call when "Tilbudsavis" menu select and Manage all Catalogue related task
 * This is a Catalogue page, where we can order perticular items on the selected item detail screen
 * All item are pin in a given location on the Catalogue image
 * Catalogue screen are created base on catalogue_page.xml file given information
 * Here have product's order button position, Catalogue image page, Product EAN, button size etc.
 * User can Zoom In/Out or drag the Catalogue items
 * User can update any item quantity on the Catalogue item details screen 
 * After pressing back button with isDetailsVisible = false always gone to home screen otherwise came back to the previous screen 
 * @author Shafiqul Alam
 * 
 */

@TargetApi(14)
public class Catalogue extends CatalogueBase implements OnGestureListener {

	private static final int MINIMUM_FINGER_MOVE_DISTANCE = 1;

	/**
	 * MINIMUM_ZOOM is the lowest zoom allowed MAXIMUM_FINAL_ZOOM Screen will
	 * resize to this, if zoomed to more. MAXIMUM_USER_ZOOM This is the max a
	 * user can zoom. It will resize to final_zoom if bigger than final_zoom
	 */
	private static final float ZOOM_FACTOR = 0.004f, INITIAL_DISTANCE = 0.0f,
			MINIMUM_DISTANCE = 10f, MINIMUM_USER_ZOOM = 0.6f,
			MAXIMUM_FINAL_ZOOM = 2.5f, MAXIMUM_USER_ZOOM = 3.0f,
			DEFAULT_ZOOM = 1f;

	// Zoom and swipe related variables
	private static final String TAG = "Touch";

	private enum Action {
		DRAG, ZOOM, NONE
	}

	boolean allowSwipe = true;
	Action mode;

	// Remember some things for zooming
	PointF previousfinger = new PointF();

	double previousDistance = 0f, currentDistance, tempOldDistance,
			scaledDistance = 1.1f, totalHeight, totalWidth, scale,
			previousScale;
	int scrolledX = 0, scrolledY = 0, cataloguePageSlidingSpeed = 40,
			distanceForHorizontalAdjustment = 0, catalogueLastPageNo;

	private static int PAGE_HEIGHT = 679, PAGE_WIDTH = 480;

	Runnable catalogueViewRunnable;
	Handler catalogueViewHandler;
	private boolean isPageSliding;
	public View selectedCataloguePage;

	// Catalogue page & viewflipper related variables
	CataloguePageList cataloguePageList;

	LinearLayout layout;
	HorizontalView horizontalView;

	/**
	 * Automatically call from menu select once initialize all controls
	 * initialize all controls
	 * pin all button for the particular item position 
	 */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.catalogue);

		try {
			inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			detailProgressBar = (ProgressBar) findViewById(R.id.catalogueProgressBar);
			detailProgressBar.setVisibility(View.INVISIBLE);
			llDiscountProgressBar = (ProgressBar) findViewById(R.id.catalogueDiscountProgressBar);
			llDiscountProgressBar.setVisibility(View.INVISIBLE);
			catalogueFlipper = (ViewFlipper) findViewById(R.id.vfCatalogue);
			cataloguePageList = new CataloguePageList(this);
			catalogueLastPageNo = cataloguePageList.pageList.size() - 1;
			gestureScanner = new GestureDetector(Catalogue.this,this);
			horizontalView = new HorizontalView(this);
			horizontalView.setHorizontalScrollBarEnabled(true);
			layout = new LinearLayout(this);
			for (int i = 0; i < cataloguePageList.pageList.size(); i++) {
				View v = preparePageView(i,
						i == cataloguePageList.pageList.size() - 1 ? false
								: true);
				selectedCataloguePage = v;
				v.setOnTouchListener(new OnTouchListener() {

					public boolean onTouch(View v, MotionEvent event) {

						try {
							return setOnTouch(v, event);
						} catch (Exception ex) {
							return false;

						}

					}
				});

				layout.addView(v);
			}

			horizontalView.addView(layout);
			catalogueFlipper.addView(horizontalView);
		} catch (Exception oEx) {
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
//		updateBasket();

	}

	@Override
	public void onBackPressed() {
		if (isDetailsVisible) {
			backFromArticleDetail();
		} else {
			currentMenuIndex = getLastIndex();
			manageActivity();
			
		}

	}

	// Preparing catalogue page

	View preparePageView(int no, boolean rm) {
		CataloguePage cp = cataloguePageList.pageList.size() > no ? cataloguePageList.pageList
				.get(no) : null;
		if (cp != null) {
			
			WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE); 
			Display display = wm.getDefaultDisplay();  
	    	Point size = new Point();
	    	display.getSize(size);
	    	int screenWidth = size.x;
	    	int screenHeight = size.y;
			PAGE_WIDTH = screenWidth;
			PAGE_HEIGHT = screenHeight;
			RelativeLayout page = new RelativeLayout(this);
			page.setTag(no);
			page.setBackgroundColor(Color.BLACK);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

//			params.width = CommonTask.convertToDimensionValue(this, 320);
			params.width = screenWidth;
//			params.height = screenHeight;
			page.setLayoutParams(params);
//			page.setLayoutParams(params);

			ImageView image = new ImageView(this);
			params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			image.setScaleType(ScaleType.FIT_XY);
			params.width = screenWidth;
//			params.height = screenHeight;
//			params.height = CommonTask.convertToDimensionValue(this, 455);
//			if (rm)
//				params.setMargins(0, 0, 10, 0);
			image.setLayoutParams(params);
//			image.setBackgroundResource(getResources().getIdentifier(
//					cp.imageName, "drawable", "com.shopper.app"));
			image.setImageBitmap(
				    decodeSampledBitmapFromResource(getResources(), getResources().getIdentifier(
							cp.imageName, "drawable", "com.shopper.app"), screenWidth, screenHeight));
			page.addView(image);

//			page.addView(image);

			for (int i = 0; i < cp.buttonList.size(); i++) {
				CatalogueButton cb = cp.buttonList.get(i);
				Button button = new Button(this);
				params = new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				params.height = cb.height;
				params.width = cb.width;
//				params.topMargin = cb.y;
				params.topMargin = (cb.y*(screenHeight-CommonTask.convertToDimensionValue(this, 50)))/CommonTask.convertToDimensionValue(this, 455);
//				params.leftMargin = cb.x;
				params.leftMargin = (cb.x*screenWidth)/CommonTask.convertToDimensionValue(this, 320);
				button.setTag(cb);
				button.setLayoutParams(params);
				button.setBackgroundResource(getResources().getIdentifier(
						cb.imageName, "drawable", "com.shopper.app"));
				button.setAlpha(0.75f);

				button.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						if (!isBasketAdding) {
							oldFlipperControls = catalogueFlipper
									.getChildCount();
							lastVisibleView = catalogueFlipper
									.getDisplayedChild();
							CatalogueButton cb = (CatalogueButton) v.getTag();
							articleId = cb.ean;
							ScaleAnimation anim = new ScaleAnimation(1.0f,
									1.2f, 1.0f, 1.2f,
									Animation.RELATIVE_TO_SELF, 0.5f,
									Animation.RELATIVE_TO_SELF, 0.5f);
							anim.setDuration(100);
							v.startAnimation(anim);
							basketButton = v;
							oldScaleForFullScreen = catalogueFlipper
									.getScaleX();

							anim.setAnimationListener(new AnimationListener() {

								public void onAnimationStart(Animation animation) {
									basketButton.setAlpha(1f);
								}

								public void onAnimationRepeat(
										Animation animation) {
								}

								public void onAnimationEnd(Animation animation) {

									ScaleAnimation anim = new ScaleAnimation(
											1.2f, 1.0f, 1.2f, 1.0f,
											Animation.RELATIVE_TO_SELF, 0.5f,
											Animation.RELATIVE_TO_SELF, 0.5f);
									anim.setDuration(100);
									basketButton.startAnimation(anim);
//									loadArticleDetailsContent("Tilbudsavis");
									basketButton.setAlpha(0.75f);
									isDetailsVisible = true;
									catalogueFlipper.setInAnimation(CommonTask.inFromRightAnimation());
									catalogueFlipper.setOutAnimation(CommonTask.outToLeftAnimation());
									new DisplayItemDetails(Catalogue.this, catalogueFlipper, articleId);

								}
							});

						}

					}
				});
				page.addView(button);
			}

			return page;
		}

		return new View(this);

	}

	// Start of Zoom and swipe related methods

	// This method adjust the catalogue view in one page view.
	// It calls after dragging horizontal scroll.
	public void adjustPagePosition(int distance) {
		distanceForHorizontalAdjustment = distance;
		catalogueViewRunnable = new Runnable() {
			public void run() {
				isPageSliding = true;
				if (Math.abs(distanceForHorizontalAdjustment) <= cataloguePageSlidingSpeed) {
					horizontalView.scrollBy(distanceForHorizontalAdjustment, 0);
					isPageSliding = false;
				} else {
					horizontalView
							.scrollBy(
									distanceForHorizontalAdjustment >= 0 ? cataloguePageSlidingSpeed
											: -cataloguePageSlidingSpeed, 0);
					distanceForHorizontalAdjustment -= distanceForHorizontalAdjustment >= 0 ? cataloguePageSlidingSpeed
							: -cataloguePageSlidingSpeed;
					new Handler().postDelayed(catalogueViewRunnable, 1);
				}

			}
		};
		catalogueViewHandler = new Handler();
		catalogueViewHandler.postDelayed(catalogueViewRunnable, 1);
	}

	public int getDragPageSlidingDistance() {
		Rect corners = new Rect();
		layout.getLocalVisibleRect(corners);
		return Math.round((float) corners.right / (PAGE_WIDTH)) * PAGE_WIDTH
				- corners.right;
	}

	// This method checks some conditions in which situation horizontal scroll
	// should not work.
	// It calls before dragging horizontal scroll.
	public boolean allowPageChange(MotionEvent ev) {
		Boolean result;
		result = (mode != Action.ZOOM && (selectedCataloguePage.getScaleX() == DEFAULT_ZOOM));
		return result;
	}

	// Implementation of touch events of catalogue page
	// It calls when touch on each catalogue page
	public boolean setOnTouch(View v, MotionEvent event) {
		try {

			resetLastPage(v);

			// saving last selected catalogue page
			selectedCataloguePage = v;

			// Handle touch events here...
			if (!isDetailsVisible) {
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					ACTION_DOWN(event);
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					ACTION_POINTER_DOWN(event);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
					ACTION_POINTER_UP();
					break;
				case MotionEvent.ACTION_MOVE:
					if (mode == Action.DRAG) {
						ACTION_DRAG(event);

					} else if (mode == Action.ZOOM) {
						ACTION_ZOOM(event);
					}
					break;
				}
			}

			return true;
		} catch (Exception ex) {
			return false;

		}
	}

	private void resetLastPage(View v) {
		if (selectedCataloguePage != null
				&& !selectedCataloguePage.getTag().toString()
						.equals(v.getTag().toString())) {
			presetSelectedCataloguePage();
		}
	}

	/**
	 * The user has put down his finger. He is either going to drag or swipe.
	 * 
	 * @param event
	 */
	void ACTION_DOWN(MotionEvent event) {
		previousfinger.set(event.getRawX(), event.getRawY());
		mode = Action.DRAG;
		allowSwipe = true;
		Log.d(TAG, "mode=DRAG");
	}

	/**
	 * User has put down second finger. He is going to zoom
	 * 
	 * @param event
	 */
	void ACTION_POINTER_DOWN(MotionEvent event) {
		adjustPagePosition(getDragPageSlidingDistance());
		tempOldDistance = INITIAL_DISTANCE;
		scaledDistance = 1.1f;
		previousDistance = currentDistance = distanceBetween(event);

		/* ignore if the fingers are not seperated by at least a little distance */
		if (currentDistance > MINIMUM_DISTANCE) {

			/*
			 * All zoom is relevant to the current scale. Save the current
			 * Scale.
			 */
			previousScale = selectedCataloguePage.getScaleX();

			mode = Action.ZOOM;
			allowSwipe = false;
			Log.d(TAG, "mode=ZOOM");
		}

	}

	/**
	 * User lifted last finger. Previously had both fingers on screen.
	 */
	void ACTION_POINTER_UP() {
		mode = Action.DRAG;
		if (previousScale < DEFAULT_ZOOM) {
			presetSelectedCataloguePage();
		} else if (DEFAULT_ZOOM < previousScale
				&& previousScale < MAXIMUM_FINAL_ZOOM) {
			// Do nothing
		} else if (previousScale > MAXIMUM_FINAL_ZOOM) {
			selectedCataloguePage.setScaleX(MAXIMUM_FINAL_ZOOM);
			selectedCataloguePage.setScaleY(MAXIMUM_FINAL_ZOOM);
		}
		Log.d(TAG, "mode=NONE");
	}

	void ACTION_DRAG(MotionEvent event) {
		if (selectedCataloguePage.getScaleX() == DEFAULT_ZOOM) {

			/* Reset position. Handles a problem with fulscreen/maximised */
			selectedCataloguePage.setScrollX(0);
			selectedCataloguePage.setScrollY(0);
			return;
		}

		Log.d("Drag", "x=" + event.getRawX() + "y=" + event.getRawY());
		Point finger = new Point((int) event.getRawX(), (int) event.getRawY());

		scroll(selectedCataloguePage, previousfinger, finger);
		undoIllegalPositions(finger);

		previousfinger.set((float) finger.x, (float) finger.y);

	}

	/**
	 * If any part of the new psotions is illegal, move that part back. ie if
	 * new x is illegal but new y is legal, undo the x part of the move. if both
	 * x and y is illegal, undo the entire move.
	 * 
	 * @param finger
	 * @param nCorners
	 * @param totalHeight
	 * @param totalWidth
	 */
	private void undoIllegalPositions(Point finger) {
		Rect nCorners = new Rect();
		/* The Visible Rectacngle uses Relative Coordinates */
		selectedCataloguePage.getLocalVisibleRect(nCorners);
		Log.d("Top,Right",
				Float.toString(nCorners.top) + ","
						+ Float.toString(nCorners.right));

		if (isNewXInvalid(nCorners, totalWidth)
				&& isNewYInvalid(nCorners, totalHeight)) {
			Log.d("New illegal position", "Undo move");
			/* Scroll ended in illegal position. Move back to previous position */
			scroll(selectedCataloguePage, -scrolledX, -scrolledY);

		} else if (isNewYInvalid(nCorners, totalHeight)) {
			Log.d("New illegal position", "Undo Y move");
			undoY(selectedCataloguePage, -scrolledY);
		} else if (isNewXInvalid(nCorners, totalWidth)) {
			Log.d("New illegal position", "Undo X move");
			undoX(selectedCataloguePage, -scrolledX);
		}
	}

	private boolean isNewXInvalid(Rect nCorners, double width) {
		return nCorners.left <= 0 || nCorners.right > width;
	}

	private boolean isNewYInvalid(Rect nCorners, double height) {
		return nCorners.top <= 0 || nCorners.bottom > height;
	}

	private void undoY(View page, int ySteps) {
		page.scrollBy(0, ySteps);

	}

	private void undoX(View page, int xSteps) {
		page.scrollBy(xSteps, 0);
	}

	private void scroll(View page, PointF from, Point to) {
		scroll(page, (int) from.x, (int) from.y, to.x, to.y);
	}

	private void scroll(View page, int xSteps, int ySteps) {
		page.scrollBy(xSteps, ySteps);
	}

	/* move from (fromX, fromY) to (toX, toY) */
	private void scroll(View page, int fromX, int fromY, int toX, int toY) {
		Log.d("Move", "(" + fromX + "," + fromY + ") to (" + toX + "," + toY
				+ ")");

		if (Math.abs(fromX - toX) > MINIMUM_FINGER_MOVE_DISTANCE) {
			scrolledX = (fromX - toX) / 2;// Adjusting scroll x moving speed x
											// axis
			page.scrollBy(scrolledX, 0);
		}
		if (Math.abs(fromY - toY) > MINIMUM_FINGER_MOVE_DISTANCE) {
			scrolledY = (fromY - toY) / 2;// Adjusting scroll moving speed y
											// axis
			page.scrollBy(0, scrolledY);
		}

	}

	void ACTION_ZOOM(MotionEvent event) {
		currentDistance = distanceBetween(event);

		/* Do nothing if the new distance is so small its irrelevant */
		if (currentDistance <= MINIMUM_DISTANCE)
			return;

		if (Math.abs(currentDistance - previousDistance) > MINIMUM_DISTANCE) {

			// /====:sim start
			scaledDistance = Math.abs(currentDistance - tempOldDistance);
			if (scaledDistance > 60) {
				scaledDistance = 60;
			}
			// /====:sim end
			if (currentDistance > previousDistance) {
				// Zoom in
				scale = previousScale + (ZOOM_FACTOR * scaledDistance);// :sim
			} else if (currentDistance < previousDistance) {
				// Zoom out
				scale = previousScale - (ZOOM_FACTOR * scaledDistance);// :sim

			}
			if ((MINIMUM_USER_ZOOM < scale && scale < DEFAULT_ZOOM)
					|| (DEFAULT_ZOOM <= scale && scale <= MAXIMUM_USER_ZOOM)) {
				setScaleAndPivot(selectedCataloguePage, scale, event);
				previousScale = scale;
			}
			tempOldDistance = currentDistance;
			previousDistance = currentDistance;
		}
	}

	/** setting scale & pivot position */
	private void setScaleAndPivot(View view, double scale, MotionEvent event) {

		if (Math.abs(view.getPivotX() - event.getX(0)) > MINIMUM_DISTANCE) {
			view.setPivotX((event.getX(0) + event.getX(1)) / 2);
		} else {

		}
		if (Math.abs(view.getPivotY() - event.getY(0)) > MINIMUM_DISTANCE) {
			view.setPivotY((event.getY(0) + event.getY(1)) / 2);
		} else {

		}
		view.setScaleX((float) scale);
		view.setScaleY((float) scale);
		totalHeight = PAGE_HEIGHT * scale;
		totalWidth = PAGE_WIDTH * scale;

	}

	/** Determine the space between the first two fingers */
	private double distanceBetween(MotionEvent event) {
		double x = event.getX(0) - event.getX(1);
		double y = event.getY(0) - event.getY(1);
		double result = Math.sqrt(x * x + y * y);
		Log.d("distanceBetween", "sqrt(" + x + "," + y + ")=" + result);
		return result;
	}

	public boolean onDown(MotionEvent e) {

		return false;
	}

	// Swipe method
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (!allowSwipe || isDetailsVisible)
			return false;
		if (!isPageSliding) {
			Log.d("Catalogue", "Swipe");

			// Swipe from right to left.
			// The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE)
			// and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
			if (e1.getX() - e2.getX() > CommonTask.SWIPE_MIN_DISTANCE()
					&& Math.abs(velocityX) > CommonTask
							.SWIPE_THRESHOLD_VELOCITY()) {
				doRightToLeftSwipe();
				return true;
			}

			// Swipe from left to right.
			// The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE)
			// and a certain velocity (SWIPE_THRESHOLD_VELOCITY).
			if (e2.getX() - e1.getX() > CommonTask.SWIPE_MIN_DISTANCE()
					&& Math.abs(velocityX) > CommonTask
							.SWIPE_THRESHOLD_VELOCITY()) {

				doLeftToRightSwipe();
				return true;

			}
		}
		return false;
	}

	private void doLeftToRightSwipe() {
		if (selectedCataloguePage != null) {
			presetSelectedCataloguePage();
			int slidingDistance = getSwipePageSlidingDistance();
			adjustPagePosition(slidingDistance == 0f ? -PAGE_WIDTH
					: -slidingDistance);
		}
	}

	private void doRightToLeftSwipe() {
		if (selectedCataloguePage != null) {
			presetSelectedCataloguePage();
			int slidingDistance = getSwipePageSlidingDistance();
			adjustPagePosition(slidingDistance == 0f ? PAGE_WIDTH : PAGE_WIDTH
					- slidingDistance);
		}
	}

	private void presetSelectedCataloguePage() {
		selectedCataloguePage.setScaleX(DEFAULT_ZOOM);
		selectedCataloguePage.setScaleY(DEFAULT_ZOOM);
		selectedCataloguePage.setScrollX(0);
		selectedCataloguePage.setScrollY(0);
	}

	private int getSwipePageSlidingDistance() {
		Rect corners = new Rect();
		layout.getLocalVisibleRect(corners);
		float layoutVisiblePortion = (float) corners.right / (PAGE_WIDTH);
		float slidingFactor = layoutVisiblePortion - (int) layoutVisiblePortion;
		return Math.round(slidingFactor * PAGE_WIDTH);
	}

	public void onLongPress(MotionEvent e) {

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	public void onShowPress(MotionEvent e) {

	}

	public boolean onSingleTapUp(MotionEvent e) {

		return false;
	}

	public boolean onInterceptTouchEvent(MotionEvent event) {
		return true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (gestureScanner != null) {
			if (gestureScanner.onTouchEvent(ev))
				return true;
		}

		return super.dispatchTouchEvent(ev);
	}

	// Start of Zoom and swipe related methods

	public boolean isAnimating() {
		return isPageSliding;
	}
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
		
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	    Log.d("insamplesize",""+reqWidth+" "+reqHeight+" "+width+" "+height);
	    if (height > reqHeight || width > reqWidth) {
	
	        // Calculate ratios of height and width to requested height and width
	        final int heightRatio = Math.round((float) height / (float) reqHeight);
	        final int widthRatio = Math.round((float) width / (float) reqWidth);
	
	        // Choose the smallest ratio as inSampleSize value, this will guarantee
	        // a final image with both dimensions larger than or equal to the
	        // requested height and width.
	        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
	        
	    }
	    if(inSampleSize == 1){
	    	//manual override
	    	inSampleSize = 2;
	    }
	    Log.d("insamplesize",""+inSampleSize);
	    return inSampleSize;
	}
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
}