package com.kimjio.mealviewer.activity;

import android.os.Bundle;
import android.util.Log;

import com.kimjio.lib.meal.helper.MealHelper;
import com.kimjio.lib.meal.helper.SchoolHelper;
import com.kimjio.lib.meal.model.Meal;
import com.kimjio.lib.meal.model.School;
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

        // Enables Always-on
        setAmbientEnabled();

        SchoolHelper.getInstance().findSchool("dge.go", "대구소", schools -> {
            for (School school : schools) {
                Log.d(TAG, "onCreate: " + school.toString());
                MealHelper.getInstance(this).getMeals(school, meals -> {
                    for (Meal meal : meals) {
                        Log.d(TAG, "onCreate: " + meal.toString());
                    }
                });
            }
        });
    }
}
