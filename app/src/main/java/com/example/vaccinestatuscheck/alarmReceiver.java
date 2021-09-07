package com.example.vaccinestatuscheck;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class alarmReceiver extends BroadcastReceiver {

    Context context;
    String channelId = "alarm_check";
    String friendId;
    int alarmId;
    MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        // alarm intent로부터 전달받은 alarmId
        alarmId = intent.getExtras().getInt("alarmId");
        friendId = intent.getExtras().getString("friendId");
        String get_yout_string = intent.getExtras().getString("state");
        Log.d("string : ", get_yout_string);

//        Intent alarmFunction = new Intent(context, AlarmFunctionActivity.class);
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addNextIntentWithParentStack(alarmFunction);
//        PendingIntent pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        final NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(context, channelId)
//                .setSmallIcon(R.mipmap.ic_launcher).setDefaults(Notification.DEFAULT_ALL)
//                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
//                .setAutoCancel(true)
//                .setContentTitle("알람")
//                .setContentText("울림")
//                .setContentIntent(pendingIntent);
//
//        final NotificationManager notificationManager=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            NotificationChannel channel=new NotificationChannel(channelId,"Channel human readable title",NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        int id=(int)System.currentTimeMillis();
//
//        notificationManager.notify(id,notificationBuilder.build());


        // RingtonePlayinService로 extra string값 보내기
        Intent service = new Intent(context, alarmService.class);
        service.putExtra("state", get_yout_string);
        service.putExtra("alarmId", alarmId);
        service.putExtra("friendId", friendId);
        service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // start the ringtone service

//        context.startActivity(service_intent);

        //switch on/off 시 설정된 알람 제어
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                this.context.startForegroundService(service);
        }else{
            this.context.startService(service);
        }
    }
}
