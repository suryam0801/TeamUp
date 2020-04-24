package com.example.teamup.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamup.MainActivity;
import com.example.teamup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail,loginPassword;
    private Button loginButton;
    private TextView signupInLogin;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFormat(PixelFormat.RGB_565);
        getSupportActionBar().hide();
        firebaseAuth=FirebaseAuth.getInstance();

        loginButton=findViewById(R.id.loginBtn);
        loginEmail=findViewById(R.id.loginEmail);
        loginPassword=findViewById(R.id.loginPassword);
        signupInLogin=findViewById(R.id.signupInLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=loginEmail.getText().toString();
                String password=loginPassword.getText().toString();
                Log.d("LoginActivity: ", email + ", " + password);
                if(!TextUtils.isEmpty(email)&& !TextUtils.isEmpty(password))

                {
                    firebaseAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        Log.d("LoginActivity: ", "DONEEEEE");
                                        if(firebaseAuth.getCurrentUser().isEmailVerified())
                                        {

                                            String token_id= FirebaseInstanceId.getInstance().getToken();
                                            String current_id=firebaseAuth.getCurrentUser().getUid();
                                            Map<String,Object> tokenMap=new HashMap<>();
                                            tokenMap.put("token_id",token_id);
                                            db.collection("Users").document(current_id).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });


                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(LoginActivity.this,"Verify your email and login",Toast.LENGTH_LONG).show();
                                            firebaseAuth.signOut();
                                        }



                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }
                else {
                    Toast.makeText(LoginActivity.this,"Enter Valid detials",Toast.LENGTH_LONG).show();

                }


            }
        });

        signupInLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });

    }
}
