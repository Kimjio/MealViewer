package com.kimjio.mealviewer.widget;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kimjio.lib.meal.model.School;
import com.kimjio.mealviewer.R;

import java.util.List;

public class SchoolAdapter extends RecyclerView.Adapter<SchoolViewHolder> {

    private List<School> schools;

    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public SchoolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SchoolViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.school_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SchoolViewHolder holder, int position) {
        final School school = schools.get(position);

        holder.binding.textType.setText(school.getType().toString());
        holder.binding.textName.setText(school.getName());
        holder.binding.textAddress.setText(school.getAddress());
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) onItemClickListener.onClick(school);
        });
    }

    public void setSchools(List<School> schools) {
        this.schools = schools;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        if (schools == null) return 0;
        return schools.size();
    }

    public interface OnItemClickListener {
        void onClick(School school);
    }
}
