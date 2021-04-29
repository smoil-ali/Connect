package com.reactive.connect.model;

/**
 * Created by KSHITIZ on 3/29/2018.
 * ----FOR RETRIEVING "chats" SECTION OF DATABASE-----
 */

public class Conv {
    public boolean seen;
    public long timeStamp;
    String userId;

    public Conv(){

    }

    public Conv(boolean seen, long timeStamp) {
        this.seen = seen;
        this.timeStamp = timeStamp;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
