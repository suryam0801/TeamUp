package com.example.teamup.Notification;

import android.util.Log;

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

        FirebaseFirestore db= FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        Map<String,Object> applicationStatus=new HashMap<>();
        applicationStatus.put("state",state);
        applicationStatus.put("projectId", projectId);
        String from=firebaseAuth.getCurrentUser().getUid();
        String timeStamp =getCurrentTimeStamp();
        applicationStatus.put("from",from);
        applicationStatus.put("projectName",projectName);
        applicationStatus.put("timestamp",timeStamp);


        if (state.equals("rejected"))
        {
            db.collection("Users/"+toUserId+"/Notifications").add(applicationStatus).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {

                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
        else if (state.equals("accepted"))
        {
            db.collection("Users/"+toUserId+"/Notifications/"+projectId).add(applicationStatus).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {

                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
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
