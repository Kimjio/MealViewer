package com.kimjio.lib.meal.helper;

import androidx.annotation.NonNull;

import com.kimjio.lib.meal.model.School;
import com.kimjio.lib.meal.network.OnTaskListener;
import com.kimjio.lib.meal.network.SchoolTask;

import java.util.List;

public class SchoolHelper {
    private static SchoolHelper INSTANCE;

    private SchoolHelper() {
    }

    @NonNull
    public static SchoolHelper getInstance() {
        if (INSTANCE == null)
            synchronized (SchoolHelper.class) {
                if (INSTANCE == null)
                    INSTANCE = new SchoolHelper();
            }
        return INSTANCE;
    }

    public void findSchool(String neisLocalDomain, String schoolName, OnTaskListener<List<School>> taskListener) {
        new SchoolTask(taskListener).execute(neisLocalDomain, schoolName);
    }
}
