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
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.teamup.model.Applicant;
import com.example.teamup.model.Member;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberListAdapter extends BaseAdapter{
    private Context mContext;
    private List<Member> MemberList;
    FirebaseFirestore db;
    Project project;
    String TAG = "APPLICANT_LIST_ADAPTER";

    public MemberListAdapter(Context mContext, List<Member> MemberList) {
        this.mContext = mContext;
        this.MemberList = MemberList;
        this.project = project;
    }

    @Override
    public int getCount() {
        return MemberList.size();
    }

    @Override
    public Object getItem(int position) {
        return MemberList.get(position);
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
                .load(MemberList.get(position).getProfilePicURL())
                .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_account_circle_black_24dp))
                .into(profPic);


        final Member a = MemberList.get(position);

        //Set text for TextView
        final String nameDisplay = a.getMemberName();
        final String pitchDisplay = String.valueOf(a.getShortPitch());
        name.setText(nameDisplay);
        pitch.setText(pitchDisplay);

        //Save product id to tag
        v.setTag(MemberList.get(position).getUserId());

        return v;
    }
}