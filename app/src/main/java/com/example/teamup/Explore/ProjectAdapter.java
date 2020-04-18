package com.example.teamup.Explore;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teamup.R;

import java.util.List;

public class ProjectAdapter extends BaseAdapter  {
    private Context mContext;
    private List<Project> Projectlist;

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

        //set text for textview
         final String projectname=Projectlist.get(position).getProjectName();
         final String projectdesc=Projectlist.get(position).getProjectDescription();
         final String creatorName=Projectlist.get(position).getCreatorName();

         proname.setText(projectname);
         prodesc.setText(projectdesc);
         procreatorname.setText(creatorName);

        pview.setTag(Projectlist.get(position).getProjectId());

        return pview;
    }
}
