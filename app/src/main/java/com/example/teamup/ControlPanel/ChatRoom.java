package com.example.teamup.ControlPanel;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
    private boolean isLoading=false;
    private int itemPos = 0;
    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private int mCurrentPage = 1;
    private String mLastKey = "";
    private String mPrevKey = "";
    private LinearLayoutManager lm;
    private int scrollPos=0;
    private static int firstVisibleInListview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        mArrayList=new ArrayList<>();
        mRecyclerView=findViewById(R.id.teams_chat_recycler_view);
        lm=new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(lm);
        mEditText=findViewById(R.id.message_editext);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("chat");
        user= FirebaseAuth.getInstance().getCurrentUser();
        Project project=getIntent().getParcelableExtra("project");
        assert project != null;
        projectId=project.getProjectId();
        mRecyclerView.scrollToPosition(mArrayList.size()-1);
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
        firstVisibleInListview = lm.findFirstVisibleItemPosition();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                super.onScrolled(recyclerView, dx, dy);

                int currentFirstVisible = lm.findFirstVisibleItemPosition();

                if(currentFirstVisible > firstVisibleInListview){
                    if (!isLoading) {
                        loadMoreMessages();
                    }
                    Log.i("RecyclerView scrolled: ", "scroll up!");
                    }
                else {
                    Log.i("RecyclerView scrolled: ", "scroll down!");
                }
                firstVisibleInListview = currentFirstVisible;
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
            mEditText.setText("");
            Log.d(TAG, "sendMessage: "+databaseReference.toString());
            String key=databaseReference.push().getKey();
            chat.setMessageId(key);
            databaseReference.child(projectId).child(key).setValue(chat).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: "+e.getMessage());
                    mRecyclerView.scrollToPosition(mArrayList.size()-1);
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "onSuccess: "+"saved");
                }
            });
            InputMethodManager inputManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(this.getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }



    private void loadMessages() {
        DatabaseReference messageRef = databaseReference.child(projectId);
        Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {

                Chat chat = dataSnapshot.getValue(Chat.class);

                itemPos++;
                if(itemPos == 1){
                    String messageKey = dataSnapshot.getKey();
                    mLastKey = messageKey;
                    mPrevKey = messageKey;
                }

                mArrayList.add(chat);
                adapter.notifyDataSetChanged();

                mRecyclerView.scrollToPosition(mArrayList.size() - 1-scrollPos);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void loadMoreMessages() {
        isLoading=true;
        DatabaseReference messageRef = databaseReference.child(projectId);

        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            int count=0;

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Chat message = dataSnapshot.getValue(Chat.class);
                String messageKey = dataSnapshot.getKey();
                assert message != null;
                Log.d(TAG, "onChildAdded: "+message.toString());
                if(!mPrevKey.equals(messageKey)){

                    mArrayList.add(count++,message);

                } else {
                    mPrevKey = mLastKey;
                }

                if(itemPos == 1) {

                    mLastKey = messageKey;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        isLoading=false;
        lm.scrollToPositionWithOffset(mArrayList.size()-1-scrollPos, 0);
    }

}
