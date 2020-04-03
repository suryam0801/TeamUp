package com.example.teamup.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {


    private EditText signUpEmail,signUpPassword,signUpFirstName,signUpLastName;
    private Button signUpButton;
    private TextView loginInSignup;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

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

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName))
                {
                    firebaseAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful())
                                    {
                                        firebaseAuth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {

                                                            Map<String,String> map= new HashMap<>();
                                                            map.put("Email",email);
                                                            map.put("First Name",fName);
                                                            map.put("Last Name",lName);

                                                            firebaseFirestore.collection("Users").document(firebaseAuth.getCurrentUser().getUid()).set(map)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful())
                                                                            {
                                                                                Toast.makeText(SignUpActivity.this,"Verification link has  been sent " +
                                                                                        "to your email, Please verify and Login",Toast.LENGTH_LONG).show();
                                                                                firebaseAuth.signOut();

                                                                                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                                                                                finish();
                                                                            }
                                                                            else {
                                                                                Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                                                firebaseAuth.signOut();
                                                                                firebaseAuth.getCurrentUser().delete();
                                                                            }
                                                                        }
                                                                    });

                                                        }
                                                        else {
                                                            firebaseAuth.signOut();
                                                            firebaseAuth.getCurrentUser().delete();
                                                            Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                        }

                                                    }
                                                });
                                    }
                                    else {
                                        Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }

                                }
                            });
                }
                else {
                    Toast.makeText(SignUpActivity.this,"Enter Valid details",Toast.LENGTH_LONG).show();
                }

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
