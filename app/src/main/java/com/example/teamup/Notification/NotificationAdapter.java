package com.example.teamup.Notification;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
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
    private TextView notificationTitle, notificationDescription;
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

        notificationTitle = v.findViewById(R.id.notification_object_title);
        notificationDescription = v.findViewById(R.id.notification_object_description);
        icon = v.findViewById(R.id.notification_object_icon);

        GradientDrawable gd = new GradientDrawable();
        gd.setShape(GradientDrawable.OVAL);
        gd.setCornerRadius(15.0f); // border corner radius

        String state = NotificationList.get(position).getState();

        switch (state) {
            case "application accepted":
                notificationTitle.setText("Application Accepted");
                notificationDescription.setText("");
                break;
            case "application rejected":
                notificationTitle.setText("Application Rejected");
                notificationDescription.setText("");
                break;
            case "new member added":
                notificationTitle.setText("New Member Onboard");
                notificationDescription.setText("");
                break;
            case "added to chatgroup":
                notificationTitle.setText("Added to New Chatgroup");
                notificationDescription.setText("");
                break;
            case "task added":
                notificationTitle.setText("New Task");
                notificationDescription.setText("");
                break;
            case "member removed":
                notificationTitle.setText("Member Removed");
                notificationDescription.setText("");
                break;
            case "resource added":
                notificationTitle.setText("New Resource");
                notificationDescription.setText("");
                break;
        }

        notificationTitle.setText(NotificationList.get(position).getProjectName());

        return v;
    }
}
