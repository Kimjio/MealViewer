package com.kimjio.mealviewer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.AcceptDenyDialog;
import android.view.View;

import androidx.recyclerview.widget.LinearSnapHelper;

import com.kimjio.lib.meal.helper.SchoolHelper;
import com.kimjio.lib.meal.model.School;
import com.kimjio.lib.meal.task.Filter;
import com.kimjio.lib.meal.task.SchoolTask;
import com.kimjio.mealviewer.R;
import com.kimjio.mealviewer.databinding.SchoolSearchActivityBinding;
import com.kimjio.mealviewer.widget.ScaleLinearLayoutManager;
import com.kimjio.mealviewer.widget.SearchAdapter;

import java.util.Objects;

public class SchoolSearchActivity extends BaseActivity<SchoolSearchActivityBinding> {

    public static final String EXTRA_LOCAL_DOMAIN = "local_domain";
    public static final String EXTRA_SCHOOL_NAME = "school_name";
    public static final String EXTRA_SCHOOL_TYPE = "school_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        new LinearSnapHelper().attachToRecyclerView(binding.list);
        //binding.list.addItemDecoration(new OffsetItemDecoration(this));
        binding.list.setEdgeItemsCenteringEnabled(true);
        binding.list.setLayoutManager(new ScaleLinearLayoutManager(this));

        String localDomain = Objects.requireNonNull(intent.getCharSequenceExtra(EXTRA_LOCAL_DOMAIN)).toString();
        String schoolName = Objects.requireNonNull(intent.getCharSequenceExtra(EXTRA_SCHOOL_NAME)).toString();
        String schoolTypeText = Objects.requireNonNull(intent.getCharSequenceExtra(EXTRA_SCHOOL_TYPE)).toString();

        SchoolHelper.getInstance().findSchool(localDomain, schoolName,
                new Filter.Builder().addFilter(SchoolTask.FILTER_SCHOOL_TYPE, Integer.parseInt(schoolTypeText)).build(), (schools, error) -> {
                    binding.progress.setVisibility(View.GONE);
                    binding.list.setAdapter(new SearchAdapter(schools, error, ((position, item) -> {
                        AcceptDenyDialog dialog = new AcceptDenyDialog(this);
                        dialog.setTitle(item.getName());
                        dialog.setMessage(getString(R.string.confirm_message));
                        dialog.setPositiveButton(((dialogInterface, which) -> {
                            setSchool(item, localDomain);
                        }));
                        dialog.setNegativeButton(((dialogInterface, which) -> {
                        }));
                        dialog.show();
                    })));
                });

    }

    private void setSchool(School item, String localDomain) {
        getPreferenceHelper().putSchoolData(item, localDomain);
        setResult(RESULT_OK);
        finish();
    }
}
