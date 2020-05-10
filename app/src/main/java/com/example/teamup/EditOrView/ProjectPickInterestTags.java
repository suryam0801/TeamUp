package com.example.teamup.EditOrView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.teamup.CreateBroadcast.CreateBroadcast;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectPickInterestTags extends AppCompatActivity {

    private List<String> interestTagList = new ArrayList<>(), locationTagList = new ArrayList<>();
    private Button finalizeInterestTag, interestTagAdd;
    private FirebaseFirestore db;
    private ChipGroup chipGroup;
    private EditText interestTagEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_project_pick_interest_tags);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        db = FirebaseFirestore.getInstance();
        finalizeInterestTag = findViewById(R.id.broadcast_finalize_interest_tags);
        chipGroup = findViewById(R.id.broadcast_interest_tag_chip_group);
        interestTagEntry = findViewById(R.id.broadcast_interest_tags_entry);
        interestTagAdd = findViewById(R.id.broadcast_interest_tag_add_button);

        loadInterestTags();

        locationTagList = SessionStorage.getUser(ProjectPickInterestTags.this).getLocationTags();

        finalizeInterestTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitNewInterestTags();
            }
        });

        interestTagEntry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(interestTagEntry, InputMethodManager.SHOW_IMPLICIT);
                        interestTagEntry.setShowSoftInputOnFocus(true);
                        interestTagEntry.setText("#");
                        interestTagEntry.setSelection(interestTagEntry.getText().length());
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

        interestTagAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String interestTag = interestTagEntry.getText().toString();
                if (!interestTag.isEmpty()) {
                    setTag(interestTag, true);
                }
            }
        });

    }

    private void setTag(final String name, final Boolean newTag) {
        List<String> userInterestTags = SessionStorage.getUser(ProjectPickInterestTags.this).getInterestTags();
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

        if (userInterestTags.contains(name) || newTag == true) {
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
            chip.setTextColor(Color.WHITE);
            interestTagList.add(name);
        } else {
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.chip_unselected_gray)));
            chip.setTextColor(Color.BLACK);
        }

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chip.getChipBackgroundColor().getDefaultColor() == -9655041) {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.chip_unselected_gray)));
                    chip.setTextColor(Color.BLACK);
                    interestTagList.remove(chip.getText().toString());
                } else {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
                    chip.setTextColor(Color.WHITE);
                    interestTagList.add(chip.getText().toString());
                }
                Log.d("INTEREST TAG PICKER", "INTEREST TAG LIST: " + interestTagList.toString());
            }
        });

        chipGroup.addView(chip);
        interestTagEntry.setText("#");
        interestTagEntry.setSelection(interestTagEntry.getText().length());
    }

    public void commitNewInterestTags() {
        User user = SessionStorage.getUser(ProjectPickInterestTags.this);
        user.setInterestTags(interestTagList);
        SessionStorage.saveUser(ProjectPickInterestTags.this, user);

        for (String loc : locationTagList) {
            Log.d("INTEREST TAG PICKER", "------------- " + loc + " --------" + interestTagList.toString());
            db.collection("Tags").document("Location-Interest").update(loc, FieldValue.arrayUnion(interestTagList.toArray()));
        }

        db.collection("Users").document(user.getUserId()).update("interestTags", interestTagList);

        Intent intent = new Intent(ProjectPickInterestTags.this, EditOrViewProfile.class);
        intent.putStringArrayListExtra("interestTagList", (ArrayList<String>) locationTagList);
        intent.putExtra("flag", "owner");
        startActivity(intent);
        finish();
    }

    public void loadInterestTags() {
        final List<String> interestTagTemp = new ArrayList<>();
        db.collection("Tags")
                .document("Location-Interest")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                for (String loc : SessionStorage.getUser(ProjectPickInterestTags.this).getLocationTags()) {
                                    List<String> group = (List<String>) document.get(loc);
                                    Log.d(" INTEREST TAG PICKER", "DOCUMENT SNAPSHOT: " + document.toString());
                                    if (group != null) {
                                        for (String in : group) {
                                            if (!interestTagTemp.contains(in)) {
                                                interestTagTemp.add(in);
                                            }
                                        }
                                    }
                                }


                                Collections.sort(interestTagTemp, new Comparator<String>() {
                                    @Override
                                    public int compare(String s1, String s2) {
                                        return s1.compareToIgnoreCase(s2);
                                    }
                                });

                                for (String interest : interestTagTemp)
                                    setTag(interest, false);

                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });
    }

}
