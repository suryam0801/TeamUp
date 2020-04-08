package com.example.teamup.ControlPanel.DisplayApplicants;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamup.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ApplicantListAdapter extends BaseAdapter{
    private Context mContext;
    private List<Applicant> ApplicantList;
    private String projectId;

    public ApplicantListAdapter(Context mContext, List<Applicant> ApplicantList,String projectId) {
        this.mContext = mContext;
        this.ApplicantList = ApplicantList;
        this.projectId=projectId;
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
        View v = View.inflate(mContext, R.layout.applicant_view_list, null);
        final TextView name = v.findViewById(R.id.applicant_name);
        TextView pitch = v.findViewById(R.id.applicant_pitch);
        Button accept = v.findViewById(R.id.applicant_accept);
        Button reject = v.findViewById(R.id.applicant_reject);

        //Set text for TextView
        final String nameDisplay = ApplicantList.get(position).getApplicantName();
        final String pitchDisplay = String.valueOf(ApplicantList.get(position).getShortPitch());
        final String applicantID = String.valueOf(ApplicantList.get(position).getUserId());

        name.setText(nameDisplay);
        pitch.setText(pitchDisplay);

        final CollectionReference collectionReference=FirebaseFirestore.getInstance().collection("Projects");
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Applicant applicant=ApplicantList.get(position);
                collectionReference.document(projectId).update("applicantList", FieldValue.arrayRemove(applicant));
                collectionReference.document(projectId).update("applicantId",FieldValue.arrayRemove(applicant.getUserId()));
                applicant.setAcceptedStatus("Accepted");
                collectionReference.document(projectId).update("workersList", FieldValue.arrayUnion(applicant));
                Toast.makeText(view.getContext(), nameDisplay,Toast.LENGTH_SHORT).show();
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), pitchDisplay,Toast.LENGTH_SHORT).show();
            }
        });

        //Save product id to tag
        v.setTag(ApplicantList.get(position).getUserId());

        return v;
    }
}
