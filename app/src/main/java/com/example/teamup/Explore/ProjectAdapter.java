package com.example.teamup.Explore;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

        //set text for textview
         final String projectname=Projectlist.get(position).getProjectName();
         final String projectdesc=Projectlist.get(position).getProjectDescription();
         final String projectid=Projectlist.get(position).getProjectId();

         proname.setText(projectname);
         prodesc.setText(projectdesc);

//         Apply.setOnClickListener(new View.OnClickListeneger() {
//             @Override
//             public void onClick(View v) {
//                 Toast.makeText(pview.getContext(),"Applied",Toast.LENGTH_LONG);
//             }
//         });

        return pview;
    }
}
