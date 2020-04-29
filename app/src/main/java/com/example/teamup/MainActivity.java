package com.example.teamup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.teamup.login.LoginActivity;
import com.example.teamup.login.PhoneLogin;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth currentUser;
    private Button button;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser=FirebaseAuth.getInstance();

        if(currentUser.getCurrentUser()!=null)
        {
            Log.d(TAG, currentUser.getCurrentUser().getUid());
            startActivity(new Intent(MainActivity.this, TabbedActivityMain.class));
            finish();
        } else {
            startActivity(new Intent(MainActivity.this, PhoneLogin.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser.getCurrentUser()!=null)
        {
            Log.d(TAG, currentUser.getCurrentUser().getUid());
            startActivity(new Intent(MainActivity.this, TabbedActivityMain.class));
            finish();
        } else {
            startActivity(new Intent(MainActivity.this, PhoneLogin.class));
            finish();
        }

    }
}
