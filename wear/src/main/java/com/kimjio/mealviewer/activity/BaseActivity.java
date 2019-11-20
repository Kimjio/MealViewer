package com.kimjio.mealviewer.activity;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.kimjio.lib.meal.helper.PreferenceHelper;
import com.kimjio.mealviewer.R;

public abstract class BaseActivity<VB extends ViewDataBinding> extends WearableActivity {

    protected VB binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
    }

    @LayoutRes
    private int getLayoutId() {
        String[] split = getClass().getSimpleName().split("(?<=.)(?=\\p{Upper})");
        StringBuilder name = new StringBuilder();

        for (int i = 0; i < split.length; i++) {
            name.append(split[i].toLowerCase());
            if (i != split.length - 1)
                name.append("_");
        }

        try {
            return R.layout.class.getDeclaredField(name.toString()).getInt(R.layout.class);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return 0;
    }

    protected PreferenceHelper getPreferenceHelper() {
        return PreferenceHelper.getInstance(this);
    }
}
