package com.example.teamup.ControlPanel.DisplayApplicants;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.teamup.EditOrView.EditOrViewProfile;
import com.example.teamup.Notification.SendNotification;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.Applicant;
import com.example.teamup.model.Broadcast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BottomsheetDialog extends BottomSheetDialogFragment {


    private BottomSheetListener mListener;
    TextView name, desc, requestjoin;
    private Context mContext;
    private List<Applicant> ApplicantList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth;
    Broadcast broadcast;
    String TAG = "APPLICANT_LIST_ADAPTER";
    String applname, appldesc, project_id, uid;
    Applicant a = new Applicant();
    ;
    String projectName;

    public BottomsheetDialog(String applname, String appldesc, String project_id, String uid, Applicant a, String projectName, List<Applicant> ApplicantList) {
        this.applname = applname;
        this.appldesc = appldesc;
        this.project_id = project_id;
        this.uid = uid;
        this.a = a;
        this.projectName = projectName;
        this.ApplicantList = ApplicantList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottomsheetlayout, container, false);
        name = v.findViewById(R.id.btmname);
        desc = v.findViewById(R.id.btmdesc);
        requestjoin = v.findViewById(R.id.reqjoin);
        Button reject = v.findViewById(R.id.btnreject);
        Button accept = v.findViewById(R.id.btnaccept);
        Button viewProfile = v.findViewById(R.id.view_applicant_profile_button);
        CircleImageView profilePic = v.findViewById(R.id.bottom_sheet_prof_pic_applicantDisplay);

        requestjoin.setText(a.getShortPitch());
        name.setText(applname);
        desc.setText(a.getApplicantPhn());

        broadcast = SessionStorage.getProject(getActivity());

        Glide.with(getContext())
                .load(a.getProfilePicURL())
                .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.ic_account_circle_black_24dp))
                .into(profilePic);


        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditOrViewProfile.class);
                intent.putExtra("userID", a.getUserId());
                intent.putExtra("flag", "applicant");
                startActivity(intent);
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicantList.remove(a);
                mListener.onButtonClicked("Rejected");
                SendNotification.sendnotification("application rejected", broadcast.getBroadcastId(), broadcast.getBroadcastName(), a.getUserId());
                Log.d(TAG, "THIS PROJECT ID" + broadcast.getBroadcastId());
                db.collection("Projects").document(broadcast.getBroadcastId()).update("applicantList", ApplicantList).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection("Projects").document(broadcast.getBroadcastId()).update("applicantId", FieldValue.arrayRemove(a.getUserId())).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "JOB SUCCESSFUL!!!!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "JOB Failed!!!!");
                            }
                        });
                    }
                });
                dismiss();
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonClicked("Accepted");
                db.collection("Projects").document(broadcast.getBroadcastId()).update("workersList", FieldValue.arrayUnion(a))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    db.collection("Projects").document(broadcast.getBroadcastId()).update("applicantList", FieldValue.arrayRemove(a)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            db.collection("Projects").document(broadcast.getBroadcastId()).update("applicantId", FieldValue.arrayRemove(a.getUserId())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "JOB SUCCESSFUL!!!!");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "JOB Failed!!!!");
                                                }
                                            });
                                            db.collection("Projects").document(broadcast.getBroadcastId()).update("workersId", FieldValue.arrayUnion(a.getUserId())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "JOB SUCCESSFUL!!!!");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG, "JOB Failed!!!!");
                                                }
                                            });
                                        }
                                    });
                                    SendNotification.sendnotification("application accepted", broadcast.getBroadcastId(), broadcast.getBroadcastName(), a.getUserId());
                                } else {
                                }
                            }
                        });
                dismiss();
            }


        });
        return v;
    }

    public interface BottomSheetListener {
        String onButtonClicked(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
