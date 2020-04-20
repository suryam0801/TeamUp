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

import com.example.teamup.model.Applicant;
import com.example.teamup.model.Project;
import com.example.teamup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ApplicantListAdapter extends BaseAdapter{
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
        ImageView profPic = v.findViewById(R.id.applicant_profile_picture);
        
        db = FirebaseFirestore.getInstance();

        final Applicant a = ApplicantList.get(position);

        //Set text for TextView
        final String nameDisplay = a.getApplicantName();
        final String pitchDisplay = String.valueOf(a.getShortPitch());
        final String applicantID = String.valueOf(a.getUserId());
        final String acceptedStatus = String.valueOf(a.getAcceptedStatus());
        name.setText(nameDisplay);
        pitch.setText(pitchDisplay);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "POSITION: " + project.toString());
                a.setAcceptedStatus("accepted");
                ApplicantList.remove(a);
                db.collection("Projects").document(project.getProjectId()).update("workersList", FieldValue.arrayUnion(a))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    db.collection("Projects").document(project.getProjectId()).update("applicantList",ApplicantList).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            db.collection("Projects").document(project.getProjectId()).update("applicantId",FieldValue.arrayRemove(a.getUserId())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "JOB SUCCESSFUL!!!!");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                }
                                            });
                                            db.collection("Projects").document(project.getProjectId()).update("workersId",FieldValue.arrayUnion(a.getUserId())).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                }else {
                                }
                            }
                        });
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        //Save product id to tag
        v.setTag(ApplicantList.get(position).getUserId());

        return v;
    }
}


/*ApplicantList.remove(a);
                db.collection("Projects").document(project.getProjectId()).update("applicantList",ApplicantList).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection("Projects").document(project.getProjectId()).update("applicantId",FieldValue.arrayRemove(a.getUserId())).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
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
                });*/