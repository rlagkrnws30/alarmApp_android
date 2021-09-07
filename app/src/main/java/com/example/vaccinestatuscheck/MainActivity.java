package com.example.vaccinestatuscheck;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button addButton;
    Button fixButton;
    Button loginButton;
    String shared = "file";
    Switch aSwitch;
    private ArrayList<alarmView> arrayList;
    private alarmViewAdaptor mainAdapter;
    public static Context context;
    String strUserPicture, usename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        usename = intent.getStringExtra("name");
        strUserPicture = intent.getStringExtra("profileImg");
        Log.d("msg", "service start");

        TextView point = findViewById(R.id.credit);
        if (point.getText() != "0"){
            int updateCredit = preConfig.readCreditPref(this);
            point.setText(String.valueOf(updateCredit));
        }

        int credit = intent.getIntExtra("credit", 0);
        Log.d("msg", String.valueOf(credit));
        if (credit == 1) {
            Log.d("msg2", String.valueOf(credit));
            int newCredit = Integer.parseInt((String) point.getText()) + credit;
            Log.d("newcredit", String.valueOf(newCredit));
            preConfig.writeCreditPref(this, newCredit);
            point.setText(String.valueOf(newCredit));
        }

        if (usename == null | strUserPicture == null) {
            usename = preConfig.readNamePref(this);
            strUserPicture = preConfig.readPicturePref(this);
        }

        TextView username = findViewById(R.id.username);
        username.setText(usename);
        ImageView userpicture = findViewById(R.id.userPicture);
        Glide.with(this).load(strUserPicture).into(userpicture);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        arrayList = preConfig.readListFromPref(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        TextView intro = findViewById(R.id.intro);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            intro.setVisibility(View.VISIBLE);
        }else{
            intro.setVisibility(View.INVISIBLE);
        }

        mainAdapter = new alarmViewAdaptor(arrayList);
        recyclerView.setAdapter(mainAdapter);
        addButton = findViewById(R.id.addButton);
//        fixButton = findViewById(R.id.fix);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MakeAlarmActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        mainAdapter.setOnItemClickListener(new alarmViewAdaptor.OnItemClickListenr() {
            @Override
            public void onItemClick(View v, int position) {
                int id = mainAdapter.items.get(position).alarmId;
                Intent intent1 = new Intent(getApplicationContext(), MakeAlarmActivity.class);
                intent1.putExtra("수정", 1);
                intent1.putExtra("alarmId", id);
                intent1.putExtra("position", position);
                startActivityForResult(intent1,105);
            }
        });

//        fixButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                params.setMargins(100, 0, 0, 0);
//                recyclerView.setLayoutParams(params);
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (data != null) {
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }
                Log.d("101 check", "right");
                String hour = data.getStringExtra("시간");
                String minute = data.getStringExtra("분");
                String helperName = data.getStringExtra("헬퍼이름");
                String helperImageId = data.getStringExtra("헬퍼사진");
                int alarmId = data.getIntExtra("알람",0);

                alarmView alarmView = new alarmView(hour + " : " + minute, helperName, alarmId, helperImageId);
                arrayList.add(alarmView);
                preConfig.writeListPref(this, arrayList);

                mainAdapter.notifyDataSetChanged();
            }

        } else if (requestCode == 102) {
            int credit = data.getIntExtra("credit", 0);
            Log.d("msg2", String.valueOf(credit));
            if (credit == 1) {
                TextView point = findViewById(R.id.credit);
                Log.d("point", String.valueOf(point));
                int newCredit = Integer.parseInt((String) point.getText()) + credit;
                Log.d("newcredit", String.valueOf(newCredit));
                point.setText(String.valueOf(newCredit));
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("msg", "service stop");
        preConfig.writeListPref(this, arrayList);
        preConfig.writeNamePref(this, usename);
        preConfig.writePicturePref(this, strUserPicture);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}