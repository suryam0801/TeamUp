package com.example.teamup.WorkBench;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.teamup.model.Project;
import com.example.teamup.R;

import java.util.List;

public class WorkbenchDisplayAdapter extends BaseAdapter {

    private Context mContext;
    private List<Project> Projectlist;
    boolean owner;

    public WorkbenchDisplayAdapter(Context mContext, List<Project> Projectlist, boolean owner) {
        this.mContext = mContext;
        this.Projectlist = Projectlist;
        this.owner = owner;
    }



    @Override
    public int getCount() {
        return Projectlist.size();
    }

    @Override
    public Object getItem(int position) {
        return Projectlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final View pview = View.inflate(mContext, R.layout.workbench_project_display_card, null);
        TextView proname=pview.findViewById(R.id.workbenchp_title);
        TextView procreatorname=pview.findViewById(R.id.workbenchp_creatorName);
        LinearLayout background=pview.findViewById(R.id.workbench_background);
        AppCompatImageView foreground=pview.findViewById(R.id.workbench_foreground);

        //set text for textview
        final String projectname=Projectlist.get(position).getProjectName();
        final String creatorName=Projectlist.get(position).getCreatorName();

        if(owner = true)
            procreatorname.setText("You");
        else
            procreatorname.setText(creatorName);

        proname.setText(projectname);

        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setCornerRadius(15.0f); // border corner radius

        switch (Projectlist.get(position).getCategory()){
            case "Physical Fitness":
                gd.setColor(Color.parseColor("#158BF1"));
                background.setBackground(gd);
                foreground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.physical_fitness_icon));
                break;
            case "Creative & Design":
                gd.setColor(Color.parseColor("#11F692"));
                background.setBackground(gd);
                foreground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.design_creative_icon));
                break;
            case "Engineering & Architecture":
                gd.setColor(Color.parseColor("#158BF1"));
                background.setBackground(gd);
                foreground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.engineering_architecture_icon));
                break;
            case "Software Development":
                gd.setColor(Color.parseColor("#FF63DA"));
                background.setBackground(gd);
                foreground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.software_development_icon));
                break;
            case "Sales & Marketing":
                gd.setColor(Color.parseColor("#11F692"));
                background.setBackground(gd);
                foreground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.sales_marketing_icon));
                break;
            case "Volunteering":
                gd.setColor(Color.parseColor("#158BF1"));
                background.setBackground(gd);
                foreground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.volunteering_icon));
                break;
            case "Art & Cuisine":
                gd.setColor(Color.parseColor("#FF63DA"));
                background.setBackground(gd);
                foreground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.art_cuisine_icon));
                break;
        }

        pview.setTag(Projectlist.get(position).getProjectId());

        return pview;
    }
}
