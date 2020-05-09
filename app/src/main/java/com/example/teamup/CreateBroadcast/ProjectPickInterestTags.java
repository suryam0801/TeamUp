package com.example.teamup.CreateBroadcast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.teamup.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

public class ProjectPickInterestTags extends AppCompatActivity {

    private List<String> interestTagList = new ArrayList<>(), locationTagList = new ArrayList<>();
    private Button finalizeInterestTag, interestTagAdd;
    private ChipGroup chipGroup;
    private EditText interestTagEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_pick_interest_tags);


        finalizeInterestTag = findViewById(R.id.broadcast_finalize_interest_tags);
        chipGroup = findViewById(R.id.broadcast_interest_tag_chip_group);
        interestTagEntry = findViewById(R.id.broadcast_interest_tags_entry);
        interestTagAdd = findViewById(R.id.broadcast_interest_tag_add_button);

        locationTagList = getIntent().getExtras().getStringArrayList("locationTags");

        finalizeInterestTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectPickInterestTags.this, CreateBroadcast.class);
                intent.putStringArrayListExtra("locationTags", (ArrayList<String>) locationTagList);
                intent.putStringArrayListExtra("interestTags", (ArrayList<String>) interestTagList);
                startActivity(intent);
            }
        });

        interestTagEntry.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(interestTagEntry, InputMethodManager.SHOW_IMPLICIT);
                        interestTagEntry.setShowSoftInputOnFocus(true);
                        interestTagEntry.setText("#");
                        interestTagEntry.setSelection(interestTagEntry.getText().length());
                        break;
                    case MotionEvent.ACTION_UP:
                        v.performClick();
                        break;
                    default:
                        break;
                }
                return true;

            }
        });

        interestTagAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String interestTag = interestTagEntry.getText().toString();
                if (!interestTag.isEmpty()) {
                    setTag(interestTag);
                }
            }
        });

    }

    private void setTag(final String name) {
        interestTagList.add(name);
        final Chip chip = new Chip(this);
        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()
        );
        chip.setPadding(
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 3,
                        getResources().getDisplayMetrics()
                ),
                paddingDp, paddingDp, paddingDp);
        chip.setText(name);
        chip.setTextAppearanceResource(R.style.ChipTextStyle_Selected);
        chip.setCloseIconResource(R.drawable.ic_clear_black_24dp);
        chip.setCloseIconEnabled(true);

        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipGroup.removeView(chip);
                interestTagList.remove(name);
                Log.d("LOCATIONTAGPICKER", interestTagList.toString());
            }
        });
        chipGroup.addView(chip);
        interestTagEntry.setText("#");
        interestTagEntry.setSelection(interestTagEntry.getText().length());
    }

}
