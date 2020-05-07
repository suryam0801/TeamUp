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
import com.example.teamup.model.Broadcast;

import java.util.ArrayList;
import java.util.Locale;

public class BroadcastAdapter extends BaseAdapter  {
    private Context mContext;

    private ArrayList<Broadcast> projectlist;
    private ArrayList<Broadcast> arraylist;
    private String TAG = "PROJECT ADAPTER";
    private LinearLayout headerBackground, iconBackground;

    public BroadcastAdapter(Context mContext, ArrayList<Broadcast> projectlist) {
        this.mContext = mContext;
        this.projectlist = projectlist;
        this.arraylist = new ArrayList<Broadcast>();
        this.arraylist.addAll(projectlist);
    }

    @Override
    public int getCount() {
        return projectlist.size();
    }

    @Override
    public Object getItem(int position) {
        return projectlist.get(position);
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

        Broadcast currentBroadcast = projectlist.get(position);

        //set text for textview
         final String projectname= currentBroadcast.getBroadcastName();
         final String projectdesc= currentBroadcast.getBroadcastDescription();
         final String creatorName= currentBroadcast.getCreatorName();

         proname.setText(projectname);
         prodesc.setText(projectdesc);
         procreatorname.setText(creatorName);

        switch (currentBroadcast.getCategory()){
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

         pview.setTag(currentBroadcast.getBroadcastId());

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
        projectlist.clear();
        if (charText.length() == 0) {
            projectlist.addAll(arraylist);
        }
        else
        {
            for (Broadcast pr : arraylist)
            {
                if (pr.getBroadcastName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    projectlist.add(pr);
                }
            }
        }
        notifyDataSetChanged();
    }
}
