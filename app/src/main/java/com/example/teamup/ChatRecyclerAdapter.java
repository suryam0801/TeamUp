package com.example.teamup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamup.ControlPanel.Chat;
import com.example.teamup.viewholder.ChatReceivedViewHolder;
import com.example.teamup.viewholder.ChatSentViewHolder;

import java.util.ArrayList;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Chat> mChatList;

    private Context mContext;

    public static final int VIEW_TYPE_SENT =1;

    public static final int VIEW_TYPE_RECEIVED = 2;


    public ChatRecyclerAdapter(ArrayList<Chat> mChatList, Context mContext) {
        this.mChatList = mChatList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==VIEW_TYPE_SENT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_sent_view, parent);
            return new ChatSentViewHolder(view);
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_received_view, parent);
            return new ChatReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chat chat=mChatList.get(position);
        if(holder instanceof ChatSentViewHolder)
        {
            ChatSentViewHolder chatSentViewHolder=(ChatSentViewHolder)holder;
            chatSentViewHolder.mTextViewMesgage.setText(chat.getMessage());
            chatSentViewHolder.mTextViewTime.setText(chat.getTimeStamp());
            chatSentViewHolder.mTextViewUserName.setText(chat.getSenderName());
        }else{
            ChatReceivedViewHolder chatReceivedViewHolder=(ChatReceivedViewHolder)holder;
            chatReceivedViewHolder.mTextViewMesgage.setText(chat.getMessage());
            chatReceivedViewHolder.mTextViewTime.setText(chat.getTimeStamp());
            chatReceivedViewHolder.mTextViewUserName.setText(chat.getSenderName());
        }
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mChatList.get(position).isSentMessage())
        {
            return VIEW_TYPE_SENT;
        }
        return VIEW_TYPE_RECEIVED;
    }

}
