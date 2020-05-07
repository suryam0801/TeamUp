package com.example.teamup.ControlPanel.DisplayApplicants;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.Applicant;
import com.example.teamup.model.Project;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ApplicantDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApplicantDisplayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FirebaseFirestore db;
    private FirebaseAuth currentUser;
    private String TAG = "APPLICANTS_DISPLAY";
    private LinearLayout emptyPlaceHolder;
    private ListView lvApplicant;
    private List<Applicant> ApplicantList;
    private ApplicantListAdapter adapter;
    private Project project;

    public ApplicantDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ApplicantDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ApplicantDisplayFragment newInstance(String param1, String param2) {
        ApplicantDisplayFragment fragment = new ApplicantDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.activity_my_projects_view, container, false);

        lvApplicant = view.findViewById(R.id.listview_applicant);
        emptyPlaceHolder = view.findViewById(R.id.applicants_empty_display);
        project = SessionStorage.getProject(getActivity());
        assert project != null;
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance();
        clearNewApplicantCount();
        populateApplicantList(project);
        return view;
    }

    public void clearNewApplicantCount() {
        project.setNewApplicants(0);
        SessionStorage.saveProject(getActivity(), project);
        db.collection("Projects").document(project.getProjectId()).update("newApplicants", 0).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: " + "New Applicants Set To 0");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + "Failed to clear new applicant count");
            }
        });
    }

    public void populateApplicantList(Project project) {
        currentUser = FirebaseAuth.getInstance();

        loadApplicants(project.getProjectId());
    }

    public void loadApplicants(String projectQueryID) {
        ApplicantList = new ArrayList<>();

        Query myProjects = db.collection("Projects").whereEqualTo("projectId", Objects.requireNonNull(projectQueryID));
        myProjects.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    //each object in the array is a hashmap. We need to read the arrays using key and value from hashmap
                    List<Map<String, String>> group = (List<Map<String, String>>) document.get("applicantList");
                    //reads each object in the array

                    if (group != null) {
                        for (Map<String, String> entry : group) {
                            //we need to store "acceptedStatus" as a string, not a boolean. It will read fluently when all values are of a single data type
                            //reads each element in the hashmap
                            String applicantName = "";
                            String applicantId = "";
                            String applicantPitch = "";
                            String applicantAcceptedStatus = "";
                            String applicantProjectID = "";
                            String applicantEmail = "";
                            String applicantProfPicURL = "";
                            String applicantSpecialization = "";
                            String applicantLocation = "";
                            String applicantWorkingProjects = "";

                            for (String key : entry.keySet()) {
                                if (key.equals("applicantName"))
                                    applicantName = entry.get(key);
                                else if (key.equals("userId"))
                                    applicantId = entry.get(key);
                                else if (key.equals("shortPitch"))
                                    applicantPitch = entry.get(key);
                                else if (key.equals("acceptedStatus"))
                                    applicantAcceptedStatus = entry.get(key);
                                else if (key.equals("projectId"))
                                    applicantProjectID = entry.get(key);
                                else if (key.equals("applicantEmail"))
                                    applicantEmail = entry.get(key);
                                else if (key.equals("profilePicURL"))
                                    applicantProfPicURL = entry.get(key);
                                else if (key.equals("specialization"))
                                    applicantSpecialization = entry.get(key);
                                else if (key.equals("workingProject"))
                                    applicantWorkingProjects = String.valueOf(entry.get(key));
                                else if (key.equals("location"))
                                    applicantLocation = entry.get(key);
                            }

                            ApplicantList.add(new Applicant(applicantProjectID, applicantName, applicantEmail, applicantId,
                                    applicantAcceptedStatus, applicantPitch, applicantProfPicURL, applicantSpecialization,
                                    applicantLocation, Integer.parseInt(applicantWorkingProjects.trim())));
                        }

                    }
                }
                if(ApplicantList.isEmpty())
                    emptyPlaceHolder.setVisibility(View.VISIBLE);
                adapter = new ApplicantListAdapter(getActivity(), ApplicantList, project);
                project.setApplicantList(ApplicantList);
                lvApplicant.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("EXPLORE ACTIVITY", "onFailure: " + e.getMessage());
            }
        });
    }
}
