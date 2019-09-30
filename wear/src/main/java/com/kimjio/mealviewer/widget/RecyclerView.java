package com.kimjio.mealviewer.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import androidx.annotation.Nullable;
import androidx.wear.widget.WearableRecyclerView;

import com.google.android.wearable.input.RotaryEncoderHelper;

public class RecyclerView extends WearableRecyclerView {
    private static final String TAG = "RecyclerView";

    public RecyclerView(Context context) {
        this(context, null);
    }

    public RecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, 0);
    }

    public RecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        return super.onGenericMotionEvent(event);

        //TODO Fling
        /*if (RotaryEncoderHelper.isFromRotaryEncoder(event)) {
            VelocityTracker tracker = VelocityTracker.obtain();
            tracker.addMovement(event);
            tracker.computeCurrentVelocity(480);


            float delta = -RotaryEncoderHelper.getRotaryAxisValue(event) * RotaryEncoderHelper.getScaledScrollFactor(getContext());

            Log.d(TAG, "onGenericMotionEvent: " + Math.abs(RotaryEncoderHelper.getRotaryAxisValue(event)));

            if (Math.abs(RotaryEncoderHelper.getRotaryAxisValue(event)) >= 0.21)
                fling(0, -80);
            else
                scrollBy(0, Math.round(delta));

            tracker.recycle();
        }*/

        //return true;
    }
}
