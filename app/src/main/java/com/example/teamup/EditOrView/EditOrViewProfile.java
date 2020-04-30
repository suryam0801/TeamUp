package com.example.teamup.EditOrView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.teamup.ControlPanel.DisplayApplicants.ApplicantsTabbedActivity;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.TabbedActivityMain;
import com.example.teamup.login.LoginActivity;
import com.example.teamup.login.PhoneLogin;
import com.example.teamup.login.SignUpActivity;
import com.example.teamup.model.Project;
import com.example.teamup.model.User;
import com.example.teamup.model.Worker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditOrViewProfile extends AppCompatActivity {

    private CircleImageView profileImageView;
    private TextView userName, userEmail, createdProjects, workingProjects, completedProjects, specialization, Hobbies, Location;
    private Button editProfPic, editSpecialization, editSecondarySkill, editLocation, finalizeChanges, logout;
    private EditText specializationEdit, HobbiesEdit, locationEdit;
    private Dialog removeConfirm;
    private ImageButton back;
    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;


    String userID, flag, TAG = "EDIT OR VIEW PROFILE";
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
        specialization = findViewById(R.id.viewProfile_specializedField);
        Hobbies = findViewById(R.id.viewProfile_secondarySkill);
        Location = findViewById(R.id.viewProfile_location);
        editProfPic = findViewById(R.id.profile_view_profilePicSetterImage);
        editSpecialization = findViewById(R.id.editSpecialization);
        editSecondarySkill = findViewById(R.id.editSecondarySkill);
        editLocation = findViewById(R.id.editLocation);
        profileImageView = findViewById(R.id.profile_view_profile_image);
        finalizeChanges = findViewById(R.id.profile_finalize_changes);
        logout = findViewById(R.id.profile_logout);
        back = findViewById(R.id.bck_view_edit_profile);
        specializationEdit = findViewById(R.id.viewProfileChangeSpecialization);
        HobbiesEdit = findViewById(R.id.viewProfileChangeSecondarySkill);
        locationEdit = findViewById(R.id.viewProfileChangeLocation);
        removeConfirm = new Dialog(EditOrViewProfile.this);
        firebaseAuth=FirebaseAuth.getInstance();

        userID = getIntent().getStringExtra("userID");
        flag = getIntent().getStringExtra("flag");


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag.equals("owner")) {
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
                //pic loading
            }
        });
        editSpecialization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                specialization.setVisibility(View.GONE);
                specializationEdit.setVisibility(View.VISIBLE);
                finalizeChanges.setBackground(getResources().getDrawable(R.drawable.confirm_application_buttom_background));
                finalizeChanges.setTextColor(Color.parseColor("#35C80B"));
            }
        });
        editSecondarySkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hobbies.setVisibility(View.GONE);
                HobbiesEdit.setVisibility(View.VISIBLE);
                finalizeChanges.setBackground(getResources().getDrawable(R.drawable.confirm_application_buttom_background));
                finalizeChanges.setTextColor(Color.parseColor("#35C80B"));
            }
        });
        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location.setVisibility(View.GONE);
                locationEdit.setVisibility(View.VISIBLE);
                finalizeChanges.setBackground(getResources().getDrawable(R.drawable.confirm_application_buttom_background));
                finalizeChanges.setTextColor(Color.parseColor("#35C80B"));
            }
        });
        finalizeChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pSkill = String.valueOf(specializationEdit.getText());
                String sSkill = String.valueOf(HobbiesEdit.getText());
                String loc = String.valueOf(locationEdit.getText());

                if(!pSkill.trim().equals(""))
                    user.setSpecialization(pSkill);
                if(!sSkill.trim().equals(""))
                    user.setSecondarySkill(sSkill);
                if(!loc.trim().equals(""))
                    user.setLocation(loc);


                db.collection("Users").document(userID).set(user);
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> tokenMap=new HashMap<>();
                tokenMap.put("token_id",FieldValue.delete());
                db.collection("Users").document(userID).update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firebaseAuth.signOut();
                        startActivity(new Intent(EditOrViewProfile.this, PhoneLogin.class));
                    }
                });
            }
        });



        db=FirebaseFirestore.getInstance();

        switch (flag) {
            case "member":
                memberLoad();
                break;
            case "owner":
                ownerLoad();
                break;
        }
    }

    public void removeUserConfirmationDialog(){
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

    public void removeUser(){
        Project project = SessionStorage.getProject(EditOrViewProfile.this);
        Worker worker = SessionStorage.getWorker(EditOrViewProfile.this);
        db.collection("Projects").document(project.getProjectId()).update("workersId",FieldValue.arrayRemove(worker.getUserId())).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        db.collection("Projects").document(project.getProjectId()).update("workersList",FieldValue.arrayRemove(worker)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void memberLoad() {
        editProfPic.setVisibility(View.GONE);
        editSpecialization.setVisibility(View.GONE);
        editSecondarySkill.setVisibility(View.GONE);
        editLocation.setVisibility(View.GONE);
        finalizeChanges.setVisibility(View.GONE);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) logout.getLayoutParams();
        lp.setMargins(0,0,0,40);
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

                Glide.with(EditOrViewProfile.this)
                        .load(user.getProfileImageLink())
                        .placeholder(ContextCompat.getDrawable(EditOrViewProfile.this, R.drawable.ic_account_circle_black_24dp))
                        .into(profileImageView);

                userName.setText(user.getFirstName() + " " + user.getLastName());
                userEmail.setText(user.getContact());
                createdProjects.setText(user.getCreatedProjects() + "");
                workingProjects.setText(user.getWorkingProjects() + "");
                completedProjects.setText(user.getCompletedProjects() + "");
                specialization.setText(user.getSpecialization());
                Hobbies.setText(user.getSecondarySkill());
                Location.setText(user.getLocation());
            }
        });
    }

    public void ownerLoad() {
        DocumentReference docRef = db.collection("Users").document(userID);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);

                Log.d("EDITORVIEW", "userID: " + user.toString());

                Glide.with(EditOrViewProfile.this)
                        .load(user.getProfileImageLink())
                        .placeholder(ContextCompat.getDrawable(EditOrViewProfile.this, R.drawable.ic_account_circle_black_24dp))
                        .into(profileImageView);

                userName.setText(user.getFirstName() + " " + user.getLastName());
                userEmail.setText(user.getContact());
                createdProjects.setText(user.getCreatedProjects() + "");
                workingProjects.setText(user.getWorkingProjects() + "");
                completedProjects.setText(user.getCompletedProjects() + "");
                specialization.setText(user.getSpecialization());
                Hobbies.setText(user.getSecondarySkill());
                Location.setText(user.getLocation());
            }
        });
    }



}
