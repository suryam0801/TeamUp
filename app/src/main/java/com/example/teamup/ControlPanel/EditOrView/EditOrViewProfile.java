package com.example.teamup.ControlPanel.EditOrView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.teamup.Explore.ProjectAdapter;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.TabbedActivityMain;
import com.example.teamup.model.Member;
import com.example.teamup.model.Project;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditOrViewProfile extends AppCompatActivity {

    CircleImageView profileImageView;
    TextView userName, userEmail, createdProjects, workingProjects, completedProjects, specialization, Hobbies, Location;
    Button editProfPic, editNameEmail, editSpecialization, editSecondarySkill, editLocation, finalizeChanges, logout;
    EditText nameEdit, emailEdit, specializationEdit, HobbiesEdit, locationEdit;
    FirebaseFirestore db;

    String userID, flag;
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
        editNameEmail = findViewById(R.id.profile_edit_button);
        editSpecialization = findViewById(R.id.editSpecialization);
        editSecondarySkill = findViewById(R.id.editSecondarySkill);
        editLocation = findViewById(R.id.editLocation);
        profileImageView = findViewById(R.id.profile_view_profile_image);
        finalizeChanges = findViewById(R.id.profile_finalize_changes);
        logout = findViewById(R.id.profile_logout);
        nameEdit = findViewById(R.id.viewProfileChangeName);
        emailEdit = findViewById(R.id.viewProfileChangeEmail);
        specializationEdit = findViewById(R.id.viewProfileChangeSpecialization);
        HobbiesEdit = findViewById(R.id.viewProfileChangeSecondarySkill);
        locationEdit = findViewById(R.id.viewProfileChangeLocation);

        editProfPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pic loading
            }
        });
        editNameEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName.setVisibility(View.GONE);
                nameEdit.setVisibility(View.VISIBLE);
                userEmail.setVisibility(View.GONE);
                emailEdit.setVisibility(View.VISIBLE);
            }
        });
        editSpecialization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                specialization.setVisibility(View.GONE);
                specializationEdit.setVisibility(View.VISIBLE);
            }
        });
        editSecondarySkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hobbies.setVisibility(View.GONE);
                HobbiesEdit.setVisibility(View.VISIBLE);
            }
        });
        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location.setVisibility(View.GONE);
                locationEdit.setVisibility(View.VISIBLE);
            }
        });
        finalizeChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(nameEdit.getText());
                String email = String.valueOf(emailEdit.getText());
                String pSkill = String.valueOf(specializationEdit.getText());
                String sSkill = String.valueOf(HobbiesEdit.getText());
                String loc = String.valueOf(locationEdit.getText());
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        db=FirebaseFirestore.getInstance();

        userID = getIntent().getStringExtra("userID");
        flag = getIntent().getStringExtra("flag");

        switch (flag) {
            case "member":
                memberLoad();
                break;
            case "owner":
                ownerLoad();
                break;
        }
    }

    public void memberLoad() {
        editProfPic.setVisibility(View.GONE);
        editNameEmail.setVisibility(View.GONE);
        editSpecialization.setVisibility(View.GONE);
        editSecondarySkill.setVisibility(View.GONE);
        editLocation.setVisibility(View.GONE);
        finalizeChanges.setVisibility(View.GONE);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) logout.getLayoutParams();
        lp.setMargins(0,0,0,40);
        logout.setLayoutParams(lp);
        logout.setText("Done Viewing");

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
                userEmail.setText(user.getEmail());
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
                userEmail.setText(user.getEmail());
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
