package com.gesall.mat_och_sov_klocka.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MatOchSovKlockaRepository {

    private MatOchSovKlockaItemDao moskDao;
    private LiveData<List<MatOchSovKlockaItem>> mOSKItemsLiveData;

    public MatOchSovKlockaRepository(Application application) {
        MatOchSovKlockaItemDatabase db = MatOchSovKlockaItemDatabase.getDatabase(application);
        moskDao = db.MOSKDao();
        mOSKItemsLiveData = moskDao.getAllItems();
    }

    public void insert(MatOchSovKlockaItem moskItem) {
        MatOchSovKlockaItemDatabase.databaseWriteExecutor.execute(() -> {
            moskDao.insert(moskItem);
        });
    }


    public void update(MatOchSovKlockaItem moskItem) {
        MatOchSovKlockaItemDatabase.databaseWriteExecutor.execute(() -> {
            moskDao.update(moskItem);
        });
    }

    public void delete(MatOchSovKlockaItem moskItem) {
        MatOchSovKlockaItemDatabase.databaseWriteExecutor.execute(() -> {
            moskDao.delete(moskItem);
        });
    }


    public LiveData<List<MatOchSovKlockaItem>> getMOSKItemsLiveData() {
        return mOSKItemsLiveData;
    }


}

