package com.kimjio.lib.meal.network;

import android.os.AsyncTask;
import android.text.Html;
import android.text.TextUtils;

import com.kimjio.lib.meal.model.Meal;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MealTask extends AsyncTask<String, Integer, List<Meal>> {

    private String schoolId;
    private String selectedYear;
    private String selectedMonth;

    private final OnTaskListener<List<Meal>> taskListener;

    @Override
    protected void onPostExecute(List<Meal> meals) {
        if (taskListener != null) taskListener.onTaskFinished(meals);
    }

    public MealTask(OnTaskListener<List<Meal>> taskListener) {
        this.taskListener = taskListener;
    }

    @Override
    protected List<Meal> doInBackground(String... strings) {
        List<Meal> meals = new ArrayList<>();
        schoolId = strings[1];
        selectedYear = strings[3];
        selectedMonth = strings[4];
        try {
            Element body = Jsoup.connect(String.format(Locale.getDefault(), "https://stu.%s.kr/sts_sci_md00_001.do?schulCode=%s&schulCrseScCode=%d&schulKndScCode=%02d&schYm=%04d%02d",
                    strings[0], schoolId, Integer.parseInt(strings[2]), Integer.parseInt(strings[2]), Integer.parseInt(strings[3]), Integer.parseInt(strings[4]))).get().body();
            Elements mealElements = body.select("table.tbl_calendar").select("tbody").select("div");
            for (Element mealElement : mealElements) {
                String mealRaw = mealElement.html();
                if (!TextUtils.isEmpty(mealRaw)) {
                    meals.add(filterWithParse(mealRaw));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return meals;
    }

    private Meal filterWithParse(String mealRaw) throws ParseException {
        String[] filters = {"[(]조[)]", "[(]중[)]", "[(]석[)]"};

        mealRaw = Html.fromHtml(mealRaw.replaceAll("^[<]br[>]", ""), Html.FROM_HTML_MODE_COMPACT).toString();

        for (int i = 18; i >= 1; i--)
            mealRaw = mealRaw.replaceAll(String.format(Locale.getDefault(), "%d\\.", i), "");
        for (String filter : filters)
            mealRaw = mealRaw.replaceAll(filter, "");

        String[] lines = mealRaw.split("\n");

        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replaceAll(" $", "");
        }
        for (int i = 1; i < lines.length; i++) {
            lines[i] = lines[i].replaceAll("\\d$|[(]$", "");
        }
        for (int i = 1; i < lines.length; i++) {
            lines[i] = lines[i].replaceAll("\\d[+]", "+");
        }
        for (int i = 1; i < lines.length; i++) {
            lines[i] = lines[i].replaceAll("\\d[(]", "(");
        }

        int mode = -1;
        Meal meal = new Meal(schoolId, new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(String.format(Locale.getDefault(), "%04d%02d%02d", Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth), Integer.parseInt(lines[0]))));

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            switch (line) {
                case "[조식]":
                    mode = 0;
                    continue;
                case "[중식]":
                    mode = 1;
                    continue;
                case "[석식]":
                    mode = 2;
                    continue;
            }

            switch (mode) {
                case 0:
                    if (i != lines.length - 1 && !lines[i + 1].contains("["))
                        meal.appendMealBreakfast(line, true);
                    else
                        meal.appendMealBreakfast(line, false);
                    break;
                case 1:
                    if (i != lines.length - 1 && !lines[i + 1].contains("["))
                        meal.appendMealLunch(line, true);
                    else
                        meal.appendMealLunch(line, false);
                    break;
                case 2:
                    if (i != lines.length - 1)
                        meal.appendMealDinner(line, true);
                    else
                        meal.appendMealDinner(line, false);
                    break;
            }
        }

        return meal;
    }
}
