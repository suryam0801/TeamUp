package com.example.teamup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamup.ControlPanel.Chat;
import com.example.teamup.viewholder.ChatReceivedViewHolder;
import com.example.teamup.viewholder.ChatSentViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Chat> mChatList;

    private Context mContext;
    private FirebaseUser firebaseUser;

    public static final int VIEW_TYPE_SENT =1;

    public static final int VIEW_TYPE_RECEIVED = 2;

    public static final String TAG = "ChatRecyclerAdapter";

    public ChatRecyclerAdapter(ArrayList<Chat> mChatList, Context mContext) {
        this.mChatList = mChatList;
        this.mContext = mContext;
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==VIEW_TYPE_SENT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_sent_view, parent,false);
            return new ChatSentViewHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_received_view, parent,false);
            return new ChatReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat chat=mChatList.get(position);
        Log.d(TAG, "onBindViewHolder: "+chat.toString());
        if(holder instanceof ChatSentViewHolder)
        {
            Log.d(TAG, "onBindViewHolder: sent");
            ChatSentViewHolder chatSentViewHolder=(ChatSentViewHolder)holder;
            chatSentViewHolder.mTextViewMesgage.setText(chat.getMessage());
            chatSentViewHolder.mTextViewTime.setText(formatDate(chat.getTimeStamp()));
            chatSentViewHolder.mTextViewUserName.setText(chat.getSenderName());
        }else{
            Log.d(TAG, "onBindViewHolder: received");
            ChatReceivedViewHolder chatReceivedViewHolder=(ChatReceivedViewHolder)holder;
            chatReceivedViewHolder.mTextViewMesgage.setText(chat.getMessage());
            chatReceivedViewHolder.mTextViewTime.setText(formatDate(chat.getTimeStamp()));
            chatReceivedViewHolder.mTextViewUserName.setText(chat.getSenderName());
        }
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mChatList.get(position).getSenderUserId().equals(firebaseUser.getUid()))
        {
            return VIEW_TYPE_SENT;
        }
        return VIEW_TYPE_RECEIVED;
    }

    public String formatDate(String a){
        long timeStamp=Long.parseLong(a);
        Date today = new Date(timeStamp*1000L);
        SimpleDateFormat formatter= new SimpleDateFormat("dd-M-yyyy hh:mm:ss a");
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(today);
    }

}
