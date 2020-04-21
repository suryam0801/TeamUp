package com.example.teamup.ControlPanel.EditOrView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.teamup.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditOrViewProfile extends AppCompatActivity {

    CircleImageView profileImageView;
    TextView userName, userEmail, createdProjects, workingProjects, completedProjects, specialization, Hobbies, Location;
    Button editProfPic, editNameEmail, editSpecialization, editSecondarySkill, editLocation;

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

    }
}
