package com.kimjio.mealviewer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.kimjio.lib.meal.helper.PreferenceHelper;
import com.kimjio.mealviewer.databinding.SplashActivityBinding;

public class SplashActivity extends BaseActivity<SplashActivityBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceHelper preferenceHelper = getPreferenceHelper();
        Class<? extends Activity> activityClass;
        if (preferenceHelper.getSchoolId() == null)
            activityClass = SchoolSelectActivity.class;
        else
            activityClass = MainActivity.class;

        startActivity(new Intent(this, activityClass));
        finish();
    }
}
