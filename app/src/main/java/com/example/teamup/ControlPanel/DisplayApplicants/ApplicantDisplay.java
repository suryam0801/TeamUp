package com.example.teamup.ControlPanel.DisplayApplicants;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamup.Explore.Project;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ApplicantDisplay extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth currentUser;
    String TAG = "APPLICANTS_DISPLAY";
    ListView lvApplicant;
    private List<Applicant> ApplicantList;
    private ApplicantListAdapter adapter;
    Project project;
    private SessionStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_projects_view);
        lvApplicant = findViewById(R.id.listview_applicant);
        project=storage.getProject(ApplicantDisplay.this);
        assert project != null;
        db = FirebaseFirestore.getInstance();
        currentUser=FirebaseAuth.getInstance();
        TextView projectNameDisplay = findViewById(R.id.myProjectNameDisplay);
        projectNameDisplay.setText(project.getProjectName());
        populateApplicantList(project);
    }

    public void populateApplicantList(Project project){
        currentUser=FirebaseAuth.getInstance();
        TextView projectNameDisplay = findViewById(R.id.myProjectNameDisplay);
        loadApplicants(project.getProjectId());
        projectNameDisplay.setText(project.getProjectName());
    }

    public void loadApplicants(String projectQueryID){
        lvApplicant = findViewById(R.id.listview_applicant);
        ApplicantList = new ArrayList<>();

        Query myProjects = db.collection("Projects").whereEqualTo("projectId", Objects.requireNonNull(projectQueryID));
        myProjects.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    //each object in the array is a hashmap. We need to read the arrays using key and value from hashmap
                    List<Map<String, String>> group = (List<Map<String, String>>) document.get("applicantList");
                    //reads each object in the array
                    for (Map<String, String> entry : group) {
                        //we need to store "acceptedStatus" as a string, not a boolean. It will read fluently when all values are of a single data type
                        //reads each element in the hashmap
                        String applicantName = "";
                        String applicantId = "";
                        String applicantPitch = "";
                        String acceptedStatus = "";
                        String projectID = "";
                        String applicantEmail = "";

                        for (String key : entry.keySet()) {
                            if(key.equals("applicantName"))
                                applicantName = entry.get(key);
                            else if(key.equals("userId"))
                                applicantId = entry.get(key);
                            else if(key.equals("shortPitch"))
                                applicantPitch = entry.get(key);
                            else if(key.equals("acceptedStatus"))
                                acceptedStatus = entry.get(key);
                            else if(key.equals("projectId"))
                                projectID = entry.get(key);
                            else if(key.equals("applicantEmail"))
                                applicantEmail = entry.get(key);
                        }
                        ApplicantList.add(new Applicant(projectID, applicantName, applicantEmail, applicantId, acceptedStatus, applicantPitch));
                    }
                }
                adapter = new ApplicantListAdapter(getApplicationContext(), ApplicantList, project);
                project.setApplicantList(ApplicantList);
                lvApplicant.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("EXPLORE ACTIVITY", "onFailure: "+e.getMessage());
            }
        });
    }
}