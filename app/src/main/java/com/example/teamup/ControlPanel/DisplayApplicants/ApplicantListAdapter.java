package com.example.teamup.ControlPanel.DisplayApplicants;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.teamup.Notification.SendNotification;
import com.example.teamup.login.GatherUserDetails;
import com.example.teamup.model.Applicant;
import com.example.teamup.model.Project;
import com.example.teamup.R;
import com.example.teamup.model.Worker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApplicantListAdapter extends BaseAdapter implements BottomsheetDialog.BottomSheetListener {
    private Context mContext;
    private List<Applicant> ApplicantList;
    FirebaseFirestore db;
    Project project;
    String TAG = "APPLICANT_LIST_ADAPTER";

    public ApplicantListAdapter(Context mContext, List<Applicant> ApplicantList, Project project) {
        this.mContext = mContext;
        this.ApplicantList = ApplicantList;
        this.project = project;
    }

    @Override
    public int getCount() {
        return ApplicantList.size();
    }

    @Override
    public Object getItem(int position) {
        return ApplicantList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.applicant_view_list_item, null);
        final TextView name = v.findViewById(R.id.applicant_name);
        TextView pitch = v.findViewById(R.id.applicant_pitch);
        Button accept = v.findViewById(R.id.applicant_accept);
        Button review = v.findViewById(R.id.applicant_review);
        CircleImageView profPic = v.findViewById(R.id.applicant_profile_picture);

        db = FirebaseFirestore.getInstance();

        Glide.with(mContext)
                .load(ApplicantList.get(position).getProfilePicURL())
                .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_account_circle_black_24dp))
                .into(profPic);

        final Applicant selectedApplicant = ApplicantList.get(position);
        long workingProjects = selectedApplicant.getWorkingProject();
        workingProjects = workingProjects + 1;
        Log.d(TAG, "WORKING PROJECTS: " + workingProjects);
        selectedApplicant.setWorkingProject(workingProjects);


        //Set text for TextView
        final String nameDisplay = selectedApplicant.getApplicantName();
        final String pitchDisplay = String.valueOf(selectedApplicant.getShortPitch());
        name.setText(nameDisplay);
        pitch.setText(pitchDisplay);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "POSITION: " + project.toString());

                List<String> locationTags = new ArrayList<>();
                List<String> interestTags = new ArrayList<>();

                String locString = selectedApplicant.getLocationTags();
                locString.replace("[", "");
                locString.replace("]", "");

                String interestString = selectedApplicant.getInterestTags();
                interestString.replace("[", "");
                interestString.replace("]", "");


                Scanner scanlocation = new Scanner(locString);
                scanlocation.useDelimiter(", ");
                while (scanlocation.hasNext()){
                    locationTags.add(scanlocation.next());
                }

                Scanner scaninterest = new Scanner(interestString);
                scaninterest.useDelimiter(", ");
                while (scaninterest.hasNext()){
                    interestTags.add(scaninterest.next());
                }

                final Worker newWorker = new Worker(project.getProjectId(), selectedApplicant.getApplicantName(), selectedApplicant.getUserId(), selectedApplicant.getProfilePicURL(), locationTags, interestTags);
                ApplicantList.remove(selectedApplicant);
                db.collection("Projects").document(project.getProjectId()).update("workersList", FieldValue.arrayUnion(newWorker))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    db.collection("Projects").document(project.getProjectId()).update("applicantList", ApplicantList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            db.collection("Projects").document(project.getProjectId()).update("applicantId", FieldValue.arrayRemove(selectedApplicant.getUserId())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    SendNotification.sendnotification("application accepted", project.getProjectId(), project.getProjectName(), selectedApplicant.getUserId());
                                                    Log.d(TAG, "JOB SUCCESSFUL!!!!");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                }
                                            });
                                            db.collection("Projects").document(project.getProjectId()).update("workersId", FieldValue.arrayUnion(newWorker.getUserId())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "JOB SUCCESSFUL!!!!");
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
                                } else {
                                }
                            }
                        });

                db.collection("Users").document(selectedApplicant.getUserId()).update("workingProjects", selectedApplicant.getWorkingProject()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "JOB SUCCESSFUL!!!!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomsheetDialog bottomSheet = new BottomsheetDialog(selectedApplicant.getApplicantName(), selectedApplicant.getShortPitch(), selectedApplicant.getProjectId(), selectedApplicant.getUserId(), selectedApplicant, project.getProjectName(), ApplicantList);
                bottomSheet.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "exampleBottomSheet");
            }
        });
        //Save product id to tag
        v.setTag(ApplicantList.get(position).getUserId());

        return v;
    }

    @Override
    public String onButtonClicked(String text) {
        return text;
    }
}