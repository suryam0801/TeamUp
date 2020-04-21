package com.example.teamup;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.teamup.model.Project;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CreateProject extends Activity implements AdapterView.OnItemSelectedListener {

    String TAG = "CreateProject", selectedCategory = "";
    FirebaseFirestore db;
    FirebaseAuth currentUser;
    ChipGroup chipGroup;
    ImageButton bck;
    EditText projName, projDescription, skillSetEntry;

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

        //initializing all UI elements
        bck = findViewById(R.id.bck_create);
        projName = findViewById(R.id.projectName);
        projDescription = findViewById(R.id.projectDescription);
        Button createProjectSubmit = (Button)findViewById(R.id.createProjectSubmit);
        skillSetEntry = findViewById(R.id.skill_entry_edittext);
        Button addSkillSet = findViewById(R.id.addSkillSet);
        chipGroup = findViewById(R.id.chip_group_create_skills);
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
                startActivity(new Intent(CreateProject.this, TabbedActivityMain.class));
                finish();
            }
        });

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

        addSkillSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entry = skillSetEntry.getText().toString();
                if(entry.equals("") || entry.equals(null) || entry.replaceAll("\\s", "").equals("")){
                    Toast.makeText(getApplicationContext(), "Please Enter Your Preferred Skill", Toast.LENGTH_LONG).show();
                } else {
                    setTag(skillSetEntry.getText().toString());
                }
            }
        });

    }

    private void setTag(String name) {
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
        chip.setCloseIconResource(R.drawable.ic_clear_black_24dp);
        chip.setCloseIconEnabled(true);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipGroup.removeView(chip);
            }
        });
        chipGroup.addView(chip);
        skillSetEntry.setText("");
    }

    @Override
    public void onBackPressed() {
        Intent about_intent=new Intent(this, TabbedActivityMain.class);
        startActivity(about_intent);
        about_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    }

    public void createProject(String projectName,String projecDesc){

        List<String> chipsTextList = new ArrayList<>();

        for (int i=0; i<chipGroup.getChildCount();i++){
            Chip chips = (Chip)chipGroup.getChildAt(i);
            chipsTextList.add(chips.getText().toString());
        }

        final Project project=new Project();
        project.setCreatorId(Objects.requireNonNull(currentUser.getCurrentUser()).getUid());
        project.setCreatorName(currentUser.getCurrentUser().getDisplayName());
        project.setProjectName(projectName);
        project.setProjectDescription(projecDesc);
        project.setCreatorEmail(currentUser.getCurrentUser().getEmail());
        project.setApplicantId(null);
        project.setApplicantList(null);
        project.setProjectStatus("Created");
        project.setRequiredSkills(chipsTextList);
        project.setWorkersList(null);
        project.setWorkersId(null);
        project.setProjectId(UUID.randomUUID().toString());
        project.setTaskList(null);
        project.setCategory(selectedCategory);


        Log.d(TAG, project.toString());

        db.collection("Projects")
                .document(project.getProjectId())
                .set(project)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Project Added");
                        db.collection("Projects").document(project.getProjectId()).update("workersId", FieldValue.arrayUnion(Objects.requireNonNull(currentUser.getCurrentUser()).getUid()))
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

        User user = SessionStorage.getUser(CreateProject.this);

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String text = parent.getItemAtPosition(position).toString();
        selectedCategory = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}