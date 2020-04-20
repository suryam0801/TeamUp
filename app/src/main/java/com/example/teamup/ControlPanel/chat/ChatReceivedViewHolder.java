package com.example.teamup.ControlPanel.chat;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teamup.R;

public class ChatReceivedViewHolder extends RecyclerView.ViewHolder {

    public TextView mTextViewUserName,mTextViewTime,mTextViewMesgage;
    public ChatReceivedViewHolder(@NonNull View itemView)
    {
        super(itemView);
        mTextViewUserName=itemView.findViewById(R.id.tv_chat_received_name);
        mTextViewTime=itemView.findViewById(R.id.tv_chat_received_time);
        mTextViewMesgage=itemView.findViewById(R.id.tv_chat_received_message);
    }


}
