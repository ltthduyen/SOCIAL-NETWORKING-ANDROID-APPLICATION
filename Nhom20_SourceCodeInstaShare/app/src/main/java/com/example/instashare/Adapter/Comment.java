package com.example.instashare.Adapter;

import android.net.Uri;

import com.google.firebase.Timestamp;

public class Comment {
    String state;
    Timestamp timestamp;
    String emoji;
    Uri uri;
    String sendId;

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public Comment(String state, Timestamp timestamp, String emoji, Uri uri, String sendId) {
        this.state = state;
        this.timestamp = timestamp;
        this.emoji = emoji;
        this.uri = uri;
        this.sendId = sendId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }


    public Comment() {
    }
}
