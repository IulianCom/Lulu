package com.example.lulu.classes;

import java.util.UUID;

public class Post {
    private String uuid;
    private String text;
    private String posterUuid;

    public Post() {
    }

    public Post(String text, String posterUuid) {
        this.uuid = UUID.randomUUID().toString();
        this.text = text;
        this.posterUuid = posterUuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPosterUuid() {
        return posterUuid;
    }

    public void setPosterUuid(String posterUuid) {
        this.posterUuid = posterUuid;
    }
}
