package com.kimjio.mealviewer.activity;

import android.os.Bundle;
import android.util.Log;

import com.kimjio.lib.meal.helper.MealHelper;
import com.kimjio.lib.meal.helper.PreferenceHelper;
import com.kimjio.mealviewer.databinding.MainActivityBinding;

import java.util.Calendar;

public class MainActivity extends BaseActivity<MainActivityBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enables Always-on
        setAmbientEnabled();

        PreferenceHelper preferenceHelper = PreferenceHelper.getInstance(this);
        MealHelper.getInstance(this).getMeals(preferenceHelper.getSchoolId(), preferenceHelper.getNeisLocalDomain(), preferenceHelper.getSchoolType(), (meals, error) -> {
            int i = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            binding.text.setText(meals.get(i - 1).toString());
        });
    }
}
