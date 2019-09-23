package com.kimjio.mealviewer.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.wear.widget.WearableRecyclerView;

import com.kimjio.handwritingfix.HandwritingHelper;
import com.kimjio.mealviewer.R;

public class SelectMenuAdapter extends WearableRecyclerView.Adapter<SelectMenuViewHolder> {

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_EDITABLE = 1;

    @Override
    @ViewType
    public int getItemViewType(int position) {
        if (position == 2)
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
                holder.binding.title.setText(R.string.select_menu_country);
                holder.binding.image.setImageResource(R.drawable.ic_rounded_location);
                break;
            case 1:
                holder.binding.title.setText(R.string.select_menu_type);
                holder.binding.image.setImageResource(R.drawable.ic_rounded_school);
                break;
            case 2:
                holder.binding.titleEditable.setHint(R.string.hint_school_name);
                new HandwritingHelper().attachToTextView(holder.binding.titleEditable);
                holder.binding.image.setImageResource(R.drawable.ic_rounded_school_name);
                break;
            case 3:
                holder.binding.title.setText(android.R.string.search_go);
                holder.binding.image.setImageResource(R.drawable.ic_rounded_search);
                break;
            case 4:
                holder.binding.title.setText(R.string.common_open_on_phone);
                holder.binding.image.setImageResource(R.drawable.ic_rounded_open_on_phone);
                break;
            default:
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
        return 5;
    }

    @IntDef({TYPE_TITLE, TYPE_EDITABLE})
    private @interface ViewType {
    }
}
