package com.example.teamup.ControlPanel;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.teamup.ChatRecyclerAdapter;
import com.example.teamup.ChatRoomBaseActivity;
import com.example.teamup.CustomChatDialogFragment;
import com.example.teamup.Explore.Project;
import com.example.teamup.R;
import com.example.teamup.UsersModel;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;

import com.example.teamup.R;

public class ChatRoom extends Fragment {
    FirebaseUser user;
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
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private UsersModel usersModel;
    private String currentUserName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: "+"Chat Room 1");
        return inflater.inflate(R.layout.chat_room_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mSwipeRefreshLayout=view.findViewById(R.id.message_swipe_layout);
        mArrayList=new ArrayList<>();
        mRecyclerView=view.findViewById(R.id.teams_chat_recycler_view);
        lm=new LinearLayoutManager(requireContext());
        mRecyclerView.setLayoutManager(lm);
        mEditText=view.findViewById(R.id.message_editext);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("chat");
        user= FirebaseAuth.getInstance().getCurrentUser();

        Bundle bundle=getArguments();
        assert bundle != null;
        final boolean isGroup=bundle.getBoolean("isgroup");
        final String id=bundle.getString("id");
        final String projectId=bundle.getString("projectId");
        final String userid=bundle.getString("userid");
        if (!isGroup)
        {
            usersModel=bundle.getParcelable("model");
        }
        loadMessages(isGroup,id,projectId,userid);
        mRecyclerView.scrollToPosition(mArrayList.size()-1);
        adapter=new ChatRecyclerAdapter(mArrayList, requireContext());
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        view.findViewById(R.id.button_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(isGroup,id,projectId,userid);
            }
        });
        removeSwipeRefreshDrawable();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                mCurrentPage++;

                itemPos = 0;

                loadMoreMessages(isGroup,id,projectId,userid);


            }
        });
    }

    public void sendMessage(boolean isGroup,String id,String projectId,String userid) {
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
            if (isGroup)
            {
                databaseReference=FirebaseDatabase.getInstance().getReference("chat").child(projectId).child("groups").child(id);
                databaseReference.child(Objects.requireNonNull(key)).setValue(chat).addOnFailureListener(new OnFailureListener() {
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
            }else{
                DatabaseReference dbReference=FirebaseDatabase.getInstance().getReference("chat").child(projectId).child("personal").child(userid).child(id);
                dbReference.child(Objects.requireNonNull(key)).setValue(chat).addOnFailureListener(new OnFailureListener() {
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
                FirebaseDatabase.getInstance().getReference("chat").child(projectId).child("personal").child(id).child(userid).child(key).setValue(chat).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: 1"+"saved");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.getMessage());
                    }
                });
                FirebaseDatabase.getInstance().getReference("personal").child(projectId).child(userid).child(id).setValue(usersModel);
                UsersModel model=new UsersModel(currentUserName, userid);
                FirebaseDatabase.getInstance().getReference("personal").child(projectId).child(id).child(userid).setValue(model);
            }

            InputMethodManager inputManager = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            assert inputManager != null;
            inputManager.hideSoftInputFromWindow(Objects.requireNonNull(requireActivity().getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }



    private void loadMessages(boolean isGroup,String id,String projectId,String userid) {
        DatabaseReference messageRef;
        if (isGroup) {
             messageRef = databaseReference.child(projectId).child("groups").child(id);
        }else{
            messageRef=databaseReference.child(projectId).child("personal").child(userid).child(id);
        }
            Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);
            messageQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {

                    Chat chat = dataSnapshot.getValue(Chat.class);

                    itemPos++;
                    if (itemPos == 1) {
                        String messageKey = dataSnapshot.getKey();
                        mLastKey = messageKey;
                        mPrevKey = messageKey;
                    }
                    mArrayList.add(chat);
                    adapter.notifyDataSetChanged();
                    assert chat != null;
                    Log.i(TAG, "onChildAdded: ad Messages:   -->  " + chat.getMessage());
                    mRecyclerView.scrollToPosition(mArrayList.size() - 1);
                    mSwipeRefreshLayout.setRefreshing(false);
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


    private void loadMoreMessages(boolean isGroup,String id,String projectId,String userid) {
        DatabaseReference messageRef;
        if (isGroup) {
            messageRef = databaseReference.child(projectId).child("groups").child(id);
        }else{
            messageRef=databaseReference.child(projectId).child("child").child(userid).child(id);
        }
        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(10);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                Chat message = dataSnapshot.getValue(Chat.class);
                String messageKey = dataSnapshot.getKey();
                assert message != null;
                Log.d(TAG, "onChildAdded: Load More:  -->  "+message.getMessage());
                if(!mPrevKey.equals(messageKey)){

                    mArrayList.add(itemPos++,message);

                } else {
                    mPrevKey = mLastKey;
                }

                if(itemPos == 1) {
                    mLastKey = messageKey;
                }
                adapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);

               // lm.scrollToPositionWithOffset(10, 0);

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
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

    }

    public void removeSwipeRefreshDrawable(){
        try {
            Field f = mSwipeRefreshLayout.getClass().getDeclaredField("mCircleView");
            f.setAccessible(true);
            ImageView img = (ImageView)f.get(mSwipeRefreshLayout);
            assert img != null;
            img.setImageResource(android.R.color.transparent);
            img.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent));
            img.setBackgroundResource(android.R.color.transparent);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
