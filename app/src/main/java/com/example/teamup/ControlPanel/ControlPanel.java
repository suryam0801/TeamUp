package com.example.teamup.ControlPanel;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.teamup.Explore.ExploreTab;
import com.example.teamup.model.Project;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ControlPanel extends AppCompatActivity {

    private LinearLayout projectWall, taskList, chatRoom, applicants;
    private Button projectWallbtn, chatroombtn, tasklistbtn, applicantsbtn;
    private String TAG = "CONTROL PANEL: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar

        setContentView(R.layout.activity_control_panel);

        projectWall = findViewById(R.id.control_panel_project_wall_navigation);
        taskList = findViewById(R.id.control_panel_task_list_navigation);
        chatRoom = findViewById(R.id.control_panel_chat_room_navigation);
        applicants = findViewById(R.id.control_panel_applicants_navigation);

        projectWallbtn = findViewById(R.id.projectWall_newNotifications_button);
        chatroombtn = findViewById(R.id.chatRoom_newNotifications_button);
        tasklistbtn = findViewById(R.id.taskList_newNotifications_button);
        applicantsbtn = findViewById(R.id.applicants_newNotifications_button);

        Project project = SessionStorage.getProject(ControlPanel.this);
        Log.d(TAG, project.toString());

        // data stored in format: tasklist / applicants / chatroom / projectwall
        String newValues = getIntent().getStringExtra("newValues");
        int length = newValues.length();
        newValues = newValues.substring(1, length-1);
        Log.d(TAG, newValues);
        Scanner scan = new Scanner(newValues);
        scan.useDelimiter(",");
        int task = Integer.parseInt(scan.next().trim());
        int application = Integer.parseInt(scan.next().trim());

        if(task > 0){
            tasklistbtn.setVisibility(View.VISIBLE);
            tasklistbtn.setText(task + " new");
        }
        if(application > 0){
            applicantsbtn.setVisibility(View.VISIBLE);
            applicantsbtn.setText(application + " new");
        }
    }

}
