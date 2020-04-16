package com.example.teamup.ControlPanel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.teamup.ControlPanel.DisplayApplicants.ApplicantDisplay;
import com.example.teamup.ControlPanel.ProjectWall.ProjectWall;
import com.example.teamup.ControlPanel.TaskList.TaskList;
import com.example.teamup.Explore.Project;
import com.example.teamup.R;

import java.util.List;

public class TabAdapter extends PagerAdapter implements Adapter {

    private List<Model> models;
    private LayoutInflater layoutInflater;
    private Context context;
    private Project project;

    public TabAdapter(List<Model> models, Context context, Project project) {
        this.models = models;
        this.context = context;
        this.project = project;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.tab_item, container, false);

        ImageView imageView;
        TextView title, desc;

        imageView = view.findViewById(R.id.image);
        title = view.findViewById(R.id.tab_title);
        desc = view.findViewById(R.id.tab_desc);

        imageView.setImageResource(models.get(position).getImage());
        title.setText(models.get(position).getTitle());
        desc.setText(models.get(position).getDesc());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //intent.putExtra("param", models.get(position).getTitle());
                switch(position)
                {
                    case 0 :
                        Intent intentWall = new Intent(context, ProjectWall.class);
                        intentWall.putExtra("project", project);
                        context.startActivity(intentWall);
                        break;
                    case 1 :
                        Intent intentTask = new Intent(context, TaskList.class);
                        intentTask.putExtra("project", project);
                        context.startActivity(intentTask);
                        break;
                    case 2 :
                        Intent intentChatRoom = new Intent(context, ChatRoom.class);
                        intentChatRoom.putExtra("project", project);
                        context.startActivity(intentChatRoom);
                        break;
                    case 3 :
                        Intent intentApplicants = new Intent(context, ApplicantDisplay.class);
                        intentApplicants.putExtra("project", project);
                        context.startActivity(intentApplicants);
                        break;
                    default :
                }

            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
