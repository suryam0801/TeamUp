package com.example.teamup.Explore;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamup.ControlPanel.ControlPanel;
import com.example.teamup.ControlPanel.DisplayApplicants.Applicant;
import com.example.teamup.CreateProject;
import com.example.teamup.R;
import com.example.teamup.WorkBenchActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExploreActivity extends Activity {

    public final String TAG=ExploreActivity.this.getClass().getSimpleName();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth currentUser;
    ListView lvproject;
    private List<Project> ProjectList;
    ProgressBar progressBar;
    Project projects;
    private ProjectAdapter adapter;
    Button createProject,workbench;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        progressBar=findViewById(R.id.progress_bar);
        currentUser=FirebaseAuth.getInstance();
        createProject = findViewById(R.id.addproject);
        workbench=findViewById(R.id.workbench);
        dialog = new Dialog(ExploreActivity.this);


        createProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ExploreActivity.this, CreateProject.class));
                finish();
            }
        });
        workbench.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExploreActivity.this, WorkBenchActivity.class));
                finish();
            }
        });

        projects=new Project();
        loadprojectlist();
    }

    public void loadprojectlist()
    {
        lvproject=findViewById(R.id.listview_explore_projects);
        ProjectList=new ArrayList<>();
        if (ProjectList.size()>0)
            ProjectList.clear();
        db.collection("Projects")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()&&task.getResult()!=null)
                        {
                            for (DocumentSnapshot documentSnapshot:task.getResult())
                            {
                                Project project= documentSnapshot.toObject(Project.class);
//                                Log.d(TAG, project.toString());
//                                Log.d(TAG, "____________________________________________________");
                                ProjectList.add(project);
                            }
                            adapter= new ProjectAdapter(getApplicationContext(),ProjectList);
                            lvproject.setAdapter(adapter);

                            progressBar.setVisibility(View.INVISIBLE);

                            lvproject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Toast.makeText(ExploreActivity.this,i + "",Toast.LENGTH_LONG).show();
                                    showDialogue(i);
                                }
                            });

                        }else {
                            Log.d("Logger","No such Valid Item");
                            Toast.makeText(ExploreActivity.this,"No such Document",Toast.LENGTH_LONG).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ExploreActivity.this,e.toString(),Toast.LENGTH_LONG).show();

            }
        });
    }

    public void showDialogue(final int pos){
        dialog.setContentView(R.layout.explore_project_display_popup);

        TextView projectName = dialog.findViewById(R.id.popup_project_name);
        TextView creatorName = dialog.findViewById(R.id.popup_project_creator);
        TextView projectShortDescription = dialog.findViewById(R.id.popup_project_short_description);
        TextView projectLongDescription = dialog.findViewById(R.id.popup_project_long_description);
        Button cancelButton = dialog.findViewById(R.id.popup_cancel_button);
        Button acceptButton = dialog.findViewById(R.id.popup_accept_button);

        projectName.setText(ProjectList.get(pos).getProjectName());
        creatorName.setText("Created By: " + ProjectList.get(pos).getCreatorName());
        projectShortDescription.setText(ProjectList.get(pos).getProjectDescription());
        projectLongDescription.setText("Looks like this project's creator has not provided a detailed description.");

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call apply dialogue
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Thank you Vist Again!!!!",Toast.LENGTH_SHORT).show();
        finishAffinity();
        System.exit(0);
    }

    public void saveApplicant(String shortPitch, final String projectId){
        final Applicant applicant=new Applicant();
        applicant.setUserId(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        applicant.setApplicantName(currentUser.getCurrentUser().getDisplayName());
        applicant.setProjectId(projectId);
        applicant.setAcceptedStatus("Applied");
        applicant.setShortPitch(shortPitch);
        applicant.setApplicantEmail(Objects.requireNonNull(currentUser.getCurrentUser()).getEmail());

        Object[] array={applicant};

        db.collection("Projects").document(projectId).update("applicantList", FieldValue.arrayUnion(array))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Log.d(TAG, "onComplete: "+"Success");
                            List<String> applicantIds = new ArrayList<>();
                            if(projects.getApplicantId()==null){
                                applicantIds.add(applicant.getUserId());
                                db.collection("Projects").document(projectId).update("applicantId",FieldValue.arrayUnion(applicantIds)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: "+"Applicant Id update");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: "+"Applicant Id update");
                                    }
                                });
                            } else {
                                db.collection("Projects").document(projectId).update("applicantId",FieldValue.arrayUnion(applicant.getUserId())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: "+"Applicant Id update");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: "+"Applicant Id update");
                                    }
                                });
                            }

                        }else {
                            Log.d(TAG, "onComplete: "+"Failure");
                        }
                    }
                });
    }

}
