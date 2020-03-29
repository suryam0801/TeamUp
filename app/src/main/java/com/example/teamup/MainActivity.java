package com.example.teamup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser=FirebaseAuth.getInstance();




    }


    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser.getCurrentUser()==null)
        {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }


    }
}
