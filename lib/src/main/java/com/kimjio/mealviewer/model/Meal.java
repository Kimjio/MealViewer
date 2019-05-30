package com.kimjio.mealviewer.model;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public final class Meal {

    private static final String EMPTY = "EMPTY_MEAL";

    private final String id;
    private final String schoolId;
    private String mealBreakfast;
    private String mealLunch;
    private String mealDinner;
    private final Date date;

    public Meal(String schoolId, Date date) {
        this.schoolId = schoolId;
        this.mealBreakfast = EMPTY;
        this.mealLunch = EMPTY;
        this.mealDinner = EMPTY;
        this.date = date;
        this.id = generateId(schoolId, date);
    }

    public Meal(String schoolId, String mealBreakfast, String mealLunch, String mealDinner, Date date) {
        this.schoolId = schoolId;
        this.mealBreakfast = checkEmpty(mealBreakfast);
        this.mealLunch = checkEmpty(mealLunch);
        this.mealDinner = checkEmpty(mealDinner);
        this.date = date;
        this.id = generateId(schoolId, date);
    }

    public Meal(String id, String schoolId, String mealBreakfast, String mealLunch, String mealDinner, Date date) {
        this.id = id;
        this.schoolId = schoolId;
        this.mealBreakfast = checkEmpty(mealBreakfast);
        this.mealLunch = checkEmpty(mealLunch);
        this.mealDinner = checkEmpty(mealDinner);
        this.date = date;
    }

    public final String getId() {
        return id;
    }

    public final String getSchoolId() {
        return schoolId;
    }

    public final String getMealBreakfast() {
        return mealBreakfast;
    }

    public final String getMealLunch() {
        return mealLunch;
    }

    public final String getMealDinner() {
        return mealDinner;
    }

    public final Date getDate() {
        return date;
    }

    public final void appendMealBreakfast(String append, boolean addLF) {
        mealBreakfast = appendMeal(mealBreakfast, append, addLF);
    }

    public final void appendMealLunch(String append, boolean addLF) {
        mealLunch = appendMeal(mealLunch, append, addLF);
    }

    public final void appendMealDinner(String append, boolean addLF) {
        mealDinner = appendMeal(mealDinner, append, addLF);
    }

    private String appendMeal(String meal, String append, boolean addLF) {
        if (meal.equals(EMPTY))
            meal = append + (addLF ? "\n" : "");
        else
            meal += append + (addLF ? "\n" : "");
        return meal;
    }

    public final boolean isEmpty() {
        return mealBreakfast.equals(EMPTY) && mealLunch.equals(EMPTY) && mealDinner.equals(EMPTY);
    }

    private String checkEmpty(String mealStr) {
        return Optional.ofNullable(mealStr).filter(meal -> !TextUtils.isEmpty(meal)).orElse(EMPTY);
    }

    public static String generateId(String schoolId, Date date) {
        return String.format("%s_%s", schoolId, new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date)); //ex. 20190424_D100000282
    }

    @NonNull
    @Override
    public String toString() {
        return "Meal{" +
                "id='" + id + '\'' +
                ", schoolId='" + schoolId + '\'' +
                ",\nmealBreakfast=\n'" + mealBreakfast + '\'' +
                ",\nmealLunch=\n'" + mealLunch + '\'' +
                ",\nmealDinner=\n'" + mealDinner + '\'' +
                ",\ndate=" + date +
                '}';
    }
}
