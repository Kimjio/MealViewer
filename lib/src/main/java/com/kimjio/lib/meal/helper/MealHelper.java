package com.kimjio.lib.meal.helper;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kimjio.lib.meal.database.DatabaseHelper;
import com.kimjio.lib.meal.model.Meal;
import com.kimjio.lib.meal.model.School;
import com.kimjio.lib.meal.network.MealTask;
import com.kimjio.lib.meal.network.OnTaskListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public final class MealHelper {
    private static MealHelper INSTANCE;
    private final DatabaseHelper helper;

    private MealHelper(@NonNull Context context) {
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

    /**
     * 오늘의 급식을 가져옴
     *
     * @param schoolId 학교 ID
     * @return 있으면 {@link Meal} 없으면 null
     */
    @Nullable
    public Meal getMeal(String schoolId) {
        return helper.find(schoolId, new Date());
    }

    public void getMeals(School school) {
        getMeals(school.getSchoolId(), school.getLocalDomain(), school.getType(), null);
    }

    public void getMeals(School school, OnTaskListener<List<Meal>> taskListener) {
        getMeals(school.getSchoolId(), school.getLocalDomain(), school.getType(), taskListener);
    }

    public void getMeals(String schoolId, String neisLocalDomain, School.Type type) {
        getMeals(schoolId, neisLocalDomain, type, null);
    }

    public void getMeals(String schoolId, String neisLocalDomain, School.Type type, OnTaskListener<List<Meal>> taskListener) {
        getMeals(schoolId, neisLocalDomain, type, new Date(), taskListener);
    }

    public void getMeals(String schoolId, String neisLocalDomain, School.Type type, Date selectedDate, OnTaskListener<List<Meal>> taskListener) {
        List<Meal> meals = helper.findAll(schoolId, selectedDate);

        if (meals.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);

            new MealTask(result -> {
                if (taskListener != null) taskListener.onTaskFinished(result);
                helper.inserts(result);
            }).execute(neisLocalDomain, schoolId, Integer.toString(type.toInteger()), Integer.toString(calendar.get(Calendar.YEAR)), Integer.toString(calendar.get(Calendar.MONTH) + 1));
        } else {
            if (taskListener != null) taskListener.onTaskFinished(meals);
        }
    }
}
