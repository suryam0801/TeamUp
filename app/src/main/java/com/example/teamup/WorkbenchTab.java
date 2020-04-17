package com.example.teamup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.teamup.ControlPanel.ControlPanel;
import com.example.teamup.ControlPanel.DisplayApplicants.Applicant;
import com.example.teamup.Explore.Project;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkbenchTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkbenchTab extends Fragment implements WorkBenchRecyclerAdapter.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView myProjectsRv,workingProjectsRv,pastProjectsRv;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG= "WORKBENCHTAB";
    private FirebaseUser firebaseUser;
    private List<Project> myProjectList=new ArrayList<>();
    private List<Project> workingProjectList=new ArrayList<>();
    private List<Project> completedProjectsList=new ArrayList<>();
    private WorkbenchDisplayAdapter myAdapter;
    private WorkbenchDisplayAdapter workingAdapter;
    private WorkbenchDisplayAdapter completedAdapter;

    public WorkbenchTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WorkbenchTab.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkbenchTab newInstance(String param1, String param2) {
        WorkbenchTab fragment = new WorkbenchTab();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeAdapters();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        getMyProjects();
        getWorkingProjects();
        assert firebaseUser != null;

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onItemClick(Project project) {
        if (project.getCreatorId().equals(firebaseUser.getUid())) {
            Intent intent = new Intent(getActivity().getBaseContext(), ControlPanel.class);
            intent.putExtra("project", project);
            Log.d(TAG, "My Project:"+project.toString());
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.activity_work_bench, container, false);

        myProjectsRv=view.findViewById(R.id.my_projects_recycler_view);
        workingProjectsRv=view.findViewById(R.id.working_projects_recycler_view);
        pastProjectsRv=view.findViewById(R.id.past_projects_recycler_view);

        return view;
    }

    public void initializeAdapters(){
        myAdapter= new WorkbenchDisplayAdapter(getActivity(),myProjectList, true);
        workingAdapter= new WorkbenchDisplayAdapter(getActivity(),workingProjectList, false);
        completedAdapter= new WorkbenchDisplayAdapter(getActivity(),completedProjectsList, false);
    }

    public void populateData(){
        myProjectsRv.setAdapter(myAdapter);
        Log.d((TAG), "MYPROJECT: " + myProjectList.toString());
        myProjectsRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

        workingProjectsRv.setAdapter(workingAdapter);
        Log.d((TAG), "MYPROJECT: " + completedProjectsList.toString());
        workingProjectsRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });


        pastProjectsRv.setAdapter(completedAdapter);
        pastProjectsRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });

    }

    public void getMyProjects(){
        //Lists the projects creted by the particular user
        final Query myProjects = db.collection("Projects").whereEqualTo("creatorId", Objects.requireNonNull(firebaseUser.getUid()));
        myProjects.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    Project project = queryDocumentSnapshots.getDocuments().get(i).toObject(Project.class);
                    assert project != null;
                    if (project.getProjectStatus().equals("Completed"))
                    {
                        completedProjectsList.add(project);
                    }else {
                        myProjectList.add(project);
                    }
                }
            }
        });
    }

    public void getWorkingProjects(){
        //Gets the projects the user is working for
        Query myProjects = db.collection("Projects").whereArrayContains("applicantId", Objects.requireNonNull(firebaseUser.getUid()));
        myProjects.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG, "onSuccess: "+queryDocumentSnapshots.size());
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    Project project = queryDocumentSnapshots.getDocuments().get(i).toObject(Project.class);
                    if (project!=null&&project.getApplicantList()!=null) {
                        List<Applicant> applicantList = project.getApplicantList();
                        for(Applicant applicant:applicantList)
                        {
                            if (applicant.getUserId().equals(firebaseUser.getUid())&& applicant.getAcceptedStatus().equals("Accepted")) {
                                Log.d(TAG, "onSuccess: "+project.toString());
                                Log.d(TAG, "onSuccess: "+applicant.toString());
                                if (project.getProjectStatus().equals("Completed"))
                                {
                                    completedProjectsList.add(project);
                                }else{
                                    workingProjectList.add(project);
                                }
                            }
                        }
                    }
                }
                populateData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.getMessage());
            }
        });
    }
}
