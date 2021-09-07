package com.example.vaccinestatuscheck

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Toast
import com.kakao.sdk.talk.TalkApiClient
import com.kakao.sdk.template.model.*
import java.util.ArrayList

class MessagePushActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_push)

        val defaultFeed = FeedTemplate(
                content = Content(
                        title = "살려줘",
                        description = "",
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

        /*Intent 넘겨 받는 코드*/
        val intent = intent;
        var uuid = intent.getStringExtra("friendId")

        TalkApiClient.instance.sendDefaultMessage(receiverUuids = listOf(uuid) as List<String>, template = defaultFeed) { result, error ->
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