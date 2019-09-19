package com.kimjio.lib.meal.network;

import android.os.AsyncTask;

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

    private final OnTaskListener<List<School>> taskListener;

    public SchoolTask(OnTaskListener<List<School>> taskListener) {
        this.taskListener = taskListener;
    }

    @Override
    protected void onPostExecute(List<School> schools) {
        if (taskListener != null) taskListener.onTaskFinished(schools);
    }

    @Override
    protected List<School> doInBackground(String... strings) {
        List<School> schools = new ArrayList<>();
        try {
            for (JSONObject object :
                    new JSONList(
                            new JSONObject(
                                    Jsoup
                                            .connect(String.format(Locale.getDefault(), "https://par.%s.kr/spr_ccm_cm01_100.do?kraOrgNm=%s", strings[0], strings[1]))
                                            .ignoreContentType(true)
                                            .execute()
                                            .body())
                                    .getJSONObject("resultSVO")
                                    .getJSONObject("data")
                                    .getJSONArray("orgDVOList"))) {
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
        }
        return schools;
    }
}
