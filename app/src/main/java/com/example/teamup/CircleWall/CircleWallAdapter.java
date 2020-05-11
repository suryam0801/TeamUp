package com.example.teamup.CircleWall;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.teamup.R;
import com.example.teamup.model.ProjectWallDataClass;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CircleWallAdapter extends RecyclerView.Adapter<CircleWallAdapter.Viewholder> {

    private Context context;
    private ArrayList<ProjectWallDataClass> arrayList;

    public CircleWallAdapter(Context context, ArrayList<ProjectWallDataClass> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CircleWallAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.file_display_template,parent,false);
        return  new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CircleWallAdapter.Viewholder holder, int position) {

        ProjectWallDataClass projectWallDataClass=arrayList.get(position);

        String description=projectWallDataClass.getDescription();
        String ownerName=projectWallDataClass.getOwnerName();
        long createdTime = projectWallDataClass.getTime();

        long currentTime = System.currentTimeMillis();

        long days = TimeUnit.MILLISECONDS.toDays(currentTime - createdTime);
        long hours = TimeUnit.MILLISECONDS.toHours(currentTime - createdTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime - createdTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(currentTime - createdTime);

        holder.ownerName.setText(ownerName);
        holder.description.setText(description);

        if(seconds < 60) {
            holder.timeSincePosting.setText(seconds + "s ago");
        } else if (minutes > 1 && minutes < 60){
            holder.timeSincePosting.setText(minutes + "m ago");
        } else if (hours > 1 && hours < 24) {
            holder.timeSincePosting.setText(hours + "h ago");
        } else if (days > 1 && days < 365 ) {
            if(days > 7)
                holder.timeSincePosting.setText((days/7) + "w ago");
            else
                holder.timeSincePosting.setText(days + "d ago");
        }

/*
        if(minutes > 60)
            holder.timeSincePosting.setText("Posted " + hours + "h ago");
        else
            holder.timeSincePosting.setText("Posted " + minutes + "min ago");
*/

        Glide.with(context)
                .load(projectWallDataClass.getOwnerPicURL())
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_account_circle_black_24dp))
                .into(holder.profPic);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView ownerName, description, timeSincePosting, viewAllComments;
        private CircleImageView profPic;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            ownerName =itemView.findViewById(R.id.projectWall_object_ownerName);
            profPic =itemView.findViewById(R.id.projectWall_profilePicture);
            description =itemView.findViewById(R.id.projectWall_object_Description);
            timeSincePosting =itemView.findViewById(R.id.projectWall_object_postedTime);
            viewAllComments =itemView.findViewById(R.id.projectWall_object_viewComments);

            viewAllComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, FileComments.class));
                }
            });
        }
    }
}