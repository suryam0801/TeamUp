package com.example.teamup.Explore;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamup.R;

import java.util.List;

public class ProjectAdapter extends BaseAdapter  {
    private Context mContext;
    private List<Project> Projectlist;
    private String TAG = "PROJECT ADAPTER";

    public ProjectAdapter(Context mContext, List<Project> Projectlist) {
        this.mContext = mContext;
        this.Projectlist = Projectlist;
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
        final View pview = View.inflate(mContext, R.layout.project_view_list, null);
        TextView proname=pview.findViewById(R.id.p_title);
        TextView prodesc=pview.findViewById(R.id.p_desc);
        TextView procreatorname=pview.findViewById(R.id.p_creatorName);
        LinearLayout background = pview.findViewById(R.id.explore_card_icon_background);
        ImageView foreground = pview.findViewById(R.id.explore_card_icon_foreground);

        Project currentProject = Projectlist.get(position);

        //set text for textview
         final String projectname=currentProject.getProjectName();
         final String projectdesc=currentProject.getProjectDescription();
         final String creatorName=currentProject.getCreatorName();

         proname.setText(projectname);
         prodesc.setText(projectdesc);
         procreatorname.setText(creatorName);

        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.RECTANGLE);
        gd.setCornerRadius(15.0f); // border corner radius

         switch (currentProject.getCategory()){
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
             case "Engineering &amp; Architecture":
                 gd.setColor(Color.parseColor("#158BF1"));
                 background.setBackground(gd);
                 foreground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.engineering_architecture_icon));
                 break;
             case "Software Development":
                 gd.setColor(Color.parseColor("#FF63DA"));
                 background.setBackground(gd);
                 foreground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.software_development_icon));
                 break;
             case "Sales &amp; Marketing":
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

         pview.setTag(currentProject.getProjectId());

        return pview;
    }
}
