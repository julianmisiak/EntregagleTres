package com.example.jmisiak.entregabletres.view.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.jmisiak.entregabletres.R;

public class ChatViewHolder  extends RecyclerView.ViewHolder {
    private TextView messageText;
    private TextView messageUser;
    private TextView messageTime;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.message_text);
        messageUser = itemView.findViewById(R.id.message_user);
        messageTime = itemView.findViewById(R.id.message_time);
    }

    public void setMessageText(String messageText) {
        this.messageText.setText(messageText);
    }

    public void setMessageUser(String messageUser) {
        this.messageUser.setText(messageUser);
    }

    public void setMessageTime(String messageTime) {
        this.messageTime.setText(messageTime);
    }
}
