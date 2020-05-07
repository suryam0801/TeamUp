package com.example.teamup.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teamup.R;
import com.example.teamup.TabbedActivityMain;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class InterestTagPicker extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private Button register;
    private String tempLoc, fName, lName, userId, downloadUri, contact;
    private List<String> skillSet, locationTags;
    private EditText interestTagsEntry;
    private User user;
    private ChipGroup chipGroup;
    private Button interestTagAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_skills_picker);

        register = findViewById(R.id.registerButton);
        chipGroup = findViewById(R.id.interest_tag_chip_group);
        interestTagsEntry = findViewById(R.id.interest_tags_entry);
        interestTagAdd = findViewById(R.id.interest_tag_add_button);

        skillSet = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        fName = getIntent().getStringExtra("fName");
        lName = getIntent().getStringExtra("lName");
        tempLoc = getIntent().getStringExtra("locationTags");
        downloadUri = getIntent().getStringExtra("uri");
        contact = getIntent().getStringExtra("contact");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerFunction();
            }
        });

        interestTagAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String interestTag = interestTagsEntry.getText().toString();
                if (!interestTag.isEmpty()) {
                    setTag(interestTag);
                }
            }
        });

    }

    private void setTag(String name) {
        final Chip chip = new Chip(this);
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()
        );
        chip.setPadding(
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 3,
                        getResources().getDisplayMetrics()
                ),
                paddingDp, paddingDp, paddingDp);
        chip.setText(name);
        chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected);
        chip.setCloseIconResource(R.drawable.ic_clear_black_24dp);
        chip.setCloseIconEnabled(true);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipGroup.removeView(chip);

            }
        });
        chipGroup.addView(chip);
        interestTagsEntry.setText("#");
    }

    public void registerFunction() {

        if (!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName)) {
            userId = firebaseAuth.getInstance().getCurrentUser().getUid();
            addUserToCollection();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(fName + " " + lName)
                    .build();
            firebaseAuth.getCurrentUser().updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(InterestTagPicker.this, "User Registered Successfully", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(InterestTagPicker.this, TabbedActivityMain.class));
                                finish();
                            } else {
                                Toast.makeText(InterestTagPicker.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                firebaseAuth.signOut();
                                firebaseAuth.getCurrentUser().delete();
                            }
                        }
                    });
        } else {
            Toast.makeText(InterestTagPicker.this, "Enter Valid details", Toast.LENGTH_LONG).show();
        }
    }

    private void addUserToCollection() {

        String token_id = FirebaseInstanceId.getInstance().getToken();

        if (downloadUri != null) {
            user = new User(fName, lName, contact, skillSet.toString(), tempLoc, userId, downloadUri, 0, 0, 0, token_id);
        } else {
            user = new User(fName, lName, contact, skillSet.toString(), tempLoc, userId, "default", 0, 0, 0, token_id);
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
