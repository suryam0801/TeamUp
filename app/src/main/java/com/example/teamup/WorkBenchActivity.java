package com.example.teamup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamup.ControlPanel.ControlPanel;
import com.example.teamup.ControlPanel.DisplayApplicants.Applicant;
import com.example.teamup.Explore.ExploreActivity;
import com.example.teamup.Explore.Project;
import com.example.teamup.Explore.ProjectAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class WorkBenchActivity extends Activity implements WorkBenchRecyclerAdapter.OnItemClickListener {

    private ListView myProjectsRv,workingProjectsRv,pastProjectsRv;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG= WorkBenchActivity.class.getSimpleName();
    private FirebaseUser firebaseUser;
    private ArrayList<Project> myProjectList=new ArrayList<>();
    private ArrayList<Project> workingProjectList=new ArrayList<>();
    private ArrayList<Project> completedProjectsList=new ArrayList<>();
    private ProjectAdapter myAdapter;
    private ProjectAdapter workingAdapter;
    private ProjectAdapter completedAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_bench);
        initializeViews();
        initializeAdapters();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        getMyProjects();
        getWorkingProjects();
        assert firebaseUser != null;
        Log.d(TAG, "onCreate:"+firebaseUser.getEmail());
    }

    public void initializeViews(){
        myProjectsRv=findViewById(R.id.my_projects_recycler_view);
        workingProjectsRv=findViewById(R.id.working_projects_recycler_view);
        pastProjectsRv=findViewById(R.id.past_projects_recycler_view);
    }

    public void initializeAdapters(){
        myAdapter= new ProjectAdapter(getApplicationContext(),myProjectList, 1);
        workingAdapter= new ProjectAdapter(getApplicationContext(),workingProjectList, 1);
        completedAdapter= new ProjectAdapter(getApplicationContext(),completedProjectsList, 1);
    }

    public void populateData(){
        myProjectsRv.setAdapter(myAdapter);
        myProjectsRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(WorkBenchActivity.this,i + "",Toast.LENGTH_LONG).show();
            }
        });


        workingProjectsRv.setAdapter(workingAdapter);
        workingProjectsRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(WorkBenchActivity.this,i + "",Toast.LENGTH_LONG).show();
            }
        });


        pastProjectsRv.setAdapter(completedAdapter);
        pastProjectsRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(WorkBenchActivity.this,i + "",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onItemClick(Project project) {
        if (project.getCreatorId().equals(firebaseUser.getUid())) {
            Intent intent = new Intent(this, ControlPanel.class);
            intent.putExtra("project", project);
            Log.d(TAG, "My Project:"+project.toString());
            startActivity(intent);
        }
    }

    public void getMyProjects(){
        //Lists the projects creted by the particular user
        Query myProjects = db.collection("Projects").whereEqualTo("creatorId", Objects.requireNonNull(firebaseUser.getUid()));
        myProjects.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    Project project = queryDocumentSnapshots.getDocuments().get(i).toObject(Project.class);
                    assert project != null;
                    if (project.getProjectStatus().equals("Completed"))
                    {
                        completedProjectsList.add(project);
                        findViewById(R.id.linear_3_wb).setVisibility(View.VISIBLE);
                    }else {
                        myProjectList.add(project);
                        findViewById(R.id.linear_1_wb).setVisibility(View.VISIBLE);
                    }
                    //Log.d(TAG, "onSuccess: "+project.toString());
                }
            }
        });
    }

    public void getWorkingProjects(){
        //Gets the projects the user is working for
        Query myProjects = db.collection("Projects").whereArrayContains("applicantId", Objects.requireNonNull(firebaseUser.getUid()));
        myProjects.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG, "onSuccess: "+queryDocumentSnapshots.size());
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    Project project = queryDocumentSnapshots.getDocuments().get(i).toObject(Project.class);
                    if (project!=null&&project.getApplicantList()!=null) {
                        List<Applicant> applicantList = project.getApplicantList();
                        for(Applicant applicant:applicantList)
                        {
                            if (applicant.getUserId().equals(firebaseUser.getUid())&& applicant.getAcceptedStatus().equals("Accepted")) {
                                Log.d(TAG, "onSuccess: "+project.toString());
                                Log.d(TAG, "onSuccess: "+applicant.toString());
                                if (project.getProjectStatus().equals("Completed"))
                                {

                                    completedProjectsList.add(project);
                                    findViewById(R.id.linear_3_wb).setVisibility(View.VISIBLE);
                                }else{
                                    workingProjectList.add(project);
                                    findViewById(R.id.linear_2_wb).setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                }
                populateData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }
        });
    }
}