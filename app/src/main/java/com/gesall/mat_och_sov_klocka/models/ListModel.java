package com.gesall.mat_och_sov_klocka.models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.gesall.mat_och_sov_klocka.data.MatOchSovKlockaItem;
import com.gesall.mat_och_sov_klocka.data.MatOchSovKlockaRepository;

import java.util.List;

public class ListModel extends AndroidViewModel {

    public static final String TAG = "ViewModel Debug";

    private int currentDisplayedFragment;

    private MatOchSovKlockaRepository repository;

    private LiveData<List<MatOchSovKlockaItem>> moskItemsLiveData;


    public ListModel(@NonNull Application application) {
        // trigger user load.
        super(application);

        repository = new MatOchSovKlockaRepository(application);
        moskItemsLiveData = repository.getMOSKItemsLiveData();
    }

    public void setCurrentFragment(int callingFragment) {
        currentDisplayedFragment = callingFragment;
    }

    public int getCurrentFragment() {
        return currentDisplayedFragment;
    }


    public LiveData<List<MatOchSovKlockaItem>> getMOSKLiveData() {
        return moskItemsLiveData;
    }

    public void insert(MatOchSovKlockaItem item) {
        repository.insert((item));
    }

    public void update(MatOchSovKlockaItem item) {
        repository.update(item);
    }

    public void delete(MatOchSovKlockaItem item) {
        repository.delete(item);
    }

}


