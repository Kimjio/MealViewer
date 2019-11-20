package com.kimjio.mealviewer.widget;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.wear.widget.WearableRecyclerView;

import com.kimjio.lib.meal.model.School;
import com.kimjio.mealviewer.R;

import java.util.List;

public class SearchAdapter extends WearableRecyclerView.Adapter<SelectMenuViewHolder> {

    private final boolean error;
    private List<School> items;
    private OnItemClickListener listener;

    public SearchAdapter(List<School> items, boolean error, OnItemClickListener listener) {
        this.items = items;
        this.error = error;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SelectMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectMenuViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectMenuViewHolder holder, final int position) {
        if (error) {
            holder.binding.image.setImageResource(R.drawable.ic_error);
            holder.binding.image.setImageTintList(ColorStateList.valueOf(Color.WHITE));
            holder.binding.title.setText(R.string.error);
            holder.binding.title.setSelected(false);
            return;
        }
        final School item = items.get(position);
        holder.binding.image.setImageResource(R.drawable.ic_rounded_school_name);
        holder.binding.title.setText(item.getName());
        holder.binding.title.setSelected(true);
        holder.binding.subTitle.setText(item.getAddress());
        holder.binding.subTitle.setSelected(true);
        holder.binding.subTitle.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClick(position, item);
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        if (error)
            return 1;
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, School item);
    }
}
