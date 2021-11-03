package com.creadle.vaccinesos;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    TextView addButton;
    Button fixButton;
    Button loginButton;
    String shared = "file";
    Switch aSwitch;
    public ArrayList<alarmView> arrayList;
    private alarmViewAdaptor mainAdapter;
    public static Context context;
    String strUserPicture, usename;
    Boolean start;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        usename = intent.getStringExtra("name");
        strUserPicture = intent.getStringExtra("profileImg");
        Log.d("msg", "service start");

        TextView point = findViewById(R.id.credit);
        TextView point_p = findViewById(R.id.textView5);
        aSwitch = findViewById(R.id.alarmSwitch);
//        Log.d("switch", String.valueOf(aSwitch));

        if (point.getText() != "0") {
            int updateCredit = preConfig.readCreditPref(this);
            point.setText(String.valueOf(updateCredit));
        }
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(100);
        Animation anim2 = new AlphaAnimation(1.0f, 0.0f);
        anim2.setDuration(100);

        int credit = intent.getIntExtra("credit", 0);
        int alarmId = intent.getIntExtra("alarmId", 0);

        Log.d("msg", String.valueOf(credit));

        AlertDialog.Builder credit_noti = new AlertDialog.Builder(this);
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                credit_noti.setMessage("메디컬 체크를 통해서 획득한 포인트입니다. " +
                        "독거 노인을 위한 기부에 쓰일 예정이며, " +
                        "정식 서비스 런칭 시 기부 기능을 통해 포인트를 사용할 수 있습니다.");
                credit_noti.setPositiveButton("확인", null);
                AlertDialog aCredit = credit_noti.create();
                aCredit.setIcon(R.drawable.icon2);
                aCredit.setTitle("기부 포인트");
                aCredit.show();
            }
        });

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
//            Log.d("list", String.valueOf(arrayList.get(i).switchBoolean));
//            Log.d("list", String.valueOf(arrayList.get(i).alarmId));
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
        } else if (arrayList.size() == 0) {
            intro.setVisibility(View.VISIBLE);
            intro2.setVisibility(View.VISIBLE);
        } else {
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

        if (credit == 1) {
            int newCredit = Integer.parseInt((String) point.getText()) + credit;
            preConfig.writeCreditPref(this, newCredit);
            point.setText(String.valueOf(newCredit));
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha);
            point.startAnimation(animation);
            point_p.startAnimation(animation);

            Log.d("switch check", "yes");
            for (int i = 0; i < arrayList.size(); i++) {
                if (alarmId == arrayList.get(i).alarmId) {
                    arrayList.get(i).switchBoolean = false;
                    break;
                }
            }
            preConfig.writeListPref(this, arrayList);
            mainAdapter.notifyDataSetChanged();
        }

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
                int hour = mainAdapter.items.get(position).hour;
                int minute = mainAdapter.items.get(position).minute;
                Log.d("클릭", String.valueOf(id));
                intent1.putExtra("이름", friendName);
                intent1.putExtra("사진", friendImage);
                intent1.putExtra("알람", id);
                intent1.putExtra("위치", position);
                intent1.putExtra("메세지", message);
                intent1.putExtra("헬퍼id", friendId);
                intent1.putExtra("시간", hour);
                intent1.putExtra("분", minute);
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

        LinearLayout alarmLayout = findViewById(R.id.alarmView_layout);
        mainAdapter.setOnItemClickListener(new alarmViewAdaptor.OnItemClickListenr3() {
            @Override
            public void onItemClick(View v, int position) {
                mainAdapter.items.get(position).aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(buttonView.isChecked()){
                            Log.d("checkOnMain", String.valueOf(mainAdapter.items.get(position).alarmId));
                        }
                    }
                });
            }
        });

//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        Log.d("finish", "yes");
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
                aSwitch = findViewById(R.id.alarmSwitch);
                int hour = data.getIntExtra("시간", 0);
                int minute = data.getIntExtra("분", 0);
                String helperName = data.getStringExtra("헬퍼이름");
                String helperImageId = data.getStringExtra("헬퍼사진");
                String message = data.getStringExtra("메세지");
                String friendId = data.getStringExtra("헬퍼id");
                Boolean switchChecked = data.getBooleanExtra("스위치", false);
                Switch aSwitch = findViewById(R.id.alarmSwitch);
                Log.d("add_alarm_switch", String.valueOf(switchChecked));
                String dayNight = null;
                int alarmId = data.getIntExtra("알람", 0);
                if (hour > 12) {
                    dayNight = "오후";
                    hour = hour - 12;
                } else {
                    dayNight = "오전";
                }
                if (minute < 10) {
                    alarmView alarmView = new alarmView(dayNight + " ", hour + ":" + "0" + minute, helperName, alarmId, helperImageId, message, friendId, switchChecked, hour, minute);
                    arrayList.add(alarmView);
                } else {
                    alarmView alarmView = new alarmView(dayNight + " ", hour + ":" + minute, helperName, alarmId, helperImageId, message, friendId, switchChecked, hour, minute);
                    arrayList.add(alarmView);
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    Log.d("list", String.valueOf(arrayList.get(i).alarmTime));
                    Log.d("list", String.valueOf(arrayList.get(i).switchBoolean));
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
                for (int i = 0; i < arrayList.size(); i++) {
                    Log.d("list_sorted", String.valueOf(arrayList.get(i).alarmTime));
                    Log.d("list_sorted", String.valueOf(arrayList.get(i).switchBoolean));
                }
                preConfig.writeListPref(this, arrayList);
                mainAdapter.notifyDataSetChanged();
            }
//        } else if (requestCode == 102) {
//            int credit = data.getIntExtra("credit", 0);
//            int alarmId = data.getIntExtra("alarmId", 0);
//            Log.d("msg2", String.valueOf(credit));
//            if (credit == 1) {
////                TextView point = findViewById(R.id.credit);
////                Log.d("point", String.valueOf(point));
////                int newCredit = Integer.parseInt((String) point.getText()) + credit;
////                Log.d("newcredit", String.valueOf(newCredit));
////                point.setText(String.valueOf(newCredit));
//                Log.d("switch check", "yes");
//                for (int i = 0; i < arrayList.size(); i++) {
//                    Log.d("switch check", "yes1");
//                    if (alarmId == arrayList.get(i).alarmId) {
//                        Log.d("switch check", "yes2");
//                        arrayList.get(i).switchBoolean = false;
//                        break;
//                    }
//                }
//                preConfig.writeListPref(this, arrayList);
//                mainAdapter.notifyDataSetChanged();
//            }
        } else if (requestCode == 105) {
            if (data != null) {
                int hour = data.getIntExtra("시간", 0);
                int minute = data.getIntExtra("분", 0);
                int position = data.getIntExtra("위치", 0);
                String helperName = data.getStringExtra("헬퍼이름");
                String helperImageId = data.getStringExtra("헬퍼사진");
                String message = data.getStringExtra("메세지");
                String friendId = data.getStringExtra("헬퍼id");
                Boolean switchChecked = data.getBooleanExtra("스위치", false);
                int newAlarmId = data.getIntExtra("뉴알람", 0);
                String dayNight = null;

                if (hour > 12) {
                    dayNight = "오후";
                    hour = hour - 12;
                } else {
                    dayNight = "오전";
                }

                if (minute < 10) {
                    alarmView alarmView = new alarmView(dayNight + " ", hour + ":" + "0" + minute, helperName, newAlarmId, helperImageId, message, friendId, switchChecked, hour, minute);
                    arrayList.set(position, alarmView);
//                    Collections.sort(arrayList, new Comparator<alarmView>() {
//                        @Override
//                        public int compare(alarmView o1, alarmView o2) {
//                            if (o1.alarmId > o2.alarmId) {
//                                return 1;
//                            } else {
//                                return -1;
//                            }
//                        }
//                    });
                    preConfig.writeListPref(this, arrayList);
                    mainAdapter.notifyDataSetChanged();

                } else {
                    alarmView alarmView = new alarmView(dayNight + " ", hour + ":" + minute, helperName, newAlarmId, helperImageId, message, friendId, switchChecked, hour, minute);
                    arrayList.set(position, alarmView);
//                    Collections.sort(arrayList, new Comparator<alarmView>() {
//                        @Override
//                        public int compare(alarmView o1, alarmView o2) {
//                            if (o1.alarmId > o2.alarmId) {
//                                return 1;
//                            } else {
//                                return -1;
//                            }
//                        }
//                    });
                    preConfig.writeListPref(this, arrayList);
                    mainAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("onStop", "service stop");
        for(int i=0; i<arrayList.size(); i++){
            Log.d("list", String.valueOf(arrayList.get(i).switchBoolean));
        }
        preConfig.writeListPref(this, arrayList);
        preConfig.writeNamePref(this, usename);
        preConfig.writePicturePref(this, strUserPicture);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onStart", "service start");
        preConfig.readListFromPref(this);
        for(int i=0; i<arrayList.size(); i++){
            Log.d("list", String.valueOf(arrayList.get(i).switchBoolean));
        }
    }

}