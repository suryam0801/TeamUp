package com.example.teamup.ControlPanel;

import android.animation.ArgbEvaluator;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.example.teamup.ControlPanel.DisplayApplicants.ApplicantsTabbedActivity;
import com.example.teamup.ControlPanel.ProjectWall.ProjectWall;
import com.example.teamup.ControlPanel.TaskList.TaskList;
import com.example.teamup.ControlPanel.chat.ChatRoom;
import com.example.teamup.ControlPanel.chat.ChatRoomBaseActivity;
import com.example.teamup.Explore.ExploreTab;
import com.example.teamup.TabbedActivityMain;
import com.example.teamup.model.Applicant;
import com.example.teamup.model.Project;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ControlPanel extends AppCompatActivity {

    private CardView projectWall, taskList, chatRoom, applicants;
    private Button projectWallbtn, chatroombtn, tasklistbtn, applicantsbtn;
    private String TAG = "CONTROL PANEL: ", MY_PREFS_NAME = "TeamUp", DEFAULT_RETRIEVE_VALUE = "no such project";
    GridLayout gridLayout;
    Project project;
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

        projectWall = findViewById(R.id.control_panel_project_wall_navigation);
        taskList = findViewById(R.id.control_panel_task_list_navigation);
        chatRoom = findViewById(R.id.control_panel_chat_room_navigation);
        applicants = findViewById(R.id.control_panel_applicants_navigation);

        projectWallbtn = findViewById(R.id.projectWall_newNotifications_button);
        chatroombtn = findViewById(R.id.chatRoom_newNotifications_button);
        tasklistbtn = findViewById(R.id.taskList_newNotifications_button);
        applicantsbtn = findViewById(R.id.applicants_newNotifications_button);

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
