package com.example.teamup.ControlPanel.DisplayApplicants;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.teamup.model.Worker;
import com.example.teamup.model.Project;
import com.example.teamup.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberListAdapter extends BaseAdapter{
    private Context mContext;
    private List<Worker> workerList;
    FirebaseFirestore db;
    Project project;
    String TAG = "APPLICANT_LIST_ADAPTER";

    public MemberListAdapter(Context mContext, List<Worker> workerList) {
        this.mContext = mContext;
        this.workerList = workerList;
        this.project = project;
    }

    @Override
    public int getCount() {
        return workerList.size();
    }

    @Override
    public Object getItem(int position) {
        return workerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.member_view_list_item, null);
        final TextView name = v.findViewById(R.id.member_name);
        TextView pitch = v.findViewById(R.id.member_primaryskill);
        CircleImageView profPic = v.findViewById(R.id.member_profile_picture);

        db = FirebaseFirestore.getInstance();

        Glide.with(mContext)
                .load(workerList.get(position).getProfilePicURL())
                .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_account_circle_black_24dp))
                .into(profPic);


        final Worker worker = workerList.get(position);

        //Set text for TextView
        final String nameDisplay = worker.getMemberName();
        final String specialization = String.valueOf(worker.getSpecialization());
        name.setText(nameDisplay);
        pitch.setText(specialization);

        //Save product id to tag
        v.setTag(workerList.get(position).getUserId());

        return v;
    }
}