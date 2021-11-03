package com.creadle.vaccinesos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;

import java.util.Calendar;

public class FixAlarmAcitivity extends AppCompatActivity {

    String friendName;
    String friendImage;
    String friendId, pushMessage;
    int alarmId;
    int positon;
    TextView name;
    ImageView image;
    TimePicker timePicker;
    EditText message;
    int hour,minute;
    Boolean switchBoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_alarm);
        image = findViewById(R.id.friend_image);
        name = findViewById(R.id.friend_name);
        timePicker = findViewById(R.id.timePicker);
        message = findViewById(R.id.pushMessage);
        Intent intent = getIntent();
        positon = intent.getIntExtra("위치", 0);
        alarmId = intent.getIntExtra("알람", 0);
        friendName = intent.getStringExtra("이름");
        friendImage = intent.getStringExtra("사진");
        pushMessage = intent.getStringExtra("메세지");
        friendId = intent.getStringExtra("헬퍼id");
        hour = intent.getIntExtra("시간", 0);
        minute = intent.getIntExtra("분", 0);
        Log.d("알람id fix", String.valueOf(alarmId));
        name.setText(friendName);
        message.setText(pushMessage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(hour);
            timePicker.setMinute(minute);
        }
        Glide.with(this).load(friendImage).into(image);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(message.getWindowToken(), 0);
        return true;
    }

    public void cancleAlarm(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void fixAlarm(View view) {
        //알람 취소
        AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), alarmReceiver.class);
        PendingIntent alarmCancle = PendingIntent.getBroadcast(getApplicationContext(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d("alarmId", String.valueOf(alarmId));
        alarmManager.cancel(alarmCancle);

        //신규 알람 생성
        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();
        int newAlarmId = hour*60 + minute;
        switchBoolean = true;
        Intent alarm = new Intent(getApplicationContext(), alarmReceiver.class);
        pushMessage = String.valueOf(message.getText());
        alarm.putExtra("alarmId", newAlarmId);
        alarm.putExtra("friendId", friendId);
        alarm.putExtra("message", pushMessage);
        PendingIntent operation = PendingIntent.getBroadcast(this, newAlarmId, alarm, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        Log.d("time", String.valueOf(calendar.getTime()));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        Log.d("current hour", String.valueOf(Calendar.HOUR_OF_DAY));

        if ((Calendar.getInstance().after(calendar))) {
            calendar.add(Calendar.DATE, 1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, operation);
            Log.d("new alarm", String.valueOf(newAlarmId));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), operation);
        }

        //신규 알람 정보 MainActivity 전달
        Intent fixAlarm = new Intent();
        fixAlarm.putExtra("시간", hour);
        fixAlarm.putExtra("분", minute);
        fixAlarm.putExtra("뉴알람", newAlarmId);
        fixAlarm.putExtra("헬퍼이름", friendName);
        fixAlarm.putExtra("헬퍼사진", friendImage);
        fixAlarm.putExtra("위치", positon);
        fixAlarm.putExtra("메세지", pushMessage);
        fixAlarm.putExtra("헬퍼id",friendId);
        fixAlarm.putExtra("스위치", switchBoolean);

        setResult(105, fixAlarm);
        finish();
    }

    public void friendFix(View view) {
        Intent intent = new Intent(this, FriendAddActitvity.class);
        startActivityForResult(intent, 106);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 106) {
            if (data != null) {
                friendId = data.getStringExtra("uuid");
                friendName = data.getStringExtra("friendName");
                friendImage = data.getStringExtra("image");
                name.setText(friendName);
                Glide.with(this).load(friendImage).into(image);
            }
        }
    }
}