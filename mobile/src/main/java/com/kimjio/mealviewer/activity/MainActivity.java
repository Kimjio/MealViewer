package com.kimjio.mealviewer.activity;

import android.os.Bundle;
import android.util.Log;

import com.kimjio.mealviewer.R;
import com.kimjio.mealviewer.databinding.MainActivityBinding;
import com.kimjio.mealviewer.meal.MealHelper;
import com.kimjio.mealviewer.model.Meal;

import java.util.List;

public class MainActivity extends BaseActivity<MainActivityBinding> {

    private static final String TAG = "MainActivity";

    @Override
    protected int layoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<Meal> meals = MealHelper.getInstance(this).getMeals("D100000282");
        for (Meal meal : meals) {
            Log.d(TAG, "onCreate: " + meal.toString());
        }

        Log.d(TAG, "onCreate: " +  MealHelper.getInstance(this).getMeal("D100000282"));
    }
}
