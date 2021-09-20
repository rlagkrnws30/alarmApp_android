package com.example.vaccinestatuscheck;

import android.widget.ImageView;
import android.widget.Switch;

import com.kakao.sdk.template.model.Button;

public class alarmView {
    String dayNight;
    String alarmTime;
    String helperName;
    String helperImageId;
    int alarmId;
    Button removeButton;
    Switch aSwitch;
    String message;
    String friendId;

    public String getHelperImageId() {
        return helperImageId;
    }

    public void setHelperImageId(String helperImageId) {
        this.helperImageId = helperImageId;
    }

    //생성자 추가
    public alarmView(String dayNight, String alarmTime, String helperName, int alarmId, String imageId, String message, String friendId) {
        this.dayNight = dayNight;
        this.alarmTime = alarmTime;
        this.helperName = helperName;
        this.alarmId = alarmId;
        this.helperImageId = imageId;
        this.message = message;
        this.friendId = friendId;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getHelperName() {
        return helperName;
    }

    public void setHelperName(String helperName) {
        this.helperName = helperName;
    }

    public String getDayNight() {
        return dayNight;
    }

    public void setDayNight(String dayNight) {
        this.dayNight = dayNight;
    }
}
