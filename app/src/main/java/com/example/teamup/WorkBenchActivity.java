package com.example.teamup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.teamup.ControlPanel.DisplayApplicants.Applicant;
import com.example.teamup.ControlPanel.DisplayApplicants.ApplicantDisplay;
import com.example.teamup.Explore.Project;
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


public class WorkBenchActivity extends AppCompatActivity implements WorkBenchRecyclerAdapter.OnItemClickListener {

    private RecyclerView myProjectsRv,workingProjectsRv,pastProjectsRv;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG= WorkBenchActivity.class.getSimpleName();
    private WorkBenchRecyclerAdapter wb1,wb2,wb3;
    private FirebaseUser firebaseUser;
    private ArrayList<Project> myProjectList=new ArrayList<>();
    private ArrayList<Project> workingProjectList=new ArrayList<>();
    private ArrayList<Project> completedProjectsList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_bench);
        initializeViews();
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

    public void populateData(){
        wb1 = new WorkBenchRecyclerAdapter(this, myProjectList, this);
        myProjectsRv.setLayoutManager(new LinearLayoutManager(this));
        myProjectsRv.setHasFixedSize(true);
        myProjectsRv.setAdapter(wb1);
        wb1.notifyDataSetChanged();
        wb2 = new WorkBenchRecyclerAdapter(this, workingProjectList,this);
        workingProjectsRv.setLayoutManager(new LinearLayoutManager(this));
        workingProjectsRv.setHasFixedSize(true);
        workingProjectsRv.setAdapter(wb2);
        wb2.notifyDataSetChanged();
        wb3 = new WorkBenchRecyclerAdapter(this, completedProjectsList, this);
        pastProjectsRv.setLayoutManager(new LinearLayoutManager(this));
        pastProjectsRv.setHasFixedSize(true);
        pastProjectsRv.setAdapter(wb3);
        wb3.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Project project) {
        if (project.getCreatorId().equals(firebaseUser.getUid())) {
            Intent intent = new Intent(this, ApplicantDisplay.class);
            intent.putExtra("project", project);
            Log.d(TAG, "onItemClick: "+project.toString());
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
                    Log.d(TAG, "onSuccess: "+project.toString());
                }
            }
        });
    }

    public void getWorkingProjects(){
        //Gets the projects the user is working for
        Query myProjects = db.collection("Projects").whereArrayContains("workersId", Objects.requireNonNull(firebaseUser.getUid()));
        myProjects.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG, "onSuccess: "+queryDocumentSnapshots.size());
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    Project project = queryDocumentSnapshots.getDocuments().get(i).toObject(Project.class);
                    if (project!=null&&project.getWorkersList()!=null) {
                        List<Applicant> workersList = project.getWorkersList();
                        for(Applicant applicant:workersList)
                        {
                            if (applicant.getUserId().equals(firebaseUser.getUid())) {
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
