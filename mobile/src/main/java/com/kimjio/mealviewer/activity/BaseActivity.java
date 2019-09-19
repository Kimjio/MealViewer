package com.kimjio.mealviewer.activity;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.kimjio.lib.meal.helper.PreferenceHelper;

public abstract class BaseActivity<VB extends ViewDataBinding> extends AppCompatActivity {

    protected VB binding;

    @LayoutRes
    protected abstract int layoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, layoutId());
    }

    protected PreferenceHelper getPreferenceHelper() {
        return PreferenceHelper.getInstance(this);
    }
}
