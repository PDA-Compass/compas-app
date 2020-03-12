package net.afterday.compas.util;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.lang.*;

/**
 * Created by spaka on 7/8/2018.
 */

public class OnSwipeTouchListener implements View.OnTouchListener
{
    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener(Context context)
    {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void onSwipeLeft()
    {
    }

    public void onSwipeRight()
    {
    }

    public void onSwipeUp()
    {
    }

    public void onSwipeDown()
    {
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener
    {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if(java.lang.Math.abs(distanceX) > java.lang.Math.abs(distanceY))
            {
                if (java.lang.Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && java.lang.Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
            }else
            {
                if (java.lang.Math.abs(distanceY) > SWIPE_DISTANCE_THRESHOLD && java.lang.Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceY > 0)
                        onSwipeDown();
                    else
                        onSwipeUp();
                    return true;
                }
            }

            return false;
        }
    }
}