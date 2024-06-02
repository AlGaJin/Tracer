package com.chex.tracer.utils;

import android.view.MotionEvent;
import androidx.recyclerview.widget.RecyclerView;

import static java.lang.Math.abs;

public class DisallowParentSwipeOnItemTouchListener implements RecyclerView.OnItemTouchListener {
    private float startPoint = 0f;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startPoint = e.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float pointX = e.getX();
                float deltaX = pointX - startPoint;
                if (abs(deltaX) > 5f) {
                    if (deltaX > 0 && !rv.canScrollHorizontally(-1)) {
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                    } else if (deltaX < 0 && !rv.canScrollHorizontally(1)) {
                        rv.getParent().requestDisallowInterceptTouchEvent(false);
                    } else {
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                rv.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
}

