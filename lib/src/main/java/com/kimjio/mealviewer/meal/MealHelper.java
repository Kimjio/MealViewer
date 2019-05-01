package com.kimjio.mealviewer.meal;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kimjio.mealviewer.database.DatabaseHelper;
import com.kimjio.mealviewer.model.Meal;
import com.kimjio.mealviewer.network.MealTask;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

public final class MealHelper {
    private DatabaseHelper helper;

    private static MealHelper INSTANCE;

    private MealHelper(Context context) {
        helper = DatabaseHelper.getInstance(context);
    }

    @NonNull
    public static MealHelper getInstance(Context context) {
        if (INSTANCE == null)
            synchronized (MealHelper.class) {
                if (INSTANCE == null)
                    INSTANCE = new MealHelper(context);
            }
        return INSTANCE;
    }

    public Meal getMeal(String schoolId) {
        return helper.find(schoolId, new Date());
    }

    public List<Meal> getMeals(String schoolId) {
        return getMeals(schoolId, new Date());
    }

    public List<Meal> getMeals(String schoolId, Date selectedDate) {
        List<Meal> meals = helper.findAll(schoolId, selectedDate);

        if (meals.isEmpty()) {
            Log.d(TAG, "getMeals: EMPTY!");
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(selectedDate);

                meals = new MealTask().execute("dge.go", schoolId, "4", Integer.toString(calendar.get(Calendar.YEAR)), Integer.toString(calendar.get(Calendar.MONTH) + 1)).get();
                helper.inserts(meals);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        return meals;
    }
}
