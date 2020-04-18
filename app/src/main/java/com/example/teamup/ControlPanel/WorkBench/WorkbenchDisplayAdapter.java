package com.example.teamup.ControlPanel.WorkBench;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.teamup.Explore.Project;
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

        //set text for textview
        final String projectname=Projectlist.get(position).getProjectName();
        final String creatorName=Projectlist.get(position).getCreatorName();

        if(owner = true)
            procreatorname.setText("You");
        else
            procreatorname.setText(creatorName);

        proname.setText(projectname);

        pview.setTag(Projectlist.get(position).getProjectId());

        return pview;
    }
}
