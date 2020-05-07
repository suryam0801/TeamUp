package com.example.teamup.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.teamup.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class LocationTagPicker extends AppCompatActivity {

    private String loc, fName, lName, userId, downloadUri, contact;
    private List<String> locationTagsList;
    private Button setInterestTags, locationTagAdd;
    private ChipGroup chipGroup;
    private EditText locationTagEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_tag_picker);

        fName = getIntent().getStringExtra("fName");
        lName = getIntent().getStringExtra("lName");
        downloadUri = getIntent().getStringExtra("uri");
        contact = getIntent().getStringExtra("contact");

        setInterestTags = findViewById(R.id.setInterestTags);
        chipGroup = findViewById(R.id.location_tag_chip_group);
        locationTagEntry = findViewById(R.id.location_tags_entry);
        locationTagAdd = findViewById(R.id.location_tag_add_button);

        setInterestTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LocationTagPicker.this, InterestTagPicker.class);
                intent.putExtra("fName", fName);
                intent.putExtra("lName", lName);
                intent.putExtra("contact", contact);
                intent.putExtra("locationTags", locationTagsList.toString());
                if(downloadUri != null)
                    intent.putExtra("uri", downloadUri.toString());

                startActivity(intent);
            }
        });

        locationTagAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String interestTag = locationTagEntry.getText().toString();
                if (!interestTag.isEmpty()) {
                    setTag(interestTag);
                }
            }
        });

    }

    private void setTag(String name) {
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

            }
        });
        chipGroup.addView(chip);
        locationTagEntry.setText("#");
    }

}
