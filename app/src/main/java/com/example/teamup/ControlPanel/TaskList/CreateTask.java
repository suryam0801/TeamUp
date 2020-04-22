package com.example.teamup.ControlPanel.TaskList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.Project;
import com.example.teamup.model.Task;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class CreateTask extends AppCompatActivity {

    private TextView projectName;
    private EditText nameEdit, descriptionEdit;
    private RadioGroup priorityGroup;
    private RadioButton priorityButton;
    private Button createTask, discardTask;
    private Project project;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_create_task);

        projectName = findViewById(R.id.create_task_project_name);
        nameEdit = findViewById(R.id.task_name_edit);
        descriptionEdit = findViewById(R.id.task_description_edit);
        priorityGroup = findViewById(R.id.taskPriorityRadioGroup);
        createTask = findViewById(R.id.task_create_submit);
        discardTask = findViewById(R.id.task_create_discard);
        project = SessionStorage.getProject(CreateTask.this);
        projectName.setText(project.getProjectName());

        discardTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateTask.this, TaskList.class));
                finish();
            }
        });

        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = String.valueOf(nameEdit.getText());
                String taskDescription = String.valueOf(descriptionEdit.getText());
                String priority = (String) priorityButton.getText();

                User user = SessionStorage.getUser(CreateTask.this);

                Log.d("TAG", user.toString());

                Task task = new Task(taskName, taskDescription, UUID.randomUUID().toString(), priority, user.getUserId(), "ongoing");
                db.collection("Projects").document(project.getProjectId()).update("taskList", FieldValue.arrayUnion(task)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        int newTasks = project.getNewTasks() + 1;
                        project.setNewTasks(newTasks);
                        SessionStorage.saveProject(CreateTask.this, project);
                        db.collection("Projects").document(project.getProjectId()).update("newTasks", newTasks).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(CreateTask.this, TaskList.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

            }
        });

    }

    public void checkButton(View v) {
        int radioId = priorityGroup.getCheckedRadioButtonId();

        priorityButton = findViewById(radioId);

        Toast.makeText(this, "Selected Radio Button: " + priorityButton.getText(),
                Toast.LENGTH_SHORT).show();
    }
}
