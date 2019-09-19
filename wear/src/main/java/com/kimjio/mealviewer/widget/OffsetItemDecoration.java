package com.kimjio.mealviewer.widget;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

public class OffsetItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "OffsetItemDecoration";

    private Context context;

    public OffsetItemDecoration(Context context) {
        this.context = context;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        int offset = (int) (getScreenHeight() / 2f) - view.getMeasuredHeight() / 2;

        if (parent.getChildAdapterPosition(view) == 0) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin = 0;
            setupOutRect(outRect, offset, true);
        } else if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
            ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).bottomMargin = 0;
            setupOutRect(outRect, offset, false);
        }


    }

    private void setupOutRect(Rect rect, int offset, boolean top) {
        if (top) {
            rect.top = offset;
        } else {
            rect.bottom = offset;
        }
    }

    private int getScreenHeight() {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = Objects.requireNonNull(wm).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }
}
