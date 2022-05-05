package com.gesall.mat_och_sov_klocka.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.gesall.mat_och_sov_klocka.R;
import com.gesall.mat_och_sov_klocka.activities.MainActivity;
import com.gesall.mat_och_sov_klocka.data.MatOchSovKlockaItem;
import com.gesall.mat_och_sov_klocka.helpers.DoneOnEditorActionListener;
import com.gesall.mat_och_sov_klocka.helpers.TimePickerUtil;
import com.gesall.mat_och_sov_klocka.models.ListModel;

public class EditFragment extends Fragment {

    private ListModel model;

    public static final int EDIT = 3;

    EditText nameOfTimer, descriptionOfTimer;
    private CheckBox recurringYesOrNo;
    private CheckBox cbMon, cbTue, cbWed, cbThu, cbFri, cbSat, cbSun;

    TimePicker timePicker;

    private int position;

    MatOchSovKlockaItem itemToEdit;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false);

    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(ListModel.class);
        model.setCurrentFragment(EDIT);

        MainActivity asd = (MainActivity) getActivity();
        assert asd != null;
        asd.changeIconFab("checkmark");


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


        itemToEdit = (MatOchSovKlockaItem) getArguments().getSerializable("itemToEdit");
        populateFields(itemToEdit);


    }

    private void populateFields(MatOchSovKlockaItem oneItem) {

        nameOfTimer.setText(oneItem.getName());
        descriptionOfTimer.setText(oneItem.getDescription());


        TimePickerUtil.setTimePickerHour(timePicker, oneItem.getHour());
        TimePickerUtil.setTimePickerMinute(timePicker, oneItem.getMinute());

        recurringYesOrNo.setChecked(oneItem.isRecurring());

        cbMon.setChecked(oneItem.isMonday());
        cbTue.setChecked(oneItem.isTuesday());
        cbWed.setChecked(oneItem.isWednesday());
        cbThu.setChecked(oneItem.isThursday());
        cbFri.setChecked(oneItem.isFriday());
        cbSat.setChecked(oneItem.isSaturday());
        cbSun.setChecked(oneItem.isSunday());


    }

    public void saveEditedItem() {
        // Toast.makeText(getActivity().getApplicationContext(), "We're in edit fragment", Toast.LENGTH_LONG).show();
// notifyItemChanged(int position)

        itemToEdit.setName(nameOfTimer.getText().toString());
        // hour and minute saved separately
        int editedHour = TimePickerUtil.getTimePickerHour(timePicker);
        itemToEdit.setHour(editedHour);
        int editedMinute = TimePickerUtil.getTimePickerMinute(timePicker);
        itemToEdit.setMinute(editedMinute);
        String editedDescription = descriptionOfTimer.getText().toString();
        itemToEdit.setDescription(editedDescription);

        boolean editedRecurring = recurringYesOrNo.isChecked();
        itemToEdit.setRecurring(editedRecurring);

        boolean editedMon = cbMon.isChecked();
        boolean editedTue = cbTue.isChecked();
        boolean editedWed = cbWed.isChecked();
        boolean editedThu = cbThu.isChecked();
        boolean editedFri = cbFri.isChecked();
        boolean editedSat = cbSat.isChecked();
        boolean editedSun = cbSun.isChecked();

        itemToEdit.setMonday(editedMon);
        itemToEdit.setTuesday(editedTue);
        itemToEdit.setWednesday(editedWed);
        itemToEdit.setThursday(editedThu);
        itemToEdit.setFriday(editedFri);
        itemToEdit.setSaturday(editedSat);
        itemToEdit.setSunday(editedSun);

        int icon;
        if (editedRecurring) {
            // TODO proper icon handling
            icon = R.drawable.ic_baseline_repeat_24;
        } else {
            icon = R.drawable.ic_baseline_one_24;
        }
        itemToEdit.setImage(icon);

        /*MatOchSovKlockaItem oneItemGetCreatedAndStarted = model.getOnePrimitiveItem(position);
        long noEditCreated = oneItemGetCreatedAndStarted.getCreated();
        boolean noEditStarted = oneItemGetCreatedAndStarted.isStarted();*/


        boolean noEditStarted = recurringYesOrNo.isChecked();
        itemToEdit.setRecurring(noEditStarted);

/*public MatOchSovKlockaItem(String name, String description, int image, int hour, int minute, long created, boolean started, boolean recurring,
        boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday) {*/
        /*MatOchSovKlockaItem editedItem = new MatOchSovKlockaItem(editedName, editedDescription, dummyIcon1, editedHour, editedMinute,
                noEditCreated, noEditStarted, editedRecurring, editedMon, editedTue, editedWed, editedThu, editedFri, editedSat, editedSun);*/

        // model.doEditOfPrimitiveItem(editedItem, position);
        itemToEdit.schedule(getActivity());
        model.update(itemToEdit);

        NavHostFragment.findNavController(EditFragment.this)
                .navigate(R.id.action_EditFragment_to_HomeFragment);

    }


}

