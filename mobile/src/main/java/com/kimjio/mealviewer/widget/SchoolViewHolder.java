package com.kimjio.mealviewer.widget;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kimjio.mealviewer.databinding.SchoolItemBinding;

import java.util.Objects;

class SchoolViewHolder extends RecyclerView.ViewHolder {

    @NonNull
    final SchoolItemBinding binding;

    SchoolViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = Objects.requireNonNull(DataBindingUtil.bind(itemView));
    }
}
