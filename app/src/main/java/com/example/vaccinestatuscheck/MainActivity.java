package com.example.vaccinestatuscheck;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    TextView addButton;
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
        if (point.getText() != "0") {
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
//        for(int i=0; i<arrayList.size(); i++){
//            Log.d("list", String.valueOf(arrayList.get(i).alarmTime));
//        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        TextView intro = findViewById(R.id.intro);
        TextView intro2 = findViewById(R.id.intro2);
        //텅 화면 제어
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            Log.d("list0", String.valueOf(arrayList.size()));
            intro.setVisibility(View.VISIBLE);
            intro2.setVisibility(View.VISIBLE);
        } else if(arrayList.size() == 0){
            intro.setVisibility(View.VISIBLE);
            intro2.setVisibility(View.VISIBLE);
        }else {
            intro.setVisibility(View.INVISIBLE);
            intro2.setVisibility(View.INVISIBLE);
            Log.d("listx", String.valueOf(arrayList.size()));
            for (int i = 0; i < arrayList.size(); i++) {
                Log.d("list", String.valueOf(arrayList.get(i).alarmTime));
            }
        }
        mainAdapter = new alarmViewAdaptor(arrayList);
        recyclerView.setAdapter(mainAdapter);
        addButton = findViewById(R.id.fixButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MakeAlarmActivity.class);
                startActivityForResult(intent, 101);
            }
        });

        //수정 기능
        mainAdapter.setOnItemClickListener(new alarmViewAdaptor.OnItemClickListenr() {
            @Override
            public void onItemClick(View v, int position) {
                int id = mainAdapter.items.get(position).alarmId;
                Intent intent1 = new Intent(getApplicationContext(), FixAlarmAcitivity.class);
                String friendName = mainAdapter.items.get(position).helperName;
                String friendImage = mainAdapter.items.get(position).helperImageId;
                String message = mainAdapter.items.get(position).message;
                String friendId = mainAdapter.items.get(position).friendId;
                Log.d("클릭", friendName);
                Log.d("클릭 헬퍼id", friendId);
                intent1.putExtra("이름", friendName);
                intent1.putExtra("사진", friendImage);
                intent1.putExtra("알람", id);
                intent1.putExtra("위치", position);
                intent1.putExtra("메세지", message);
                intent1.putExtra("헬퍼id", friendId);
                startActivityForResult(intent1, 105);
            }
        });

        mainAdapter.setOnItemClickListener(new alarmViewAdaptor.OnItemClickListenr2() {
            @Override
            public void onItemClick(View v, int position) {
                intro.setVisibility(View.VISIBLE);
                intro2.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            TextView intro = findViewById(R.id.intro);
            TextView intro2 = findViewById(R.id.intro2);
            if (data != null) {
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }

                if (arrayList.size() == 0) {
                    intro.setVisibility(View.INVISIBLE);
                    intro2.setVisibility(View.INVISIBLE);
                }

                Log.d("101 check", "right");
                int hour = data.getIntExtra("시간", 0);
                int minute = data.getIntExtra("분", 0);
                String helperName = data.getStringExtra("헬퍼이름");
                String helperImageId = data.getStringExtra("헬퍼사진");
                String message = data.getStringExtra("메세지");
                String friendId = data.getStringExtra("헬퍼id");
                Log.d("헬퍼id", friendId);
                String dayNight = null;
                int alarmId = data.getIntExtra("알람", 0);
                if (hour > 12) {
                    dayNight = "PM";
                    hour = hour - 12;
                } else {
                    dayNight = "AM";
                }
                if (minute < 10) {
                    alarmView alarmView = new alarmView(dayNight,hour + ":" + "0" + minute, helperName, alarmId, helperImageId, message, friendId);
                    arrayList.add(alarmView);
                } else {
                    alarmView alarmView = new alarmView(dayNight,hour + ":" + minute, helperName, alarmId, helperImageId, message, friendId);
                    arrayList.add(alarmView);
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    Log.d("list", String.valueOf(arrayList.get(i).alarmTime));
                }
                //정렬
                Collections.sort(arrayList, new Comparator<alarmView>() {
                    @Override
                    public int compare(alarmView o1, alarmView o2) {
                        if (o1.alarmId > o2.alarmId) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
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
        else if (requestCode == 105) {
            Log.d("msg", "105");
            if (data != null) {
                int hour = data.getIntExtra("시간", 0);
                int minute = data.getIntExtra("분", 0);
                int position = data.getIntExtra("위치", 0);
                Log.d("position", String.valueOf(position));
                String helperName = data.getStringExtra("헬퍼이름");
                String helperImageId = data.getStringExtra("헬퍼사진");
                String message = data.getStringExtra("메세지");
                String friendId = data.getStringExtra("헬퍼id");
                int newAlarmId = data.getIntExtra("알람", 0);
                String dayNight = null;

                if (hour > 12) {
                    dayNight = "PM";
                    hour = hour - 12;
                } else {
                    dayNight = "AM";
                }

                if (minute < 10) {
                    alarmView alarmView = new alarmView(dayNight,hour + ":" + "0" + minute, helperName, newAlarmId, helperImageId, message, friendId);
                    arrayList.set(position, alarmView);
                    Collections.sort(arrayList, new Comparator<alarmView>() {
                        @Override
                        public int compare(alarmView o1, alarmView o2) {
                            if (o1.alarmId > o2.alarmId) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                    preConfig.writeListPref(this, arrayList);
                    mainAdapter.notifyDataSetChanged();

                } else {
                    alarmView alarmView = new alarmView(dayNight,":" + minute, helperName, newAlarmId, helperImageId, message, friendId);
                    arrayList.set(position, alarmView);
                    Collections.sort(arrayList, new Comparator<alarmView>() {
                        @Override
                        public int compare(alarmView o1, alarmView o2) {
                            if (o1.alarmId > o2.alarmId) {
                                return 1;
                            } else {
                                return -1;
                            }
                        }
                    });
                    preConfig.writeListPref(this, arrayList);
                    mainAdapter.notifyDataSetChanged();
                }
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