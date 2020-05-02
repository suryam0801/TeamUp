package com.example.teamup.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;

import com.example.teamup.EditOrView.EditOrViewProfile;
import com.example.teamup.R;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SkillsPicker extends AppCompatActivity {

    BubblePicker bubblePicker;
    String[] skills = {"surya", "dinesh", "mathan", "jacob", "ajay", "arijit", "sanjay"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills_picker);

        bubblePicker = findViewById(R.id.skills_bubble_picker);
        ArrayList<PickerItem> listItems = new ArrayList<>();

        bubblePicker.setBubbleSize(10);
        bubblePicker.setCenterImmediately(true);
        bubblePicker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return skills.length;
            }

            @NotNull
            @Override
            public PickerItem getItem(int i) {
                PickerItem item = new PickerItem();
                item.setTitle(skills[i]);
                item.setGradient(new BubbleGradient(Color.BLACK,
                        Color.BLUE, BubbleGradient.VERTICAL));
                item.setTextColor(ContextCompat.getColor(SkillsPicker.this, android.R.color.white));
                return item;
            }
        });

        bubblePicker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem item) {

            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem item) {

            }
        });
    }
}
