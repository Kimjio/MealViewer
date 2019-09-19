package com.kimjio.mealviewer.activity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.kimjio.lib.meal.Constants;
import com.kimjio.lib.meal.helper.PreferenceHelper;
import com.kimjio.lib.meal.helper.SchoolHelper;
import com.kimjio.mealviewer.R;
import com.kimjio.mealviewer.databinding.SchoolSelectActivityBinding;
import com.kimjio.mealviewer.widget.SchoolAdapter;

public class SchoolSelectActivity extends BaseActivity<SchoolSelectActivityBinding> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SchoolSelectActivity";

    private GoogleApiClient apiClient;
    private SchoolAdapter adapter;

    private String url;

    @Override
    protected int layoutId() {
        return R.layout.school_select_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.appBar.toolbar);
        binding.listSchool.setAdapter(adapter = new SchoolAdapter());

        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.getMessageClient(this).getApi())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                url = getResources().getStringArray(R.array.cont_education_urls)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter.setOnItemClickListener(school -> {
            Wearable.getDataClient(this).deleteDataItems(new Uri.Builder().scheme(PutDataRequest.WEAR_URI_SCHEME).path(Constants.DATA_PATH_SCHOOL).build(), DataClient.FILTER_PREFIX);
            /*PutDataMapRequest dataMapRequest = PutDataMapRequest.create(Constants.DATA_PATH_SCHOOL);
            dataMapRequest.getDataMap().putString(Constants.DATA_KEY_SCHOOL_ID, school.getSchoolId());
            PutDataRequest dataRequest = dataMapRequest.asPutDataRequest();
            Wearable.getDataClient(this).putDataItem(dataRequest);*/

            PreferenceHelper preferenceHelper = getPreferenceHelper();
            preferenceHelper.putSchoolData(school, url);
        });

        binding.btnSearch.setOnClickListener(v -> SchoolHelper.getInstance().findSchool(url, binding.inputSchoolName.getText().toString(), schools -> {
            adapter.setSchools(schools);
        }));

    }

    @Override
    protected void onStart() {
        super.onStart();
        apiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        apiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
