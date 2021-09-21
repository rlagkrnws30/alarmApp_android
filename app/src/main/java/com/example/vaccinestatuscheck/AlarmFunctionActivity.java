package com.example.vaccinestatuscheck;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;

public class AlarmFunctionActivity extends AppCompatActivity {

    Button button;
    AlarmManager alarmManager;
    SwipeButton swipeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_function);
        Log.d("알람", "동작");
        swipeButton = (SwipeButton)findViewById(R.id.cancel_btn);
        //alarmId 가져오기
        Intent intent = getIntent();
        int alarmId = intent.getExtras().getInt("alarmId");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(this.KEYGUARD_SERVICE);
            keyguardManager.requestDismissKeyguard(this,null);
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                Log.d("swipe","yes");
                alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), alarmReceiver.class);
//                intent.putExtra("중지", 1);
//                sendBroadcast(intent);
                Intent intent1 = new Intent(getApplicationContext(), alarmService.class);
                PendingIntent alarmCancle = PendingIntent.getBroadcast(getApplicationContext(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(alarmCancle);
                stopService(intent1);
                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
                intent2.putExtra("credit", 1);
                startActivityForResult(intent2, 102);
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.money);
                mediaPlayer.start();
                finish();
            }
        });
//        button = findViewById(R.id.button5);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//                Intent intent = new Intent(getApplicationContext(), alarmReceiver.class);
////                intent.putExtra("중지", 1);
////                sendBroadcast(intent);
//                Intent intent1 = new Intent(getApplicationContext(), alarmService.class);
//                PendingIntent alarmCancle = PendingIntent.getBroadcast(getApplicationContext(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                alarmManager.cancel(alarmCancle);
//                stopService(intent1);
//                Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
//                intent2.putExtra("credit", 1);
//                startActivityForResult(intent2, 102);
//                finish();
//            }
//        });
    }
}