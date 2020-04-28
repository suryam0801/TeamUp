package com.example.teamup.ControlPanel.TaskList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private TextView name, description;
    private ImageView colorIcon;
    private ImageView selectedIndicator;

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
        LinearLayout background = v.findViewById(R.id.task_object_background);
        colorIcon = v.findViewById(R.id.task_object_color_icon);
        selectedIndicator = v.findViewById(R.id.selectedIndicator);

        name.setText(TaskList.get(position).getTaskName());
        description.setText(TaskList.get(position).getTaskName());

        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.OVAL);
        gd.setCornerRadius(15.0f); // border corner radius

        switch (TaskList.get(position).getPriority()){
            case "High":
                background.setBackground(mContext.getResources().getDrawable(R.drawable.high_priority_task_background));
                gd.setColor(Color.parseColor("#FF6161"));
                colorIcon.setBackground(gd);
                break;
            case "Medium":
                background.setBackground(mContext.getResources().getDrawable(R.drawable.medium_priority_taskbackground));
                gd.setColor(Color.parseColor("#36D1DC"));
                colorIcon.setBackground(gd);
                break;
            case "Low":
                background.setBackground(mContext.getResources().getDrawable(R.drawable.low_priority_taskbackground));
                gd.setColor(Color.parseColor("#11F692"));
                colorIcon.setBackground(gd);
                break;
        }

        //Save product id to tag
        v.setTag(TaskList.get(position).getTaskID());
        return v;
    }
}
