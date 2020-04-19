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
import java.util.List;

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
    }

}
