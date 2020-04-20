package com.example.teamup.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teamup.R;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class GatherUserDetails extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    String primSkill, secSkill, loc, fName, lName, email, password, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_gather_user_details);
        firebaseAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        final EditText primarySkill = findViewById(R.id.primarySkill);
        final EditText secondarySkill = findViewById(R.id.secondarySkill);
        final EditText location = findViewById(R.id.location);
        Button register = findViewById(R.id.registerButton);
        Button profilepicButton = findViewById(R.id.profilePicSetterImage);

        fName = getIntent().getStringExtra("fName");
        lName = getIntent().getStringExtra("lName");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                primSkill = primarySkill.getText().toString();
                secSkill = secondarySkill.getText().toString();
                loc = location.getText().toString();

                registerFunction();
            }
        });
    }

    private void addUserToCollection() {

        User user = new User(fName,lName,email,primSkill,secSkill,loc,userId);

        db.collection("Users")
                .document(userId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(GatherUserDetails.this, "Verification link has  been sent " +
                                "to your email, Please verify and Login", Toast.LENGTH_LONG).show();

                        firebaseAuth.signOut();

                        startActivity(new Intent(GatherUserDetails.this, LoginActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to create user", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void registerFunction(){

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName))
        {
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {

                                userId = firebaseAuth.getInstance().getCurrentUser().getUid();
                                Log.d("GATHERUSERDETAILS: ", userId);
                                addUserToCollection();

                                firebaseAuth.getCurrentUser().sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {

                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(fName+" "+lName)
                                                            .build();

                                                    firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    if(task.isSuccessful()) {

                                                                    }
                                                                    else {
                                                                        Toast.makeText(GatherUserDetails.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                                        firebaseAuth.signOut();
                                                                        firebaseAuth.getCurrentUser().delete();
                                                                    }
                                                                }
                                                            });
                                                }
                                                else {
                                                    firebaseAuth.signOut();
                                                    firebaseAuth.getCurrentUser().delete();
                                                    Toast.makeText(GatherUserDetails.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                                }

                                            }
                                        });
                            }
                            else {
                                Toast.makeText(GatherUserDetails.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(GatherUserDetails.this,"Enter Valid details",Toast.LENGTH_LONG).show();
        }
    }
}
