package com.example.teamup.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teamup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LocationTagPicker extends AppCompatActivity {

    private String loc, fName, lName, userId, downloadUri, contact;
    private List<String> locationTagsList = new ArrayList<>();
    private List<String> loadlocList;
    private Button setInterestTags, locationTagAdd;
    private ChipGroup chipGroup;
    private EditText locationTagEntry;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_tag_picker);

        loadTag();
        fName = getIntent().getStringExtra("fName");
        lName = getIntent().getStringExtra("lName");
        downloadUri = getIntent().getStringExtra("uri");
        contact = getIntent().getStringExtra("contact");

        setInterestTags = findViewById(R.id.setInterestTags);
        chipGroup = findViewById(R.id.location_tag_chip_group);
        locationTagEntry = findViewById(R.id.location_tags_entry);
        locationTagAdd = findViewById(R.id.location_tag_add_button);

        setInterestTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!locationTagsList.isEmpty()){
                    Intent intent = new Intent(LocationTagPicker.this, InterestTagPicker.class);
                    intent.putExtra("fName", fName);
                    intent.putExtra("lName", lName);
                    intent.putExtra("contact", contact);
                    intent.putExtra("locationTags", locationTagsList.toString());
                    if (downloadUri != null)
                        intent.putExtra("uri", downloadUri.toString());

                    startActivity(intent);
                } else {
                    Toast.makeText(LocationTagPicker.this,"Select tags before you continue",Toast.LENGTH_LONG).show();
                }
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
                String interestTag = locationTagEntry.getText().toString();
                if (!interestTag.isEmpty()) {
                    setTag(interestTag);
                }
            }
        });

    }

    private void setTag(final String name) {
        final Chip chip = new Chip(this);
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()
        );
        chip.setRippleColor(ColorStateList.valueOf(Color.WHITE));
        chip.setPadding(
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 3,
                        getResources().getDisplayMetrics()
                ),
                paddingDp, paddingDp, paddingDp);
        chip.setText(name);
        chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.chip_unselected_gray)));

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chip.getChipBackgroundColor().getDefaultColor() == -9655041) {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.chip_unselected_gray)));
                    locationTagsList.remove(chip.getText().toString());
                } else {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
                    locationTagsList.add(chip.getText().toString());

                }

                Log.d("LOCATION TAG PICKER", "LOCATION TAG LIST: " + locationTagsList.toString());
            }

        });
        chipGroup.addView(chip);
        locationTagEntry.setText("#");
        locationTagEntry.setSelection(locationTagEntry.getText().length());
    }

    private void loadTag() {
        loadlocList = new ArrayList<String>();
        if (loadlocList.size() > 0)
            loadlocList.clear();

        db.collection("Tags")
                .document("Location")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                loadlocList = (List<String>) document.get("locationTags");
                                Log.d("LOCATIONTAGPICKER", "Array data :" + loadlocList);

                                Collections.sort(loadlocList, new Comparator<String>() {
                                    @Override
                                    public int compare(String s1, String s2) {
                                        return s1.compareToIgnoreCase(s2);
                                    }
                                });

                                for(String loc : loadlocList) {
                                    setTag(loc);
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

    @Override
    protected void onStart() {
        super.onStart();
        chipGroup.removeAllViews();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {
            // Write you code here if permission already given.
        }
    }
}
