package com.example.chatapp.Class_Structures;

public class ChatInfo {

    private String message;
    private String sendOrReceived;

    public ChatInfo(String message, String sendOrReceived) {
        this.message = message;
        this.sendOrReceived = sendOrReceived;
    }

    public String getMessage() {
        return message;
    }

    public String getSendOrReceived() {
        return sendOrReceived;
    }
}
