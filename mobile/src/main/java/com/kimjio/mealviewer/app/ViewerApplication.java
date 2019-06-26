package com.kimjio.mealviewer.app;

import android.app.Application;
import android.content.Intent;

import com.kimjio.mealviewer.service.WearListenerService;

public class ViewerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, WearListenerService.class));
    }
}
