package com.example.teamup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.teamup.model.Applicant;
import com.example.teamup.model.Project;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth currentUser;
    private Button button;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button);
        currentUser=FirebaseAuth.getInstance();
        saveProject();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //currentUser.signOut();
                startActivity(new Intent(MainActivity.this,WorkBenchActivity.class));
                //finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser.getCurrentUser()==null)
        {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }

    }


    public void saveProject(){
        if (currentUser.getCurrentUser()!=null) {
            Project project=new Project();
            project.setCreatorId(currentUser.getCurrentUser().getUid());
            project.setCreatorFirstName("Gowtham");
            project.setCreatorLastName("K K");
            project.setProjectName("Lorem Ipsum");
            project.setProjectDescription("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum");
            project.setCreatorEmail(currentUser.getCurrentUser().getEmail());
            project.setApplicantId(null);
            project.setApplicantList(null);
            project.setProjectStatus("Created");
            project.setRequiredSkills(null);
            project.setProjectId(UUID.randomUUID().toString()
            );
            db.collection("Projects")
                    .document(project.getProjectId())
                    .set(project)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: Project Added");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onSuccess: Project Not Added");
                        }
                    });
        }
    }

    public void saveApplicant(final String projectId){
        final Applicant applicant=new Applicant();
        applicant.setUserId("hSL6TQCJciVllERsQv0nHD9Y5Q43");
        applicant.setApplicantName("Surya");
        applicant.setProjectId(projectId);
        applicant.setAcceptedStatus(false);
        applicant.setShortPitch("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam");
        applicant.setApplicantEmail(Objects.requireNonNull(currentUser.getCurrentUser()).getEmail());
        Object[] array={applicant};
        db.collection("Projects").document(projectId).update("applicantList", FieldValue.arrayUnion(array))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Log.d(TAG, "onComplete: "+"Success");
                            db.collection("Projects").document(projectId).update("applicantId",FieldValue.arrayUnion(applicant.getUserId())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: "+"Applicant Id update");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: "+"Applicant Id update");
                                }
                            });
                        }else {
                            Log.d(TAG, "onComplete: "+"Failure");
                        }
                    }
                });
    }

}
