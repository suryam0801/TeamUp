package com.example.teamup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUser.signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });
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
