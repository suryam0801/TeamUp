package com.example.teamup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teamup.Explore.ExploreActivity;
import com.example.teamup.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth currentUser;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button);

        currentUser=FirebaseAuth.getInstance();

        if(currentUser.getCurrentUser()!=null)
        {
            startActivity(new Intent(MainActivity.this, ExploreActivity.class));
            finish();
        } else {
            startActivity(new Intent(MainActivity.this, ExploreActivity.class));
            finish();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser.getCurrentUser()!=null)
        {
            startActivity(new Intent(MainActivity.this, ExploreActivity.class));
            finish();
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }
}
