package com.example.jahirul.myapplication;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by Jahirul on 9/9/2015.
 */
public class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return Math.abs(distanceY) > Math.abs(distanceX);
    }
}
