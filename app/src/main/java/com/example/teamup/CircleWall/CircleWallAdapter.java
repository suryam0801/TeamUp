package com.example.teamup.CircleWall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.teamup.R;
import com.example.teamup.SessionStorage;
import com.example.teamup.model.Broadcast;
import com.example.teamup.model.Poll;
import com.example.teamup.model.ProjectWallDataClass;
import com.example.teamup.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import de.hdodenhof.circleimageview.CircleImageView;

public class CircleWallAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ProjectWallDataClass> arrayList;
    private TextView ownerName, description, timeSincePosting, fileName, viewAllComments, pollTitle;
    private Button pollSubmit;
    private RadioGroup pollOptionsGroup;
    private User user;
    private List<Poll> pollList;
    private LinearLayout fileDisplay, pollDisplay;
    private CircleImageView profPic;
    private String broadcastID;
    private Map<String, Object> pollUserAnswers;

    private FirebaseFirestore firebaseFirestore;

    public CircleWallAdapter(Context context, ArrayList<ProjectWallDataClass> arrayList, List<Poll> pollList, String broadcastID) {
        this.context = context;
        this.arrayList = arrayList;
        this.pollList = pollList;
        this.broadcastID = broadcastID;
        firebaseFirestore = FirebaseFirestore.getInstance();
        loadPollAnswers();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View pview = View.inflate(context, R.layout.file_display_template, null);
        firebaseFirestore = FirebaseFirestore.getInstance();
        ownerName = pview.findViewById(R.id.projectWall_object_ownerName);
        profPic = pview.findViewById(R.id.projectWall_profilePicture);
        description = pview.findViewById(R.id.projectWall_object_Description);
        fileName = pview.findViewById(R.id.project_wall_fileName);
        timeSincePosting = pview.findViewById(R.id.projectWall_object_postedTime);
        viewAllComments = pview.findViewById(R.id.projectWall_object_viewComments);
        pollTitle = pview.findViewById(R.id.poll_question_textview);
        pollDisplay = pview.findViewById(R.id.poll_display);
        pollOptionsGroup = pview.findViewById(R.id.poll_options_radio_group);
        fileDisplay = pview.findViewById(R.id.attachment_display);
        pollDisplay.setVisibility(View.GONE);
        fileDisplay.setVisibility(View.GONE);

        user = SessionStorage.getUser((CircleWall) context);

        viewAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, FileComments.class));
            }
        });

        final ProjectWallDataClass projectWallDataObject = arrayList.get(i);

        if (projectWallDataObject.getLink() != null) {
            fileDisplay.setVisibility(View.VISIBLE);
        }

        if (projectWallDataObject.isHasPoll() == true) {
            pollDisplay.setVisibility(View.VISIBLE);
            final HashMap<String, Object> pollOnly = new HashMap<>();
            pollOnly.putAll(projectWallDataObject.getPoll());

            String question = String.valueOf(pollOnly.get("Question"));
            pollTitle.setText(question);
            long numberOfOptions = (long) pollOnly.get("NumberOfOptions");
            for (int x = 1; x <= numberOfOptions; x++) {
                String answerOption = String.valueOf(pollOnly.get("Option " + x));
                RadioButton button = new RadioButton(context);
                button.setHighlightColor(Color.BLACK);
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{
                                new int[]{-android.R.attr.state_enabled}, //disabled
                                new int[]{android.R.attr.state_enabled} //enabled
                        },
                        new int[]{
                                Color.BLACK //disabled
                                , Color.BLUE //enabled
                        }
                );
                button.setButtonTintList(colorStateList);
                button.setTextColor(Color.BLACK);
                button.setText(answerOption);
                HashMap<String, String> currentPollAsnwers = (HashMap<String, String>) pollUserAnswers.get(projectWallDataObject.getPollID());
                pollOptionsGroup.addView(button);
                if(currentPollAsnwers.get(user.getUserId()).equals(answerOption))
                    pollOptionsGroup.check(button.getId());

            }

            pollOptionsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = pview.findViewById(checkedId);
                    HashMap<String, String> answer = new HashMap<>();
                    answer.put(user.getUserId(), rb.getText().toString());
                    HashMap<String, Object> store = new HashMap<>();
                    store.put(projectWallDataObject.getPollID(), answer);
                    Log.d("BROADCASTID: ", broadcastID);
                    firebaseFirestore.collection("ProjectWall").document(broadcastID).set(store, SetOptions.merge());
                }
            });

        }

        String descriptionString = projectWallDataObject.getDescription();
        String ownerNameString = projectWallDataObject.getOwnerName();
        long createdTime = projectWallDataObject.getTime();

        long currentTime = System.currentTimeMillis();

        long days = TimeUnit.MILLISECONDS.toDays(currentTime - createdTime);
        long hours = TimeUnit.MILLISECONDS.toHours(currentTime - createdTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(currentTime - createdTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(currentTime - createdTime);

        //holder.fileName.setText(fileName);
        ownerName.setText(ownerNameString);
        description.setText(descriptionString);

        if (seconds < 60) {
            timeSincePosting.setText(seconds + "s ago");
        } else if (minutes > 1 && minutes < 60) {
            timeSincePosting.setText(minutes + "m ago");
        } else if (hours > 1 && hours < 24) {
            timeSincePosting.setText(hours + "h ago");
        } else if (days > 1 && days < 365) {
            if (days > 7)
                timeSincePosting.setText((days / 7) + "w ago");
            else
                timeSincePosting.setText(days + "d ago");
        }

        Glide.with(context)
                .load(projectWallDataObject.getOwnerPicURL())
                .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_account_circle_black_24dp))
                .into(profPic);

        return pview;
    }

    public void loadPollAnswers() {
        firebaseFirestore
                .collection("ProjectWall")
                .document(broadcastID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                pollUserAnswers = document.getData();
                            }
                        }
                    }
                });
    }

}