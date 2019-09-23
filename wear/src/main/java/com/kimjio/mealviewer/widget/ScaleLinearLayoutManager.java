package com.kimjio.mealviewer.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableLinearLayoutManager;

import com.kimjio.mealviewer.R;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ScaleLinearLayoutManager extends WearableLinearLayoutManager {

    private float scaleDownBy = 0.37f;
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
            Optional.ofNullable(getChildAt(i)).ifPresent(child -> {
                float childMid = (child.getTop() + child.getBottom()) / 2.0f;
                float absMid = Math.abs(mid - childMid);
                float minMid = Math.min(unitScaleDownDist, absMid);
                float scale = 1.0f + (-1 * scaleDownBy) * (minMid) / unitScaleDownDist;
                View childImg = child.findViewById(R.id.image);
                childImg.setScaleX(scale);
                childImg.setScaleY(scale);
                View childTitle = child.findViewById(R.id.title);
                View childTitleEditable = child.findViewById(R.id.title_editable);
                if (childTitle.getVisibility() == View.GONE)
                    childTitleEditable.setTranslationX(-(((ViewGroup.MarginLayoutParams) childTitle.getLayoutParams()).getMarginStart() * 3) * (1 - scale));
                else
                    childTitle.setTranslationX(-(((ViewGroup.MarginLayoutParams) childTitle.getLayoutParams()).getMarginStart() * 3) * (1 - scale));
            });
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (state == 0) {
            if (onScrollStopListener != null) {
                AtomicInteger selected = new AtomicInteger();
                AtomicReference<Float> lastWidth = new AtomicReference<>(0f);
                for (AtomicInteger i = new AtomicInteger(0); i.getAndIncrement() < getChildCount(); ) {
                    Optional.ofNullable(getChildAt(i.get())).ifPresent(child -> {
                        if (lastWidth.get() < child.getScaleY()) {
                            lastWidth.set(child.getScaleY());
                            selected.set(i.get());
                        }
                    });
                }
                onScrollStopListener.selectedView(getChildAt(selected.get()));
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
