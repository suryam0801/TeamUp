package com.example.teamup.ControlPanel.TaskList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.teamup.ControlPanel.TaskList.Task;
import com.example.teamup.Explore.Project;
import com.example.teamup.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TaskAdapter extends BaseAdapter{
    private Context mContext;
    private List<Task> TaskList;
    String taskName;
    String taskDescription;
    FirebaseFirestore db;
    Project project;
    String TAG = "APPLICANT_LIST_ADAPTER";

    public TaskAdapter(Context mContext, String taskName, String taskDescription, String taskId){
        this.mContext = mContext;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
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

        /*accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/

        //Save product id to tag
        v.setTag(TaskList.get(position).getTaskID());
        return v;
    }
}
