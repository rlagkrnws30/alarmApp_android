package com.creadle.vaccinesos

import android.app.*
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link

class alarmService : Service() {
    var mediaPlayer: MediaPlayer? = null
    var channelId = "alarm_check"
    private lateinit var timer: CountDownTimer
    private var tempTime: Long = 0
    var friendId = ""
    var message = ""
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val alarmId = intent.extras!!.getInt("alarmId")
        friendId = intent.extras!!.getString("friendId").toString()
        message = intent.extras!!.getString("message").toString()
//        val getState = intent.extras!!.getString("state")

        Log.d("serviceID", alarmId.toString())
        //        Intent alarmFunction = new Intent(this, AlarmFunctionActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val alarm = Intent(this, AlarmFunctionActivity::class.java)
            alarm.putExtra("alarmId", alarmId)
            val stackBuilder = TaskStackBuilder.create(this)
            stackBuilder.addNextIntentWithParentStack(alarm)
            val pendingIntent = stackBuilder.getPendingIntent(alarmId, PendingIntent.FLAG_UPDATE_CURRENT)
            val channel = NotificationChannel(channelId,
                    "누구없소 헬프 카톡 서비스",
                    NotificationManager.IMPORTANCE_HIGH)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            val notification = NotificationCompat.Builder(this, channelId)
                    .setContentTitle("메디컬 체크 시작")
                    .setContentIntent(pendingIntent)
                    .setContentText("1분 후에 헬프 카톡이 송신됩니다!")
                    .setSmallIcon(R.drawable.notip)
                    .build()
            startForeground(alarmId, notification)
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
            mediaPlayer!!.start()
            mediaPlayer!!.isLooping = true;
            //30초 타이머 작동
            startTimer()
            //30초 초과 시 카톡 발송 acitivty 호출 후 본 서비스 정료
//            Intent push = new Intent(this, MessagePushActivity.class);
//            push.putExtra("friendId", friendId);
//            push.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(push);
            //Service가 강제 종료되었을 경우 시스템이 다시 Service를 재시작 시켜 주지만 intent 값을 null로 초기화 시켜서 재시작
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("onDestory() 실행", "서비스 파괴")
        mediaPlayer!!.stop()
        stopTimer()
        //        unregisterReceiver(alarmReceiverKakao);
    }

    private fun startTimer() {
        timer = object : CountDownTimer(60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tempTime = millisUntilFinished
//                updateTimer()
                Log.d("service", message);
                Log.d("msg", "타이머 시작")
            }

            override fun onFinish() {
                Log.d("msg", "타이머 끝")
                Log.d("service", message);
                /*카카오톡 메시지 발송하는 액티비티로 넘어가는 코드 추가*/
//                var intent = Intent();
//                var message = intent.getStringExtra("message").toString();
//                Log.d("service", message);
                val defaultFeed = FeedTemplate(
                        content = Content(
                                title = "헬프 메세지",
                                description = message,
                                imageUrl = "",
                                link = Link(
                                        webUrl = "https://developers.kakao.com",
                                        mobileWebUrl = "https://developers.kakao.com"
                                )
                        ),
                        buttons = listOf(
                                Button(
                                        "웹으로 보기",
                                        Link(
                                                webUrl = "https://developers.kakao.com",
                                                mobileWebUrl = "https://developers.kakao.com"
                                        )
                                ),
                                Button(
                                        "앱으로 보기",
                                        Link(
                                                androidExecutionParams = mapOf("key1" to "value1", "key2" to "value2"),
                                                iosExecutionParams = mapOf("key1" to "value1", "key2" to "value2")
                                        )
                                )
                        )
                )
                TalkApiClient.instance.sendDefaultMessage(receiverUuids = listOf(friendId) as List<String>, template = defaultFeed) { result, error ->
                    if (error != null) {
                        Log.e(ContentValues.TAG, "메시지 보내기 실패", error)
                    } else if (result != null) {
                        Log.i(ContentValues.TAG, "메시지 보내기 성공 ${result.successfulReceiverUuids}")

                        if (result.failureInfos != null) {
                            Log.d(ContentValues.TAG, "메시지 보내기에 일부 성공했으나, 일부 대상에게는 실패 \n${result.failureInfos}")
                        }
                    }
                }
            }
        }
        timer.start()
    }

    private fun stopTimer() {
        Log.d("타이머", "중단")
        timer.cancel()
    }
}