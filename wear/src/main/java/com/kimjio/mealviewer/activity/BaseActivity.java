package com.kimjio.mealviewer.activity;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseActivity<VB extends ViewDataBinding> extends WearableActivity {

    protected VB binding;

    @LayoutRes
    protected abstract int layoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, layoutId());
    }
}
