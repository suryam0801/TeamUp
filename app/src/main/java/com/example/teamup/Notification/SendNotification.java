package com.example.teamup.Notification;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendNotification {


    public static void sendnotification(String state, String projectId, String projectName, String toUserId) {
//        This is the function to store the

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //REFER NOTIFICATIONADAPTER FOR THE STATUS CODES!

        Map<String, Object> applicationStatus = new HashMap<>();
        applicationStatus.put("state", state);
        applicationStatus.put("projectId", projectId);
        String from = firebaseAuth.getCurrentUser().getUid();
        String getDate = getCurrentDateStamp();
        applicationStatus.put("from", from);
        applicationStatus.put("projectName", projectName);
        applicationStatus.put("date", getDate);
        applicationStatus.put("timestamp", System.currentTimeMillis());

        db.collection("Users/" + toUserId + "/Notifications").add(applicationStatus).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });


    }

    public static String getCurrentDateStamp() {
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
