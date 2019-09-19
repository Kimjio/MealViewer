package com.kimjio.mealviewer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;
import androidx.wear.activity.ConfirmationActivity;
import androidx.wear.widget.WearableLinearLayoutManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.kimjio.lib.meal.Constants;
import com.kimjio.mealviewer.R;
import com.kimjio.mealviewer.databinding.SchoolSelectActivityBinding;
import com.kimjio.mealviewer.widget.OffsetItemDecoration;
import com.kimjio.mealviewer.widget.ScaleLinearLayoutManager;
import com.kimjio.mealviewer.widget.SelectMenuAdapter;

public class SchoolSelectActivity extends BaseActivity<SchoolSelectActivityBinding> implements DataClient.OnDataChangedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SchoolSelectActivity";
    private static final int WAIT_FOR_RESULT = 0;

    private GoogleApiClient apiClient;
    private Node node;

    @Override
    protected int layoutId() {
        return R.layout.school_select_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.getMessageClient(this).getApi())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.list);
        binding.list.addItemDecoration(new OffsetItemDecoration(this));
        binding.list.setAdapter(new SelectMenuAdapter());
        binding.list.setLayoutManager(new ScaleLinearLayoutManager(this));
        //binding.imageView.setOnClickListener(v -> openOnPhone());
    }

    private void openOnPhone() {
        if (node != null && apiClient != null && apiClient.isConnected()) {
            startActivity(new Intent(this, ConfirmationActivity.class).putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.OPEN_ON_PHONE_ANIMATION));
            startActivityForResult(new Intent(this, OpenOnPhoneActivity.class), WAIT_FOR_RESULT);
            Wearable.getMessageClient(this).sendMessage(node.getId(), Constants.MESSAGE_PATH_OPEN_ON_PHONE, null);
        }
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
                    startActivity(new Intent(this, ConfirmationActivity.class).putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION).putExtra(ConfirmationActivity.EXTRA_MESSAGE, dataMap.getString(Constants.DATA_KEY_SCHOOL_ID)));
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
