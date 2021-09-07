package com.example.vaccinestatuscheck;

import android.widget.ImageView;
import android.widget.Switch;

import com.kakao.sdk.template.model.Button;

public class alarmView {
    String alarmTime;
    String helperName;
    String helperImageId;
    int alarmId;
    Button removeButton;
    Switch aSwitch;

    public String getHelperImageId() {
        return helperImageId;
    }

    public void setHelperImageId(String helperImageId) {
        this.helperImageId = helperImageId;
    }

    //생성자 추가
    public alarmView(String alarmTime, String helperName, int alarmId, String imageId) {
        this.alarmTime = alarmTime;
        this.helperName = helperName;
        this.alarmId = alarmId;
        this.helperImageId = imageId;
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

}
