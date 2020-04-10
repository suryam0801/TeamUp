package com.example.teamup.ControlPanel;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.teamup.Explore.ExploreActivity;
import com.example.teamup.Explore.Project;
import com.example.teamup.R;

import java.util.ArrayList;
import java.util.List;

public class ControlPanel extends AppCompatActivity {
    ViewPager viewPager;
    TabAdapter adapter;
    List<Model> models;
    Integer[] colors = null;
    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        Project project=getIntent().getParcelableExtra("project");

        back=findViewById(R.id.btnback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ControlPanel.this, ExploreActivity.class));
                finish();
            }
        });

        models = new ArrayList<>();
        models.add(new Model(R.drawable.brochure, "Project Wall", "Post all the media content for your project and discuss about it as a team on this shared wall"));
        models.add(new Model(R.drawable.sticker, "Task List", "Decide amongst yourselves what tasks need to be accomplished and manage your priorities using this Task List"));
        models.add(new Model(R.drawable.poster, "Chatroom", "Enter the chatroom to have a conversation with everybody in your project and if needed, create your own chatroom with only those who are relevant"));
        models.add(new Model(R.drawable.namecard, "Applicants", "Since you have created this project, view a list of the applicants who want to join your project and accept or reject them"));

        adapter =  new TabAdapter(models, this, project);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setPadding(130, 0, 130, 0);

        Integer[] colors_temp = {
                getResources().getColor(R.color.color1),
                getResources().getColor(R.color.color2),
                getResources().getColor(R.color.color3),
                getResources().getColor(R.color.color4)
        };

        colors = colors_temp;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() -1) && position < (colors.length - 1)) {
                    viewPager.setBackgroundColor(

                            (Integer) argbEvaluator.evaluate(
                                    positionOffset,
                                    colors[position],
                                    colors[position + 1]
                            )
                    );
                }

                else {
                    viewPager.setBackgroundColor(colors[colors.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

}
