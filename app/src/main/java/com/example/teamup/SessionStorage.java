package com.example.teamup;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.teamup.Explore.Project;
import com.google.gson.Gson;

public class SessionStorage {

    private static final String TAG = "SessionStorage";
    public static final String PREF_NAME= "Project";

    public static void saveProject(Activity activity, Project project){
        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME,Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String string = new Gson().toJson(project);
        Log.d(TAG, "saveProject: "+string);
        editor.putString("1", "1");
        editor.putString("project", string);
        editor.apply();
    }

    public static Project getProject(Activity activity)
    {
        SharedPreferences sharedPref = activity.getSharedPreferences(PREF_NAME,Activity.MODE_PRIVATE);
        String string = sharedPref.getString("project","1234");
        Log.d(TAG, "getProject: "+string);
        return new Gson().fromJson(string, Project.class);
    }
}