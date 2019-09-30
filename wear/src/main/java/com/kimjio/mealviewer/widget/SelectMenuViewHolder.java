package com.kimjio.mealviewer.widget;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.wear.widget.WearableRecyclerView;

import com.kimjio.mealviewer.databinding.SelectMenuBinding;

import java.util.Objects;

class SelectMenuViewHolder extends WearableRecyclerView.ViewHolder {

    @NonNull
    SelectMenuBinding binding;

    SelectMenuViewHolder(@NonNull View itemView) {
        this(itemView, false);
    }

    SelectMenuViewHolder(@NonNull View itemView, boolean editable) {
        super(itemView);
        binding = Objects.requireNonNull(DataBindingUtil.bind(itemView));
        if (editable) {
            binding.title.setVisibility(View.GONE);
            binding.titleEditable.setVisibility(View.VISIBLE);
        } else {
            binding.title.setVisibility(View.VISIBLE);
            binding.titleEditable.setVisibility(View.GONE);
        }
    }
}
