package com.gesall.mat_och_sov_klocka.service;

import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;

import com.gesall.mat_och_sov_klocka.data.MatOchSovKlockaItem;
import com.gesall.mat_och_sov_klocka.data.MatOchSovKlockaRepository;

import java.util.List;

public class RescheduleAlarmService extends LifecycleService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        MatOchSovKlockaRepository moskRepository = new MatOchSovKlockaRepository(getApplication());

        moskRepository.getMOSKItemsLiveData().observe(this, new Observer<List<MatOchSovKlockaItem>>() {
            @Override
            public void onChanged(List<MatOchSovKlockaItem> moskItems) {
                for (MatOchSovKlockaItem moskItem : moskItems) {
                    if (moskItem.isStarted()) {
                        moskItem.schedule(getApplicationContext());
                    }
                }
            }
        });

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }
}
