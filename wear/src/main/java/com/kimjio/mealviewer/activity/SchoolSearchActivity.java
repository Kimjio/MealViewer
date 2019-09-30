package com.kimjio.mealviewer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearSnapHelper;

import com.kimjio.lib.meal.helper.SchoolHelper;
import com.kimjio.lib.meal.task.Filter;
import com.kimjio.lib.meal.task.SchoolTask;
import com.kimjio.mealviewer.databinding.SchoolSearchActivityBinding;
import com.kimjio.mealviewer.widget.ScaleLinearLayoutManager;
import com.kimjio.mealviewer.widget.SearchAdapter;

public class SchoolSearchActivity extends BaseActivity<SchoolSearchActivityBinding> {

    public static final String EXTRA_LOCAL_DOMAIN = "local_domain";
    public static final String EXTRA_SCHOOL_NAME = "school_name";
    public static final String EXTRA_SCHOOL_TYPE = "school_type";

    private static final String TAG = "SchoolSearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        new LinearSnapHelper().attachToRecyclerView(binding.list);
        //binding.list.addItemDecoration(new OffsetItemDecoration(this));
        binding.list.setEdgeItemsCenteringEnabled(true);
        binding.list.setLayoutManager(new ScaleLinearLayoutManager(this));

        SchoolHelper.getInstance().findSchool(intent.getCharSequenceExtra(EXTRA_LOCAL_DOMAIN).toString(), intent.getCharSequenceExtra(EXTRA_SCHOOL_NAME).toString(),
                new Filter.Builder().addFilter(SchoolTask.FILTER_SCHOOL_TYPE, Integer.parseInt(intent.getCharSequenceExtra(EXTRA_SCHOOL_TYPE).toString())).build(), (schools, error) -> {
                    binding.progress.setVisibility(View.GONE);
                    binding.list.setAdapter(new SearchAdapter(schools, error, ((position, item) -> {

                    })));
                });

    }
}
