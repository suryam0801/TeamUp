package com.example.teamup.ControlPanel.ProjectWall;

import android.content.Context;
import android.content.Intent;
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

public class ProjectWallAdapter extends RecyclerView.Adapter<ProjectWallAdapter.Viewholder> {

    private Context context;
    private ArrayList<ProjectWallDataClass> arrayList;

    public ProjectWallAdapter(Context context, ArrayList<ProjectWallDataClass> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ProjectWallAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.file_display_template,parent,false);
        return  new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectWallAdapter.Viewholder holder, int position) {

        ProjectWallDataClass projectWallDataClass=arrayList.get(position);

        String fileName=projectWallDataClass.getFileName();
        String description=projectWallDataClass.getDescription();
        String ownerName=projectWallDataClass.getOwnerName();
        long createdTime = projectWallDataClass.getTime();

        long currentTime = System.currentTimeMillis();

        long hours = TimeUnit.MILLISECONDS.toHours(currentTime - createdTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime - createdTime);

        holder.fileName.setText(fileName);
        holder.ownerName.setText(ownerName);
        holder.description.setText(description);
        if(minutes > 60)
            holder.timeSincePosting.setText("Posted " + hours + "h ago");
        else
            holder.timeSincePosting.setText("Posted " + minutes + "min ago");

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

        private TextView ownerName, description, timeSincePosting, fileName, viewAllComments;
        private CircleImageView profPic;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            ownerName =itemView.findViewById(R.id.projectWall_object_ownerName);
            profPic =itemView.findViewById(R.id.projectWall_profilePicture);
            description =itemView.findViewById(R.id.projectWall_object_Description);
            fileName =itemView.findViewById(R.id.project_wall_fileName);
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
