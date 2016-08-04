package com.example.jahirul.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

/**
 * Created by Jahirul on 9/3/2015.
 */
public class FolderListView extends ListView {

    private float xDistance, yDistance, lastX, lastY;

    private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;


    // If built programmatically
    public FolderListView(Context context) {
        super(context);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);
        // init();
    }

    // This example uses this method since being built from XML
    public FolderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);
        // init();
    }

    // Build from XML layout
    public FolderListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);
        // init();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }
}
