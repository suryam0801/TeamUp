package com.example.teamup.ControlPanel;

import android.animation.ArgbEvaluator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.example.teamup.ControlPanel.DisplayApplicants.ApplicantsTabbedActivity;
import com.example.teamup.ControlPanel.ProjectWall.ProjectWall;
import com.example.teamup.ControlPanel.TaskList.TaskList;
import com.example.teamup.ControlPanel.chat.ChatRoom;
import com.example.teamup.ControlPanel.chat.ChatRoomBaseActivity;
import com.example.teamup.EditOrView.EditOrViewProfile;
import com.example.teamup.EditOrView.EditOrViewProject;
import com.example.teamup.Explore.ExploreTab;
import com.example.teamup.TabbedActivityMain;
import com.example.teamup.model.Applicant;
import com.example.teamup.model.Project;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ControlPanel extends AppCompatActivity {

    private CardView projectWall, taskList, chatRoom, applicants;
    private Button projectWallbtn, chatroombtn, tasklistbtn, applicantsbtn, editProject, removeProject;
    private String TAG = "CONTROL PANEL: ", MY_PREFS_NAME = "TeamUp", DEFAULT_RETRIEVE_VALUE = "no such project";
    private GridLayout gridLayout;
    private Project project;
    FirebaseFirestore db;
    private ImageButton back;
    private Dialog removeConfirm;
    int newApplicants = 0, newTasks = 0;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar

        setContentView(R.layout.activity_control_panel);

        gridLayout = findViewById(R.id.gridLayoutControlPanel);
        setSingleEvent(gridLayout);

        db = FirebaseFirestore.getInstance();

        projectWall = findViewById(R.id.control_panel_project_wall_navigation);
        taskList = findViewById(R.id.control_panel_task_list_navigation);
        chatRoom = findViewById(R.id.control_panel_chat_room_navigation);
        applicants = findViewById(R.id.control_panel_applicants_navigation);
        removeConfirm = new Dialog(ControlPanel.this);

        projectWallbtn = findViewById(R.id.projectWall_newNotifications_button);
        chatroombtn = findViewById(R.id.chatRoom_newNotifications_button);
        tasklistbtn = findViewById(R.id.taskList_newNotifications_button);
        applicantsbtn = findViewById(R.id.applicants_newNotifications_button);
        back = findViewById(R.id.bck_control_panel);
        editProject = findViewById(R.id.control_panel_edit_project);
        removeProject = findViewById(R.id.control_panel_remove_project);

        editProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ControlPanel.this, EditOrViewProject.class));
                finish();
            }
        });

        removeProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRemoveProjectDialog();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ControlPanel.this, TabbedActivityMain.class));
                finish();
            }
        });

        project = SessionStorage.getProject(ControlPanel.this);

        newTasks = project.getNewTasks();
        newApplicants = project.getNewApplicants();

        if (newTasks > 0) {
            tasklistbtn.setVisibility(View.VISIBLE);
            tasklistbtn.setText(newTasks + " new");
        }
        if (newApplicants > 0) {
            applicantsbtn.setVisibility(View.VISIBLE);
            applicantsbtn.setText(newApplicants + " new");
        }
    }

    public void showRemoveProjectDialog() {
        removeConfirm.setContentView(R.layout.remove_user_dialog_layout);
        TextView title = removeConfirm.findViewById(R.id.remove_dialog_title);
        TextView description = removeConfirm.findViewById(R.id.remove_dialog_description);
        Button remove = removeConfirm.findViewById(R.id.remove_user_accept_button);
        title.setText("Delete " + project.getProjectName());
        description.setText("Are you sure you want to delete " + project.getProjectName() + "?");
        remove.setText("Delete");
        Button cancel = removeConfirm.findViewById(R.id.remove_user_cancel_button);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeConfirm.dismiss();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeProject();
                removeConfirm.dismiss();
            }
        });

        removeConfirm.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        removeConfirm.show();
    }

    public void removeProject() {
        db.collection("Projects").document(project.getProjectId()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        User user = SessionStorage.getUser(ControlPanel.this);
        int newCreatedProjects = user.getCreatedProjects() - 1;
        user.setCreatedProjects(newCreatedProjects);
        db.collection("Users").document(user.getUserId()).update("createdProjects", user.getCreatedProjects())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "JOB SUCCESSFUL!!!!");
                        startActivity(new Intent(ControlPanel.this, TabbedActivityMain.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();

        project = SessionStorage.getProject(ControlPanel.this);
        newTasks = project.getNewTasks();
        newApplicants = project.getNewApplicants();

        if (newTasks == 0) {
            tasklistbtn.setVisibility(View.INVISIBLE);
        }
        if (newApplicants == 0) {
            applicantsbtn.setVisibility(View.INVISIBLE);
        }
    }

    private void setSingleEvent(GridLayout gridLayout) {

        preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            CardView cardView = (CardView) gridLayout.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (finalI) {
                        case 0:
                            Intent projectWallIntent = new Intent(getApplicationContext(), ProjectWall.class);
                            startActivity(projectWallIntent);
                            break;
                        case 1:
                            Intent taskListIntent = new Intent(getApplicationContext(), TaskList.class);
                            startActivity(taskListIntent);
                            break;
                        case 2:
                            Intent chatRoomIntent = new Intent(getApplicationContext(), ChatRoomBaseActivity.class);
                            startActivity(chatRoomIntent);
                            break;
                        case 3:
                            Intent applicantsIntent = new Intent(getApplicationContext(), ApplicantsTabbedActivity.class);
                            startActivity(applicantsIntent);
                            break;
                    }
                }
            });
        }
    }

}
