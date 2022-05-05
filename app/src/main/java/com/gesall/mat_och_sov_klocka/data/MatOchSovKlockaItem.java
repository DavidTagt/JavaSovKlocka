package com.gesall.mat_och_sov_klocka.data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.gesall.mat_och_sov_klocka.broadcastreceiver.AlarmBroadcastReceiver;
import com.gesall.mat_och_sov_klocka.helpers.DayUtil;

import java.io.Serializable;
import java.util.Calendar;

import static com.gesall.mat_och_sov_klocka.broadcastreceiver.AlarmBroadcastReceiver.FRIDAY;
import static com.gesall.mat_och_sov_klocka.broadcastreceiver.AlarmBroadcastReceiver.MONDAY;
import static com.gesall.mat_och_sov_klocka.broadcastreceiver.AlarmBroadcastReceiver.RECURRING;
import static com.gesall.mat_och_sov_klocka.broadcastreceiver.AlarmBroadcastReceiver.SATURDAY;
import static com.gesall.mat_och_sov_klocka.broadcastreceiver.AlarmBroadcastReceiver.SUNDAY;
import static com.gesall.mat_och_sov_klocka.broadcastreceiver.AlarmBroadcastReceiver.THURSDAY;
import static com.gesall.mat_och_sov_klocka.broadcastreceiver.AlarmBroadcastReceiver.TITLE;
import static com.gesall.mat_och_sov_klocka.broadcastreceiver.AlarmBroadcastReceiver.TUESDAY;
import static com.gesall.mat_och_sov_klocka.broadcastreceiver.AlarmBroadcastReceiver.WEDNESDAY;


@Entity(tableName = "matochsovklockaitem_table")
public class MatOchSovKlockaItem implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int alarmId;

    private String name, description;

    private int image;


    private int hour;
    private int minute;

    private boolean started, recurring;

    private boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    private long created;

    public MatOchSovKlockaItem(String name, String description, int image, int hour, int minute, long created, boolean started, boolean recurring,
                               boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.hour = hour;
        this.minute = minute;
        this.created = created;
        this.started = started;
        this.recurring = recurring;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public boolean isStarted() {
        return started;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean isRecurring) {
        this.recurring = isRecurring;
    }

    public boolean isMonday() {
        return monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setMonday(boolean day) {
        this.monday = day;
    }

    public void setTuesday(boolean day) {
        this.tuesday = day;
    }

    public void setWednesday(boolean day) {
        this.wednesday = day;
    }

    public void setThursday(boolean day) {
        this.thursday = day;
    }

    public void setFriday(boolean day) {
        this.friday = day;
    }

    public void setSaturday(boolean day) {
        this.saturday = day;
    }

    public void setSunday(boolean day) {
        this.sunday = day;
    }


    public void schedule(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(RECURRING, recurring);
        intent.putExtra(MONDAY, monday);
        intent.putExtra(TUESDAY, tuesday);
        intent.putExtra(WEDNESDAY, wednesday);
        intent.putExtra(THURSDAY, thursday);
        intent.putExtra(FRIDAY, friday);
        intent.putExtra(SATURDAY, saturday);
        intent.putExtra(SUNDAY, sunday);

        intent.putExtra(TITLE, name);

        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // if alarm time has already passed, increment day by 1
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        if (!recurring) {
            String toastText = null;
            try {
                toastText = String.format("One Time Alarm %s scheduled for %s at %02d:%02d", name, DayUtil.toDay(calendar.get(Calendar.DAY_OF_WEEK)), hour, minute, alarmId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        alarmPendingIntent
                );
            }
        } else {
            String toastText = String.format("Recurring Alarm %s scheduled for %s at %02d:%02d", name, getRecurringDaysText(), hour, minute, alarmId);
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();

            final long RUN_DAILY = 24 * 60 * 60 * 1000;
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    RUN_DAILY,
                    alarmPendingIntent
            );
        }

        this.started = true;
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, 0);
        alarmManager.cancel(alarmPendingIntent);
        this.started = false;

        String toastText = String.format("Alarm cancelled for %02d:%02d with id %d", hour, minute, alarmId);
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
        Log.i("cancel", toastText);
    }

    public String getRecurringDaysText() {
        if (!recurring) {
            return null;
        }

        String days = "";
        if (monday) {
            days += "Mo ";
        }
        if (tuesday) {
            days += "Tu ";
        }
        if (wednesday) {
            days += "We ";
        }
        if (thursday) {
            days += "Th ";
        }
        if (friday) {
            days += "Fr ";
        }
        if (saturday) {
            days += "Sa ";
        }
        if (sunday) {
            days += "Su ";
        }

        return days;
    }


}

