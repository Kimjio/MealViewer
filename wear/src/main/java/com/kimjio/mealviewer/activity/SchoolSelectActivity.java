package com.kimjio.mealviewer.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.wear.activity.ConfirmationActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
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
import com.kimjio.mealviewer.Constants;
import com.kimjio.mealviewer.R;
import com.kimjio.mealviewer.databinding.SchoolSelectActivityBinding;

public class SchoolSelectActivity extends BaseActivity<SchoolSelectActivityBinding> implements DataClient.OnDataChangedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MessageClient.OnMessageReceivedListener {

    private static final String TAG = "SchoolSelectActivity";

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

        binding.imageView.setOnClickListener(v -> openOnPhone());
    }

    private void openOnPhone() {
        if (node != null && apiClient != null && apiClient.isConnected()) {
            startActivity(new Intent(this, ConfirmationActivity.class).putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.OPEN_ON_PHONE_ANIMATION));
            Wearable.getMessageClient(this).sendMessage(node.getId(), Constants.MESSAGE_PATH_OPEN_ON_PHONE, null).addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "openOnPhone: FAIL: " + task.getResult());
                    Log.w(TAG, "openOnPhone: FAIL", task.getException());
                }
            });
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
        //Wearable.getMessageClient(this).addListener(this);
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Wearable.getMessageClient(this).removeListener(this);
        Wearable.getDataClient(this).removeListener(this);
    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        if (messageEvent.getPath().equals(Constants.DATA_PATH_SCHOOL)) {
            DataMap map = DataMap.fromByteArray(messageEvent.getData());
            startActivity(new Intent(this, ConfirmationActivity.class).putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION).putExtra(ConfirmationActivity.EXTRA_MESSAGE, map.getString(Constants.DATA_KEY_SCHOOL_ID)));
        }
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath() != null && item.getUri().getPath().equals(Constants.DATA_PATH_SCHOOL)) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    startActivity(new Intent(this, ConfirmationActivity.class).putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION).putExtra(ConfirmationActivity.EXTRA_MESSAGE, dataMap.getString(Constants.DATA_KEY_SCHOOL_ID)));
                }
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
