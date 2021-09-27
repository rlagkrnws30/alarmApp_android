package com.creadle.vaccinesos;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;

import java.util.Calendar;
import java.util.Random;

public class MakeAlarmActivity extends AppCompatActivity {

    TextView cancleAlarm, addAlarm;
    TextView friendAdd;
    LinearLayout selectFriend, friendAdd2, makeAlarm;
    TimePicker timePicker;
    TextView friendName;
    ImageView friendImage;
    String friendId;
    String helperName, helperImageId, pushMessage;
    EditText message;
    int hour, minute, alarmId;
    private AlarmManager alarmManager;
    Switch switchView;
    int friendNoti;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_alarm);
        addAlarm = findViewById(R.id.fixButton);
        cancleAlarm = findViewById(R.id.cancleButton);
        timePicker = findViewById(R.id.timePicker);
        friendAdd = findViewById(R.id.friend_add);
        friendAdd2 = findViewById(R.id.friend_add2);
        selectFriend = findViewById(R.id.select_friend);
        friendName = findViewById(R.id.friend_name);
        friendImage = findViewById(R.id.friend_image);
        message = findViewById(R.id.pushMessage);
        makeAlarm = findViewById(R.id.makeAlarm);
        selectFriend.setVisibility(View.INVISIBLE);
        addAlarm.setVisibility(View.INVISIBLE);
//        Intent intent = getIntent();
//        if (intent.getStringExtra("uuid") != null) {
//            friendAdd.setVisibility(View.INVISIBLE);
//            selectFriend.setVisibility(View.VISIBLE);
//            friendName.setText(intent.getStringExtra("friendName"));
//            Glide.with(this).load(intent.getStringExtra("image")).into(friendImage);
//            addAlarm.setVisibility(View.VISIBLE);
    }
//        addAlarm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //시간, 분 데이터 전달
//                Intent intent = new Intent();
//                hour = timePicker.getCurrentHour();
//                minute = timePicker.getCurrentMinute();
//                intent.putExtra("시간", String.valueOf(hour));
//                intent.putExtra("분", String.valueOf(minute));
//                setResult(RESULT_OK, intent);
//
//                alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//
//                Intent intent2 = new Intent(this, alarmReceiver.class);
//                PendingIntent operation = PendingIntent.getBroadcast(this, 0, intent2, 0);
//
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.HOUR_OF_DAY, hour);
//                calendar.set(Calendar.MINUTE, minute);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), operation );
//                }
//                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), operation );
//                }
//                finish();
//            }
//        });
////        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
////            @Override
////            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
////                String strTime = hourOfDay + " : " + minute;
//                Toast.makeText(makeAlarm.this, strTime, Toast.LENGTH_SHORT).show();
//            }
//        });

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

    public void addAlarm(View view) {
        //adapter view 생성 시간 데이터 정보 전달
        Intent intent = new Intent();
        Random random = new Random();
//        int alarmId = random.nextInt(100);
        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();
        pushMessage = String.valueOf(message.getText());
        int alarmId = hour*60 + minute;
        Log.d("메세지", String.valueOf(message.getText()));
        intent.putExtra("시간", hour);
        intent.putExtra("분", minute);
        intent.putExtra("알람", alarmId);
        intent.putExtra("헬퍼이름", helperName);
        intent.putExtra("헬퍼사진", helperImageId);
        intent.putExtra("메세지", pushMessage);
        intent.putExtra("헬퍼id", friendId);
        setResult(101, intent);
        Log.d("time", hour + " : " + minute);

        //알람 세팅

        Log.d("ID:", String.valueOf(alarmId));
        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent alarm = new Intent(this, alarmReceiver.class);
        alarm.putExtra("state", "alarm on");
        alarm.putExtra("alarmId", alarmId);
        alarm.putExtra("friendId", friendId);
        alarm.putExtra("message", pushMessage);
        PendingIntent operation = PendingIntent.getBroadcast(this, alarmId, alarm, PendingIntent.FLAG_UPDATE_CURRENT);

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
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), operation);
        }

        finish();
    }

    public void friendAdd(View view) {
//        팝업창 표출 이제그만보기 버튼 키 값 저장, 버튼 클릭 되지 않을 시 앱을 사용하는 친구만 목록 표출된다는 팝업창 표출
        Log.d("친구 클릭", "클릭");
        AlertDialog.Builder friend_noti = new AlertDialog.Builder(this);
        friendNoti = preConfig.readFriendPref(getApplicationContext());
        friend_noti.setMessage("누구없소와 함께하는 회원님들이 친구 목록에 표출됩니다.");
        friend_noti.setIcon(R.drawable.icon2);
        friend_noti.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*카카오톡이 설치되어있는지 먼저 확인*/
                Log.d("login", "클릭2");
                Intent intent = new Intent(getApplicationContext(), FriendAddActitvity.class);
                startActivityForResult(intent, 103);
            }
        });
        friend_noti.setNeutralButton("앞으로 보지 않기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                friendNoti = 1;
                preConfig.writeFriendPref(getApplicationContext(),friendNoti);
                Intent intent = new Intent(getApplicationContext(), FriendAddActitvity.class);
                startActivityForResult(intent, 103);
            }
        });
        AlertDialog aFriend = friend_noti.create();
        aFriend.setIcon(R.drawable.icon2);
        aFriend.setTitle("이용 팁!");
//        Intent intent = new Intent(this, FriendAddActitvity.class);
//        startActivityForResult(intent, 103);
        if (friendNoti != 1) {
            aFriend.show();
        } else {
            Intent intent = new Intent(this, FriendAddActitvity.class);
            startActivityForResult(intent, 103);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 103) {
            if (data != null) {
                friendAdd2.setVisibility(View.INVISIBLE);
                selectFriend.setVisibility(View.VISIBLE);
                friendId = data.getStringExtra("uuid");
                helperName = data.getStringExtra("friendName");
                helperImageId = data.getStringExtra("image");
                friendName.setText(data.getStringExtra("friendName"));
                Glide.with(this).load(data.getStringExtra("image")).into(friendImage);
                addAlarm.setVisibility(View.VISIBLE);
            }
        }
    }
}
