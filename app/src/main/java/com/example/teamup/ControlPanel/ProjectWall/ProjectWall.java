package com.example.teamup.ControlPanel.ProjectWall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamup.Explore.Project;
import com.example.teamup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ProjectWall extends AppCompatActivity {


    private static final int PICK_IMAGE_REQUEST = 100;
    private FloatingActionButton fab;

    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private  Project project;
    private ArrayList<ProjectWallDataClass> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_wall);


        fab=findViewById(R.id.newResourceFAB);

        arrayList=new ArrayList<>();

        firebaseFirestore=FirebaseFirestore.getInstance();

        recyclerView=findViewById(R.id.projectViewRecyclerView);

        project = getIntent().getParcelableExtra("project");

        final Query query=firebaseFirestore.collection("ProjectWall").document(project.getProjectId())
                .collection("Files").orderBy("Time",Query.Direction.DESCENDING);

        final ProjectWallAdapter projectWallAdapter=new ProjectWallAdapter(this,arrayList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(projectWallAdapter);
        recyclerView.setHasFixedSize(true);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    arrayList.clear();
                    for(DocumentSnapshot doc:task.getResult()) {

                        ProjectWallDataClass projectWallDataClass=doc.toObject(ProjectWallDataClass.class);

                        arrayList.add(projectWallDataClass);

                        projectWallAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ProjectWall.this,UploadFile.class);
                intent.putExtra("project",project);
                startActivity(intent);
            }
        });



    }
}