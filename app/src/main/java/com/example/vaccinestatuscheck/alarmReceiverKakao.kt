package com.example.vaccinestatuscheck

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import com.google.gson.annotations.Until
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link

class alarmReceiverKakao : BroadcastReceiver() {
    var context: Context? = null
    val channelId = "alarm_check"
    var friendId: String? = null
    var alarmId = 0

    override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context;

        // alarm intent로부터 전달받은 alarmId
        alarmId = intent!!.extras!!.getInt("alarmId")
        friendId = intent.extras!!.getString("friendId")

        val service = Intent(context, alarmService::class.java)
        service.putExtra("alarmId", alarmId)
        service.putExtra("friendId", friendId)
        service.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.context!!.startForegroundService(service)
        } else {
            this.context!!.startService(service)
        }



        val defaultFeed = FeedTemplate(
                content = Content(
                        title = "SOS",
                        description = "연락 좀 부탁해!!",
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