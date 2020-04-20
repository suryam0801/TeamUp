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
import android.widget.ListView;

import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.Applicant;
import com.example.teamup.model.Member;
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
 * Use the {@link AllMembersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllMembersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView listViewWorkers;
    private List<Member> WorkersList;
    private MemberListAdapter adapter;
    Project project;
    FirebaseFirestore db;
    FirebaseAuth currentUser;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllMembersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllMembersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllMembersFragment newInstance(String param1, String param2) {
        AllMembersFragment fragment = new AllMembersFragment();
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

        View view = inflater.inflate(R.layout.fragment_all_members, container, false);

        listViewWorkers = view.findViewById(R.id.listview_allmembers);
        project= SessionStorage.getProject(getActivity());
        assert project != null;
        db = FirebaseFirestore.getInstance();
        currentUser=FirebaseAuth.getInstance();
        populateApplicantList(project);

        return view;
    }

    public void loadMembers(String projectQueryID){
        WorkersList = new ArrayList<>();

        Query myProjects = db.collection("Projects").whereEqualTo("projectId", Objects.requireNonNull(projectQueryID));
        myProjects.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                    //each object in the array is a hashmap. We need to read the arrays using key and value from hashmap
                    List<Map<String, String>> group = (List<Map<String, String>>) document.get("workersList");
                    //reads each object in the array

                    if(group != null) {
                        for (Map<String, String> entry : group) {
                            //we need to store "acceptedStatus" as a string, not a boolean. It will read fluently when all values are of a single data type
                            //reads each element in the hashmap
                            String applicantName = "";
                            String applicantId = "";
                            String applicantPitch = "";
                            String acceptedStatus = "";
                            String projectID = "";
                            String applicantEmail = "";

                            for (String key : entry.keySet()) {
                                if(key.equals("applicantName"))
                                    applicantName = entry.get(key);
                                else if(key.equals("userId"))
                                    applicantId = entry.get(key);
                                else if(key.equals("shortPitch"))
                                    applicantPitch = entry.get(key);
                                else if(key.equals("acceptedStatus"))
                                    acceptedStatus = entry.get(key);
                                else if(key.equals("projectId"))
                                    projectID = entry.get(key);
                                else if(key.equals("applicantEmail"))
                                    applicantEmail = entry.get(key);
                            }
                            SharedPreferences sharedPref = getActivity().getSharedPreferences("Current User", Activity.MODE_PRIVATE);
                            String string = sharedPref.getString("user","1234");
                            User user = new Gson().fromJson(string, User.class);

                            WorkersList.add(new Member(projectID, applicantName, applicantEmail, applicantId,
                                    acceptedStatus, applicantPitch, user.getProfileImageLink(), user.getSpecialization(),
                                    user.getLocation()));
                        }
                    }
                }
                adapter = new MemberListAdapter(getActivity(), WorkersList);
                listViewWorkers.setAdapter(adapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("EXPLORE ACTIVITY", "onFailure: "+e.getMessage());
            }
        });
    }

    public void populateApplicantList(Project project){
        currentUser=FirebaseAuth.getInstance();

        loadMembers(project.getProjectId());
    }
}
