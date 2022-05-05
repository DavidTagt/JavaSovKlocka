package com.gesall.mat_och_sov_klocka.recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gesall.mat_och_sov_klocka.R;
import com.gesall.mat_och_sov_klocka.data.MatOchSovKlockaItem;
import com.gesall.mat_och_sov_klocka.helpers.OnToggleAlarmListener;

import java.util.Locale;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView nameTextView, descriptionTextView, recurringDays, alarmTime;
    private ImageView timerImage;

    SwitchCompat alarmStarted;

    private OnToggleAlarmListener listener;

    public RecyclerViewHolder(@NonNull View itemView, OnToggleAlarmListener listener) {
        super(itemView);

        nameTextView = itemView.findViewById(R.id.tvName);
        descriptionTextView = itemView.findViewById(R.id.tvDescription);
        alarmTime = itemView.findViewById(R.id.tvTimer);

        timerImage = itemView.findViewById(R.id.ivImageIconForRecurringOrOnceOf);
        recurringDays = itemView.findViewById(R.id.tvRecurringDays);
        alarmStarted = itemView.findViewById(R.id.swStarted);

        this.listener = listener;
    }

    public void bind(MatOchSovKlockaItem moskItem) {
        String alarmText = String.format(Locale.ENGLISH, "%02d:%02d", moskItem.getHour(), moskItem.getMinute());

        alarmTime.setText(alarmText);
        alarmStarted.setChecked(moskItem.isStarted());

        if(moskItem.isRecurring()){
            timerImage.setImageResource(R.drawable.ic_baseline_repeat_24);
            recurringDays.setText(moskItem.getRecurringDaysText());
        } else {
            timerImage.setImageResource(R.drawable.ic_baseline_one_24);
            // TODO How can i make this say "only once on Mon Thu" etc?
            recurringDays.setText(R.string.alarm_recurrance_once);
            //String.format("One Time Alarm %s scheduled for %s at %02d:%02d", name, DayUtil.toDay(calendar.get(Calendar.DAY_OF_WEEK)), hour, minute, alarmId);
        }

        if(moskItem.getName().length() != 0){
            // why am i interested in when the damn thing was created?
            //nameTextView.setText(String.format(Locale.ENGLISH,"%s | %d", moskItem.getName(), moskItem.getCreated()));
            nameTextView.setText(moskItem.getName());
        } else {
            nameTextView.setText(R.string.incase_no_name_entered);
        }

        descriptionTextView.setText(moskItem.getDescription());

        alarmStarted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            listener.onToggle(moskItem);
        });

    }
}
