package com.kimjio.mealviewer.widget;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kimjio.mealviewer.databinding.SchoolItemBinding;

public class SchoolViewHolder extends RecyclerView.ViewHolder {

    public SchoolItemBinding binding;

    public SchoolViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
}
