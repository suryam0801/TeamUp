package com.example.teamup.WorkBench;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.example.teamup.CircleWall.CircleWall;
import com.example.teamup.CreateBroadcast.CreateBroadcast;
import com.example.teamup.model.Broadcast;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.Worker;
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
import java.util.Scanner;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkbenchTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkbenchTab extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView myProjectsRv, workingProjectsRv, pastProjectsRv;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "WORKBENCHTAB";
    private FirebaseUser firebaseUser;
    private List<Broadcast> myBroadcastList = new ArrayList<>();
    private List<Broadcast> workingBroadcastList = new ArrayList<>();
    private List<Broadcast> completedProjectsList = new ArrayList<>();
    private WorkbenchDisplayAdapter myAdapter;
    private ViewPager viewPager;
    private WorkbenchDisplayAdapter workingAdapter;
    private LinearLayout myProjectsEmpty, workingProjectsEmpty, completedProjectsEmpty;
    private WorkbenchDisplayAdapter completedAdapter;
    private Button createProject, exploreProjects;
    private String MY_PREFS_NAME = "TeamUp", DEFAULT_RETRIEVE_VALUE = "no such project";

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

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        initializeAdapters();
        getMyProjects();
        getWorkingProjects();
        assert firebaseUser != null;

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.activity_work_bench, container, false);

        myProjectsRv = view.findViewById(R.id.my_projects_recycler_view);
        workingProjectsRv = view.findViewById(R.id.working_projects_recycler_view);
        pastProjectsRv = view.findViewById(R.id.past_projects_recycler_view);
        myProjectsEmpty = view.findViewById(R.id.workbench_empty_myprojects_placeholder);
        workingProjectsEmpty = view.findViewById(R.id.workbench_empty_workingProjects_placeholder);
        createProject = view.findViewById(R.id.workbench_create_project_nav);
        exploreProjects = view.findViewById(R.id.workbench_explore_projects_nav);
        viewPager = getActivity().findViewById(R.id.main_viewpager);

        createProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CreateBroadcast.class));
                getActivity().finish();
            }
        });

        exploreProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });

        return view;
    }

    public void initializeAdapters() {
        myAdapter = new WorkbenchDisplayAdapter(getActivity(), myBroadcastList, true);
        workingAdapter = new WorkbenchDisplayAdapter(getActivity(), workingBroadcastList, false);
        completedAdapter = new WorkbenchDisplayAdapter(getActivity(), completedProjectsList, false);
    }

    public void populateData() {

        if (!myBroadcastList.isEmpty()) {
            myProjectsEmpty.setVisibility(View.GONE);
            myProjectsRv.setVisibility(View.VISIBLE);
            myProjectsRv.setAdapter(myAdapter);
            myProjectsRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    SessionStorage.saveProject(getActivity(), myBroadcastList.get(i));
                    setNewItemValues();
                    Intent intent = new Intent(getActivity().getBaseContext(), CircleWall.class);
                    startActivity(intent);
                }
            });
            Utility.setListViewHeightBasedOnChildren(myProjectsRv);
        } else {
            myProjectsEmpty.setVisibility(View.VISIBLE);
            myProjectsRv.setVisibility(View.GONE);
        }

        if (!workingBroadcastList.isEmpty()) {
            workingProjectsEmpty.setVisibility(View.GONE);
            workingProjectsRv.setVisibility(View.VISIBLE);
            workingProjectsRv.setAdapter(workingAdapter);
            workingProjectsRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d(TAG, "WORKBENCH ACTIVITY: " + workingBroadcastList.get(i).toString());
                    SessionStorage.saveProject(getActivity(), workingBroadcastList.get(i));
                    setNewItemValues();
                    Intent intent = new Intent(getActivity().getBaseContext(), CircleWall.class);
                    startActivity(intent);
                }
            });
            Utility.setListViewHeightBasedOnChildren(workingProjectsRv);
        } else {
            workingProjectsEmpty.setVisibility(View.VISIBLE);
            workingProjectsRv.setVisibility(View.GONE);
        }

        pastProjectsRv.setAdapter(completedAdapter);
        pastProjectsRv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //figuring out what to do when completed projects is selected
            }
        });
        Utility.setListViewHeightBasedOnChildren(pastProjectsRv);
    }

    public static class Utility {

        public static void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }

    public void setNewItemValues() {

        Broadcast p = SessionStorage.getProject(getActivity());


        SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (prefs.getString(p.getBroadcastName(), DEFAULT_RETRIEVE_VALUE).equals(DEFAULT_RETRIEVE_VALUE)) {
            // data stored in format: tasklist / applicants / chatroom / projectwall
            editor.putString(p.getBroadcastName(), 0 + "/" + 0 + "/" + 0 + "/" + 0);
            editor.commit();
        } else {
            String values = prefs.getString(p.getBroadcastName(), DEFAULT_RETRIEVE_VALUE);
            Scanner scan = new Scanner(values);
            scan.useDelimiter("/");

            int t = Math.abs(p.getTaskList().size() - Integer.parseInt(scan.next()));
            int a = Math.abs(p.getApplicantId().size() - Integer.parseInt(scan.next()));

            editor.putString(p.getBroadcastName().trim(), t + "/" + a + "/" + 0 + "/" + 0);
            editor.commit();
        }
    }

    public void getMyProjects() {
        Log.d(TAG, "INSIDE GET PROJECTS: " + firebaseUser.getUid());
        //Lists the projects creted by the particular user
        final Query myProjects = db.collection("Projects").whereEqualTo("creatorId", Objects.requireNonNull(firebaseUser.getUid()));
        myProjects.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    Broadcast broadcast = queryDocumentSnapshots.getDocuments().get(i).toObject(Broadcast.class);
                    assert broadcast != null;
                    if (broadcast.getBroadcastStatus().equals("Completed")) {
                        completedProjectsList.add(broadcast);
                    } else {
                        myBroadcastList.add(broadcast);
                    }
                }
                populateData();
            }
        });
    }

    public void getWorkingProjects() {
        //Gets the projects the user is working for
        Query myProjects = db.collection("Projects").whereArrayContains("workersId", Objects.requireNonNull(firebaseUser.getUid()));
        myProjects.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG, "onSuccess: " + queryDocumentSnapshots.size());
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    Broadcast broadcast = queryDocumentSnapshots.getDocuments().get(i).toObject(Broadcast.class);
                    if (broadcast != null && broadcast.getWorkersId() != null && !broadcast.getCreatorId().equals(firebaseUser.getUid())) {
                        List<Worker> workerList = broadcast.getWorkersList();
                        for (Worker worker : workerList) {
                            if (worker.getUserId().equals(firebaseUser.getUid())) {
                                if (broadcast.getBroadcastStatus().equals("Completed"))
                                    completedProjectsList.add(broadcast);
                                else
                                    workingBroadcastList.add(broadcast);
                            }
                        }
                    }
                }
                populateData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });
    }
}
