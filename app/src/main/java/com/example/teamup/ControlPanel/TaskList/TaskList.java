package com.example.teamup.ControlPanel.TaskList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamup.CreateProject;
import com.example.teamup.model.Project;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.Task;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class TaskList extends AppCompatActivity {
    private static final String TAG = "TASKLIST";
    private Button newTaskButton;
    private Project project;
    private List<Task> tasksHigh = new ArrayList<>();
    private List<Task> tasksMedium = new ArrayList<>();
    private List<Task> tasksLow = new ArrayList<>();
    private List<Task> TaskList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TaskAdapter adapter;
    ListView lvApplicant;
    private SessionStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_task_list);
        storage = new SessionStorage();
        lvApplicant = findViewById(R.id.listview_applicant);
        newTaskButton = findViewById(R.id.new_task_button);
        project = storage.getProject(TaskList.this);
        clearNewTaskCount();
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTask();
            }
        });
        loadTasks(project.getProjectId());
    }

    public void clearNewTaskCount(){
        project.setNewTasks(0);
        SessionStorage.saveProject(TaskList.this, project);
        db.collection("Projects").document(project.getProjectId()).update("newTasks", 0).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: "+"New Tasks Set To 0");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+"Failed to clear new task count");
            }
        });
    }

    public void loadTasks(String projectQueryID) {
        lvApplicant = findViewById(R.id.listview_applicant);
        TaskList = new ArrayList<>();

        Query myProjects = db.collection("Projects").whereEqualTo("projectId", Objects.requireNonNull(projectQueryID));
        myProjects.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    //each object in the array is a hashmap. We need to read the arrays using key and value from hashmap
                    List<Map<String, String>> group = (List<Map<String, String>>) document.get("taskList");
                    //reads each object in the array
                    if (group != null) {
                        for (Map<String, String> entry : group) {
                            //we need to store "acceptedStatus" as a string, not a boolean. It will read fluently when all values are of a single data type
                            //reads each element in the hashmap
                            String taskName = "";
                            String taskDescription = "";
                            String taskId = "";
                            String priority = "";
                            String creatorId = "";
                            String taskStatus = "";

                            for (String key : entry.keySet()) {
                                if (key.equals("taskName"))
                                    taskName = entry.get(key);
                                if (key.equals("taskDescription"))
                                    taskDescription = entry.get(key);
                                if (key.equals("taskID"))
                                    taskId = entry.get(key);
                                if (key.equals("priority"))
                                    priority = entry.get(key);
                                if (key.equals("creatorId"))
                                    creatorId = entry.get(key);
                                if (key.equals("taskStatus"))
                                    taskStatus = entry.get(key);
                            }

                            Task task = new Task(taskName, taskDescription, taskId, priority, creatorId, taskStatus);

                            switch (priority) {
                                case "High":
                                    tasksHigh.add(task);
                                    break;
                                case "Medium":
                                    tasksMedium.add(task);
                                    break;
                                case "Low":
                                    tasksLow.add(task);
                                    break;
                            }
                        }
                    }
                }

                if (tasksHigh != null)
                    for (Task t : tasksHigh)
                        TaskList.add(t);

                if (tasksMedium != null)
                    for (Task t : tasksMedium)
                        TaskList.add(t);

                if (tasksLow != null)
                    for (Task t : tasksLow)
                        TaskList.add(t);

                adapter = new TaskAdapter(getApplicationContext(), TaskList);
                project.setTaskList(TaskList);
                lvApplicant.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("EXPLORE ACTIVITY", "onFailure: " + e.getMessage());
            }
        });
    }

    public void createNewTask() {
        Intent intent = new Intent(TaskList.this, CreateTask.class);
        startActivity(intent);
    }

}
