package com.example.teamup.EditOrView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.teamup.ControlPanel.ControlPanel;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.Broadcast;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditOrViewProject extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String TAG = "EDIT PROJECT", selectedCategory = "";
    private FirebaseFirestore db;
    private FirebaseAuth currentUser;
    private List<String> skillset;
    private ChipGroup editProjectChipGroup;
    private ImageButton bck;
    private Broadcast broadcast;
    private Spinner spinner;
    private EditText editprojName, editprojDescription, editskillSetEntry;
    private Button editSkillAdd, finalizeChanges, editDiscard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_edit_project);

        db = FirebaseFirestore.getInstance();

        bck = findViewById(R.id.bck_create);
        editprojName = findViewById(R.id.edit_project_projectname);
        editprojDescription = findViewById(R.id.edit_project_projectDescription);
        finalizeChanges = findViewById(R.id.edit_project_finalize_changes);
        editDiscard = findViewById(R.id.edit_project_discardChanges);
        editskillSetEntry = findViewById(R.id.edit_project_skill_entry_edittext);
        editSkillAdd = findViewById(R.id.edit_project_addSkillSet);
        editProjectChipGroup = findViewById(R.id.editProject_chip_group_skills);

        skillset = new ArrayList<>();

        broadcast = SessionStorage.getProject(EditOrViewProject.this);

        editprojName.setText(broadcast.getBroadcastName());
        editprojDescription.setText(broadcast.getBroadcastDescription());
        skillset = broadcast.getInterestTags();
        for (String s: skillset)
            setTag(s);

        spinner = findViewById(R.id.edit_project_categorySpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.interactive, android.R.layout.simple_spinner_item);
        List<String> categoryList = Arrays.asList((getResources().getStringArray(R.array.interactive)));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        int index = categoryList.indexOf(broadcast.getCategory());
        spinner.setSelection(index);

        editSkillAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entry = editskillSetEntry.getText().toString();
                if(entry.equals("") || entry.equals(null) || entry.replaceAll("\\s", "").equals("")){
                    Toast.makeText(getApplicationContext(), "Please Enter Your Preferred Skill", Toast.LENGTH_LONG).show();
                } else {
                    setTag(entry);
                }
            }
        });

        editprojName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                finalizeChanges.setBackground(getResources().getDrawable(R.drawable.confirm_application_buttom_background));
                finalizeChanges.setTextColor(Color.parseColor("#35C80B"));
            }
        });

        editprojDescription.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                finalizeChanges.setBackground(getResources().getDrawable(R.drawable.confirm_application_buttom_background));
                finalizeChanges.setTextColor(Color.parseColor("#35C80B"));
            }
        });

        editskillSetEntry.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                finalizeChanges.setBackground(getResources().getDrawable(R.drawable.confirm_application_buttom_background));
                finalizeChanges.setTextColor(Color.parseColor("#35C80B"));
            }
        });

        finalizeChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalizeChanges(editprojName.getText().toString(), editprojDescription.getText().toString());
            }
        });
        editDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditOrViewProject.this, ControlPanel.class));
                finish();
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
                editProjectChipGroup.removeView(chip);
            }
        });
        editProjectChipGroup.addView(chip);
        editskillSetEntry.setText("");
    }

    public void finalizeChanges(String projectName,String projecDesc){
        List<String> chipsTextList = new ArrayList<>();

        for (int i=0; i<editProjectChipGroup.getChildCount();i++){
            Chip chips = (Chip)editProjectChipGroup.getChildAt(i);
            chipsTextList.add(chips.getText().toString());
        }

        broadcast.setBroadcastName(projectName);
        broadcast.setBroadcastDescription(projecDesc);
        broadcast.setInterestTags(chipsTextList);
        broadcast.setCategory(selectedCategory);

        db.collection("Projects").document(broadcast.getBroadcastId()).set(broadcast);

        startActivity(new Intent(EditOrViewProject.this, ControlPanel.class));
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
        String text = parent.getItemAtPosition(position).toString();
        selectedCategory = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
