package com.kimjio.mealviewer.activity;

import android.os.Bundle;

import com.kimjio.mealviewer.R;
import com.kimjio.mealviewer.databinding.MainActivityBinding;

public class MainActivity extends BaseActivity<MainActivityBinding> {

    @Override
    protected int layoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enables Always-on
        setAmbientEnabled();
    }
}
