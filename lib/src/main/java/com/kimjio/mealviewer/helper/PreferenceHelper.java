package com.kimjio.mealviewer.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.kimjio.mealviewer.Constants;
import com.kimjio.mealviewer.model.School;

public class PreferenceHelper {
    private SharedPreferences preference;
    private static PreferenceHelper INSTANCE;

    private PreferenceHelper(String name, Context context) {
        preference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static PreferenceHelper getInstance(Context context) {
        if (INSTANCE == null)
            synchronized (PreferenceHelper.class) {
                if (INSTANCE == null)
                    INSTANCE = new PreferenceHelper(Constants.PREF_NAME, context);
            }
        return INSTANCE;
    }

    public void putSchoolData(School school, String neisLocalDomain) {
        preference.edit()
                .putString(Constants.PREF_KEY_SCHOOL_ID, school.getSchoolId())
                .putString(Constants.PREF_KEY_NEIS_LOCAL_DOMAIN, neisLocalDomain)
                .putInt(Constants.PREF_KEY_SCHOOL_TYPE, school.getType().toInteger())
                .apply();
    }

    public String getSchoolId() {
        return preference.getString(Constants.PREF_KEY_SCHOOL_ID, null);
    }

    public String getNeisLocalDomain() {
        return preference.getString(Constants.PREF_KEY_NEIS_LOCAL_DOMAIN, null);
    }

    public School.Type getSchoolType() {
        int type = preference.getInt(Constants.PREF_KEY_SCHOOL_TYPE, 0);

        switch (type) {
            case 1:
                return School.Type.KINDER;

            case 2:
                return School.Type.ELEMENT;

            case 3:
                return School.Type.MIDDLE;

            case 4:
                return School.Type.HIGH;
        }

        return null;
    }
}
