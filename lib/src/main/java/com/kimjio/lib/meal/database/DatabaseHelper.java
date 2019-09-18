package com.kimjio.lib.meal.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kimjio.lib.meal.model.Meal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper INSTANCE;

    private DatabaseHelper(@Nullable Context context) {
        super(context, "meal_viewer.db", null, DatabaseManager.DB_VERSION);
    }

    @NonNull
    public static DatabaseHelper getInstance(Context context) {
        if (INSTANCE == null)
            synchronized (DatabaseHelper.class) {
                if (INSTANCE == null)
                    INSTANCE = new DatabaseHelper(context);
            }
        return INSTANCE;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.execSQL(DatabaseManager.getCreateTableMeal());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseManager.TABLE_NAME_MEAL);
        onCreate(db);
    }

    public void insert(Meal meal) {
        if (existsMeal(meal.getId())) return;
        final SQLiteDatabase database = getWritableDatabase();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(meal.getDate());

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseManager.MEAL_COLUMN_ID, meal.getId());
        contentValues.put(DatabaseManager.MEAL_COLUMN_SCHOOL_ID, meal.getSchoolId());
        contentValues.put(DatabaseManager.MEAL_COLUMN_MEAL_B, meal.getMealBreakfast());
        contentValues.put(DatabaseManager.MEAL_COLUMN_MEAL_L, meal.getMealLunch());
        contentValues.put(DatabaseManager.MEAL_COLUMN_MEAL_D, meal.getMealDinner());

        contentValues.put(DatabaseManager.MEAL_COLUMN_DATE_D, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        contentValues.put(DatabaseManager.MEAL_COLUMN_DATE_YM, calendar.getTimeInMillis());

        database.insert(DatabaseManager.TABLE_NAME_MEAL, null, contentValues);
    }

    public void inserts(List<Meal> meals) {
        for (Meal meal : meals) {
            insert(meal);
        }
    }

    private boolean existsMeal(String id) {
        final SQLiteDatabase database = getReadableDatabase();
        boolean exists;
        Cursor cursor = database.query(DatabaseManager.TABLE_NAME_MEAL, null, DatabaseManager.MEAL_COLUMN_ID + " = ?", new String[]{id}, null, null, null);
        exists = cursor.moveToNext();
        cursor.close();
        return exists;
    }

    public List<Meal> findAll(String schoolId, Date selectedDate) {
        final SQLiteDatabase database = getReadableDatabase();
        final List<Meal> list = new ArrayList<>();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Cursor cursor = database.query(DatabaseManager.TABLE_NAME_MEAL, null, DatabaseManager.MEAL_COLUMN_SCHOOL_ID + " = ? AND " + DatabaseManager.MEAL_COLUMN_DATE_YM + " = ?", new String[]{schoolId, Long.toString(calendar.getTimeInMillis())}, null, null, DatabaseManager.MEAL_COLUMN_DATE_D + " ASC");

        while (cursor.moveToNext()) {
            Calendar date = Calendar.getInstance();
            date.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DatabaseManager.MEAL_COLUMN_DATE_YM)));
            date.set(Calendar.DAY_OF_MONTH, cursor.getInt(cursor.getColumnIndex(DatabaseManager.MEAL_COLUMN_DATE_D)));
            list.add(new Meal(
                    cursor.getString(cursor.getColumnIndex(DatabaseManager.MEAL_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseManager.MEAL_COLUMN_SCHOOL_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseManager.MEAL_COLUMN_MEAL_B)),
                    cursor.getString(cursor.getColumnIndex(DatabaseManager.MEAL_COLUMN_MEAL_L)),
                    cursor.getString(cursor.getColumnIndex(DatabaseManager.MEAL_COLUMN_MEAL_D)),
                    new Date(date.getTimeInMillis()))
            );
        }

        cursor.close();

        return list;
    }

    public Meal find(String schoolId, Date date) {
        final SQLiteDatabase database = getReadableDatabase();
        Meal found = null;
        Cursor cursor = database.query(DatabaseManager.TABLE_NAME_MEAL, null, DatabaseManager.MEAL_COLUMN_ID + " = ? AND " + DatabaseManager.MEAL_COLUMN_SCHOOL_ID + " = ?", new String[]{Meal.generateId(schoolId, date), schoolId}, null, null, null);

        if (cursor.moveToNext()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(DatabaseManager.MEAL_COLUMN_DATE_YM)));
            calendar.set(Calendar.DAY_OF_MONTH, cursor.getInt(cursor.getColumnIndex(DatabaseManager.MEAL_COLUMN_DATE_D)));
            found = new Meal(
                    cursor.getString(cursor.getColumnIndex(DatabaseManager.MEAL_COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseManager.MEAL_COLUMN_SCHOOL_ID)),
                    cursor.getString(cursor.getColumnIndex(DatabaseManager.MEAL_COLUMN_MEAL_B)),
                    cursor.getString(cursor.getColumnIndex(DatabaseManager.MEAL_COLUMN_MEAL_L)),
                    cursor.getString(cursor.getColumnIndex(DatabaseManager.MEAL_COLUMN_MEAL_D)),
                    new Date(calendar.getTimeInMillis())
            );
        }

        cursor.close();

        return found;
    }
}
