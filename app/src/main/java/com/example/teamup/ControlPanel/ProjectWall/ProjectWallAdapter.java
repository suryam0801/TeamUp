package com.example.teamup.ControlPanel.ProjectWall;

import android.content.Context;
import android.icu.text.MessagePattern;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.teamup.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.name.setText(fileName);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView name;
        public Viewholder(@NonNull View itemView) {
            super(itemView);


            name=itemView.findViewById(R.id.project_wall_fileName);

        }
    }
}
