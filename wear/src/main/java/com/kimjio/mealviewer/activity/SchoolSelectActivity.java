package com.kimjio.mealviewer.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.kimjio.mealviewer.Constants;
import com.kimjio.mealviewer.R;
import com.kimjio.mealviewer.databinding.SchoolSelectActivityBinding;

public class SchoolSelectActivity extends BaseActivity<SchoolSelectActivityBinding> implements DataClient.OnDataChangedListener {

    @Override
    protected int layoutId() {
        return R.layout.school_select_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath() != null && item.getUri().getPath().equals(Constants.DATA_PATH_SCHOOL)) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    dataMap.getString(Constants.DATA_KEY_SCHOOL_ID);
                }
            }
        }
    }
}
