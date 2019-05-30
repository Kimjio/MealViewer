package com.kimjio.mealviewer.activity;

import android.os.Bundle;
import android.util.Log;

import com.kimjio.mealviewer.R;
import com.kimjio.mealviewer.databinding.MainActivityBinding;
import com.kimjio.mealviewer.helper.MealHelper;
import com.kimjio.mealviewer.helper.SchoolHelper;
import com.kimjio.mealviewer.model.Meal;
import com.kimjio.mealviewer.model.School;

public class MainActivity extends BaseActivity<MainActivityBinding> {

    private static final String TAG = "MainActivity";

    @Override
    protected int layoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MealHelper.getInstance(this).getMeals("D100000282", "dge.go", School.Type.HIGH, meals -> {
            for (Meal meal : meals) {
                Log.d(TAG, "onCreate: " + meal.toString());
            }
        });

        Log.d(TAG, "onCreate: " + MealHelper.getInstance(this).getMeal("D100000282"));

        SchoolHelper.getInstance().findSchool("dge.go", "대구소", schools -> {
            for (School school : schools) {
                Log.d(TAG, "onCreate: " + school.toString());
            }
        });
    }
}
