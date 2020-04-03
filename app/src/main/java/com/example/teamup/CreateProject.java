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

public class CreateProject extends AppCompatActivity {

    String TAG = "CreateProject", pName, pDesc;
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
                pName = projName.getText().toString();
                pDesc = projDescription.getText().toString();
                makeDatabaseEntry();
                projName.setText("");
                projDescription.setText("");
            }
        });
    }

    public void makeDatabaseEntry(){
        //retrieving document reference ID
        DocumentReference addedDocRef = db.collection("Projects").document();
        String refId = addedDocRef.getId();

        //creating document of new project
        Map<String, Object> docData = new HashMap<>();
        docData.put("projectId", refId);
        docData.put("projectName", pName);
        docData.put("projectDescription", pDesc);
        docData.put("projectStatus", "In Progress");
        docData.put("creatorID", currentUser.getUid());

        //adding a new project
        db.collection("Projects").add(docData);
    }
}