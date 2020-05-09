package com.example.teamup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.teamup.model.Applicant;
import com.example.teamup.model.Broadcast;
import com.example.teamup.model.Worker;
import com.example.teamup.model.User;
import com.google.gson.Gson;

public class SessionStorage {

    private static final String TAG = "SessionStorage";
    public static final String PREF_NAME= "Project";

    public static void saveProject(Activity activity, Broadcast broadcast)
    {
        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME,Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String string = new Gson().toJson(broadcast);
        Log.d(TAG, "saveProject: "+string);
        editor.putString("1", "1");
        editor.putString("project", string);
        editor.apply();
    }

    public static Broadcast getProject(Activity activity)
    {
        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME,Activity.MODE_PRIVATE);
        String string = sharedPref.getString("project","1234");
        Log.d(TAG, "getProject: "+string);
        return new Gson().fromJson(string, Broadcast.class);
    }

    public static void saveWorker(Activity activity, Worker worker)
    {
        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME,Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String string = new Gson().toJson(worker);
        editor.putString("memberTransfer", string);
        editor.apply();
    }

    public static Worker getWorker(Activity activity)
    {
        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME,Activity.MODE_PRIVATE);
        String string = sharedPref.getString("memberTransfer","1234");
        return new Gson().fromJson(string, Worker.class);
    }

    public static void saveUser(Activity activity, User user)
    {
        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME,Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String string = new Gson().toJson(user);
        editor.putString("tempUser", string);
        editor.apply();
    }

    public static User getUser(Activity activity)
    {
        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME,Activity.MODE_PRIVATE);
        String string = sharedPref.getString("tempUser","1234");
        return new Gson().fromJson(string, User.class);
    }

    public static void saveApplicant(Activity activity, Applicant applicant)
    {
        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME,Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String string = new Gson().toJson(applicant);
        editor.putString("tempApplicant", string);
        editor.apply();
    }

    public static Applicant getApplicant(Activity activity)
    {
        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME,Activity.MODE_PRIVATE);
        String string = sharedPref.getString("tempApplicant","1234");
        return new Gson().fromJson(string, Applicant.class);
    }
}