package com.google.zxing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Ricky on 2017/3/21.
 */

public class CustomizeTextView extends TextView {

    public CustomizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        do {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                return true;
            }

            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                return true;
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                    return performClick();
            }

            return false;

        } while (false);

    }
}
