package com.gesall.mat_och_sov_klocka.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gesall.mat_och_sov_klocka.R;
import com.gesall.mat_och_sov_klocka.activities.MainActivity;
import com.gesall.mat_och_sov_klocka.data.MatOchSovKlockaItem;
import com.gesall.mat_och_sov_klocka.helpers.OnToggleAlarmListener;
import com.gesall.mat_och_sov_klocka.models.ListModel;
import com.gesall.mat_och_sov_klocka.recyclerview.MyAdapter;
import com.gesall.mat_och_sov_klocka.recyclerview.SwipeToDeleteCallBack;

import java.util.List;

public class HomeFragment extends Fragment implements OnToggleAlarmListener {


    public static final String TAG_HOME = "HOME_FRAGMENT";

    public static final int HOME = 1;

    private ListModel model;

    private RecyclerView recyclerView;

    private MyAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(ListModel.class);
        mAdapter = new MyAdapter(this, getContext(), model);

        model.getMOSKLiveData().observe(this, new Observer<List<MatOchSovKlockaItem>>() {
            @Override
            public void onChanged(List<MatOchSovKlockaItem> matOchSovKlockaItems) {
                if (matOchSovKlockaItems != null) {
                    mAdapter.setMOSKItems(matOchSovKlockaItems);
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // code for changing the fab icon
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        mainActivity.changeIconFab("plus");


        model.setCurrentFragment(HOME);


        recyclerView = view.findViewById(R.id.recyclerViewHomeList);
        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        try {
            ItemTouchHelper itemTouchHelper = new
                    ItemTouchHelper(new SwipeToDeleteCallBack(mAdapter));
            itemTouchHelper.attachToRecyclerView(recyclerView);
            recyclerView.setAdapter(mAdapter);


        } catch (Exception error) {
            Log.e(TAG_HOME, error.getMessage());
        }

    }


    @Override
    public void onToggle(MatOchSovKlockaItem moskItem) {
        if (moskItem.isStarted()) {
            moskItem.cancelAlarm(getContext());
        } else {
            moskItem.schedule(getActivity());
        }
        model.update(moskItem);
    }
}





