package com.example.teamup.ControlPanel.TaskList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.teamup.model.Project;
import com.example.teamup.R;
import com.example.teamup.model.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TaskAdapter extends BaseAdapter{
    private Context mContext;
    private List<Task> TaskList;
    private FirebaseFirestore db;
    private Project project;
    private String TAG = "TASK_LIST_ADAPTER";
    private TextView name, description, priority;
    private View priorityBar;


    public TaskAdapter(Context mContext, List<Task> TaskList){
        this.mContext = mContext;
        this.TaskList = TaskList;
    }

    @Override
    public int getCount() {
        return TaskList.size();
    }

    @Override
    public Object getItem(int position) {
        return TaskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.task_object, null);
        db = FirebaseFirestore.getInstance();

        name = v.findViewById(R.id.task_name_taskObject);
        description = v.findViewById(R.id.task_description_taskObject);
        priority = v.findViewById(R.id.priorityTaskObject);
        priorityBar = v.findViewById(R.id.priority_bar);

        name.setText(TaskList.get(position).getTaskName());
        description.setText(TaskList.get(position).getTaskName());

        switch (TaskList.get(position).getPriority()){
            case "High":
                priority.setText(TaskList.get(position).getPriority());
                priority.setTextColor(Color.parseColor("#FF3838"));
                priorityBar.setBackgroundColor(Color.parseColor("#FF3838"));
                break;
            case "Medium":
                priority.setText(TaskList.get(position).getPriority());
                priority.setTextColor(Color.parseColor("#36D1DC"));
                priorityBar.setBackgroundColor(Color.parseColor("#FF3838"));
                break;
            case "Low":
                priority.setText(TaskList.get(position).getPriority());
                priority.setTextColor(Color.parseColor("#6CACFF"));
                priorityBar.setBackgroundColor(Color.parseColor("#6CACFF"));
                break;
        }

        //Save product id to tag
        v.setTag(TaskList.get(position).getTaskID());
        return v;
    }
}
