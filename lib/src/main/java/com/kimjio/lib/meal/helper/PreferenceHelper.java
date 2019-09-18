package com.kimjio.lib.meal.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.kimjio.lib.meal.Constants;
import com.kimjio.lib.meal.model.School;

public class PreferenceHelper {
    private final SharedPreferences preference;
    private static PreferenceHelper INSTANCE;

    private PreferenceHelper(Context context) {
        preference = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceHelper getInstance(Context context) {
        if (INSTANCE == null)
            synchronized (PreferenceHelper.class) {
                if (INSTANCE == null)
                    INSTANCE = new PreferenceHelper(context);
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
