package com.chattingku.krenzfer.chattingku.Model;

import java.util.Date;

/**
 * Created by krenzfer on 21/05/17.
 */

public class ChatMessage {

    private String Username;
    private String MessageText;
    private long MessageTime;

    public ChatMessage(String username, String messageText) {
        Username = username;
        MessageText = messageText;
        MessageTime = new Date().getTime();
    }

    public ChatMessage(){}

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getMessageText() {
        return MessageText;
    }

    public void setMessageText(String messageText) {
        MessageText = messageText;
    }

    public long getMessageTime() {
        return MessageTime;
    }

    public void setMessageTime(long messageTime) {
        MessageTime = messageTime;
    }
}
