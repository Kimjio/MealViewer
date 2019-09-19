package com.kimjio.mealviewer.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.wear.widget.WearableRecyclerView;

import com.kimjio.mealviewer.R;

public class SelectMenuAdapter extends WearableRecyclerView.Adapter<SelectMenuViewHolder> {

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_EDITABLE = 1;

    @Override
    @ViewType
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1)
            return TYPE_EDITABLE;
        return TYPE_TITLE;
    }

    @NonNull
    @Override
    public SelectMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @ViewType int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_menu, parent, false);
        if (viewType == TYPE_EDITABLE)
            return new SelectMenuViewHolder(view, true);
        return new SelectMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectMenuViewHolder holder, int position) {
        switch (position) {
            case 0:
            default:
                holder.binding.title.setText(R.string.select_menu_country);
                holder.binding.image.setImageResource(R.drawable.ic_rounded_location);
                break;
        }
    }

    @Override
    public int getItemCount() {
        /*
            지역
            타입
          이름 Edit
          검색 Butt
          OOP Butt
         */
        return 9;
    }

    @IntDef({TYPE_TITLE, TYPE_EDITABLE})
    private @interface ViewType {
    }
}
