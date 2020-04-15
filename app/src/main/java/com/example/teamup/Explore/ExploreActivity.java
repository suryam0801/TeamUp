package com.example.teamup.Explore;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
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

public class ExploreActivity extends AppCompatActivity implements Dialogue.DialogueInterfaceListener {

    public final String TAG=ExploreActivity.this.getClass().getSimpleName();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth currentUser;
    SwipeMenuListView lvproject;
    private List<Project> ProjectList;
    ProgressBar progressBar;
    Project projects;
    int globalIndex;
    private ProjectAdapter adapter;
    String proid="";
    Button createProject,workbench;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        progressBar=findViewById(R.id.progress_bar);
        currentUser=FirebaseAuth.getInstance();
        createProject = findViewById(R.id.addproject);
        workbench=findViewById(R.id.workbench);


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
        lvproject=(SwipeMenuListView) findViewById(R.id.listview);

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
                                ProjectList.add(project);
                            }
                            adapter= new ProjectAdapter(getApplicationContext(),ProjectList);
                            lvproject.setAdapter(adapter);

                            SwipeMenuCreator creator = new SwipeMenuCreator() {

                                @Override
                                public void create(SwipeMenu menu) {
                                    // create "open" item
                                    SwipeMenuItem openItem = new SwipeMenuItem(
                                            getApplicationContext());
                                    // set item background
                                    openItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x66,
                                            0xff)));
                                    // set item width
                                    openItem.setWidth(250);
                                    // set item title
                                    openItem.setTitle("Apply");
                                    // set item title fontsize
                                    openItem.setTitleSize(18);
                                    // set item title font color
                                    openItem.setTitleColor(Color.WHITE);
                                    // add to menu
                                    menu.addMenuItem(openItem);


                                    // create "delete" item
//                                    SwipeMenuItem deleteItem = new SwipeMenuItem(
//                                            getApplicationContext());
//                                    // set item backgroundmenu.getViewType()
//                                    deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
//                                            0x3F, 0x25)));
//                                    // set item width
//                                    deleteItem.setWidth(170);
//                                    // set a icon
//                                    deleteItem.setTitle("Apply");
////                                    deleteItem.setIcon(R.drawable.ic_phone);
//                                    // add to menu
//                                    menu.addMenuItem(deleteItem);
                                }
                            };

                            lvproject.setMenuCreator(creator);

                            lvproject.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                                    switch (index) {
                                        case 0:
                                            apply(ProjectList.get(position).getProjectId());
                                            globalIndex = position;
                                            break;
                                    }
                                    return false;
                                }
                            });


                            progressBar.setVisibility(View.INVISIBLE);

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

    private void apply(String projectId) {
        Dialogue dialog=new Dialogue(projectId);
        dialog.show(getSupportFragmentManager(),"Dialogue");
    }

    @Override
    public void applydesc(String shortdesc,String projectId) {
            saveApplicant(shortdesc,projectId);
//        DocumentReference addedDocRef = db.collection("Projects").document();
//        String refId = addedDocRef.getId();
//        String applicanid=currentUser.getUid();
//        String pitch=shortdesc;
//        String pid=ProjectList.get(globalIndex).getProjectId();
//        Map<String,Object> docData=new HashMap<>();
//        docData.put("applicantID",applicanid);
//        docData.put("pitch",pitch);
//        docData.put("projectID",pid);
//        db.collection("Applicants").add(docData);
//        Toast.makeText(this,"Your Application Has been Successfully sended to Project Creator!!!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Thank you Vist Again!!!!",Toast.LENGTH_SHORT).show();
    }

    public void saveApplicant(String shortPitch, final String projectId){
        final Applicant applicant=new Applicant();
        applicant.setUserId(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        applicant.setApplicantName("Surya");
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
                        }else {
                            Log.d(TAG, "onComplete: "+"Failure");
                        }
                    }
                });
    }

}
