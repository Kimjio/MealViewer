package com.kimjio.mealviewer.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.kimjio.mealviewer.Constants;
import com.kimjio.mealviewer.R;
import com.kimjio.mealviewer.databinding.SchoolSelectActivityBinding;
import com.kimjio.mealviewer.helper.SchoolHelper;
import com.kimjio.mealviewer.widget.SchoolAdapter;

public class SchoolSelectActivity extends BaseActivity<SchoolSelectActivityBinding> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SchoolSelectActivity";

    private GoogleApiClient apiClient;
    private Node node;
    private SchoolAdapter adapter;

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

        PutDataMapRequest dataMapRequest = PutDataMapRequest.create(Constants.DATA_PATH_SCHOOL);

        adapter.setOnItemClickListener(school -> {
            DataMap

            Wearable.getMessageClient(this).sendMessage(node.getId(), Constants.MESSAGE_PATH_OPEN_ON_PHONE, null);

            dataMapRequest.getDataMap().putString(Constants.DATA_KEY_SCHOOL_ID, school.getSchoolId());
            PutDataRequest dataRequest = dataMapRequest.asPutDataRequest();
            Task<DataItem> itemTask = Wearable.getDataClient(this).putDataItem(dataRequest);
            itemTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "onCreate: OK");
                } else {
                    Log.w(TAG, "onCreate: ERROR: ", task.getException());
                }
            });
        });

        binding.btnSearch.setOnClickListener(v -> SchoolHelper.getInstance().findSchool("dge.go", binding.inputSchoolName.getText().toString(), schools -> {
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
        Wearable.getNodeClient(this).getConnectedNodes().addOnSuccessListener(nodes -> {
            for (Node node : nodes) {
                this.node = node;
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
