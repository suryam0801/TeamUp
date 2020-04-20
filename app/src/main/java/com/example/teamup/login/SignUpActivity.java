package com.example.teamup.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {


    private EditText signUpEmail,signUpPassword,signUpFirstName,signUpLastName;
    private Button signUpButton;
    private TextView loginInSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        signUpButton=findViewById(R.id.signUpBtn);
        signUpEmail=findViewById(R.id.signUpEmail);
        signUpPassword=findViewById(R.id.signUpPassword);
        loginInSignup=findViewById(R.id.loginInSignup);
        signUpFirstName=findViewById(R.id.signUpFirstName);
        signUpLastName=findViewById(R.id.signUpLastName);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email=signUpEmail.getText().toString();
                String password=signUpPassword.getText().toString();
                final String fName=signUpFirstName.getText().toString();
                final String lName=signUpLastName.getText().toString();

                Intent intent = new Intent(SignUpActivity.this, GatherUserDetails.class);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("fName", fName);
                intent.putExtra("lName", lName);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        loginInSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });
    }
}
