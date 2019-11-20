package com.kimjio.mealviewer.widget;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.wear.widget.WearableRecyclerView;

import com.kimjio.handwritingfix.HandwritingHelper;
import com.kimjio.mealviewer.R;
import com.kimjio.mealviewer.activity.SchoolSearchActivity;
import com.kimjio.mealviewer.activity.SubSelectActivity;

public class SelectMenuAdapter extends WearableRecyclerView.Adapter<SelectMenuViewHolder> {

    private static final int REQ_COUNTRY = 2;
    private static final int REQ_SCHOOL_TYPE = 3;
    public static final int REQ_SEARCH = 4;

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_EDITABLE = 1;

    private OpenSearchListener searchListener;
    private OpenSubMenuListener menuListener;
    private OnOpenOnPhoneClickListener oopListener;

    private MutableLiveData<CharSequence> countryData = new MutableLiveData<>();
    private MutableLiveData<CharSequence> schoolTypeData = new MutableLiveData<>();

    private Observer<CharSequence> countryObserver;
    private Observer<CharSequence> schoolTypeObserver;
    private Observer<CharSequence> searchObserver;

    private CharSequence country;
    private CharSequence schoolType = "0";
    private CharSequence schoolName = "";

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
    public void onBindViewHolder(@NonNull SelectMenuViewHolder holder, final int position) {
        holder.binding.title.setEnabled(true);
        holder.binding.wrapper.setEnabled(true);
        holder.binding.wrapper.setOnClickListener(null);
        switch (position) {
            case 0:
                holder.binding.title.setText(R.string.select_menu_country);
                countryData.observeForever(countryObserver = sequence -> {
                    holder.binding.subTitle.setText(sequence);
                    holder.binding.subTitle.setVisibility(View.VISIBLE);
                });
                holder.binding.image.setImageResource(R.drawable.ic_rounded_location);
                holder.binding.wrapper.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), SubSelectActivity.class);
                    intent.putExtra(SubSelectActivity.EXTRA_CURRENT_VALUE, country)
                            .putExtra(SubSelectActivity.EXTRA_MENU_ICON, R.drawable.ic_rounded_location)
                            .putExtra(SubSelectActivity.EXTRA_MENUS, R.array.country_educations)
                            .putExtra(SubSelectActivity.EXTRA_MENU_VALUES, R.array.country_education_urls);
                    if (menuListener != null)
                        menuListener.openSubMenu(intent, REQ_COUNTRY);
                });
                break;
            case 1:
                holder.binding.title.setText(R.string.select_menu_type);
                schoolTypeData.observeForever(schoolTypeObserver = sequence -> {
                    holder.binding.subTitle.setText(sequence);
                    holder.binding.subTitle.setVisibility(View.VISIBLE);
                });
                if (schoolTypeData.getValue() == null)
                    schoolTypeData.setValue(holder.itemView.getContext().getText(R.string.all));
                holder.binding.image.setImageResource(R.drawable.ic_rounded_school);
                holder.binding.wrapper.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), SubSelectActivity.class);
                    intent.putExtra(SubSelectActivity.EXTRA_CURRENT_VALUE, schoolType)
                            .putExtra(SubSelectActivity.EXTRA_MENU_ICON, R.drawable.ic_rounded_school)
                            .putExtra(SubSelectActivity.EXTRA_MENUS, R.array.school_type)
                            .putExtra(SubSelectActivity.EXTRA_MENU_VALUES, SubSelectActivity.VALUE_IS_INDEX);
                    if (menuListener != null)
                        menuListener.openSubMenu(intent, REQ_SCHOOL_TYPE);
                });
                break;
            case 2:
                holder.binding.titleEditable.setHint(R.string.hint_school_name);
                holder.binding.titleEditable.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        schoolName = s;
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });
                holder.binding.subTitle.setVisibility(View.GONE);
                new HandwritingHelper().attachToTextView(holder.binding.titleEditable);
                holder.binding.image.setImageResource(R.drawable.ic_rounded_school_name);
                break;
            case 3:
                holder.binding.title.setText(android.R.string.search_go);
                holder.binding.title.setEnabled(false);
                holder.binding.wrapper.setEnabled(false);
                countryData.observeForever(searchObserver = sequence -> {
                    holder.binding.title.setEnabled(true);
                    holder.binding.wrapper.setEnabled(true);
                });
                holder.binding.subTitle.setVisibility(View.GONE);
                holder.binding.image.setImageResource(R.drawable.ic_rounded_search);
                holder.binding.wrapper.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), SchoolSearchActivity.class);
                    intent.putExtra(SchoolSearchActivity.EXTRA_LOCAL_DOMAIN, country)
                            .putExtra(SchoolSearchActivity.EXTRA_SCHOOL_TYPE, schoolType)
                            .putExtra(SchoolSearchActivity.EXTRA_SCHOOL_NAME, schoolName);
                    if (searchListener != null)
                        searchListener.openSearch(intent, REQ_SEARCH);
                });
                break;
            case 4:
                holder.binding.title.setText(R.string.common_open_on_phone);
                holder.binding.subTitle.setVisibility(View.GONE);
                holder.binding.image.setImageResource(R.drawable.ic_rounded_open_on_phone);
                holder.binding.wrapper.setOnClickListener(v -> {
                    if (oopListener != null)
                        oopListener.onOpenOnPhoneClick();
                });
                break;
            default:
                break;
        }
    }

    public void setOpenSubMenuListener(OpenSubMenuListener menuListener) {
        this.menuListener = menuListener;
    }

    public void setOpenSearchListener(OpenSearchListener searchListener) {
        this.searchListener = searchListener;
    }

    public void setOnOpenOnPhoneClickListener(OnOpenOnPhoneClickListener oopListener) {
        this.oopListener = oopListener;
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            CharSequence title = data.getCharSequenceExtra(SubSelectActivity.RESULT_MENU_TITLE);
            CharSequence value = data.getCharSequenceExtra(SubSelectActivity.RESULT_MENU_VALUE);
            switch (requestCode) {
                case REQ_COUNTRY:
                    country = value;
                    countryData.setValue(title);
                    break;
                case REQ_SCHOOL_TYPE:
                    schoolType = value;
                    schoolTypeData.setValue(title);
                    break;
            }
        }
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        countryData.removeObserver(countryObserver);
        schoolTypeData.removeObserver(schoolTypeObserver);
    }

    public interface OpenSearchListener {
        void openSearch(Intent data, int requestCode);
    }

    public interface OpenSubMenuListener {
        void openSubMenu(Intent data, int requestCode);
    }

    public interface OnOpenOnPhoneClickListener {
        void onOpenOnPhoneClick();

    }

    @IntDef({TYPE_TITLE, TYPE_EDITABLE})
    private @interface ViewType {
    }
}
