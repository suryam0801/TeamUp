package com.example.teamup.ControlPanel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamup.ChatRecyclerAdapter;
import com.example.teamup.Explore.Project;
import com.example.teamup.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ChatRoom extends AppCompatActivity {

    FirebaseUser user;
    private String projectId;
    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private ChatRecyclerAdapter adapter;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<Chat> mArrayList;
    private DatabaseReference databaseReference;
    private String TAG=this.getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        mArrayList=new ArrayList<>();
        mRecyclerView=findViewById(R.id.teams_chat_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mEditText=findViewById(R.id.message_editext);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("chat");
        user= FirebaseAuth.getInstance().getCurrentUser();
        Project project=getIntent().getParcelableExtra("project");
        assert project != null;
        projectId=project.getProjectId();
        loadMessages();
        adapter=new ChatRecyclerAdapter(mArrayList, getApplicationContext());
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        findViewById(R.id.button_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }

    public void sendMessage() {
        Log.d(TAG, "sendMessage: ");
        String message=mEditText.getText().toString().trim();
        String senderId=user.getUid();
        String timeStamp=String.valueOf(System.currentTimeMillis()/1000L);
        if (message.length()>0)
        {
            Log.d(TAG, "sendMessage: "+">0");
            final Chat chat=new Chat();
            chat.setMessage(message);
            chat.setSenderUserId(senderId);
            chat.setTimeStamp(timeStamp);
            chat.setSenderName("Gowtham");
            Log.d(TAG, "sendMessage: "+chat.toString());
            mArrayList.add(chat);
            mEditText.setText("");
            mEditText.clearFocus();
            Log.d(TAG, "sendMessage: "+databaseReference.toString());
            String key=databaseReference.push().getKey();
            chat.setMessageId(key);
            databaseReference.child(projectId).child(key).setValue(chat).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: "+e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: "+"saved");
                }
            });

        }
    }

    public void loadMessages()
    {
        databaseReference.child(projectId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    Chat chat=dataSnapshot1.getValue(Chat.class);
                    assert chat != null;
                    Log.d(TAG, "onDataChange: "+chat.toString());
                    mArrayList.add(chat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
