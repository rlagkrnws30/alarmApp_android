package com.creadle.vaccinesos

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import com.creadle.vaccinesos.databinding.ActivityFriendAddActitvityBinding
import com.kakao.sdk.talk.TalkApiClient
import java.util.ArrayList


class FriendAddActitvity : AppCompatActivity() {

    private lateinit var binding : ActivityFriendAddActitvityBinding
    private lateinit var friendArrayList: ArrayList<friendData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendAddActitvityBinding.inflate(layoutInflater);
        setContentView(binding.root);

        TalkApiClient.instance.friends { friends, error ->
            if (error != null) {
                Log.e(ContentValues.TAG, "카카오톡 친구 목록 가져오기 실패", error)
            } else if (friends != null) {

                var receiverUuids = friends.elements?.map { it.uuid };
                var friendName = friends.elements?.map { it.profileNickname }
                var image = friends.elements?.map { it.profileThumbnailImage }
                var map = arrayListOf(receiverUuids, friendName, image);
                friendArrayList = ArrayList();

                for (i in receiverUuids!!.indices) {
                    val friend = friendData(friendName!![i].toString(), image!![i].toString(), receiverUuids[i], check = false)
                    friendArrayList.add(friend);
                }

                binding.listview.adapter = friendAdapter(this, friendArrayList);
                binding.listview.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                    var selectItem = parent.getItemAtPosition(position) as friendData
//                    Toast.makeText(this, selectItem.uuid, Toast.LENGTH_SHORT).show()
                    var intent1 = Intent();
                    setResult(103, intent1)
                    setResult(106, intent1)
//                    var intent = Intent(this, MakeAlarmActivity::class.java)
                    intent1.putExtra("uuid",selectItem.uuid);
                    intent1.putExtra("friendName", selectItem.name);
                    intent1.putExtra("image", selectItem.ImageId);
                    finish();
                }

                Log.i(ContentValues.TAG, "카카오톡 친구 목록 가져오기 성공 \n${friends.elements?.joinToString("\n")}")

            }
        }

    }

    fun cancle(view: View) {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    fun shareApp(view: View) {
        var share : Intent = Intent().apply {
            action = Intent.ACTION_SEND
            addCategory(Intent.CATEGORY_DEFAULT)
            putExtra(Intent.EXTRA_TEXT, "https://play.google.com/apps/internaltest/4700355144589210319")
            putExtra(Intent.EXTRA_TITLE, "누구없소")
            type = "text/plain"
        }
        var shareIntent = Intent.createChooser(share, null)
        startActivity(shareIntent)
    }
}