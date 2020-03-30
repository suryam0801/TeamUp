package com.example.teamup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

public class MyProjectsView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_projects_view);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        TextView projectNameDisplay = (TextView)findViewById(R.id.projectNameDisplay);

    }
}