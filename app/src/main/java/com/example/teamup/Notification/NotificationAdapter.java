package com.example.teamup.Notification;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.teamup.model.Notification;
import com.example.teamup.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class NotificationAdapter extends BaseAdapter{
    private Context mContext;
    private List<Notification> NotificationList;
    private FirebaseFirestore db;
    private String TAG = "NOTIFICATION_LIST_ADAPTER";
    private TextView name, description;
    private ImageView icon;

    public NotificationAdapter(Context mContext, List<Notification> NotificationList){
        this.mContext = mContext;
        this.NotificationList = NotificationList;
    }

    @Override
    public int getCount() {
        return NotificationList.size();
    }

    @Override
    public Object getItem(int position) {
        return NotificationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.notification_object, null);
        db = FirebaseFirestore.getInstance();

        name = v.findViewById(R.id.notification_object_title);
        description = v.findViewById(R.id.notification_object_description);
        icon = v.findViewById(R.id.notification_object_icon);

        name.setText(NotificationList.get(position).getProjectName());

        return v;
    }
}
