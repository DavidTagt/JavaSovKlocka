package com.gesall.mat_och_sov_klocka.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.gesall.mat_och_sov_klocka.R;
import com.gesall.mat_och_sov_klocka.activities.MainActivity;
import com.gesall.mat_och_sov_klocka.data.MatOchSovKlockaItem;
import com.gesall.mat_och_sov_klocka.helpers.DoneOnEditorActionListener;
import com.gesall.mat_och_sov_klocka.helpers.TimePickerUtil;
import com.gesall.mat_och_sov_klocka.models.ListModel;

public class CreateFragment extends Fragment {

    private ListModel model;

    public static final int CREATE = 2;

    EditText nameOfTimer, descriptionOfTimer;
    private CheckBox recurringYesOrNo;
    private CheckBox cbMon, cbTue, cbWed, cbThu, cbFri, cbSat, cbSun;

    TimePicker timePicker;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity asd = (MainActivity) getActivity();
        assert asd != null;
        asd.changeIconFab("checkmark");


        model = new ViewModelProvider(requireActivity()).get(ListModel.class);
        model.setCurrentFragment(CREATE);

        nameOfTimer = (EditText) view.findViewById(R.id.etCreateName);
        timePicker = (TimePicker) view.findViewById(R.id.tpCreateTimepicker);
        timePicker.setIs24HourView(android.text.format.DateFormat.is24HourFormat(getActivity()));
        descriptionOfTimer = (EditText) view.findViewById(R.id.etCreateDescription);

        recurringYesOrNo = (CheckBox) view.findViewById(R.id.cbCreateRecurring);
        cbMon = view.findViewById(R.id.cbCreateMon);
        cbTue = view.findViewById(R.id.cbCreateTue);
        cbWed = view.findViewById(R.id.cbCreateWed);
        cbThu = view.findViewById(R.id.cbCreateThu);
        cbFri = view.findViewById(R.id.cbCreateFri);
        cbSat = view.findViewById(R.id.cbCreateSat);
        cbSun = view.findViewById(R.id.cbCreateSun);

        nameOfTimer.setOnEditorActionListener(new DoneOnEditorActionListener());
        descriptionOfTimer.setOnEditorActionListener(new DoneOnEditorActionListener());


    }

    public void saveInputFromTextFields() {


        String createName = nameOfTimer.getText().toString();
        String createDescription = descriptionOfTimer.getText().toString();

        int createHour = TimePickerUtil.getTimePickerHour(timePicker);
        int createMinute = TimePickerUtil.getTimePickerMinute(timePicker);


        long createTimerCreated = System.currentTimeMillis();
        boolean createStarted = true;

        boolean createRecurring = recurringYesOrNo.isChecked();
        int icon;

        if (createRecurring) {
            icon = R.drawable.ic_baseline_repeat_24;
        } else {
            icon = R.drawable.ic_baseline_one_24;
        }


        boolean createMon = cbMon.isChecked();
        boolean createTue = cbTue.isChecked();
        boolean createWed = cbWed.isChecked();
        boolean createThu = cbThu.isChecked();
        boolean createFri = cbFri.isChecked();
        boolean createSat = cbSat.isChecked();
        boolean createSun = cbSun.isChecked();


        MatOchSovKlockaItem newEntry = new MatOchSovKlockaItem(createName, createDescription, icon, createHour, createMinute,
                createTimerCreated, createStarted, createRecurring, createMon, createTue, createWed, createThu, createFri, createSat, createSun);


        // model.addPrimitiveData(newEntry);
        model.insert(newEntry);

        newEntry.schedule(getActivity());

        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.action_CreateFragment_to_HomeFragment);
    }
}