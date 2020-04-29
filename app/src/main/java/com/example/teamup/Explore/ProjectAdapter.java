package com.example.teamup.Explore;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.teamup.R;
import com.example.teamup.model.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProjectAdapter extends BaseAdapter  {
    private Context mContext;

    private ArrayList<Project> Projectlist;
    private ArrayList<Project> arraylist;
    private String TAG = "PROJECT ADAPTER";
    private LinearLayout headerBackground, iconBackground;

    public ProjectAdapter(Context mContext,  ArrayList<Project> Projectlist) {
        this.mContext = mContext;
        this.Projectlist = Projectlist;
        this.arraylist = new ArrayList<Project>();
        this.arraylist.addAll(Projectlist);
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
        headerBackground = pview.findViewById(R.id.exploreCardHeaderBackground);
        iconBackground = pview.findViewById(R.id.explore_card_icon_background);
        ImageView iconForeground = pview.findViewById(R.id.explore_card_icon_foreground);

        Project currentProject = Projectlist.get(position);

        //set text for textview
         final String projectname=currentProject.getProjectName();
         final String projectdesc=currentProject.getProjectDescription();
         final String creatorName=currentProject.getCreatorName();

         proname.setText(projectname);
         prodesc.setText(projectdesc);
         procreatorname.setText(creatorName);

        switch (currentProject.getCategory()){
             case "Physical Fitness":
                 setResources("#D8E9FF", "#158BF1");
                 iconForeground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.physical_fitness_icon));
                 break;
             case "Creative & Design":
                 setResources("#BBFFE1", "#11F692");
                 iconForeground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.design_creative_icon));
                 break;
             case "Engineering & Architecture":
                 setResources("#FFD1E9", "#FF38A2");
                 iconForeground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.engineering_architecture_icon));
                 break;
             case "Software Development":
                 setResources("#FFDDBB", "#FF9C38");
                 iconForeground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.software_development_icon));
                 break;
             case "Sales & Marketing":
                 setResources("#FFD1E9", "#FF38A2");
                 iconForeground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.sales_marketing_icon));
                 break;
             case "Volunteering":
                 setResources("#BBFFE1", "#11F692");
                 iconForeground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.volunteering_icon));
                 break;
             case "Art & Cuisine":
                 setResources("#D8E9FF", "#158BF1");
                 iconForeground.setBackground(pview.getContext().getResources().getDrawable(R.drawable.art_cuisine_icon));
                 break;
         }

         pview.setTag(currentProject.getProjectId());

        return pview;
    }

    public void setResources(String headerColor, String iconColor){
        GradientDrawable gdIcon = new GradientDrawable();
        gdIcon.setShape(GradientDrawable.RECTANGLE);
        gdIcon.setCornerRadius(15.0f); // border corner radius

        GradientDrawable gdHeader = new GradientDrawable();
        gdHeader.setShape(GradientDrawable.RECTANGLE);
        gdHeader.setCornerRadius(15.0f); // border corner radius

        gdIcon.setColor(Color.parseColor(iconColor));
        gdHeader.setColor(Color.parseColor(headerColor));
        iconBackground.setBackground(gdIcon);
        headerBackground.setBackground(gdHeader);
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        Projectlist.clear();
        if (charText.length() == 0) {
            Projectlist.addAll(arraylist);
        }
        else
        {
            for (Project pr : arraylist)
            {
                if (pr.getProjectName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    Projectlist.add(pr);
                }
            }
        }
        notifyDataSetChanged();
    }
}
