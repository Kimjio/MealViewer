package com.kimjio.mealviewer.service;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.kimjio.mealviewer.Constants;
import com.kimjio.mealviewer.activity.SchoolSelectActivity;

public class WearListenerService extends WearableListenerService {

    private static final String TAG = "WearListenerService";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.d(TAG, "onMessageReceived: " + messageEvent.getPath());

        if (messageEvent.getPath().equals(Constants.MESSAGE_PATH_OPEN_ON_PHONE)) {
            startActivity(new Intent(this, SchoolSelectActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
