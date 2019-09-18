package com.kimjio.lib.meal.database;

final class DatabaseManager {
    static final int DB_VERSION = 1;
    static final String DB_NAME = "meal_viewer";
    static final String TABLE_NAME_MEAL = "meal";

    static final String MEAL_COLUMN_ID = "id";
    static final String MEAL_COLUMN_SCHOOL_ID = "school_id";
    static final String MEAL_COLUMN_MEAL_B = "meal_b";
    static final String MEAL_COLUMN_MEAL_L = "meal_l";
    static final String MEAL_COLUMN_MEAL_D = "meal_d";
    static final String MEAL_COLUMN_DATE_YM = "date_ym";
    static final String MEAL_COLUMN_DATE_D = "date_d";

    static String getCreateTableMeal() {
        return "CREATE TABLE MEAL (" +
                MEAL_COLUMN_ID + " TEXT UNIQUE PRIMARY KEY," +
                MEAL_COLUMN_SCHOOL_ID + " TEXT," +
                MEAL_COLUMN_MEAL_B + " TEXT," +
                MEAL_COLUMN_MEAL_L + " TEXT," +
                MEAL_COLUMN_MEAL_D + " TEXT," +
                MEAL_COLUMN_DATE_YM + " NUMERIC," +
                MEAL_COLUMN_DATE_D + " INTEGER" +
                ")";
    }
}
