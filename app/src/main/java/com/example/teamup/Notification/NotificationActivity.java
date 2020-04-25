package com.example.teamup.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamup.ControlPanel.TaskList.TaskAdapter;
import com.example.teamup.ControlPanel.TaskList.TaskList;
import com.example.teamup.Explore.ProjectAdapter;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.Notification;
import com.example.teamup.model.Project;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "NotificationActivity";
    private ListView thisWeekListView, previousListView;
    private List<Notification> thisWeekNotifs, previousNotifs;
    private NotificationAdapter adapterThisWeek, adapterPrevious;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar
        setContentView(R.layout.activity_notify);

        db = FirebaseFirestore.getInstance();

        thisWeekListView = findViewById(R.id.thisweek_notifications_display);
        previousListView = findViewById(R.id.all_time_notifications_display);

        thisWeekNotifs = new ArrayList<>();
        previousNotifs = new ArrayList<>();

        loadNotifications();
    }

    public void loadNotifications() {
        db.collection("Users")
                .document("VCZlNIGu44SzUMMD0ODvA5Yx5Py2")
                .collection("Notifications")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d(TAG, "FOUND COLLECTION");
                        if (task.isSuccessful()) {
                            Log.d(TAG, task.getResult().toString());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Notification notification = document.toObject(Notification.class);
                                String currentTimeStamp = getCurrentTimeStamp();
                                Scanner scan = new Scanner(currentTimeStamp);
                                scan.useDelimiter("-");
                                int currentDay = Integer.parseInt(scan.next());
                                int currentMonth = Integer.parseInt(scan.next());

                                String notificationTimeStamp = notification.getTimestamp();
                                scan = new Scanner(notificationTimeStamp);
                                scan.useDelimiter("-");
                                int notificationDay = Integer.parseInt(scan.next());
                                int notificationMonth = Integer.parseInt(scan.next());

                                if(Math.abs(notificationDay - currentDay) > 6 || Math.abs(notificationMonth - currentMonth) >= 1)
                                    previousNotifs.add(notification);
                                else
                                    thisWeekNotifs.add(notification);

                            }

                            adapterThisWeek = new NotificationAdapter(getApplicationContext(), thisWeekNotifs);
                            adapterPrevious = new NotificationAdapter(getApplicationContext(), previousNotifs);

                            previousListView.setAdapter(adapterPrevious);
                            thisWeekListView.setAdapter(adapterThisWeek);

                            NotificationActivity.ListUtils.setDynamicHeight(previousListView);
                            NotificationActivity.ListUtils.setDynamicHeight(thisWeekListView);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

    public static String getCurrentTimeStamp(){
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String currentDateTime = dateFormat.format(new Date()); // Find todays date

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
