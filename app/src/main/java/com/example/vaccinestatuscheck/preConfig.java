package com.example.vaccinestatuscheck;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class preConfig {

    private static final String LIST_KEY = "list_key";

    public static void writeNamePref(MainActivity context, String userName){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name", userName);
        editor.apply();
    }

    public static void writePicturePref(MainActivity context, String picture){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("picture", picture);
        editor.apply();
    }

    public static void writeCreditPref(MainActivity context, int credit){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("credit", credit);
        editor.apply();
    }

    public static void writeListPref(MainActivity context, ArrayList<alarmView> list){
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(LIST_KEY, jsonString);
        editor.apply();
    }

    public static ArrayList<alarmView> readListFromPref(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String jsonString = pref.getString(LIST_KEY, "");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<alarmView>>() {}.getType();
        ArrayList<alarmView> list = gson.fromJson(jsonString, type);
        
        return list;
    }

    public static String readNamePref(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String usrName = pref.getString("name", "");
        return usrName;
    }

    public static String readPicturePref(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String picture = pref.getString("picture", "");
        return picture;
    }

    public static Integer readCreditPref(Context context){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Integer credit = pref.getInt("credit", 0);
        return credit;
    }
}
