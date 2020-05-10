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
    private List<String> locationTagList = new ArrayList<>(), interestTagList = new ArrayList<>(), selectedLocationList = new ArrayList<>(), selectedInterestList = new ArrayList<>();
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
                locationTagDialogShow("fresh load");
            }
        });
        addInterestTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interestTagDialogShow("fresh load");
            }
        });
    }

    public void locationTagDialogShow(String addOrNot) {
        locationTagPicker.setContentView(R.layout.activity_project_pick_location_tags);
        final Button finalizeLocationTag = locationTagPicker.findViewById(R.id.broadcast_finalize_location_tags);
        locationChipGroup = locationTagPicker.findViewById(R.id.broadcast_location_tag_chip_group);
        locationTagEntry = locationTagPicker.findViewById(R.id.broadcast_location_tags_entry);
        final Button locationTagAdd = locationTagPicker.findViewById(R.id.broadcast_location_tag_add_button);

        if (addOrNot.equals("add")) {
            List<String> tempLocList = locationTagList;
            for (String loc : tempLocList)
                setLocationTag(loc, "add");
        } else {
            loadLocationTag();
        }

        finalizeLocationTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationTagPicker.dismiss();
                addLocationTags.setVisibility(View.GONE);
                selectedLocationList.add("edit");
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
                String locationTag = locationTagEntry.getText().toString();
                if (!locationTag.isEmpty()) {
                    setLocationTag(locationTag, "new");
                }
            }
        });

        locationTagPicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(final DialogInterface arg0) {
                Log.d(TAG, "LOCATION TAG SELECTION: " + selectedLocationList.toString());
            }
        });

        locationTagPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        locationTagPicker.show();
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

        if (userNewTag.equals("add")) {
            if (selectedLocationList.contains(name)) {
                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
                chip.setTextColor(Color.WHITE);
            } else {
                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.chip_unselected_gray)));
                chip.setTextColor(Color.BLACK);
            }
        } else if (userNewTag.equals("new")) {
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
            chip.setTextColor(Color.WHITE);
            selectedLocationList.add(name);
            Log.d("INTEREST TAG PICKER", "INTEREST TAG LIST: " + selectedLocationList.toString());
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
                    selectedLocationList.remove(chip.getText().toString());
                } else {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
                    chip.setTextColor(Color.WHITE);
                    selectedLocationList.add(chip.getText().toString());
                }

                Log.d(TAG, "LOCATION TAG LIST: " + selectedLocationList.toString());
            }

        });

        locationChipGroup.addView(chip);
        locationTagEntry.setText("#");
        locationTagEntry.setSelection(locationTagEntry.getText().length());
    }

    private void setSelectedLocationTagsView() {
        for (String location : selectedLocationList) {
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

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (chip.getText().equals("edit")) {
                        selectedLocationList.remove("add +");
                        locationTagDialogShow("add");
                        selectedLocationTagGroup.removeAllViews();
                    }
                }
            });

            chip.setText(location);
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
            if (location.equals("edit"))
                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.opaque_orange)));
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
                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                    }
                });


    }

    public void interestTagDialogShow(String addOrNot) {
        interestTagPicker.setContentView(R.layout.activity_project_pick_interest_tags);
        final Button finalizeInterestTag = interestTagPicker.findViewById(R.id.broadcast_finalize_interest_tags);
        interestChipGroup = interestTagPicker.findViewById(R.id.broadcast_interest_tag_chip_group);
        interestTagEntry = interestTagPicker.findViewById(R.id.broadcast_interest_tags_entry);
        final Button interestTagAdd = interestTagPicker.findViewById(R.id.broadcast_interest_tag_add_button);

        if (addOrNot.equals("add"))
            loadInterestTag("add");
        else
            loadInterestTag("fresh load");


        finalizeInterestTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interestTagPicker.dismiss();
                addInterestTags.setVisibility(View.GONE);
                selectedInterestList.add("edit");
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
                Log.d(TAG, "INTEREST TAG SELECTION: " + selectedInterestList.toString());
            }
        });

        interestTagPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        interestTagPicker.show();

    }

    private void setInterestTag(final String name, String userNewTag) {
        if (!interestTagList.contains(name))
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

        if (userNewTag.equals("add")){
            if(selectedInterestList.contains(name)){
                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
                chip.setTextColor(Color.WHITE);
            } else {
                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.chip_unselected_gray)));
                chip.setTextColor(Color.BLACK);
            }
        } else if (userNewTag.equals("new")) {
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
            chip.setTextColor(Color.WHITE);
            selectedInterestList.add(name);
            Log.d("INTEREST TAG PICKER", "INTEREST TAG LIST: " + selectedInterestList.toString());
        } else {
            if (selectedInterestList.contains(name)) {
                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
                chip.setTextColor(Color.WHITE);
            } else {
                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.chip_unselected_gray)));
                chip.setTextColor(Color.BLACK);
            }
        }

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chip.getChipBackgroundColor().getDefaultColor() == -9655041) {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.chip_unselected_gray)));
                    chip.setTextColor(Color.BLACK);
                    selectedInterestList.remove(chip.getText().toString());
                } else {
                    chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
                    chip.setTextColor(Color.WHITE);
                    selectedInterestList.add(chip.getText().toString());
                }
                Log.d("INTEREST TAG PICKER", "INTEREST TAG LIST: " + selectedInterestList.toString());
            }

        });

        interestChipGroup.addView(chip);
        interestTagEntry.setText("#");
        interestTagEntry.setSelection(locationTagEntry.getText().length());
    }

    private void setSelectedInterestTagsView() {
        for (String interest : selectedInterestList) {
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

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (chip.getText().equals("edit")) {
                        selectedInterestList.remove("add +");
                        interestTagDialogShow("add");
                        selectedInterestTagGroup.removeAllViews();
                    }
                }
            });

            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));

            if (interest.equals("edit"))
                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.opaque_orange)));
            chip.setTextColor(Color.WHITE);

            selectedInterestTagGroup.addView(chip);
        }
    }

    private void loadInterestTag(final String addOrNot) {
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
                                if(!interestTagList.isEmpty())
                                    interestTagList.clear();

                                for (String loc : selectedLocationList) {
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

                                if (addOrNot.equals("add")) {
                                    List<String> tempInterest = new ArrayList<>();
                                    for(String s : selectedInterestList)
                                        tempInterest.add(s);
                                    selectedInterestList.clear();
                                    for (String interest : interestTagListTemp) {
                                        interestTagList.add(interest);
                                        if(tempInterest.contains(interest)) {
                                            selectedInterestList.add(interest);
                                        }
                                    }

                                    for (String interest : interestTagListTemp)
                                        setInterestTag(interest, "add");

                                    Log.d("DEBUGGGINGGGGGGGG", selectedInterestList.toString());

                                } else {
                                    for (String interest : interestTagListTemp)
                                        setInterestTag(interest, "old");
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

    private void createInMasterStore(Broadcast broadcast) {
        for (String loc : selectedLocationList) {
            db.collection("Tags")
                    .document("Location-Interest")
                    .update(loc, FieldValue.arrayUnion(selectedInterestList.toArray()));
            for (String interest : selectedInterestList) {
                db.collection("MasterProjectCollection").document(loc).collection(interest).document(broadcast.getBroadcastId()).set(broadcast);
            }
        }

        db.collection("Tags")
                .document("Location")
                .update("locationTags", FieldValue.arrayUnion(selectedLocationList.toArray()));

        db.collection("Tags")
                .document("Interest")
                .update("interestTags", FieldValue.arrayUnion(selectedInterestList.toArray()));


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
        broadcast.setInterestTags(selectedInterestList);
        broadcast.setLocationTags(selectedLocationList);
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