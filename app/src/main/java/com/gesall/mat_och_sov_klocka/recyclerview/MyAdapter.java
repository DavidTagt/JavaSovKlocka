package com.gesall.mat_och_sov_klocka.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.gesall.mat_och_sov_klocka.R;
import com.gesall.mat_och_sov_klocka.data.MatOchSovKlockaItem;
import com.gesall.mat_och_sov_klocka.helpers.OnToggleAlarmListener;
import com.gesall.mat_och_sov_klocka.models.ListModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    public static final String TAG_ADAPTER = "ADAPTER";


    private MatOchSovKlockaItem mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    private List<MatOchSovKlockaItem> usableList;
    Context c;
    private OnToggleAlarmListener listener;

    private View activityView;

    ListModel model;


    public MyAdapter(OnToggleAlarmListener listener, Context context, ListModel notOOPModel) {

        model = notOOPModel;

        this.listener = listener;

        c = context;

        usableList = new ArrayList<MatOchSovKlockaItem>();
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.frame_timer;
    }


    public void setMOSKItems(List<MatOchSovKlockaItem> moskItems) {
        this.usableList = moskItems;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {

        return usableList.size();
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //View view = LayoutInflater.from(c).inflate(viewType, parent, false);

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new RecyclerViewHolder(view, listener);

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {


        if (usableList.size() != 0) {

            MatOchSovKlockaItem moskItem = usableList.get(position);
            holder.bind(moskItem);

            holder.itemView.setOnClickListener(v -> {
                //code here
                Toast.makeText(getContext(), "OnClicked!", Toast.LENGTH_LONG).show();


                /*NavHostFragment.findNavController(HomeFragment)
                        .navigate(R.id.action_HomeFragment_to_EditFragment);*/

                // NavDirections action = HomeFragmentDirections.actionHomeFragmentToEditFragment(position);
                MatOchSovKlockaItem itemToSend = getMOSKItemAt(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemToEdit",itemToSend);


                /*HomeFragmentDirections.ActionHomeFragmentToEditFragment action = HomeFragmentDirections.actionHomeFragmentToEditFragment();
                action.setPositionToEdit(position);*/

                Navigation.findNavController(v).navigate(R.id.action_HomeFragment_to_EditFragment, bundle);


            });
        } else {
            Log.i(TAG_ADAPTER, "else in myadapter reached");
        }


    }

    public MatOchSovKlockaItem getMOSKItemAt(int position) {
        return usableList.get(position);
    }

    public void deleteMOSKItemAt(int position) {

        MatOchSovKlockaItem itemToDelete = getMOSKItemAt(position);
        deleteItem(itemToDelete, position);
        model.delete(itemToDelete);
    }


    public void deleteItem(MatOchSovKlockaItem moskItem, int position) {

        // mRecentlyDeletedItem = usablePrimitiveData.get(position);
        mRecentlyDeletedItemPosition = position;
        // usablePrimitiveData.remove(position);
        mRecentlyDeletedItem = moskItem;
        usableList.remove(position);

        notifyItemRemoved(position);
        try {
            showUndoSnackbar();
        } catch (Exception error) {
            Log.e(TAG_ADAPTER, error.getMessage());
        }
    }

    private void showUndoSnackbar() {
        // View view = activityView.findViewById(R.id.fragment_home);
        View view = activityView.findViewById(R.id.fragment_home);
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        // usablePrimitiveData.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        usableList.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    public Context getContext() {
        return c;
    }
}
