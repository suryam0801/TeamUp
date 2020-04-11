package com.example.teamup.ControlPanel.TaskList;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamup.Explore.Project;
import com.example.teamup.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskList extends AppCompatActivity implements TaskDialog.TaskDialogListener {
    private static final String TAG = "TASKLIST";
    private Button newTaskButton;
    private Project project;
    private List<Task> tasks = new ArrayList<>();
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        newTaskButton = findViewById(R.id.new_task_button);
        project=getIntent().getParcelableExtra("project");
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        Log.d(TAG, "TASKLIST ON RETRIEVAL: " + project.toString());
        displayTasks();
    }

    private void displayTasks() {

    }

    public void openDialog() {
        TaskDialog exampleDialog = new TaskDialog();
        exampleDialog.show(getSupportFragmentManager(), "example dialog");
    }

    @Override
    public void applyTexts(String taskName, String taskDescription) {
        Log.d(TAG, "onSuccess: " + taskName + " | " + taskDescription);
        Task task = new Task(taskName, taskDescription, UUID.randomUUID().toString());
        Object[] array = {task};
        db.collection("Projects").document(project.getProjectId()).update("taskList", FieldValue.arrayUnion(array)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) { Log.d(TAG, "onSuccess: "+"Applicant Id update");
                }
        }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: "+"Applicant Id update");
                }
        });

    }
}
