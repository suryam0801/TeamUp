package com.example.teamup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.teamup.model.Project;

public class ProjectActivity extends AppCompatActivity {

    public static final String TAG="ProjectActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Project project=getIntent().getParcelableExtra("project");
        assert project != null;
        Log.d(TAG, "onCreate: "+project.toString());
    }
}
