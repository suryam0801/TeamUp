package com.example.teamup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ControlPanel extends AppCompatActivity {
    TabLayout tabLayout;
    TabItem tabp1,tabp2,tabp3,tabp4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);
    tabLayout=findViewById(R.id.tablayout);
    tabp1=findViewById(R.id.panel1);
    tabp2=findViewById(R.id.panel2);
    tabp3=findViewById(R.id.panel3);
    tabp4=findViewById(R.id.panel4);



    }
}
