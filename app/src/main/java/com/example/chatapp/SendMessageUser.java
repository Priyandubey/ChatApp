package com.example.chatapp;

public class SendMessageUser {

    private String username;
    private String uuid;

    public SendMessageUser(String username, String uuid) {
        this.username = username;
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getUuid() {
        return uuid;
    }
}