package com.example.lulu.classes;

public class Post {
    private String uuid;
    private String title;
    private String text;
    private String posterUuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
