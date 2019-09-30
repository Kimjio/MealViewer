package com.kimjio.mealviewer.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.ArrayRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableRecyclerView;

import com.kimjio.mealviewer.R;

import java.util.ArrayList;
import java.util.List;

public class SelectSubMenuAdapter extends WearableRecyclerView.Adapter<SelectMenuViewHolder> {

    @DrawableRes
    private int iconRes;
    private List<MenuItem> items;
    private OnMenuItemClickListener listener;

    public SelectSubMenuAdapter(@DrawableRes int iconRes, List<MenuItem> items) {
        this.iconRes = iconRes;
        this.items = items;
    }

    public SelectSubMenuAdapter(@DrawableRes int iconRes, List<MenuItem> items, OnMenuItemClickListener listener) {
        this(iconRes, items);
        this.listener = listener;
    }

    @NonNull
    @Override
    public SelectMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SelectMenuViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.select_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectMenuViewHolder holder, final int position) {
        final MenuItem item = items.get(position);
        if (iconRes != 0)
            holder.binding.image.setImageResource(iconRes);
        holder.binding.title.setText(item.title);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onMenuItemClicked(position, item);
        });
    }

    public int findPositionByValue(CharSequence currentValue) {
        for (int i = 0; i < items.size(); i++) {
            MenuItem item = items.get(i);
            if (item.value.equals(currentValue))
                return i;

        }
        return RecyclerView.NO_POSITION;
    }

    public void setOnItemClickListener(OnMenuItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnMenuItemClickListener {
        void onMenuItemClicked(int position, MenuItem item);
    }

    public static class MenuItem {
        public CharSequence title;
        public CharSequence value;

        private MenuItem(CharSequence title, CharSequence value) {
            this.title = title;
            this.value = value;
        }

        public static class Factory {
            private static MenuItem.Factory sInstance = new Factory();

            private Factory() {
            }

            public static Factory getInstance() {
                return sInstance;
            }

            public List<MenuItem> newMenus(Context context, @ArrayRes int titlesRes) {
                List<MenuItem> tmp = new ArrayList<>();

                CharSequence[] menuTexts = context.getResources().getTextArray(titlesRes);
                for (int i = 0; i < menuTexts.length; i++) {
                    CharSequence menuText = menuTexts[i];
                    tmp.add(new MenuItem(menuText, Integer.toString(i)));
                }

                return tmp;
            }

            public List<MenuItem> newMenus(Context context, @ArrayRes int titlesRes, @ArrayRes int valuesRes) {
                List<MenuItem> tmp = new ArrayList<>();

                CharSequence[] menuTexts = context.getResources().getTextArray(titlesRes);
                CharSequence[] menuValues = context.getResources().getTextArray(valuesRes);
                for (int i = 0; i < menuTexts.length; i++) {
                    CharSequence menuText = menuTexts[i];
                    CharSequence menuValue = menuValues[i];
                    tmp.add(new MenuItem(menuText, menuValue));
                }

                return tmp;
            }
        }
    }
}
