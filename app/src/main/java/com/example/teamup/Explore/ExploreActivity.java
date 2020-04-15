package com.example.teamup.Explore;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamup.ControlPanel.ControlPanel;
import com.example.teamup.ControlPanel.DisplayApplicants.Applicant;
import com.example.teamup.CreateProject;
import com.example.teamup.R;
import com.example.teamup.WorkBenchActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.internal.FlowLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.Inflater;

public class ExploreActivity extends Activity {

    public final String TAG=ExploreActivity.this.getClass().getSimpleName();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth currentUser;
    ListView lvproject;
    private List<Project> ProjectList;
    Project projects;
    private ProjectAdapter adapter;
    Button createProject,workbench;
    Dialog dialog;
    ChipGroup chipGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_explore);
        currentUser=FirebaseAuth.getInstance();
        createProject = findViewById(R.id.addproject);
        workbench=findViewById(R.id.workbench);
        dialog = new Dialog(ExploreActivity.this);
        chipGroup = findViewById(R.id.chip_group_create_skills);

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
        Chip chip1 = dialog.findViewById(R.id.chip1);
        Chip chip2 = dialog.findViewById(R.id.chip2);
        Chip chip3 = dialog.findViewById(R.id.chip3);
        Chip chip4 = dialog.findViewById(R.id.chip4);
        Chip chip5 = dialog.findViewById(R.id.chip5);
        Chip chip6 = dialog.findViewById(R.id.chip6);
        Chip chip7 = dialog.findViewById(R.id.chip7);
        Chip chip8 = dialog.findViewById(R.id.chip8);
        Chip chip9 = dialog.findViewById(R.id.chip9);
        Chip chip10 = dialog.findViewById(R.id.chip10);
        Chip chip11 = dialog.findViewById(R.id.chip11);
        Chip chip12 = dialog.findViewById(R.id.chip12);
        Chip chip13 = dialog.findViewById(R.id.chip13);
        Chip chip14 = dialog.findViewById(R.id.chip14);
        Chip chip15 = dialog.findViewById(R.id.chip15);

        projectName.setText(ProjectList.get(pos).getProjectName());
        creatorName.setText("Created By: " + ProjectList.get(pos).getCreatorName());
        projectShortDescription.setText(ProjectList.get(pos).getProjectDescription());

        List<Chip> allChips = new ArrayList<>();
        allChips.add(chip1);
        allChips.add(chip2);
        allChips.add(chip3);
        allChips.add(chip4);
        allChips.add(chip5);
        allChips.add(chip6);
        allChips.add(chip7);
        allChips.add(chip8);
        allChips.add(chip9);
        allChips.add(chip10);
        allChips.add(chip11);
        allChips.add(chip12);
        allChips.add(chip13);
        allChips.add(chip14);
        allChips.add(chip15);

        List<String> skillsArray = new ArrayList<>();
        skillsArray = (ProjectList.get(pos).getRequiredSkills());

        int i = 0;

        if(skillsArray!=null){
            for(String s : skillsArray){
                allChips.get(i).setText(s);
                allChips.get(i).setVisibility(View.VISIBLE);
                i++;
            }
        }

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

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Thank you Vist Again!!!!",Toast.LENGTH_SHORT).show();
        finishAffinity();
        System.exit(0);
    }

    private void setChips(List<String> skills) {

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
