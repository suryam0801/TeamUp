package com.example.teamup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamup.Explore.ExploreActivity;
import com.example.teamup.Explore.Project;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.UUID;

public class CreateProject extends AppCompatActivity {

    String TAG = "CreateProject";
    FirebaseFirestore db;
    FirebaseAuth currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project2);

        //Initializing firestore
        db = FirebaseFirestore.getInstance();
        currentUser=FirebaseAuth.getInstance();

        //initializing all UI elements
        final EditText projName = findViewById(R.id.projectName);
        final EditText projDescription = findViewById(R.id.projectDescription);
        Button createProjectSubmit = (Button)findViewById(R.id.createProjectSubmit);

        createProjectSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String pName = String.valueOf(projName.getText());
                String pDescription = String.valueOf(projDescription.getText());
                if(pName.equals("") || pDescription.equals("") || pName.replaceAll("\\s", "").equals("") || pDescription.replaceAll("\\s", "").equals("")){
                    Toast.makeText(getApplicationContext(), "Please Fill Out All Fields", Toast.LENGTH_LONG).show();
                } else {
                    createProject(projName.getText().toString(),projDescription.getText().toString());
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent about_intent=new Intent(this,ExploreActivity.class);
        startActivity(about_intent);
        about_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    public void createProject(String projectName,String projecDesc){
            Project project=new Project();
            project.setCreatorId(Objects.requireNonNull(currentUser.getCurrentUser()).getUid());
            project.setCreatorName(currentUser.getCurrentUser().getDisplayName());
            project.setProjectName(projectName);
            project.setProjectDescription(projecDesc);
            project.setCreatorEmail(currentUser.getCurrentUser().getEmail());
            project.setApplicantId(null);
            project.setApplicantList(null);
            project.setProjectStatus("Created");
            project.setRequiredSkills(null);
            project.setWorkersList(null);
            project.setProjectId(UUID.randomUUID().toString()
            );
            db.collection("Projects")
                    .document(project.getProjectId())
                    .set(project)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: Project Added");
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

    }
}