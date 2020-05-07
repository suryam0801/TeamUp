package com.example.teamup.CreateBroadcast;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.TabbedActivityMain;
import com.example.teamup.model.Broadcast;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CreateBroadcast extends Activity implements AdapterView.OnItemSelectedListener {

    private String TAG = "CreateProject", selectedCategory = "";
    private FirebaseFirestore db;
    private FirebaseAuth currentUser;
    private EditText broadcastName, broadcastDescription;
    private List<String> locationTags, interestTags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.parseColor("#158BF1"));
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title

        setContentView(R.layout.activity_create_project2);

        //Initializing firestore
        db = FirebaseFirestore.getInstance();
        currentUser=FirebaseAuth.getInstance();

        if(getIntent().hasExtra("locationTags"))
            locationTags = getIntent().getExtras().getStringArrayList("locationTags");
        if(getIntent().hasExtra("interestTags"))
            interestTags = getIntent().getExtras().getStringArrayList("interestTags");

        //initializing all UI elements
        broadcastName = findViewById(R.id.projectName);
        broadcastDescription = findViewById(R.id.projectDescription);
        Button createProjectSubmit = (Button)findViewById(R.id.createProjectSubmit);
        Button addSkillSet = findViewById(R.id.addSkillSet);
        ImageButton back_create = findViewById(R.id.bck_create);


        Spinner spinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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
                if(pName.equals("") || pDescription.equals("") || pName.replaceAll("\\s", "").equals("") || pDescription.replaceAll("\\s", "").equals("")){
                    Toast.makeText(getApplicationContext(), "Please Fill Out All Fields", Toast.LENGTH_LONG).show();
                } else {
                    createProject(broadcastName.getText().toString(), broadcastDescription.getText().toString());
                }

            }
        });

        addSkillSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateBroadcast.this, ProjectPickLocationTags.class));
            }
        });
    }

    private void assignProjectLocations(Broadcast broadcast) {

        for (String loc : locationTags)
            db.collection("Location-Projects").document(loc).collection("projects").document(broadcast.getBroadcastId()).set(broadcast);

        for (String interest : interestTags)
            db.collection("Interest-Projects").document(interest).collection("projects").document(broadcast.getBroadcastId()).set(broadcast);

    }

    @Override
    public void onBackPressed() {
        Intent about_intent=new Intent(this, TabbedActivityMain.class);
        startActivity(about_intent);
        about_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    public void createProject(String projectName,String projecDesc){

        List<String> chipsTextList = new ArrayList<>();

        final Broadcast broadcast =new Broadcast();
        broadcast.setCreatorId(Objects.requireNonNull(currentUser.getCurrentUser()).getUid());
        broadcast.setCreatorName(currentUser.getCurrentUser().getDisplayName());
        broadcast.setBroadcastName(projectName);
        broadcast.setBroadcastDescription(projecDesc);
        broadcast.setCreatorEmail(currentUser.getCurrentUser().getEmail());
        broadcast.setApplicantId(null);
        broadcast.setApplicantList(null);
        broadcast.setBroadcastStatus("Created");
        broadcast.setInterestTags(interestTags);
        broadcast.setLocationTags(locationTags);
        broadcast.setWorkersList(null);
        broadcast.setNewApplicants(0);
        broadcast.setNewTasks(0);
        broadcast.setWorkersId(null);
        broadcast.setBroadcastId(UUID.randomUUID().toString());
        broadcast.setTaskList(null);
        broadcast.setCategory(selectedCategory);

        assignProjectLocations(broadcast);

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

        db.collection("Users").document(currentUser.getInstance().getUid()).update("createdProjects",createdProjects).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("projectName", broadcastName.getText().toString());
        savedInstanceState.putString("projectDescription", broadcastDescription.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        String name = savedInstanceState.getString("projectName");
        String description = savedInstanceState.getString("projectDescription");
        broadcastName.setText(name);
        broadcastDescription.setText(description);
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