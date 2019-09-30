package com.kimjio.mealviewer.activity;

import android.os.Bundle;

import com.kimjio.lib.meal.helper.MealHelper;
import com.kimjio.lib.meal.helper.PreferenceHelper;
import com.kimjio.lib.meal.model.Meal;
import com.kimjio.mealviewer.R;
import com.kimjio.mealviewer.databinding.MainActivityBinding;

public class MainActivity extends BaseActivity<MainActivityBinding> {

    private static final String TAG = "MainActivity";

    @Override
    protected int layoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceHelper helper = getPreferenceHelper();

        Meal meal = MealHelper.getInstance(this).getMeal(helper.getSchoolId());
        if (meal == null)
            MealHelper.getInstance(this).getMeals(helper.getSchoolId(), helper.getNeisLocalDomain(), helper.getSchoolType(), (meals, error) -> {

            });
    }
}
