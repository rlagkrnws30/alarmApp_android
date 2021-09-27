package com.creadle.vaccinesos

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide

class friendAdapter(private val context: Activity, private val arrayList: ArrayList<friendData>) : ArrayAdapter<friendData>(context,
        R.layout.friend_item, arrayList) {

    //list view를 가져왔을 때 어떻게 뿌려줄꺼냐, layoutinflater의 역할은 view를 붙이는 역할을 한다
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.friend_item, null);
//        var cnt: Int = 0;
        val imageView: ImageView = view.findViewById(R.id.friend_image);
        val friendName: TextView = view.findViewById(R.id.friend_name);
//        val checkFriend: CheckBox = view.findViewById(R.id.friend_check);
        //position : 뿌려진 list view의 index(위치) 0,1,2,3,4를 의미한다
        var friend = arrayList[position];
//        checkFriend.setOnClickListener(View.OnClickListener {
//            if (checkFriend.isChecked == true) {
//                if (cnt == 0) {
//                    Log.d("msg", "friend_check")
//                    cnt += 1;
//                }else{
//                    Toast.makeText(getContext(), "친구는 한명만 추가 가능합니다.", Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Log.d("msg", "check -1")
//                cnt -= 1;
//            }
//        })
        Log.d("imageID", friend.ImageId);
        Glide.with(view).load(friend.ImageId).into(imageView);
        friendName.text = friend.name;
//        checkFriend.isChecked = friend.check;

        return view
    }

    override fun getItem(position: Int): friendData? {
        return super.getItem(position)
    }
}