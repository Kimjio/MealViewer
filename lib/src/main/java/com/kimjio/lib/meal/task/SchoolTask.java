package com.kimjio.lib.meal.task;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.RestrictTo;

import com.kimjio.lib.meal.model.School;

import org.json.JSONException;
import org.json.JSONList;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP_PREFIX;

@RestrictTo(LIBRARY_GROUP_PREFIX)
public class SchoolTask extends AsyncTask<String, Integer, List<School>> {

    public static final String FILTER_SCHOOL_TYPE = "schulCrseScCode";
    private static final String TAG = "SchoolTask";
    private final Filter filter;
    private final OnTaskListener<List<School>> taskListener;
    private boolean error;

    public SchoolTask(Filter filter, OnTaskListener<List<School>> taskListener) {
        this.filter = filter;
        this.taskListener = taskListener;
    }

    @Override
    protected void onPostExecute(List<School> schools) {
        if (taskListener != null) taskListener.onTaskFinished(schools, error);
    }

    @Override
    protected List<School> doInBackground(String... strings) {
        List<School> schools = new ArrayList<>();
        try {
            String raw = Jsoup.connect(String.format(Locale.getDefault(), "https://par.%s.kr/spr_ccm_cm01_100.do?kraOrgNm=%s", strings[0], strings[1]))
                    .ignoreContentType(true)
                    .execute()
                    .body();

            Log.d(TAG, "doInBackground: " + String.format(Locale.getDefault(), "https://par.%s.kr/spr_ccm_cm01_100.do?kraOrgNm=%s", strings[0], strings[1]) + " " + raw);

            for (JSONObject object :
                    new JSONList(
                            new JSONObject(raw)
                                    .getJSONObject("resultSVO")
                                    .getJSONObject("data")
                                    .getJSONArray("orgDVOList"))) {
                if (filter != null && filter.get(FILTER_SCHOOL_TYPE, Integer.class) != 0 && object.getInt("schulCrseScCode") != filter.get(FILTER_SCHOOL_TYPE, Integer.class))
                    continue;
                schools.add(
                        new School(
                                object.getString("orgCode"),
                                object.getString("kraOrgNm"),
                                object.getString("zipAdres"),
                                object.getInt("schulCrseScCode"),
                                strings[0]
                        ));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            error = true;
        }
        return schools;
    }
}
