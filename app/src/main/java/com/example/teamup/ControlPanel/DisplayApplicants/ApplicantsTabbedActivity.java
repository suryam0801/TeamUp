package com.example.teamup.ControlPanel.DisplayApplicants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.teamup.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ApplicantsTabbedActivity extends AppCompatActivity  implements BottomsheetDialog.BottomSheetListener  {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem newRequests, membersDisplay;
    public PagerAdapterApplicants pagerAdapter;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_applicants_tabbed);

        tabLayout = findViewById(R.id.applicants_tab_layout);
        back = findViewById(R.id.bck_applicants);
        newRequests = findViewById(R.id.applicants_new_requests);
        //membersDisplay = findViewById(R.id.main_workbench_tab);
        viewPager = findViewById(R.id.applicants_viewpager);

        pagerAdapter = new PagerAdapterApplicants(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*startActivity(new Intent(ApplicantsTabbedActivity.this, ControlPanel.class));
                finish();*/
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    @Override
    public String onButtonClicked(String text) {
        Toast.makeText(this,"Application "+text,Toast.LENGTH_SHORT).show();
        return text;
    }
}
