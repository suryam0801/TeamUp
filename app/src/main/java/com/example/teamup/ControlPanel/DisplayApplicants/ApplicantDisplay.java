package com.example.teamup.ControlPanel.DisplayApplicants;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamup.Explore.Project;
import com.example.teamup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApplicantDisplay extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth currentUser;
    String TAG = "MY_PROJECTS_VIEW_ACTIVITY";
    ListView lvApplicant;
    private List<Applicant> ApplicantList;
    private ApplicantListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_projects_view);
        lvApplicant = findViewById(R.id.listview_applicant);
        Project project=getIntent().getParcelableExtra("project");
        assert project != null;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        currentUser=FirebaseAuth.getInstance();
        TextView projectNameDisplay = findViewById(R.id.myProjectNameDisplay);
        projectNameDisplay.setText(project.getProjectName());
        populateApplicantList(project);
    }

    public void populateApplicantList(Project project){
        List<Applicant> list=new ArrayList<>();
        list=project.getApplicantList();
        adapter=new ApplicantListAdapter(getApplicationContext(), list);
        lvApplicant.setAdapter(adapter);
    }

}