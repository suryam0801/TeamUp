package com.example.teamup.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.teamup.EditOrView.EditOrViewProfile;
import com.example.teamup.R;
import com.example.teamup.TabbedActivityMain;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SkillsPicker extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private BubblePicker bubblePicker;
    private String[] skills = {"Sports", "Gym", "Yoga", "Cooking", "Design", "Volunteering", "Event Management", "Writing", "Programming", "Public Speaking"
            , "Photography", "Music", "Art", "Acting", "Fashion", "Architecture", "Dance", "gaming", "Teaching", "Animals", "Comedy", "Automobiles", "Film"};
    private Button register;
    private String loc, fName, lName, userId,downloadUri,contact;
    private List<String> skillSet;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_skills_picker);

        register = findViewById(R.id.registerButton);
        skillSet = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        fName = getIntent().getStringExtra("fName");
        lName = getIntent().getStringExtra("lName");
        loc = getIntent().getStringExtra("loc");
        downloadUri = getIntent().getStringExtra("uri");
        contact = getIntent().getStringExtra("contact");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerFunction();
            }
        });

        bubblePicker = findViewById(R.id.skills_bubble_picker);

        bubblePicker.setBubbleSize(30);
        bubblePicker.setCenterImmediately(true);
        bubblePicker.setMaxSelectedCount(4);
        bubblePicker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return skills.length;
            }

            @NotNull
            @Override
            public PickerItem getItem(int i) {
                final int min = 1;
                final int max = 3;
                final int random = new Random().nextInt((max - min) + 1) + min;

                PickerItem item = new PickerItem();

/*                switch (random) {
                    case 1:
                        item.setGradient(new BubbleGradient(Color.parseColor("#FFE9E9"), Color.parseColor("#FF6161"), BubbleGradient.VERTICAL));
                        break;
                    case 2:
                        item.setGradient(new BubbleGradient(Color.parseColor("#E0FDFF"), Color.parseColor("#36D1DC"), BubbleGradient.VERTICAL));
                        break;
                    case 3:
                        item.setGradient(new BubbleGradient(Color.parseColor("#E0FFF1"), Color.parseColor("#11F692"), BubbleGradient.VERTICAL));
                        break;
                    *//*case 4:
                        item.setGradient(new BubbleGradient(Color.parseColor("#E4CCFF"), Color.parseColor("#953AFA"), BubbleGradient.VERTICAL));
                        break;*//*
                }*/

                item.setTitle(skills[i]);
                item.setColor(Color.parseColor("#F3F3F3"));
                item.setTextColor(ContextCompat.getColor(SkillsPicker.this, android.R.color.black));
                return item;
            }
        });

        bubblePicker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem item) {
                bubblePicker.setBackgroundColor(Color.parseColor("#158BF1"));
                skillSet.add(item.getTitle());
                Log.d("SKILLSPICKERACTIVITY", skillSet.toString());
            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem item) {
                skillSet.remove(item.getTitle());
                Log.d("SKILLSPICKERACTIVITY", skillSet.toString());
            }
        });


    }

    public void registerFunction(){

        if(!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName))
        {
            userId = firebaseAuth.getInstance().getCurrentUser().getUid();
            addUserToCollection();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(fName+" "+lName)
                    .build();
            firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(SkillsPicker.this, "User Registered Successfully", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(SkillsPicker.this, TabbedActivityMain.class));
                                finish();
                            }
                            else {
                                Toast.makeText(SkillsPicker.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                firebaseAuth.signOut();
                                firebaseAuth.getCurrentUser().delete();
                            } }
                    });
        }
        else {
            Toast.makeText(SkillsPicker.this,"Enter Valid details",Toast.LENGTH_LONG).show();
        }
    }

    private void addUserToCollection() {

        String token_id = FirebaseInstanceId.getInstance().getToken();

        if (downloadUri != null) {
            user = new User(fName, lName, contact, skillSet.toString(), loc, userId, downloadUri, 0, 0, 0, token_id);
        } else {
            user = new User(fName, lName, contact, skillSet.toString(), loc, userId, "default", 0, 0, 0, token_id);
        }

        db.collection("Users")
                .document(userId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to create user", Toast.LENGTH_LONG).show();
                    }
                });
    }

}
