package com.example.teamup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class createProject extends AppCompatActivity {

    ArrayList<String> listOfSkills = new ArrayList<String>();
    String TAG = "CreateProject", pName, pDesc;
    FirebaseFirestore db;
    int newProjectID=0;
    TableLayout skillsetDisplay;
    FirebaseAuth currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project2);

        //Initializing firestore
        db = FirebaseFirestore.getInstance();
        currentUser=FirebaseAuth.getInstance();

        //initializing all UI elements
        final EditText projName = (EditText)findViewById(R.id.projectName);
        final EditText projDescription = (EditText)findViewById(R.id.projectDescription);
        final EditText skillEntry = (EditText)findViewById(R.id.skillEntry);
        Button skillEntrySubmit = (Button)findViewById(R.id.skillEntrySubmit);
        Button createProjectSubmit = (Button)findViewById(R.id.createProjectSubmit);

        //Listens for onClick on skillEntries and calls the addSkills function
        skillEntrySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String skillName = skillEntry.getText().toString();
                if(!skillName.equals("") || skillName.trim().length() != 0) {
                    listOfSkills.add(skillName);
                    addSkills(skillName);
                    skillEntry.setText("");
                }
            }
        });

        createProjectSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                pName = projName.getText().toString();
                pDesc = projDescription.getText().toString();

                DocumentReference docRef = db.collection("Projects").document("ProjectIdCounter");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                newProjectID = Integer.parseInt(document.get("idNumber").toString()) + 1 ;
                                makeDatabaseEntry();
                                projName.setText("");
                                projDescription.setText("");
                                if(skillsetDisplay.getChildCount() > 0){
                                    skillsetDisplay.removeAllViews();
                                    listOfSkills.clear();
                                }
                            } else {
                                Log.d("LOGGER", "No such document");
                            }
                        } else {
                            Log.d("LOGGER", "get failed with ", task.getException());
                        }
                    }
                });
            }
        });
    }

    public void makeDatabaseEntry(){

        //creating document of new project
        Map<String, Object> docData = new HashMap<>();
        docData.put("projectId", newProjectID);
        docData.put("projectName", pName);
        docData.put("projectDescription", pDesc);
        docData.put("skillsRequired", listOfSkills);
        docData.put("creatorID", currentUser.getUid());
        // use: docData.put("regions", Arrays.asList("item1", "item2")); to add list of applicants
        docData.put("applicants","");

        //adding created project to user profile in User Database
        DocumentReference docRef = db.collection("Users").document(currentUser.getUid());
        docRef.update("CreatedProjects", FieldValue.arrayUnion(pName + ":" + newProjectID));

        //updating projectIdCounter
        Map<String, Object> newID = new HashMap<>();
        newID.put("idNumber", newProjectID);

        //adding a new project
        db.collection("Projects").document(pName + ":" + newProjectID).set(docData, SetOptions.merge());
        //updating projectIdValue
        db.collection("Projects").document("ProjectIdCounter").set(newID);
    }

    //Adds skills to the listView for user to see
    public void addSkills(String skillName){
/*
        //logic for creating buttons across multiple rows
        for (int i = 0; i < 3; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < 4; j++) {
                Button btnTag = new Button(this);
                btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                btnTag.setText(skillName);
                btnTag.setId(j + 1 + (i * 4));
                row.addView(btnTag);
            }

            skillsetDisplay.addView(row);
        }
*/

        skillsetDisplay = (TableLayout) findViewById(R.id.skilssDisplay);
        Button b = new Button(this);
        skillsetDisplay.addView(b);
        b.setText(skillName);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}