package com.example.teamup.CreateBroadcast;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.TabbedActivityMain;
import com.example.teamup.model.Broadcast;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CreateBroadcast extends Activity implements AdapterView.OnItemSelectedListener {

    private String TAG = "CreateProject", selectedCategory = "";
    private FirebaseFirestore db;
    private FirebaseAuth currentUser;
    private EditText broadcastName, broadcastDescription, locationTagEntry, interestTagEntry;
    private Dialog locationTagPicker, interestTagPicker;
    private List<String> locationTagList = new ArrayList<>(), interestTagList = new ArrayList<>();
    private ChipGroup locationChipGroup, interestChipGroup, selectedLocationTagGroup, selectedInterestTagGroup;
    private TextView addLocationTags, addInterestTags;
    private RadioGroup acceptanceTypeRadioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#158BF1"));
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title

        setContentView(R.layout.activity_create_project2);

        //Initializing firestore
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance();
        locationTagPicker = new Dialog(CreateBroadcast.this);
        interestTagPicker = new Dialog(CreateBroadcast.this);
        acceptanceTypeRadioGroup = findViewById(R.id.acceptanceRadioGroup);

        //initializing all UI elements
        broadcastName = findViewById(R.id.projectName);
        broadcastDescription = findViewById(R.id.projectDescription);
        selectedLocationTagGroup = findViewById(R.id.selectedLocationTags);
        selectedInterestTagGroup = findViewById(R.id.selectedInterestTags);
        Button createProjectSubmit = findViewById(R.id.createProjectSubmit);
        addLocationTags = findViewById(R.id.createBroadcastAddLocationTags);
        addInterestTags = findViewById(R.id.createBroadcastAddInterestTags);
        ImageButton back_create = findViewById(R.id.bck_create);

        back_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateBroadcast.this, TabbedActivityMain.class));
                finish();
            }
        });

        createProjectSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String pName = String.valueOf(broadcastName.getText());
                String pDescription = String.valueOf(broadcastDescription.getText());
                if (pName.equals("") || pDescription.equals("") || pName.replaceAll("\\s", "").equals("") || pDescription.replaceAll("\\s", "").equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Fill Out All Fields", Toast.LENGTH_LONG).show();
                } else if (locationTagList.isEmpty() || interestTagList.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Select all your tags", Toast.LENGTH_LONG).show();
                } else {
                    createProject(broadcastName.getText().toString(), broadcastDescription.getText().toString());
                }
            }
        });

        addLocationTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationTagDialogShow();
            }
        });
        addInterestTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interestTagDialogShow();
            }
        });
    }

    public void locationTagDialogShow() {
        locationTagPicker.setContentView(R.layout.activity_project_pick_location_tags);
        final Button finalizeLocationTag = locationTagPicker.findViewById(R.id.broadcast_finalize_location_tags);
        locationChipGroup = locationTagPicker.findViewById(R.id.broadcast_location_tag_chip_group);
        locationTagEntry = locationTagPicker.findViewById(R.id.broadcast_location_tags_entry);
        final Button locationTagAdd = locationTagPicker.findViewById(R.id.broadcast_location_tag_add_button);

        loadLocationTag();

        finalizeLocationTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationTagPicker.dismiss();
                addLocationTags.setVisibility(View.GONE);
                setSelectedLocationTagsView();
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
                    setLocationTag(interestTag, "new");
                }
            }
        });

        locationTagPicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface arg0) {
                Log.d(TAG, "LOCATION TAG SELECTION: " + locationTagList.toString());
            }
        });

        locationTagPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        locationTagPicker.show();
    }

    public void interestTagDialogShow() {
        interestTagPicker.setContentView(R.layout.activity_project_pick_interest_tags);

        loadInterestTag();

        final Button finalizeInterestTag = interestTagPicker.findViewById(R.id.broadcast_finalize_interest_tags);
        interestChipGroup = interestTagPicker.findViewById(R.id.broadcast_interest_tag_chip_group);
        interestTagEntry = interestTagPicker.findViewById(R.id.broadcast_interest_tags_entry);
        final Button interestTagAdd = interestTagPicker.findViewById(R.id.broadcast_interest_tag_add_button);

        finalizeInterestTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interestTagPicker.dismiss();
                addInterestTags.setVisibility(View.GONE);
                setSelectedInterestTagsView();
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
                    setInterestTag(interestTag, "new");
                }
            }
        });

        interestTagPicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface arg0) {
                Log.d(TAG, "INTEREST TAG SELECTION: " + interestTagList.toString());
            }
        });

        interestTagPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        interestTagPicker.show();

    }

    private void setLocationTag(final String name, String userNewTag) {
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

        if (userNewTag.equals("new")) {
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
            chip.setTextColor(Color.WHITE);
            locationTagList.add(name);
            Log.d("INTEREST TAG PICKER", "INTEREST TAG LIST: " + locationTagList.toString());
        } else {
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.chip_unselected_gray)));
            chip.setTextColor(Color.BLACK);
        }

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chip.getChipBackgroundColor().getDefaultColor() == -9655041) {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.chip_unselected_gray)));
                    chip.setTextColor(Color.BLACK);
                    locationTagList.remove(chip.getText().toString());
                } else {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
                    chip.setTextColor(Color.WHITE);
                    locationTagList.add(chip.getText().toString());

                }

                Log.d(TAG, "LOCATION TAG LIST: " + locationTagList.toString());
            }

        });

        locationChipGroup.addView(chip);
        locationTagEntry.setText("#");
        locationTagEntry.setSelection(locationTagEntry.getText().length());
    }

    private void setInterestTag(final String name, String userNewTag) {
        interestTagList.add(name);
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

        if (userNewTag.equals("new")) {
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
            chip.setTextColor(Color.WHITE);
            interestTagList.add(name);
            Log.d("INTEREST TAG PICKER", "INTEREST TAG LIST: " + interestTagList.toString());
        } else {
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.chip_unselected_gray)));
            chip.setTextColor(Color.BLACK);
        }

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        interestChipGroup.addView(chip);
        interestTagEntry.setText("#");
        interestTagEntry.setSelection(locationTagEntry.getText().length());
    }

    private void setSelectedInterestTagsView() {
        for (String interest : interestTagList) {
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
            chip.setText(interest);
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
            chip.setTextColor(Color.WHITE);

            selectedInterestTagGroup.addView(chip);
        }
    }

    private void setSelectedLocationTagsView() {
        for (String location : locationTagList) {
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
            chip.setText(location);
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
            chip.setTextColor(Color.WHITE);

            selectedLocationTagGroup.addView(chip);
        }
    }

    private void loadLocationTag() {
        if (locationTagList.size() > 0)
            locationTagList.clear();

        db.collection("Tags")
                .document("Location")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                locationTagList = (List<String>) document.get("locationTags");
                                Log.d("LOCATIONTAGPICKER", "Array data :" + locationTagList);

                                Collections.sort(locationTagList, new Comparator<String>() {
                                    @Override
                                    public int compare(String s1, String s2) {
                                        return s1.compareToIgnoreCase(s2);
                                    }
                                });

                                for (String loc : locationTagList) {
                                    setLocationTag(loc, "old");
                                }

                                locationTagList.clear();

                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });


    }

    private void loadInterestTag() {
        if (interestTagList.size() > 0)
            interestTagList.clear();
        db.collection("Tags")
                .document("Location-Interest")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                List<String> interestTagListTemp = new ArrayList<>();
                                for (String loc : locationTagList) {

                                    if (document.contains(loc)) {
                                        List<String> group = (List<String>) document.get(loc);
                                        for (String in : group) {
                                            if (!interestTagListTemp.contains(in)) {
                                                interestTagListTemp.add(in);
                                            }
                                        }
                                    }
                                }

                                Collections.sort(interestTagListTemp, new Comparator<String>() {
                                    @Override
                                    public int compare(String s1, String s2) {
                                        return s1.compareToIgnoreCase(s2);
                                    }
                                });

                                for (String interest : interestTagListTemp)
                                    setInterestTag(interest, "old");

                                interestTagList.clear();
                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void createInMasterStore(Broadcast broadcast) {
        for (String loc : locationTagList) {
            db.collection("Tags")
                    .document("Location-Interest")
                    .update(loc, FieldValue.arrayUnion(interestTagList.toArray()));
            for (String interest : interestTagList) {
                db.collection("MasterProjectCollection").document(loc).collection(interest).document(broadcast.getBroadcastId()).set(broadcast);
            }
        }

        db.collection("Tags")
                .document("Location")
                .update("locationTags", FieldValue.arrayUnion(locationTagList.toArray()));

        db.collection("Tags")
                .document("Interest")
                .update("interestTags", FieldValue.arrayUnion(interestTagList.toArray()));


    }

    @Override
    public void onBackPressed() {
        Intent about_intent = new Intent(this, TabbedActivityMain.class);
        startActivity(about_intent);
        about_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    public void createProject(String projectName, String projecDesc) {

        int radioId = acceptanceTypeRadioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        final Broadcast broadcast = new Broadcast();
        broadcast.setCreatorId(Objects.requireNonNull(currentUser.getCurrentUser()).getUid());
        broadcast.setCreatorName(currentUser.getCurrentUser().getDisplayName());
        broadcast.setBroadcastName(projectName);
        broadcast.setBroadcastDescription(projecDesc);
        broadcast.setCreatorEmail(currentUser.getCurrentUser().getEmail());
        broadcast.setApplicantId(null);
        broadcast.setApplicantList(null);
        broadcast.setBroadcastStatus("Created");
        broadcast.setInterestTags(interestTagList);
        broadcast.setLocationTags(locationTagList);
        broadcast.setWorkersList(null);
        broadcast.setNewApplicants(0);
        broadcast.setNewTasks(0);
        broadcast.setWorkersId(null);
        broadcast.setBroadcastId(UUID.randomUUID().toString());
        broadcast.setTaskList(null);
        broadcast.setAcceptanceType(radioButton.getText().toString());

        createInMasterStore(broadcast);

        Log.d(TAG, broadcast.toString());

        db.collection("Projects")
                .document(broadcast.getBroadcastId())
                .set(broadcast)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Project Added");
                        db.collection("Projects").document(broadcast.getBroadcastId()).update("workersId", FieldValue.arrayUnion(Objects.requireNonNull(currentUser.getCurrentUser()).getUid()))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                        Toast.makeText(getApplicationContext(), "Created Successfully", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to create project", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onSuccess: Project Not Added");
                    }
                });

        User user = SessionStorage.getUser(CreateBroadcast.this);

        int createdProjects = user.getCreatedProjects();
        createdProjects = createdProjects + 1;

        db.collection("Users").document(currentUser.getInstance().getUid()).update("createdProjects", createdProjects).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "JOB SUCCESSFUL!!!!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String text = parent.getItemAtPosition(position).toString();
        selectedCategory = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}