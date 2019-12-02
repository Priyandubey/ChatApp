package com.example.chatapp;

public class PhotoMessageModel {

    String sendOrReceived;
    String url;
    String message;

    public PhotoMessageModel(String sendOrReceived, String url, String message) {
        this.sendOrReceived = sendOrReceived;
        this.url = url;
        this.message = message;
    }

    public String getSendOrReceived() {
        return sendOrReceived;
    }

    public String getUrl() {
        return url;
    }

    public String getMessage() {
        return message;
    }
}
