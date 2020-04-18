package com.example.teamup.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Chat {

    private String messageId;

    private String message;

    private String senderUserId;

    private String senderName;

    private String timeStamp;


    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "messageId='" + messageId + '\'' +
                ", message='" + message + '\'' +
                ", senderUserId='" + senderUserId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }

}
