package com.example.teamup.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teamup.Explore.ProjectAdapter;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.Notification;
import com.example.teamup.model.Project;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {

    private static final String TAG = "NotificationActivity";
    ListView thisWeekListView, previousListView;
    List<String> thisWeekNotifs, previousNotifs;
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
                                Notification notification= document.toObject(Notification.class);
                                Log.d(TAG, notification.toString());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
