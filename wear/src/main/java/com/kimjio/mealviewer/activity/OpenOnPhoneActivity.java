package com.kimjio.mealviewer.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.wear.activity.ConfirmationActivity;

import com.kimjio.mealviewer.R;
import com.kimjio.mealviewer.databinding.OpenOnPhoneActivityBinding;

public class OpenOnPhoneActivity extends BaseActivity<OpenOnPhoneActivityBinding> {

    @Override
    protected int layoutId() {
        return R.layout.open_on_phone_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
