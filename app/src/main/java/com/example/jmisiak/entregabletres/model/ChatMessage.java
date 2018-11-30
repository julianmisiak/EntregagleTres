package com.example.jmisiak.entregabletres.model;

import java.util.Date;

public class ChatMessage {
    private String uid;
    private String messageText;
    private String messageUser;
    private long messageTime;

    public ChatMessage(String uid, String messageText, String messageUser) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageTime = new Date().getTime();
        this.uid = uid;
    }

    public ChatMessage() {

    }

    public String getUid() {
        return uid;
    }

    public String getMessageText() {
        return messageText;
    }


    public String getMessageUser() {
        return messageUser;
    }


    public long getMessageTime() {
        return messageTime;
    }

}
