package com.example.teamup.EditOrView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.teamup.ControlPanel.DisplayApplicants.ApplicantsTabbedActivity;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.TabbedActivityMain;
import com.example.teamup.login.PhoneLogin;
import com.example.teamup.model.Broadcast;
import com.example.teamup.model.User;
import com.example.teamup.model.Worker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditOrViewProfile extends AppCompatActivity {

    private CircleImageView profileImageView;
    private TextView userName, userEmail, createdProjects, workingProjects, completedProjects;
    private Button editProfPic, finalizeChanges, logout;
    private Dialog removeConfirm;
    private ImageButton back;
    private ChipGroup locationTags, interestTags;
    private Uri filePath;
    private StorageReference storageReference;
    private Uri downloadUri;
    private FirebaseFirestore db;
    private List<String> locationTagList, interestTagList;
    private FirebaseAuth firebaseAuth;
    private static final int PICK_IMAGE_REQUEST = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private boolean change = false;
    String userID = "", flag = "", TAG = "EDIT OR VIEW PROFILE";
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_edit_profile);

        userName = findViewById(R.id.viewProfile_name);
        userEmail = findViewById(R.id.viewProfile_email);
        createdProjects = findViewById(R.id.viewProfile_createdProjectsCount);
        workingProjects = findViewById(R.id.viewProfileWorkingProjectCount);
        completedProjects = findViewById(R.id.viewProfile_CompletedProjectsCount);
        editProfPic = findViewById(R.id.profile_view_profilePicSetterImage);
        profileImageView = findViewById(R.id.profile_view_profile_image);
        finalizeChanges = findViewById(R.id.profile_finalize_changes);
        logout = findViewById(R.id.profile_logout);
        back = findViewById(R.id.bck_view_edit_profile);
        locationTags = findViewById(R.id.viewProfile_locationTags);
        interestTags = findViewById(R.id.viewProfile_interestTags);
        removeConfirm = new Dialog(EditOrViewProfile.this);
        firebaseAuth = FirebaseAuth.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference();


        userID = getIntent().getStringExtra("userID");
        flag = getIntent().getStringExtra("flag");

        user = SessionStorage.getUser(EditOrViewProfile.this);

        createdProjects.setText(user.getCreatedProjects() + "");
        workingProjects.setText(user.getWorkingProjects() + "");
        completedProjects.setText(user.getCompletedProjects() + "");
        userName.setText(user.getFirstName() + " " + user.getLastName());
        userEmail.setText(user.getContact() + "");

        Glide.with(EditOrViewProfile.this)
                .load(user.getProfileImageLink())
                .placeholder(ContextCompat.getDrawable(EditOrViewProfile.this, R.drawable.profile_image))
                .into(profileImageView);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(EditOrViewProfile.this, PhoneLogin.class));
                Log.d(TAG, "SIGNING OUTTTTTTTTTT");
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag.equals("owner")) {
                    startActivity(new Intent(EditOrViewProfile.this, TabbedActivityMain.class));
                    finish();
                } else if (flag.equals("member")) {
                    startActivity(new Intent(EditOrViewProfile.this, ApplicantsTabbedActivity.class));
                    finish();
                }
            }
        });

        editProfPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(EditOrViewProfile.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditOrViewProfile.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_CODE);
                } else {
                    pickFile();
                }
            }
        });
        finalizeChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(change = true){

                }
            }
        });

        db = FirebaseFirestore.getInstance();

        switch (flag) {
            case "applicant":
                //applicantLoad();
                break;
            case "member":
                //memberLoad();
                break;
            case "owner":
                ownerLoad();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(EditOrViewProfile.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
                pickFile();
            } else {
                Toast.makeText(EditOrViewProfile.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void pickFile () {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            if (filePath != null) {

                final ProgressDialog progressDialog = new ProgressDialog(EditOrViewProfile.this);
                progressDialog.setTitle("Uploading");
                progressDialog.show();

                String id = UUID.randomUUID().toString();
                final StorageReference profileRef = storageReference.child("ProfilePics/" + id);

                profileRef.putFile(filePath).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                        //displaying percentage in progress dialog
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    }
                })
                        .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                // Continue with the task to get the download URL
                                return profileRef.getDownloadUrl();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        progressDialog.dismiss();
                        //and displaying a success toast
                        Toast.makeText(getApplicationContext(), "Profile Pic Uploaded ", Toast.LENGTH_LONG).show();
                        downloadUri = uri;
                        Glide.with(EditOrViewProfile.this).load(downloadUri.toString()).into(profileImageView);

                        user.setProfileImageLink(String.valueOf(downloadUri));

                        db.collection("Users")
                                .document(user.getUserId())
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
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                //if the upload is not successfull
                                //hiding the progress dialog
                                progressDialog.dismiss();

                                //and displaying error message
                                Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

            }

        }
    }

    public void removeUserConfirmationDialog() {
        removeConfirm.setContentView(R.layout.remove_user_dialog_layout);
        final Button remove = removeConfirm.findViewById(R.id.remove_user_accept_button);
        Button cancel = removeConfirm.findViewById(R.id.remove_user_cancel_button);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeConfirm.dismiss();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeUser();
                removeConfirm.dismiss();
            }
        });

        removeConfirm.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        removeConfirm.show();
    }

    public void removeUser() {
        Broadcast broadcast = SessionStorage.getProject(EditOrViewProfile.this);
        Worker worker = SessionStorage.getWorker(EditOrViewProfile.this);
        db.collection("Projects").document(broadcast.getBroadcastId()).update("workersId", FieldValue.arrayRemove(worker.getUserId())).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        db.collection("Projects").document(broadcast.getBroadcastId()).update("workersList", FieldValue.arrayRemove(worker)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    //applicant and member profile loading
    /*public void applicantLoad() {
        editProfPic.setVisibility(View.GONE);
        editSpecialization.setVisibility(View.GONE);
        editLocation.setVisibility(View.GONE);
        finalizeChanges.setVisibility(View.GONE);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) logout.getLayoutParams();
        lp.setMargins(0, 0, 0, 40);
        logout.setLayoutParams(lp);

        logout.setText("Return to applicant view");

        //code for removing worker from project
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditOrViewProfile.this, ApplicantsTabbedActivity.class));
                finish();
            }
        });

        DocumentReference docRef = db.collection("Users").document(userID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);

                Log.d("EDITORVIEW", "userID: " + user.toString());

                userName.setText(user.getFirstName() + " " + user.getLastName());
                userEmail.setText("User Number is Private"); //user.getContact()
                createdProjects.setText(user.getCreatedProjects() + "");
                workingProjects.setText(user.getWorkingProjects() + "");
                completedProjects.setText(user.getCompletedProjects() + "");
                specialization.setText(user.getInterests());
                Location.setText(user.getLocation());
            }
        });
    }

    public void memberLoad() {
        editProfPic.setVisibility(View.GONE);
        editSpecialization.setVisibility(View.GONE);
        editLocation.setVisibility(View.GONE);
        finalizeChanges.setVisibility(View.GONE);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) logout.getLayoutParams();
        lp.setMargins(0, 0, 0, 40);
        logout.setLayoutParams(lp);

        logout.setText("Remove From Project");
        logout.setBackgroundColor(Color.parseColor("#FFF5F5"));
        logout.setTextColor(Color.parseColor("#FF6C6C"));

        //code for removing worker from project
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeUserConfirmationDialog();
            }
        });

        DocumentReference docRef = db.collection("Users").document(userID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);

                Log.d("EDITORVIEW", "userID: " + user.toString());

                userName.setText(user.getFirstName() + " " + user.getLastName());
                userEmail.setText("User Number is Private"); //user.getContact()
                createdProjects.setText(user.getCreatedProjects() + "");
                workingProjects.setText(user.getWorkingProjects() + "");
                completedProjects.setText(user.getCompletedProjects() + "");
                specialization.setText(user.getInterests());

                Location.setText(user.getLocation());
            }
        });
    }
*/

    @Override
    public void onResume(){
        super.onResume();
        if(getIntent().hasExtra("locationTagList")) {
            locationTagList = getIntent().getStringArrayListExtra("locationTagList");
            finalizeChanges.setBackground(getResources().getDrawable(R.drawable.confirm_application_buttom_background));
            finalizeChanges.setTextColor(Color.parseColor("#35C80B"));
            locationTags.removeAllViews();
            setLocationTags(true);
            change = true;
        }
        if(getIntent().hasExtra("interestTagList")){
            interestTagList = getIntent().getStringArrayListExtra("interestTagList");
            finalizeChanges.setBackground(getResources().getDrawable(R.drawable.confirm_application_buttom_background));
            finalizeChanges.setTextColor(Color.parseColor("#35C80B"));
            interestTags.removeAllViews();
            setInterestTags();
            change = true;
        }
    }

    public void ownerLoad() {
        setLocationTags(false);
        setInterestTags();
    }

    public void setLocationTags(boolean refresh){
        List<String> locationList = new ArrayList<>();
        if(refresh == false){
            locationList = SessionStorage.getUser(EditOrViewProfile.this).getLocationTags();
        } else {
            for(String s : locationTagList){
                locationList.add(s);
            }
        }

        locationList.add("edit");
        for(String s: locationList){
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
            chip.setText(s);
            if(s.equals("edit")){
                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.opaque_orange)));
            } else {
                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
            }
            chip.setTextColor(Color.WHITE);

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(chip.getText().equals("edit")){
                        startActivity(new Intent(EditOrViewProfile.this, ProjectPickLocationTags.class));
                    }
                }

            });
            locationTags.addView(chip);
        }
    }

    public void setInterestTags(){
        final List<String> interestList = SessionStorage.getUser(EditOrViewProfile.this).getInterestTags();
        interestList.add("edit");
        for(String s: interestList){
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
            chip.setText(s);
            if(s.equals("edit")){
                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.opaque_orange)));
            } else {
                chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.color_blue)));
            }
            chip.setTextColor(Color.WHITE);

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(chip.getText().equals("edit")){
                        startActivity(new Intent(EditOrViewProfile.this, ProjectPickInterestTags.class));
                    }
                }

            });
            interestTags.addView(chip);
        }
    }
}
