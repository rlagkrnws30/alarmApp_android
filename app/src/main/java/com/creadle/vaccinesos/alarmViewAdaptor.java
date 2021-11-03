package com.creadle.vaccinesos;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class alarmViewAdaptor extends RecyclerView.Adapter<alarmViewAdaptor.ViewHolder> {

    ArrayList<alarmView> items = new ArrayList<alarmView>();

    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public alarmViewAdaptor(ArrayList<alarmView> items) {
        this.items = items;
    }

    public void addItem(alarmView item) {
        items.add(item);
    }

    public void setItems(ArrayList<alarmView> items) {
        this.items = items;
    }

    public alarmView getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, alarmView item) {
        items.set(position, item);
    }

    @NonNull
    @Override
    public alarmViewAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.alarm_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull alarmViewAdaptor.ViewHolder holder, int position) {
        alarmView item = items.get(position);
        holder.setItem(item);
        Context context = holder.itemView.getContext();
        if (viewBinderHelper != null) {
            viewBinderHelper.bind(holder.swipeRelativeLayout, String.valueOf(item.alarmId));
        }
        //false, true flag 확인,
        if (item.switchBoolean != null) {
            if (item.switchBoolean == true) {
                Log.d("switch check", "true_holder");
                holder.aSwitch.setChecked(true);
                item.switchBoolean = true;
                holder.aLayout.setBackgroundResource(R.drawable.onalarm_bg);
                holder.alarmTime.setTextColor(Color.parseColor("#FFFFFF"));
                holder.dayNight.setTextColor(Color.parseColor("#FFFFFF"));
                holder.helperName.setTextColor(Color.parseColor("#FFFFFF"));
                holder.txt.setTextColor(Color.parseColor("#FFFFFF"));
                preConfig.writeListPref((MainActivity) context, items);

            } else if (item.switchBoolean == false){
                Log.d("switch check", "false_holder");
                holder.aSwitch.setChecked(false);
                item.switchBoolean = false;
                holder.aLayout.setBackgroundResource(R.drawable.unalarm_bg);
                holder.alarmTime.setTextColor(Color.parseColor("#E8E8E8"));
                holder.dayNight.setTextColor(Color.parseColor("#E8E8E8"));
                holder.helperName.setTextColor(Color.parseColor("#E8E8E8"));
                holder.txt.setTextColor(Color.parseColor("#E8E8E8"));
                preConfig.writeListPref((MainActivity) context, items);
            }
        }

        holder.aLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListner.onItemClick(v, position);
            }
        });
        holder.aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.switchBoolean == true){
                    holder.aSwitch.setChecked(false);
                    item.switchBoolean = false;
                    holder.aLayout.setBackgroundResource(R.drawable.unalarm_bg);
                    holder.alarmTime.setTextColor(Color.parseColor("#E8E8E8"));
                    holder.dayNight.setTextColor(Color.parseColor("#E8E8E8"));
                    holder.helperName.setTextColor(Color.parseColor("#E8E8E8"));
                    holder.txt.setTextColor(Color.parseColor("#E8E8E8"));
                    preConfig.writeListPref((MainActivity) context, items);
                    Intent receiver = new Intent(holder.itemView.getContext(), alarmReceiver.class);
                    AlarmManager alarmManager;
                    alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Log.d("msg", String.valueOf(item.alarmId));
                    PendingIntent alarmCancle = PendingIntent.getBroadcast(context, item.alarmId, receiver, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(alarmCancle);
                }else {
                    holder.aSwitch.setChecked(true);
                    item.switchBoolean = true;
                    holder.aLayout.setBackgroundResource(R.drawable.onalarm_bg);
                    holder.alarmTime.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.dayNight.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.helperName.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.txt.setTextColor(Color.parseColor("#FFFFFF"));
                    preConfig.writeListPref((MainActivity) context, items);
                    Intent receiver = new Intent(holder.itemView.getContext(), alarmReceiver.class);
                    AlarmManager alarmManager;
                    alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    receiver.putExtra("alarmId", item.alarmId);
                    receiver.putExtra("friendId", item.friendId);
                    receiver.putExtra("message", item.message);
                    Log.d("msg", String.valueOf(item.alarmId));
                    PendingIntent reOperation = PendingIntent.getBroadcast(context, item.alarmId, receiver, PendingIntent.FLAG_UPDATE_CURRENT);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, item.hour);
                    calendar.set(Calendar.MINUTE, item.minute);
                    if ((Calendar.getInstance().after(calendar))) {
                        calendar.add(Calendar.DATE, 1);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, reOperation);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), reOperation);
                    }
                }
            }
        });
//        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (buttonView.isChecked()) {
//                    Log.d("checkOn", String.valueOf(item.alarmId));
//                    item.switchBoolean = true;
//                    preConfig.writeListPref((MainActivity) context, items);
////                    mListner3.onItemClick(buttonView, position);
//                    Intent receiver = new Intent(holder.itemView.getContext(), alarmReceiver.class);
//                    AlarmManager alarmManager;
//                    alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                    receiver.putExtra("alarmId", item.alarmId);
//                    receiver.putExtra("friendId", item.friendId);
//                    receiver.putExtra("message", item.message);
//                    Log.d("msg", String.valueOf(item.alarmId));
//                    PendingIntent reOperation = PendingIntent.getBroadcast(context, item.alarmId, receiver, PendingIntent.FLAG_UPDATE_CURRENT);
//
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.set(Calendar.HOUR_OF_DAY, item.hour);
//                    calendar.set(Calendar.MINUTE, item.minute);
//                    if ((Calendar.getInstance().after(calendar))) {
//                        calendar.add(Calendar.DATE, 1);
//                    }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, reOperation);
//                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), reOperation);
//                    }
//                    holder.aLayout.setBackgroundResource(R.drawable.onalarm_bg);
//                    holder.alarmTime.setTextColor(Color.parseColor("#FFFFFF"));
//                    holder.dayNight.setTextColor(Color.parseColor("#FFFFFF"));
//                    holder.helperName.setTextColor(Color.parseColor("#FFFFFF"));
//                    holder.txt.setTextColor(Color.parseColor("#FFFFFF"));
//
//                } else{
//                    Log.d("checkOff", String.valueOf(item.alarmId));
//                    item.switchBoolean = false;
//                    preConfig.writeListPref((MainActivity) context, items);
////                    mListner3.onItemClick(buttonView, position);
//                    Intent receiver = new Intent(holder.itemView.getContext(), alarmReceiver.class);
//                    AlarmManager alarmManager;
//                    alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                    Log.d("msg", String.valueOf(item.alarmId));
//                    PendingIntent alarmCancle = PendingIntent.getBroadcast(context, item.alarmId, receiver, PendingIntent.FLAG_UPDATE_CURRENT);
//                    alarmManager.cancel(alarmCancle);
//                    holder.aLayout.setBackgroundResource(R.drawable.unalarm_bg);
//                    holder.alarmTime.setTextColor(Color.parseColor("#E8E8E8"));
//                    holder.dayNight.setTextColor(Color.parseColor("#E8E8E8"));
//                    holder.helperName.setTextColor(Color.parseColor("#E8E8E8"));
//                    holder.txt.setTextColor(Color.parseColor("#E8E8E8"));
//                }
//            }
//        });
        //휴지통 버튼으로 변경해서 삭제 기능으로 대체
        //지정된 alarmID를 다시 pendingintent와 매칭해서 service 종료시키기
        holder.delete.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 Intent receiver = new Intent(holder.itemView.getContext(), alarmReceiver.class);
                                                 Context context = holder.itemView.getContext();
                                                 AlarmManager alarmManager;
                                                 alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                                 Log.d("msg", String.valueOf(item.alarmId));
                                                 PendingIntent alarmCancle = PendingIntent.getBroadcast(context, item.alarmId, receiver, PendingIntent.FLAG_UPDATE_CURRENT);
                                                 alarmManager.cancel(alarmCancle);
                                                 remove(holder.getAdapterPosition());
                                                 Log.d("itemSize", String.valueOf(getItemCount()));
                                                 if (getItemCount() == 0) {
                                                     mListner2.onItemClick(v, position);
                                                 }
                                             }
                                         }
        );
    }

    public interface OnItemClickListenr {
        void onItemClick(View v, int position);
    }


    public interface OnItemClickListenr2 {
        void onItemClick(View v, int position);
    }

    public interface OnItemClickListenr3 {
        void onItemClick(View v, int position);
    }


    public OnItemClickListenr mListner = null;
    public OnItemClickListenr2 mListner2 = null;
    public OnItemClickListenr3 mListner3 = null;

    public void setOnItemClickListener(OnItemClickListenr listener) {
        this.mListner = listener;
    }

    public void setOnItemClickListener(OnItemClickListenr2 listener) {
        this.mListner2 = listener;
    }

    public void setOnItemClickListener(OnItemClickListenr3 listener) {
        this.mListner3 = listener;
    }


    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void remove(int position) {
        try {
            items.remove(position);
            notifyItemRemoved(position);
            notifyDataSetChanged();

        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayNight;
        TextView alarmTime;
        TextView helperName;
        TextView txt;
        LinearLayout delete;
        Switch aSwitch;
        ImageView helperImage;
        LinearLayout aLayout;
        SwipeRevealLayout swipeRelativeLayout;

        //생성자
        public ViewHolder(View itemView) {
            super(itemView);
            dayNight = itemView.findViewById(R.id.alarmTimeD);
            txt = itemView.findViewById(R.id.textView2);
            alarmTime = itemView.findViewById(R.id.alarmTime);
            helperName = itemView.findViewById(R.id.helperName);
            delete = itemView.findViewById(R.id.deleteAlarm);
            helperImage = itemView.findViewById(R.id.helperImage);
            aSwitch = itemView.findViewById(R.id.alarmSwitch);
            aLayout = itemView.findViewById(R.id.alarmView_layout);
            swipeRelativeLayout = itemView.findViewById(R.id.swipeRevealLayout);
        }

        public void setItem(alarmView item) {
            dayNight.setText(item.getDayNight());
            alarmTime.setText(item.getAlarmTime());
            helperName.setText(item.getHelperName());
            Glide.with(itemView).load(item.getHelperImageId()).into(helperImage);
//            if (item.switchBoolean = true){
//                aSwitch.setChecked(true);
//            }else{
//                aSwitch.setChecked(false);
//            }
        }
    }
}
