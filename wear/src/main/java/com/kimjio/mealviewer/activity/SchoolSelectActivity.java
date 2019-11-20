package com.kimjio.mealviewer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.wear.activity.ConfirmationActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.kimjio.lib.meal.Constants;
import com.kimjio.mealviewer.R;
import com.kimjio.mealviewer.databinding.SchoolSelectActivityBinding;
import com.kimjio.mealviewer.widget.ScaleLinearLayoutManager;
import com.kimjio.mealviewer.widget.SelectMenuAdapter;

public class SchoolSelectActivity extends BaseActivity<SchoolSelectActivityBinding> implements DataClient.OnDataChangedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /*
     *
     * 전체 검색 가능 지역
     *
     * 대구
     * 광주
     * 대전???
     * 울산
     * 세종
     * 충북
     * 제주
     *
     * */
    private static final String TAG = "SchoolSelectActivity";
    private static final int WAIT_FOR_RESULT = 0;

    private GoogleApiClient apiClient;
    private Node node;

    private SelectMenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.getMessageClient(this).getApi())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        new LinearSnapHelper().attachToRecyclerView(binding.list);
        //binding.list.addItemDecoration(new OffsetItemDecoration(this));
        binding.list.setOnGenericMotionListener(null);
        binding.list.setEdgeItemsCenteringEnabled(true);
        binding.list.setAdapter(adapter = new SelectMenuAdapter());
        binding.list.setLayoutManager(new ScaleLinearLayoutManager(this));
        adapter.setOpenSearchListener(this::startActivityForResult);
        adapter.setOpenSubMenuListener(this::startActivityForResult);
        adapter.setOnOpenOnPhoneClickListener(this::openOnPhone);
    }

    private void openOnPhone() {
        if (node != null && apiClient != null && apiClient.isConnected()) {
            startActivityForResult(new Intent(this, OpenOnPhoneActivity.class), WAIT_FOR_RESULT);
            Wearable.getMessageClient(this).sendMessage(node.getId(), Constants.MESSAGE_PATH_OPEN_ON_PHONE, null);
        } else {
            startActivity(new Intent(this, ConfirmationActivity.class).putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.FAILURE_ANIMATION).putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(R.string.not_connected)));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectMenuAdapter.REQ_SEARCH && resultCode == RESULT_OK) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        adapter.onActivityResult(requestCode, resultCode, data);
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
    protected void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Wearable.getDataClient(this).removeListener(this);
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath() != null && item.getUri().getPath().equals(Constants.DATA_PATH_SCHOOL)) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    finishActivity(WAIT_FOR_RESULT);
                    startActivity(new Intent(this, ConfirmationActivity.class)
                            .putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION)
                            .putExtra(ConfirmationActivity.EXTRA_MESSAGE, dataMap.getString(Constants.DATA_KEY_SCHOOL_ID)));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.d(TAG, "onDataChanged: " + event.getDataItem().getUri().getPath());
            }
        }
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
