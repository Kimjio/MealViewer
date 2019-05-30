package com.kimjio.mealviewer.helper;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kimjio.mealviewer.database.DatabaseHelper;
import com.kimjio.mealviewer.model.Meal;
import com.kimjio.mealviewer.model.School;
import com.kimjio.mealviewer.network.MealTask;
import com.kimjio.mealviewer.network.OnTaskListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public final class MealHelper {
    private static final String TAG = "MealHelper";
    private static MealHelper INSTANCE;
    private DatabaseHelper helper;

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

    public void getMeals(String schoolId, String neisLocalDomain, School.Type type, OnTaskListener<List<Meal>> taskListener) {
        getMeals(schoolId, neisLocalDomain, type, new Date(), taskListener);
    }

    public void getMeals(String schoolId, String neisLocalDomain, School.Type type, Date selectedDate, OnTaskListener<List<Meal>> taskListener) {
        List<Meal> meals = helper.findAll(schoolId, selectedDate);

        if (meals.isEmpty()) {
            Log.d(TAG, "getMeals: EMPTY!");
            try {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(selectedDate);

                new MealTask(result -> {
                    if (taskListener != null) taskListener.onTaskFinished(result);
                    helper.inserts(result);
                }).execute(neisLocalDomain, schoolId, type.toString(), Integer.toString(calendar.get(Calendar.YEAR)), Integer.toString(calendar.get(Calendar.MONTH) + 1)).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            if (taskListener != null) taskListener.onTaskFinished(meals);
        }
    }
}
