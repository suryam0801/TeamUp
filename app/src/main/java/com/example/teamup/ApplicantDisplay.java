package com.example.teamup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ApplicantDisplay extends AppCompatActivity {

    TableLayout applicantsDisplay;
    FirebaseFirestore db;
    FirebaseAuth currentUser;
    String TAG = "MY_PROJECTS_VIEW_ACTIVITY";
    ListView lvApplicant;
    private List<Applicant> ApplicantList, tempStore;
    private ApplicantListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_projects_view);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        currentUser=FirebaseAuth.getInstance();
        TextView projectNameDisplay = findViewById(R.id.myProjectNameDisplay);
        loadApplicants("SmartHostel:5");
        projectNameDisplay.setText("SmartHotel");
    }

    public void loadApplicants(String projectQueryID){
        db = FirebaseFirestore.getInstance();
        final Button applicant = new Button(this);
        lvApplicant = findViewById(R.id.listview_applicant);
        ApplicantList = new ArrayList<>();
        DocumentReference docRef = db.collection("Projects").document(projectQueryID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        List<String> group = (List<String>) document.get("applicants");
                        Log.d(TAG, "ListOfApplicants: " + group.toString());
                        for(int i = 0; i < group.size(); i+=2){
                            Scanner delimiter = new Scanner(group.get(i));
                            delimiter.useDelimiter(" /// ");
                            String id = delimiter.next();
                            String nme = delimiter.next();
                            ApplicantList.add(new Applicant(nme, id, group.get(i+1)));
                        }
                        adapter = new ApplicantListAdapter(getApplicationContext(), ApplicantList);
                        lvApplicant.setAdapter(adapter);
                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }
            }
        });
    }
}