package com.example.teamup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamup.ControlPanel.ChatRoom;
import com.example.teamup.Explore.Project;
import com.example.teamup.R;

import java.util.ArrayList;

public class WorkBenchRecyclerAdapter extends RecyclerView.Adapter<WorkBenchRecyclerAdapter.MyViewHolder> {

    private Context mContext;
  private   ArrayList<Project> mList;

    private OnItemClickListener onItemClickListener;
    public WorkBenchRecyclerAdapter(Context mContext, ArrayList<Project> mList,OnItemClickListener onItemClickListener){
        this.mContext=mContext;
        this.mList=mList;
        this.onItemClickListener=onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_item_myproject, parent,false);
        return new MyViewHolder(view,onItemClickListener);
    }


    @Override
    public void onBindViewHolder(@NonNull WorkBenchRecyclerAdapter.MyViewHolder holder, int position) {
            Project project=mList.get(position);
            holder.bindView(project);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView projectNameTv,projectDescTv;
        private OnItemClickListener onItemClickListener;
        private Project project;
        private Button chatButton;
        public MyViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener=onItemClickListener;
            projectNameTv=itemView.findViewById(R.id.project_name_text_view);
            projectDescTv=itemView.findViewById(R.id.project_desc_text_view);
            chatButton=itemView.findViewById(R.id.goto_chat_btn);
            chatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, ChatRoomBaseActivity.class);
                    intent.putExtra("project", project);
                    mContext.startActivity(intent);
                }
            });
            itemView.setOnClickListener(this);

        }

        public void bindView(Project project){
            this.project=project;
            projectNameTv.setText(project.getProjectName());
            projectDescTv.setText(project.getProjectDescription());
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(project);
        }
    }

   public  interface OnItemClickListener{
         void onItemClick(Project project);
    }
}
