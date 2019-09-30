package com.kimjio.mealviewer.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearSnapHelper;

import com.kimjio.mealviewer.databinding.SubSelectActivityBinding;
import com.kimjio.mealviewer.widget.ScaleLinearLayoutManager;
import com.kimjio.mealviewer.widget.SelectSubMenuAdapter;

public class SubSelectActivity extends BaseActivity<SubSelectActivityBinding> {

    public static final String EXTRA_CURRENT_VALUE = "sub_menu_current";
    public static final String EXTRA_MENU_ICON = "sub_menu_icon";
    public static final String EXTRA_MENUS = "sub_menus";
    public static final String EXTRA_MENU_VALUES = "sub_menu_values";
    public static final String RESULT_MENU_TITLE = "sub_menu_title";
    public static final String RESULT_MENU_VALUE = "sub_menu_value";
    public static final int VALUE_IS_INDEX = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        CharSequence currentValue = intent.getCharSequenceExtra(EXTRA_CURRENT_VALUE);
        int iconRes = intent.getIntExtra(EXTRA_MENU_ICON, 0);
        int menusRes = intent.getIntExtra(EXTRA_MENUS, 0);
        int menuValuesRes = intent.getIntExtra(EXTRA_MENU_VALUES, 0);
        SelectSubMenuAdapter adapter;

        if (menusRes == 0) {
            throw new IllegalArgumentException("Cannot empty TextArray Resource.");
        }

        if (menuValuesRes == 0) {
            throw new IllegalArgumentException("Cannot empty Value Array Resource.");
        }

        if (menuValuesRes == VALUE_IS_INDEX) {
            adapter = new SelectSubMenuAdapter(iconRes, SelectSubMenuAdapter.MenuItem.Factory.getInstance().newMenus(this, menusRes));
        } else {
            adapter = new SelectSubMenuAdapter(iconRes, SelectSubMenuAdapter.MenuItem.Factory.getInstance().newMenus(this, menusRes, menuValuesRes));
        }

        new LinearSnapHelper().attachToRecyclerView(binding.list);
        binding.list.setEdgeItemsCenteringEnabled(true);
        //binding.list.addItemDecoration(new OffsetItemDecoration(this));
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new ScaleLinearLayoutManager(this));

        if (currentValue != null) {
            binding.list.scrollToPosition(adapter.findPositionByValue(currentValue));
        }

        adapter.setOnItemClickListener((position, item) -> {
            Intent data = new Intent();
            data.putExtra(RESULT_MENU_TITLE, item.title);
            data.putExtra(RESULT_MENU_VALUE, item.value);
            setResult(RESULT_OK, data);
            finish();
        });
    }
}
