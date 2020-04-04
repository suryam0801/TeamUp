package com.example.teamup.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamup.R;

public class ChatSentViewHolder extends RecyclerView.ViewHolder {

    public TextView mTextViewUserName,mTextViewTime,mTextViewMesgage;

    public ChatSentViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextViewUserName=itemView.findViewById(R.id.tv_chat_sent_name);
        mTextViewTime=itemView.findViewById(R.id.tv_chat_sent_time);
        mTextViewMesgage=itemView.findViewById(R.id.tv_chat_sent_message);
    }



}
