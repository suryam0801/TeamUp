package com.example.teamup.ControlPanel.ProjectWall;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.teamup.R;
import com.example.teamup.model.Comment;
import com.example.teamup.model.Project;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends BaseAdapter {

    private Context mContext;
    private List<Comment> CommentList;
    private LinearLayout iconBackground, headerBackground;

    public CommentAdapter(Context mContext, List<Comment> CommentList) {
        this.mContext = mContext;
        this.CommentList = CommentList;
    }



    @Override
    public int getCount() {
        return CommentList.size();
    }

    @Override
    public Object getItem(int position) {
        return CommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final View pview = View.inflate(mContext, R.layout.comment_display_card, null);

        CircleImageView profPic = pview.findViewById(R.id.comment_profilePicture);
        TextView userName = pview.findViewById(R.id.comment_object_ownerName);
        TextView comment = pview.findViewById(R.id.comment_object_comment);
        TextView timeElapsed = pview.findViewById(R.id.comments_object_postedTime);

        //set text for textview
        final String name=CommentList.get(position).getCommentorName();
        final String cmnt=CommentList.get(position).getComment();
        final String profPicURI = CommentList.get(position).getCommentorPicURL();
        final long createdTime = CommentList.get(position).getTimestamp();

        final long currentTime = System.currentTimeMillis();

        long hours = TimeUnit.MILLISECONDS.toHours(currentTime - createdTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime - createdTime);


        userName.setText(name);
        comment.setText(cmnt);
        Glide.with(mContext)
                .load(profPicURI)
                .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_account_circle_black_24dp))
                .into(profPic);
        if(minutes > 60)
            timeElapsed.setText("Posted " + hours + "h ago");
        else
            timeElapsed.setText("Posted " + minutes + "min ago");


        return pview;
    }
}
