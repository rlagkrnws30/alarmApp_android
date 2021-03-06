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
import android.widget.Button;
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

    Button cancleAlarm, addAlarm;
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
//                //??????, ??? ????????? ??????
//                Intent intent = new Intent();
//                hour = timePicker.getCurrentHour();
//                minute = timePicker.getCurrentMinute();
//                intent.putExtra("??????", String.valueOf(hour));
//                intent.putExtra("???", String.valueOf(minute));
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
        //adapter view ?????? ?????? ????????? ?????? ??????
        Intent intent = new Intent();
        Random random = new Random();
//        int alarmId = random.nextInt(100);
        hour = timePicker.getCurrentHour();
        minute = timePicker.getCurrentMinute();
        pushMessage = String.valueOf(message.getText());
        int alarmId = hour*60 + minute;
        boolean switchBoolean = true;
        intent.putExtra("??????", hour);
        intent.putExtra("???", minute);
        intent.putExtra("??????", alarmId);
        intent.putExtra("????????????", helperName);
        intent.putExtra("????????????", helperImageId);
        intent.putExtra("?????????", pushMessage);
        intent.putExtra("??????id", friendId);
        intent.putExtra("?????????", switchBoolean);
        setResult(101, intent);

        //?????? ??????

        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent alarm = new Intent(this, alarmReceiver.class);
        alarm.putExtra("state", "alarm on");
        alarm.putExtra("alarmId", alarmId);
        alarm.putExtra("friendId", friendId);
        alarm.putExtra("message", pushMessage);
        PendingIntent operation = PendingIntent.getBroadcast(this, alarmId, alarm, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

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
//        ????????? ?????? ?????????????????? ?????? ??? ??? ??????, ?????? ?????? ?????? ?????? ??? ?????? ???????????? ????????? ?????? ??????????????? ????????? ??????
//        AlertDialog.Builder friend_noti = new AlertDialog.Builder(this);
//        friendNoti = preConfig.readFriendPref(getApplicationContext());
//        friend_noti.setMessage("??????????????? ???????????? ??????????????? ?????? ????????? ???????????????.");
        Intent intent = new Intent(getApplicationContext(), FriendAddActitvity.class);
        startActivityForResult(intent, 103);

//        friend_noti.setIcon(R.drawable.icon2);
//        friend_noti.setPositiveButton("??????", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                /*??????????????? ????????????????????? ?????? ??????*/
//                Log.d("login", "??????2");
//                Intent intent = new Intent(getApplicationContext(), FriendAddActitvity.class);
//                startActivityForResult(intent, 103);
//            }
//        });
//        friend_noti.setNeutralButton("????????? ?????? ??????", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                friendNoti = 1;
//                preConfig.writeFriendPref(getApplicationContext(),friendNoti);
//                Intent intent = new Intent(getApplicationContext(), FriendAddActitvity.class);
//                startActivityForResult(intent, 103);
//            }
//        });
//
//        AlertDialog aFriend = friend_noti.create();
//        aFriend.setIcon(R.drawable.icon2);
//        aFriend.setTitle("?????? ???!");
//        Intent intent = new Intent(this, FriendAddaActitvity.class);
//        startActivityForResult(intent, 103);
//        if (friendNoti != 1) {
//            aFriend.show();
//        } else {
//            Intent intent = new Intent(this, FriendAddActitvity.class);
//            startActivityForResult(intent, 103);
//        }
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
