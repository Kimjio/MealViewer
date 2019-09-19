package com.kimjio.mealviewer.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableLinearLayoutManager;

import com.kimjio.mealviewer.R;

public class ScaleLinearLayoutManager extends WearableLinearLayoutManager {

    private float scaleDownBy = 0.36f;
    private float scaleDownDistance = 0.9f;

    private onScrollStopListener onScrollStopListener;

    public ScaleLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scaleDownView();
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == RecyclerView.VERTICAL) {
            int scrolled = super.scrollVerticallyBy(dy, recycler, state);
            scaleDownView();
            return scrolled;
        } else return 0;
    }

    private void scaleDownView() {
        float mid = getHeight() / 2.0f;
        float unitScaleDownDist = scaleDownDistance * mid;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            float childMid = (child.getTop() + child.getBottom()) / 2.0f;
            float absMid = Math.abs(mid - childMid);
            float minMid = Math.min(unitScaleDownDist, absMid);
            float scale = 1.0f + (-1 * scaleDownBy) * (minMid) / unitScaleDownDist;
            View childImg = child.findViewById(R.id.image);
            childImg.setScaleX(scale);
            childImg.setScaleY(scale);
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == 0) {
            if (onScrollStopListener != null) {
                int selected = 0;
                float lastWidth = 0f;
                for (int i = 0; i < getChildCount(); i++) {
                    if (lastWidth < getChildAt(i).getScaleY()) {
                        lastWidth = getChildAt(i).getScaleY();
                        selected = i;
                    }
                }
                onScrollStopListener.selectedView(getChildAt(selected));
            }
        }
    }

    public float getScaleDownBy() {
        return scaleDownBy;
    }

    public void setScaleDownBy(float scaleDownBy) {
        this.scaleDownBy = scaleDownBy;
    }

    public float getScaleDownDistance() {
        return scaleDownDistance;
    }

    public void setScaleDownDistance(float scaleDownDistance) {
        this.scaleDownDistance = scaleDownDistance;
    }

    public void setOnScrollStopListener(onScrollStopListener onScrollStopListener) {
        this.onScrollStopListener = onScrollStopListener;
    }

    public interface onScrollStopListener {
        void selectedView(View view);
    }
}
