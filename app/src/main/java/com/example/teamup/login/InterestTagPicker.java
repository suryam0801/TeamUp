package com.example.teamup.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.TabbedActivityMain;
import com.example.teamup.model.Tags;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class InterestTagPicker extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private Button register;
    private String tempLoc, fName, lName, userId, downloadUri, contact;
    private List<String> locationTags = new ArrayList<>(), interestTagsList = new ArrayList<>();
    private EditText interestTagsEntry;
    private User user;
    private Tags tags;
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

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        fName = getIntent().getStringExtra("fName");
        lName = getIntent().getStringExtra("lName");
        tempLoc = getIntent().getStringExtra("locationTags");
        downloadUri = getIntent().getStringExtra("uri");
        contact = getIntent().getStringExtra("contact");

        tempLoc = tempLoc.replace("[", "");
        tempLoc = tempLoc.replace("]", "");
        Scanner scan = new Scanner(tempLoc);
        scan.useDelimiter(", ");
        while (scan.hasNext()) {
            locationTags.add(scan.next());
        }
        Log.d("INTERESTTAGPICKER", locationTags.toString());

        interestTagsEntry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(interestTagsEntry, InputMethodManager.SHOW_IMPLICIT);
                        interestTagsEntry.setShowSoftInputOnFocus(true);
                        interestTagsEntry.setText("#");
                        interestTagsEntry.setSelection(interestTagsEntry.getText().length());
                        break;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        break;
                    default:
                        break;
                }
                return true;

            }
        });

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

    private void setTag(final String name) {
        interestTagsList.add(name);
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
                interestTagsList.remove(name);
            }
        });
        chipGroup.addView(chip);
        interestTagsEntry.setText("#");
        interestTagsEntry.setSelection(interestTagsEntry.getText().length());
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
            user = new User(fName, lName, contact, downloadUri, locationTags, interestTagsList, userId, 0, 0, 0, token_id);
            tags = new Tags(locationTags, interestTagsList);
        } else {
            user = new User(fName, lName, contact, "default", locationTags, interestTagsList, userId, 0, 0, 0, token_id);
            tags = new Tags(locationTags, interestTagsList);
        }

        List<String> locationinterest = locationTags;
        HashMap<String, List<String>> List = new HashMap<>();
        List.put("locationTags", locationinterest);

        for (int i = 0; i < locationTags.size(); i++) {
            String loc = locationTags.get(i);
            db.collection("Tags")
                    .document("Location-Interest")
                    .update(loc, FieldValue.arrayUnion(interestTagsList.toArray()))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to create user", Toast.LENGTH_LONG).show();
                }
            });
        }


        List<String> locationlist = locationTags;
        HashMap<String, List<String>> ListLocation = new HashMap<>();
        ListLocation.put("locationTags", locationlist);


        db.collection("Tags")
                .document("Location")
                .update("locationTags", FieldValue.arrayUnion(locationTags.toArray()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to create user", Toast.LENGTH_LONG).show();
            }
        });

        List<String> interestlist = interestTagsList;
        HashMap<String, List<String>> InterestList = new HashMap<>();
        InterestList.put("interestTags", interestlist);

        db.collection("Tags")
                .document("Interest")
                .update("interestTags", FieldValue.arrayUnion(interestlist.toArray()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to create user", Toast.LENGTH_LONG).show();
            }
        });


        db.collection("Users")
                .document(userId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        SessionStorage.saveUser(InterestTagPicker.this, user);
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
