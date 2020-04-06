package com.example.teamup.ControlPanel.DisplayApplicants;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamup.R;

import java.util.List;

public class ApplicantListAdapter extends BaseAdapter{
    private Context mContext;
    private List<Applicant> ApplicantList;

    public ApplicantListAdapter(Context mContext, List<Applicant> ApplicantList) {
        this.mContext = mContext;
        this.ApplicantList = ApplicantList;
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.applicant_view_list, null);
        final TextView name = v.findViewById(R.id.applicant_name);
        TextView pitch = v.findViewById(R.id.applicant_pitch);
        Button accept = v.findViewById(R.id.applicant_accept);
        Button reject = v.findViewById(R.id.applicant_reject);

        //Set text for TextView
        final String nameDisplay = ApplicantList.get(position).getApplicantName();
        final String pitchDisplay = String.valueOf(ApplicantList.get(position).getShortPitch());
        name.setText(nameDisplay);
        pitch.setText(pitchDisplay);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
