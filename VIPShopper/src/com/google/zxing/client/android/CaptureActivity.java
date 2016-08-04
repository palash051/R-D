/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.common.BitMatrix;
import com.shopper.app.R;
import com.shopper.app.activities.Home;
import com.shopper.app.base.MainActionbarBase;
import com.shopper.app.entities.ArticleImage;
import com.shopper.app.entities.ArticleInq;
import com.shopper.app.entities.Discount;
import com.shopper.app.entities.DownloadArticleImage;
import com.shopper.app.entities.DownloadArticleInq;
import com.shopper.app.entities.PriceInquiery;
import com.shopper.app.enums.CameraMessageStatus;
import com.shopper.app.utils.CommonBasketValues;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;

/**
 * The barcode reader activity itself. This is loosely based on the
 * CameraPreview example included in the Android SDK.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 * @Modified by Shafiqul Alam
 */
public final class CaptureActivity extends MainActionbarBase implements
		SurfaceHolder.Callback {

	private static final String TAG = CaptureActivity.class.getSimpleName();

	private static final long BULK_MODE_SCAN_DELAY_MS = 1L;
	private static final long PRODUCT_DETAILS_DELAY_MS = 2500L;
	private static final int AUTO_FOCUS_START_INTERVAL = 3;
	private static final int SAME_PRODUCT_RESCAN_INTERVAL = 3;
	private static final float SHAKING_DELTA = 0.1f;
	private static final float SHAKING_Z_DELTA = 0.1f;

	private static CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	public TextView statusView;
	private static boolean hasSurface;
	
	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;
	private AmbientLightManager ambientLightManager;
	
	private Animation animTop;
	private Animation animBottom;

	public static String BARCODE;

	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}
	
	private CameraManager cameraManager;	
	CameraManager getCameraManager() {
	    return cameraManager;
	  }

	//TODO: do we need all these handlers or we can use one handler?
	private Handler mHandler, pAHandler, pDHandler, pIAHandler, bHandler;

	private static Thread statusThread, productAddThread,
			productImageAddThread, productDisplayThread, bThread;
	private Runnable mWaitRunnable, pAWaitRunnable, pIAWaitRunnable,
			pDWaitRunnable, bRunnable;
	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity
	private TableLayout productDetailsLayOut;
	private int autoFocusCounter = 0;
	public static boolean isAnyProductDisplaying;
	public String lastEan;
	public String currentEan;
	public static ArrayList<ArticleInq> articleInqList, displayArticleList,
			basketArticleList;
	public static ArrayList<String> sacannedItemListForArticle,
			sacannedItemListForImage;
	public static ArrayList<ArticleImage> articleImageList;
	private TextView dt, pt, pt2, tr, tf, ft, ai,tvScanningProgressCounter;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	public String currentPoolEanForArticle, currentPoolEanForImage;
	Time now;
	private int lastScanTime;
	float old_z;
	public static boolean lock_autofocus, light_on;
	public static boolean isBasketAddingReady;
	private Button lockAutoFocus, lightButton;

	public static boolean displayAddingStarted, displayRemovingStarted;
	public static boolean basketAddingStarted, basketRemovingStarted;

	public static boolean scanImageAddingStarted, scanImageRemovingStarted;
	public static boolean scanEanAddingStarted, scanEanRemovingStarted;
	
	public boolean calledFromCreate;
	
	// animation related
	private ViewGroup animationGroup;
	private Animation captureMoveAnimation;
	private ImageView captureImageView;
	private TextView capturedBarcodeTextView;
	private int screenWidth = 0;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Window window = getWindow();
	    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    hasSurface = false;
		setContentView(R.layout.capture);
		mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		surfaceHolder = surfaceView.getHolder();
		initializeTextviewControls();
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		handler = null;
//		decodeFormats = null;
//		characterSet = null;
		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);
		ambientLightManager = new AmbientLightManager(this);
		mHandler = new Handler();
		pAHandler = new Handler();
		pDHandler = new Handler();
		pIAHandler = new Handler();
		bHandler = new Handler();
		sacannedItemListForArticle = new ArrayList<String>();
		sacannedItemListForImage = new ArrayList<String>();
		displayArticleList = new ArrayList<ArticleInq>();
		basketArticleList = new ArrayList<ArticleInq>();
		articleInqList = new ArrayList<ArticleInq>();
		articleImageList = new ArrayList<ArticleImage>();
		animTop = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
		animBottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);
		initSensorManager();
		isAnyProductDisplaying = false;
		currentPoolEanForArticle = "";
		currentPoolEanForImage = "";
		isBasketAddingReady = true;
		displayAddingStarted = false;
		displayRemovingStarted = false;
		basketAddingStarted = false;
		basketRemovingStarted = false;
		scanImageAddingStarted = false;
		scanImageRemovingStarted = false;
		scanEanAddingStarted = false;
		scanEanRemovingStarted = false;
		now = new Time();
		calledFromCreate = true;
		BARCODE = "";
		initThread();
		light_on = false;
		lock_autofocus = true;
		lightButton = (Button) findViewById(R.id.status_light);
		lockAutoFocus = (Button) findViewById(R.id.status_eye);
		lightButton.setBackgroundResource(R.drawable.light);
		lockAutoFocus.setBackgroundResource(R.drawable.eye);
		lightButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				toggle_light();
			}
		});
		lockAutoFocus.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				toggle_focus();
			}
		});
		//

	}

	/**
	 * Initialize textviews
	 */
	public void initializeTextviewControls() {
		statusView = (TextView) findViewById(R.id.status_view);
		dt = ((TextView) findViewById(R.id.discountText));
		pt = ((TextView) findViewById(R.id.productText));
		pt2 = ((TextView) findViewById(R.id.productText2));
		tr = ((TextView) findViewById(R.id.priceReal));
		tf = ((TextView) findViewById(R.id.priceFraction));
		ft = ((TextView) findViewById(R.id.productFooterText));
		ai = ((TextView) findViewById(R.id.articleImageText));
		
//		tvScanningProgressCounter = ((TextView) findViewById(R.id.tvScanningProgressCounter));
		
		/*AnimationUtils.loadAnimation(this, R.anim.bottom_to_top);
		AnimationUtils.loadAnimation(this, R.anim.top_to_bottom);*/
		
		//animation related code............
		loadCaptureAnimation();
	}
	///////////anim realted////////////
	/**
	 * Pre-load the capture animation related views and animations from resources
	 */
	void loadCaptureAnimation() {
		animationGroup = (ViewGroup) findViewById(R.id.capturedbarcodeholder);
		captureImageView = (ImageView) animationGroup
				.findViewById(R.id.capturedimageview);
		capturedBarcodeTextView = (TextView) animationGroup
				.findViewById(R.id.capturedbarcodetv);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		animationGroup.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		captureMoveAnimation = AnimationUtils.loadAnimation(this,
				R.anim.captureanimationset);
		captureMoveAnimation.setFillAfter(false);
		captureMoveAnimation.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				// lets hide the layer after animation is complete
				animationGroup.setVisibility(View.GONE);
				animationGroup.clearAnimation();
			}
		});
	}
	/**
	 * Displays a barcode animation of the provided barcode data in parameter. The function generates a Bitmap with
	 * barcode data and sets the barcode data in a textview below it and shows the animation
	 * @param barcode the barcode data in string. Barcode image generated using BarcodeFormat.CODE_128
	 */
	private void displayBarcodeAnimation(String barcode) {

		// barcode image
		Bitmap bitmap = null;
		// show original barcode animation
		try {
			int barcodeWidth = (screenWidth * 3) / 4;
			int barcodeHeight = barcodeWidth / 2;
			bitmap = encodeAsBitmap(barcode, BarcodeFormat.CODE_128,
					barcodeWidth, barcodeHeight);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		captureImageView.setImageBitmap(bitmap);
		capturedBarcodeTextView.setText(barcode);

		bitmap = null;
		animationGroup.clearAnimation();
		animationGroup.setVisibility(View.VISIBLE);
		animationGroup.startAnimation(captureMoveAnimation);

	}
	LayoutParams imageLayoutParams;
	private void displayBarcodeAnimation2(String barcodeText,Bitmap barcodeBmp) {

		// barcode image
//		Bitmap bitmap = null;
//		// show original barcode animation
//		try {
//			int barcodeWidth = (screenWidth * 3) / 4;
//			int barcodeHeight = barcodeWidth / 2;
//			bitmap = encodeAsBitmap(barcode, BarcodeFormat.CODE_128,
//					barcodeWidth, barcodeHeight);
//		} catch (WriterException e) {
//			e.printStackTrace();
//		}
		if(imageLayoutParams==null){
			imageLayoutParams = new LinearLayout.LayoutParams(cameraManager.getFramingRect().width()-50,cameraManager.getFramingRect().height()-50);
			captureImageView.setLayoutParams(imageLayoutParams);
		}
		captureImageView.setImageBitmap(barcodeBmp);
		capturedBarcodeTextView.setText(barcodeText);

		barcodeBmp = null;
		animationGroup.clearAnimation();
		animationGroup.setVisibility(View.VISIBLE);
		animationGroup.startAnimation(captureMoveAnimation);

	}
	/**************************************************************
	 * getting from com.google.zxing.client.android.encode.QRCodeEncoder
	 * 
	 * See the sites below http://code.google.com/p/zxing/
	 * http://code.google.com
	 * /p/zxing/source/browse/trunk/android/src/com/google/
	 * zxing/client/android/encode/EncodeActivity.java
	 * http://code.google.com/p/zxing
	 * /source/browse/trunk/android/src/com/google/
	 * zxing/client/android/encode/QRCodeEncoder.java
	 */

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	/**
	 * Creates a Bitmap image of a barcode content using provided parameters. Zxing library used to generate barcode.
	 * @param contents the barcode content
	 * @param format barcode format from BarcodeFormat
	 * @param img_width the desired width of produced Bitmap
	 * @param img_height the desired height of produced Bitmap
	 * @return On success, A bitmap representing the barcode, on failure null is returned 
	 * @throws WriterException
	 */
	Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width,
			int img_height) throws WriterException {
		String contentsToEncode = contents;
		if (contentsToEncode == null) {
			return null;
		}
		Map<EncodeHintType, Object> hints = null;
		String encoding = guessAppropriateEncoding(contentsToEncode);
		if (encoding != null) {
			hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
			hints.put(EncodeHintType.CHARACTER_SET, encoding);
		}
		MultiFormatWriter writer = new MultiFormatWriter();
		BitMatrix result;
		try {
			result = writer.encode(contentsToEncode, format, img_width,
					img_height, hints);
		} catch (IllegalArgumentException iae) {
			// Unsupported format
			return null;
		}
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	private static String guessAppropriateEncoding(CharSequence contents) {
		// Very crude at the moment
		for (int i = 0; i < contents.length(); i++) {
			if (contents.charAt(i) > 0xFF) {
				return "UTF-8";
			}
		}
		return null;
	}
//////////////////////////////////////// end of animation related code///////////////

	/**
	 * Use for on/off camera light
	 */
	private void toggle_light() {
		try {
			if (!light_on) {
				lightButton.setBackgroundResource(R.drawable.light_selected);
				if (handler != null) {
					handler.quitSynchronously();
					handler = null;
				}
//				CameraManager.get().closeDriver();
				getCameraManager().closeDriver();
				initCamera(surfaceHolder);
				getCameraManager().getFramingRect();
//				CameraManager.get().turn_onFlash();
				getCameraManager().setTorch(true);

			} else {
				lightButton.setBackgroundResource(R.drawable.light);
				if (handler != null) {
					handler.quitSynchronously();
					handler = null;
				}
//				CameraManager.get().closeDriver();
				getCameraManager().closeDriver();
				initCamera(surfaceHolder);
//				CameraManager.get().turn_offFlash();
				getCameraManager().setTorch(false);

			}
			light_on = !light_on;
		} catch (Exception oEx) {

		}
	}

	// Use for auto focus of camera
	private void toggle_focus() {
		try {
			if (!lock_autofocus) {
				//getCameraManager().requestAutoFocus(handler, R.id.auto_focus);
				
				lockAutoFocus.setBackgroundResource(R.drawable.eye_selected);
			} else {
				lockAutoFocus.setBackgroundResource(R.drawable.eye);
				statusView.setText(getString(R.string.set_focus));
				CommonValues.getInstance().CameraMessage = CameraMessageStatus.Stopped;
			   //getCameraManager().requestAutoFocus(handler, R.id.auto_focus);
				
			}
			lock_autofocus = !lock_autofocus;
			cameraManager.setAutoFocus(lock_autofocus);
		} catch (Exception oEx) {

		}
	}

	private static boolean isScannednAddingFinished() {
		return sacannedItemListForArticle.size() == 0
				&& sacannedItemListForImage.size() == 0
				&& displayArticleList.size() == 0
				&& basketArticleList.size() == 0 && !isAnyProductDisplaying;
	}

	/**	 
	 * Stop capture Thread and reset all list after setting changed	 
	 */
	public static void resetScanningValuesAfterSettingChanged() {
		if (statusThread != null)
			statusThread.interrupt();
		if (productAddThread != null)
			productAddThread.interrupt();
		if (productImageAddThread != null)
			productImageAddThread.interrupt();
		if (productDisplayThread != null)
			productDisplayThread.interrupt();
		if (bThread != null)
			bThread.interrupt();
		if(handler!=null)
			handler.quitSynchronously();
		sacannedItemListForArticle = new ArrayList<String>();
		sacannedItemListForImage = new ArrayList<String>();
		displayArticleList = new ArrayList<ArticleInq>();
		basketArticleList = new ArrayList<ArticleInq>();
		isAnyProductDisplaying = false;	
	}

	/**
	 * Checking scanned items are adding to basket finished or not
	 * 
	 * @return
	 */
	public static boolean isBasketAddingFinished() {
		return (sacannedItemListForArticle == null || sacannedItemListForArticle
				.size() == 0)
				&& (basketArticleList == null || basketArticleList.size() == 0);
	}

	

	// initialize controls and variable for display the scanned product
	private void initProductDisplay() {
		if (isScannednAddingFinished()) {
			articleInqList = new ArrayList<ArticleInq>();
			articleImageList = new ArrayList<ArticleImage>();
		}
		if (isBasketAddingFinished()
				&& (CommonBasketValues.getInstance().Basket == null || CommonBasketValues
						.getInstance().Basket.OrderNo == 0)) {
			displayArticleList = new ArrayList<ArticleInq>();

		}

		lastScanTime = 0;
		lastEan = "";
		currentEan = "";
		if (light_on) {
//			CameraManager.get().turn_onFlash();
			getCameraManager().setTorch(true);
		}
		if (calledFromCreate) {
//			CameraManager.get().requestAutoFocus(handler, R.id.auto_focus);
			calledFromCreate = false;
		}

	}

	/**
	 * Manage scan and add scanned item to the basket
	 */
	private void initThread() {
		articleInqList = new ArrayList<ArticleInq>();
		articleImageList = new ArrayList<ArticleImage>();
		// for hide display
		if (statusThread == null) {
			productDetailsLayOut = (TableLayout) findViewById(R.id.tblProductDetails);
			mWaitRunnable = new Runnable() {
				public void run() {
					if (isAnyProductDisplaying) {
						productDetailsLayOut.startAnimation(animBottom);
						productDetailsLayOut.setVisibility(View.INVISIBLE);
						isAnyProductDisplaying = false;
					}

				}
			};

		}
		statusThread = new Thread(mWaitRunnable);
		statusThread.start();

		// For adding only Article images
		if (productImageAddThread == null) {
			pIAWaitRunnable = new Runnable() {
				public void run() {
					if (currentPoolEanForImage.equals("")
							&& !sacannedItemListForImage.isEmpty()) {
						try {
							currentPoolEanForImage = sacannedItemListForImage
									.get(0);
							addArticleImage();
						} catch (Exception oEx) {
							currentPoolEanForImage = "";
						}
					}
					pIAHandler.postDelayed(pIAWaitRunnable, 1);
				}
			};
		}
		productImageAddThread = new Thread(pIAWaitRunnable);
		productImageAddThread.start();

		// For adding only Article informations
		if (productAddThread == null) {

			pAWaitRunnable = new Runnable() {
				public void run() {
					if (currentPoolEanForArticle.equals("")
							&& !sacannedItemListForArticle.isEmpty()) {
						try {
							currentPoolEanForArticle = sacannedItemListForArticle
									.get(0);
							addArticle();
						} catch (Exception oEx) {
							currentPoolEanForArticle = "";
						}
					}
					pAHandler.postDelayed(pAWaitRunnable, 2);
				}
			};
		}
		productAddThread = new Thread(pAWaitRunnable);
		productAddThread.start();

		// For showing display
		if (productDisplayThread == null) {
			pDWaitRunnable = new Runnable() {
				public void run() {
					if (!isAnyProductDisplaying
							&& !displayArticleList.isEmpty()) {
						try {
							isAnyProductDisplaying = true;
							int lastDisplayedItemIndex = displayArticleList
									.size();
							addProduct(displayArticleList
									.get(lastDisplayedItemIndex - 1));
							
							displayRemovingStarted = true;
							while (!displayAddingStarted) {
								for (int i = 0; i < lastDisplayedItemIndex; i++) {
									displayArticleList.remove(0);
								}
								// displayArticleList.removeAll(displayArticleList.subList(0,
								// lastDisplayedItemIndex-1));
								displayRemovingStarted = false;
								break;
							}
//							tvScanningProgressCounter.setText(""+sacannedItemListForArticle.size()+" remaining");

						} catch (Exception oEx) {
							isAnyProductDisplaying = false;
						}
					}
					pDHandler.postDelayed(pDWaitRunnable, 3);
				}
			};

		}
		productDisplayThread = new Thread(pDWaitRunnable);
		productDisplayThread.start();

		// For adding basket
		if (bThread == null) {
			bRunnable = new Runnable() {
				public void run() {
					if (isBasketAddingReady && basketArticleList.size() > 0) {
						try {
							isBasketAddingReady = false;
							addBasket(basketArticleList.get(0));
						} catch (Exception oEx) {
							isBasketAddingReady = true;
						}
					}
					bHandler.postDelayed(bRunnable, 4);
				}
			};

		}
		bThread = new Thread(bRunnable);
		bThread.start();
	}

	private void addBasket(ArticleInq art) {
		art.addBasketAndImage(this);

	}

	private void addArticleImage() {
		new DownloadArticleImage().execute(this);

	}

	/**
	 * Use for getting article image
	 * 
	 * @param eanno
	 * @return
	 */
	public BitmapDrawable getArticleImage(String eanno) {
//		ArticleImage artimage = null;
//		try {
//			for (int i = 0; i < articleImageList.size(); i++) {
//				artimage = articleImageList.get(i);
//				if (artimage.eanNo.equals(eanno)) {
//					return artimage.image;
//				}
//			}
			for (ArticleImage artimage : articleImageList) {
				if (artimage.eanNo.equals(eanno)) {
					return artimage.image;
				}
			}
//		} catch (Exception oEx) {
//		}
		return null;

	}

	private void addArticle() {
		new DownloadArticleInq().execute(this);

	}

	public ArticleInq getArticle(String eanno) {
		ArticleInq article = null;
		try {
			for (int i = 0; i < articleInqList.size(); i++) {
				article = articleInqList.get(i);
				if (article.EAN.trim() == eanno.trim()) {
					return article;
				}
			}
		} catch (Exception oEx) {
		}
		return null;

	}

	/**
	 * Check and verified auto focus mode
	 */
	private void initSensorManager() {
		try {
			SensorManager sensorManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
			List<Sensor> mySensors = sensorManage
					.getSensorList(Sensor.TYPE_ACCELEROMETER);
			mAccel = 0.00f;
			mAccelCurrent = SensorManager.GRAVITY_EARTH;
			mAccelLast = SensorManager.GRAVITY_EARTH;
			sensorManage.registerListener(new SensorEventListener() {
				public void onAccuracyChanged(Sensor sensor, int accuracy) {

				}

				@SuppressLint("FloatMath")
				public void onSensorChanged(SensorEvent event) {

					float x = event.values[0];// Get X-Coordinator

					float y = event.values[1];// Get Y-Coordinator

					float z = event.values[2];// Get Z-Coordinator

					mAccelLast = mAccelCurrent;
					mAccelCurrent = (float) Math
							.sqrt((double) (x * x + y * y + z * z));
					float delta = mAccelCurrent - mAccelLast;
					mAccel = mAccel * 0.9f + delta; // perform low-cut filter

					if (SHAKING_DELTA > mAccel
							&& CommonValues.getInstance().CameraMessage != CameraMessageStatus.Focused) {
						// BY SAM for start autofocusing
						if (!lock_autofocus) {
							if (autoFocusCounter > AUTO_FOCUS_START_INTERVAL
									&& CommonValues.getInstance().CameraMessage == CameraMessageStatus.Shacked) {
//								CameraManager.get().requestAutoFocus(handler,
//										R.id.auto_focus);
								autoFocusCounter = 0;
								CommonValues.getInstance().CameraMessage = CameraMessageStatus.Stopped;
							} else if (autoFocusCounter > AUTO_FOCUS_START_INTERVAL) {
								autoFocusCounter = 0;
							}
						}
						if (!lock_autofocus
								&& Math.abs(z - old_z) > SHAKING_Z_DELTA) {

							statusView.setText(getString(R.string.set_focus));
//							CameraManager.get().requestAutoFocus(handler,
//									R.id.auto_focus);
							autoFocusCounter = 0;

						} else {
							if (!lock_autofocus) {

								statusView
										.setText(getString(R.string.brcd_img_middle));
								autoFocusCounter++;
							} else {

								statusView
										.setText(getString(R.string.brcd_press_focus));
							}
						}

					} else if (mAccel > SHAKING_DELTA)// Shaking
					{

						statusView.setText(getString(R.string.phone_silent));
						CommonValues.getInstance().CameraMessage = CameraMessageStatus.Shacked;

					} else {

						statusView.setText(getString(R.string.set_focus));
						CommonValues.getInstance().CameraMessage = CameraMessageStatus.Stopped;

					}
					old_z = z;

				}
			}, mySensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);

		} catch (Exception e) {
			Log.e("Unable to detect SensorManager. ",
					"Error data " + e.toString());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			Home.showBasketMenu();
			initCameraManager();
			viewfinderView.setCameraManager(cameraManager);
			
			resetStatusView();
			
			surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		    surfaceHolder = surfaceView.getHolder();
		    if (hasSurface) {
		      // The activity was paused but not stopped, so the surface still exists. Therefore
		      // surfaceCreated() won't be called, so init the camera here.
		      initCamera(surfaceHolder);
		    } else {
		      // Install the callback and wait for surfaceCreated() to init the camera.
		      surfaceHolder.addCallback(this);
		      surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		    }
			
//			if (hasSurface) {
//				initCamera(surfaceHolder);
//			} else {
//				surfaceHolder.addCallback(this);
//			}

			beepManager.updatePrefs();
			ambientLightManager.start(cameraManager);
			
			inactivityTimer.onResume();
			initProductDisplay();

		} catch (Exception e) {
			CommonTask.ShowMessage(this, e.getMessage());
		}

	}
	
	void initCameraManager(){
		 // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
	    // want to open the camera driver and measure the screen size if we're going to show the help on
	    // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
	    // off screen.
		cameraManager = new CameraManager(getApplication());
	}

	@Override
	protected void onPause() {
		
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		ambientLightManager.stop();
		getCameraManager().closeDriver();
		if (!hasSurface) {
		      SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		      SurfaceHolder surfaceHolder = surfaceView.getHolder();
		      surfaceHolder.removeCallback(this);
		 }
		super.onPause();
		
		/*if (!hasSurface) {
			surfaceHolder.removeCallback(this);
		}*/

		//hasSurface = true;
	}

	@Override
	protected void onStop() {
		inactivityTimer.shutdown();

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();

		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		currentMenuIndex = getLastIndex();
		manageActivity();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
	      Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
	    }
	    if (!hasSurface) {
	      hasSurface = true;
	      initCamera(holder);
	    }
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}
	
	
	/**
	   * A valid barcode has been found, so give an indication of success and show the results.
	   *
	   * @param rawResult The contents of the barcode.
	   * @param scaleFactor amount by which thumbnail was scaled
	   * @param barcode   A greyscale bitmap of the camera data which was decoded.
	   */
	  
	  
	  public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
	    inactivityTimer.onActivity();
	    lastResult = rawResult;
//	    ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, rawResult);
//
//	    boolean fromLiveScan = barcode != null;
//	    if (fromLiveScan) {
//	      historyManager.addHistoryItem(rawResult, resultHandler);
//	      // Then not from history, so beep/vibrate and we have an image to draw on
//	      beepManager.playBeepSoundAndVibrate();
//	      drawResultPoints(barcode, scaleFactor, rawResult);
//	    }
//
//	      displayBarcodeAnimation(currentEan);
//	      displayBarcodeAnimation2(rawResult.getText(),barcode);
//	    	String message = "Barcode result: "
//	            + " (" + rawResult.getText() + ')';
//	        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//	        // Wait a moment or else it will scan the same barcode continuously about 3 times
	        restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
//	        
	    
	        
	        try {
				if (barcode != null) {
					CaptureActivity.BARCODE = "";
					now.setToNow();
					currentEan = rawResult.getText().trim();
					
					if (!lastEan.equals(currentEan)
							|| Math.abs(now.second - lastScanTime) >= SAME_PRODUCT_RESCAN_INTERVAL) {
						beepManager.playBeepSoundAndVibrate();

						lastEan = currentEan;
						lastScanTime = now.second;
						
						if (currentEan!=null && !currentEan.isEmpty()) {
							drawResultPoints(barcode, scaleFactor, rawResult);
							displayBarcodeAnimation2(rawResult.getText(),barcode);					
						}
						
						//
						
//						restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
						scanEanAddingStarted = true;
						while (!scanEanRemovingStarted) {
							sacannedItemListForArticle.add(currentEan);
							scanEanAddingStarted = false;
							break;
						}
						scanImageAddingStarted = true;
						while (!scanImageRemovingStarted) {
							sacannedItemListForImage.add(currentEan);
							scanImageAddingStarted = false;
							break;
						}
						
//						tvScanningProgressCounter.setText(""+sacannedItemListForArticle.size()+" remaining");
					}
				}
			} catch (Exception e) {
				CommonTask.ShowMessage(this, e.getMessage());
			}
	  }
	

	/**
	 * A valid barcode has been found, so give an indication of success and show
	 * the results.
	 * 
	 * @param rawResult
	 *            The contents of the barcode.
	 * @param barcode
	 *            A greyscale bitmap of the camera data which was decoded.
	 */
	public void handleDecode(Result rawResult, Bitmap barcode) {
		if (barcode == null) {

		} else {
			handleDecode(rawResult.getText());
		}

	}

	public void handleDecode(String barcode) {
		try {
			if (barcode != null || barcode != "") {
				CaptureActivity.BARCODE = "";
				now.setToNow();
				currentEan = barcode;
				if (handler != null)
					handler.sendEmptyMessageDelayed(R.id.restart_preview,
							BULK_MODE_SCAN_DELAY_MS);
				inactivityTimer.onActivity();
				if (!lastEan.equals(currentEan)
						|| Math.abs(now.second - lastScanTime) >= SAME_PRODUCT_RESCAN_INTERVAL) {
					beepManager.playBeepSoundAndVibrate();

					lastEan = currentEan;
					lastScanTime = now.second;
					
					if (currentEan!=null && !currentEan.isEmpty()) {
						displayBarcodeAnimation(currentEan);						
					}
					
					//
					
					if (!lock_autofocus) {
						lock_autofocus = true;
						lockAutoFocus
								.setBackgroundResource(R.drawable.eye_selected);
					}
					scanEanAddingStarted = true;
					while (!scanEanRemovingStarted) {
						sacannedItemListForArticle.add(currentEan);
						scanEanAddingStarted = false;
						break;
					}
					scanImageAddingStarted = true;
					while (!scanImageRemovingStarted) {
						sacannedItemListForImage.add(currentEan);
						scanImageAddingStarted = false;
						break;
					}
					

				}
			}
		} catch (Exception e) {
			CommonTask.ShowMessage(this, e.getMessage());
		}

	}

	/**
	 * Use for adding scanned product to basket
	 * 
	 * @param artInq
	 */

	public void addProduct(ArticleInq artInq) {
		if (artInq != null) {
			dt.setVisibility(View.INVISIBLE);
			PriceInquiery priceInquiery = artInq.getPriceInquiery();
			try {

				if (ArticleInq.IsProductFound) {

					pt.setText(CommonTask.toCamelCase(priceInquiery.text, " "));
					if (priceInquiery.text2 != null) {
						pt2.setText(priceInquiery.text2);
					}
					String[] vals = String.valueOf(priceInquiery.price)
							.replace('.', ':').split(":");
					tr.setText(vals[0]);
					tf.setText(vals[1].length() > 1 ? vals[1] : vals[1] + "0");
					ft.setText(String.valueOf(priceInquiery.contents)
							+ priceInquiery.contentsdesc + " ("
							+ priceInquiery.priceperdesc + "-pris" + " "
							+ CommonTask.getString(priceInquiery.priceper)
							+ ")");
					if (priceInquiery.getDiscount().quantity > 0) {
						Discount dis = priceInquiery.getDiscount();
						dt.setText(String.valueOf(dis.quantity) + " "
								+ dis.text + " " + Math.round(dis.amount));
						dt.setVisibility(View.VISIBLE);
					}

				} else {
					pt.setText(getString(R.string.productError));
					pt2.setText("");
					tr.setText("");
					tf.setText("");
					ft.setText("");
				}

			} catch (Exception e) {
				pt.setText(getString(R.string.productError));
				pt2.setText("");
				tr.setText("");
				tf.setText("");
				ft.setText("");
			}

			productDetailsLayOut.setVisibility(View.VISIBLE);
			productDetailsLayOut.startAnimation(animTop);
			
			mHandler.postDelayed(mWaitRunnable, PRODUCT_DETAILS_DELAY_MS);
			ai.setBackgroundDrawable(null);
			if (ArticleInq.IsProductFound) {
				BitmapDrawable image = getArticleImage(artInq.EAN);
				if (image != null) {
					ai.setBackgroundDrawable(image);
				} else {
					CommonTask.setAsyncImageBackground(ai, artInq.EAN);
				}
			}
		}
	}

	// Use for initialize the camera property
	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
		      throw new IllegalStateException("No SurfaceHolder provided");
		    }
		if (cameraManager.isOpen()) {
		      Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
		      return;
		    }
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			 if (handler == null) {
			        handler = new CaptureActivityHandler(this, null, null, "UTF-8", cameraManager);
			      }
			 decodeOrStoreSavedBitmap(null, null);
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			// displayFrameworkBugMessageAndExit();
			CommonTask.ShowMessage(this, "IOException\n" + ioe.getMessage());
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializating camera", e);
			CommonTask.ShowMessage(this, "RuntimeException\n" + e.getMessage());
			// displayFrameworkBugMessageAndExit();
		}
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}
	
	
	/**
	   * Superimpose a line for 1D or dots for 2D to highlight the key features of the barcode.
	   *
	   * @param barcode   A bitmap of the captured image.
	   * @param scaleFactor amount by which thumbnail was scaled
	   * @param rawResult The decoded results which contains the points to draw.
	   */
	  private void drawResultPoints(Bitmap barcode, float scaleFactor, Result rawResult) {
	    ResultPoint[] points = rawResult.getResultPoints();
	    if (points != null && points.length > 0) {
	      Canvas canvas = new Canvas(barcode);
	      Paint paint = new Paint();
	      paint.setColor(getResources().getColor(R.color.result_points));
	      if (points.length == 2) {
	        paint.setStrokeWidth(4.0f);
	        drawLine(canvas, paint, points[0], points[1], scaleFactor);
	      } else if (points.length == 4 &&
	                 (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A ||
	                  rawResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
	        // Hacky special case -- draw two lines, for the barcode and metadata
	        drawLine(canvas, paint, points[0], points[1], scaleFactor);
	        drawLine(canvas, paint, points[2], points[3], scaleFactor);
	      } else {
	        paint.setStrokeWidth(10.0f);
	        for (ResultPoint point : points) {
	          canvas.drawPoint(scaleFactor * point.getX(), scaleFactor * point.getY(), paint);
	        }
	      }
	    }
	  }
	  
	  private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b, float scaleFactor) {
	    if (a != null && b != null) {
	      canvas.drawLine(scaleFactor * a.getX(), 
	                      scaleFactor * a.getY(), 
	                      scaleFactor * b.getX(), 
	                      scaleFactor * b.getY(), 
	                      paint);
	    }
	  }
	  
	  private Result savedResultToShow;
	  private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
		    // Bitmap isn't used yet -- will be used soon
		    if (handler == null) {
		      savedResultToShow = result;
		    } else {
		      if (result != null) {
		        savedResultToShow = result;
		      }
		      if (savedResultToShow != null) {
		        Message message = Message.obtain(handler, R.id.decode_succeeded, savedResultToShow);
		        handler.sendMessage(message);
		      }
		      savedResultToShow = null;
		    }
		  }

	  public void restartPreviewAfterDelay(long delayMS) {
		    if (handler != null) {
		      handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		    }
		    resetStatusView();
		  }
	  private Result lastResult;
	  private void resetStatusView() {
//		    resultView.setVisibility(View.GONE);
		    statusView.setText(R.string.msg_default_status);
		    statusView.setVisibility(View.VISIBLE);
		    viewfinderView.setVisibility(View.VISIBLE);
		    lastResult = null;
	  }
	  
}
