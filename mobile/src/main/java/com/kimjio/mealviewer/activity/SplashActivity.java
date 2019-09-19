package com.kimjio.mealviewer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.kimjio.lib.meal.helper.PreferenceHelper;
import com.kimjio.mealviewer.R;
import com.kimjio.mealviewer.databinding.SplashActivityBinding;

public class SplashActivity extends BaseActivity<SplashActivityBinding> {

    @Override
    protected int layoutId() {
        return R.layout.splash_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceHelper preferenceHelper = getPreferenceHelper();
        Class<? extends Activity> activityClass;
        activityClass = SchoolSelectActivity.class;
        /*if (preferenceHelper.getSchoolId() == null)
        else
            activityClass = MainActivity.class;*/

        startActivity(new Intent(this, activityClass));
        finish();
    }
}
