package com.example.linkupappv1;

import com.google.firebase.Timestamp;

import kotlin.time.TimeSource;

/**
 * Model class for message(chats between users)
 */
public class Message {
    public String senderID;
    public String messageContent;

    //TimeStamp for when message was sent
    public Timestamp timestamp;

    //Empty Constructor
    public Message() {

    }

    //Getters & setters
    public String getMessageContent(){
        return this.messageContent;
    }

    public String getSenderID(){
        return this.senderID;
    }
}
