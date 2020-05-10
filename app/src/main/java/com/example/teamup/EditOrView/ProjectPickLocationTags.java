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

public class ProjectPickLocationTags extends AppCompatActivity {

    private List<String> locationTagList = new ArrayList<>();
    private Button finalizeLocationTag, locationTagAdd;
    private FirebaseFirestore db;
    private ChipGroup chipGroup;
    private EditText locationTagEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_project_pick_location_tags);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        finalizeLocationTag = findViewById(R.id.broadcast_finalize_location_tags);
        chipGroup = findViewById(R.id.broadcast_location_tag_chip_group);
        locationTagEntry = findViewById(R.id.broadcast_location_tags_entry);
        locationTagAdd = findViewById(R.id.broadcast_location_tag_add_button);
        db = FirebaseFirestore.getInstance();

        Log.d("LOCATIONTAGPICKER", "Array data :" + locationTagList);

        loadLocationTag();

        finalizeLocationTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commitNewLocationTags();
            }
        });

        locationTagEntry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(locationTagEntry, InputMethodManager.SHOW_IMPLICIT);
                        locationTagEntry.setShowSoftInputOnFocus(true);
                        locationTagEntry.setText("#");
                        locationTagEntry.setSelection(locationTagEntry.getText().length());
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

        locationTagAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locationTag = locationTagEntry.getText().toString().trim();
                if (!locationTag.isEmpty() && !locationTag.equals("#")) {
                    setTag(locationTag, true);
                }
            }
        });
    }

    public void commitNewLocationTags(){
        User user = SessionStorage.getUser(ProjectPickLocationTags.this);
        user.setLocationTags(locationTagList);
        SessionStorage.saveUser(ProjectPickLocationTags.this, user);

        db.collection("Users").document(user.getUserId()).update("locationTags", locationTagList);
        db.collection("Tags").document("Location").update("locationTags", FieldValue.arrayUnion(locationTagList.toArray()));

        Intent intent = new Intent(ProjectPickLocationTags.this, EditOrViewProfile.class);
        intent.putStringArrayListExtra("locationTagList", (ArrayList<String>) locationTagList);
        intent.putExtra("flag", "owner");
        startActivity(intent);
        finish();
    }

    private void setTag(final String name, final Boolean newTag) {
        List<String> userLocTagList = SessionStorage.getUser(ProjectPickLocationTags.this).getLocationTags();
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

        if (userLocTagList.contains(name) || newTag == true) {
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
            chip.setTextColor(Color.WHITE);
            locationTagList.add(name);
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
                    locationTagList.remove(chip.getText().toString());
                } else {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
                    chip.setTextColor(Color.WHITE);
                    locationTagList.add(chip.getText().toString());
                }
                Log.d("INTEREST TAG PICKER", "INTEREST TAG LIST: " + locationTagList.toString());
            }
        });

        chipGroup.addView(chip);
        locationTagEntry.setText("#");
        locationTagEntry.setSelection(locationTagEntry.getText().length());
    }

    private void loadLocationTag() {
        db.collection("Tags")
                .document("Location")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<String> tempList = (List<String>) document.get("locationTags");
                                Log.d("LOCATIONTAGPICKER", "Array data :" + locationTagList);

                                Collections.sort(tempList, new Comparator<String>() {
                                    @Override
                                    public int compare(String s1, String s2) {
                                        return s1.compareToIgnoreCase(s2);
                                    }
                                });

                                for (String loc : tempList) {
                                    setTag(loc, false);
                                }
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
